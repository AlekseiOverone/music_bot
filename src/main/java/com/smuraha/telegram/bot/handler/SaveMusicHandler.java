package com.smuraha.telegram.bot.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smuraha.telegram.bot.Command;
import com.smuraha.telegram.model.MyAudio;
import com.smuraha.telegram.model.repo.MyAudioRepo;
import com.smuraha.telegram.util.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Audio;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Service
public class SaveMusicHandler implements Handler{

    private final MyAudioRepo myAudioRepo;

    private static final String API="https://api.telegram.org";

    @Value("${bot.token}")
    private String token;

    @Autowired
    public SaveMusicHandler(MyAudioRepo myAudioRepo) {
        this.myAudioRepo = myAudioRepo;
    }

    @Override
    public List<PartialBotApiMethod<? extends Serializable>> handle(Update update) {

        final Audio audio = update.getMessage().getAudio();
        final String fileId = audio.getFileId();

        // Create a neat value object to hold the URL
        InputStream getFileStream = null;
        try {
            getFileStream = new URL(API+"/bot"+token+"/getFile?file_id="+fileId).openStream();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Manually converting the response body InputStream to Response using Jackson
        ObjectMapper mapper = new ObjectMapper();
        Response response = null;
        try {
            response = mapper.readValue(getFileStream, Response.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        final String file_path = response.getResult().getFile_path();

        InputStream fileDownloadStream = null;
        try {
            fileDownloadStream = new URL(API+"/file/bot"+token+"/"+file_path).openStream();
        } catch (IOException e) {
            e.printStackTrace();
        }

        List<PartialBotApiMethod<? extends Serializable>> list = new ArrayList<>();
        SendMessage e = new SendMessage();
        e.setChatId(update.getMessage().getChatId() + "");

        try(ByteArrayOutputStream buffer = new ByteArrayOutputStream()) {

            int nRead;
            byte[] data = new byte[16384];

            while ((nRead = fileDownloadStream.read(data, 0, data.length)) != -1) {
                buffer.write(data, 0, nRead);
            }

            byte[] audioBytes = buffer.toByteArray();

            MyAudio myAudio = new MyAudio();
            myAudio.setAudio(audioBytes);
            myAudio.setDuration(audio.getDuration());
            myAudio.setFileId(fileId);
            myAudio.setFileSize(audio.getFileSize());
            myAudio.setPerformer(audio.getPerformer());
            myAudio.setTitle(audio.getTitle());
            myAudio.setSearchTitle(audio.getTitle().toLowerCase());

            myAudioRepo.save(myAudio);
            e.setText(myAudio.getTitle()+" Успешно сохранено в бд");

            getFileStream.close();
            fileDownloadStream.close();

        } catch (IOException ioException) {
            ioException.printStackTrace();
            e.setText("Ошибка сохранения");
        }

        list.add(e);
        return list;
    }

    @Override
    public Command getCommand() {
        return Command.AUTO_SAVE;
    }
}
