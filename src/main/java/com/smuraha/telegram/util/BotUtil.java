package com.smuraha.telegram.util;

import com.smuraha.telegram.model.MyAudio;
import com.smuraha.telegram.model.repo.MyAudioRepo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.telegram.telegrambots.meta.api.methods.send.SendAudio;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.io.ByteArrayInputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BotUtil {

    public static Map<String,String> getParamRequest(Update update) {
        String text;
        if(update.getMessage()==null && update.hasCallbackQuery()){
            text = update.getCallbackQuery().getData();
        }
        else text = update.getMessage().getText();
        Map<String,String> mapParams = new HashMap<>();
        String[] params = text.split("&");
        for (String param:params){
            if(param.contains("_")) {
                String key = param.substring(0, param.indexOf("_"));
                String value = param.substring(param.indexOf("_") + 1);
                mapParams.put(key, value);
            }else
                mapParams.put("/list","0");
        }
        return mapParams;
    }

    public static void setAudioParams(SendAudio sendAudio, MyAudio myAudio) {
        sendAudio.setAudio(new InputFile(new ByteArrayInputStream(myAudio.getAudio()),myAudio.getTitle()));
        sendAudio.setDuration(myAudio.getDuration());
        sendAudio.setPerformer(myAudio.getPerformer());
        sendAudio.setTitle(myAudio.getTitle());
    }

    public static MyAudio getRandomAudio(MyAudioRepo myAudioRepo) {
        long totalCount = myAudioRepo.count();
        int randomId = (int) (Math.random()*totalCount);
        Page<MyAudio> audios = myAudioRepo.findAll(PageRequest.of(randomId, 1));
        return audios.getContent().get(0);
    }
}
