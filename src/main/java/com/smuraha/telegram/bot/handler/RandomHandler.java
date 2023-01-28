package com.smuraha.telegram.bot.handler;

import com.smuraha.telegram.bot.Command;
import com.smuraha.telegram.model.MyAudio;
import com.smuraha.telegram.model.repo.MyAudioRepo;
import com.smuraha.telegram.util.BotUtil;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendAudio;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Service
public class RandomHandler implements Handler{

    private final MyAudioRepo myAudioRepo;

    public RandomHandler(MyAudioRepo myAudioRepo) {
        this.myAudioRepo = myAudioRepo;
    }

    @Override
    public List<PartialBotApiMethod<? extends Serializable>> handle(Update update) {
        List<PartialBotApiMethod<? extends Serializable>> list = new ArrayList<>();
        MyAudio myAudio = BotUtil.getRandomAudio(myAudioRepo);
        SendAudio sendAudio = new SendAudio();
        BotUtil.setAudioParams(sendAudio,myAudio);
        sendAudio.setChatId(update.getMessage().getChatId()+"");
        list.add(sendAudio);
        return list;
    }

    @Override
    public Command getCommand() {
        return Command.RANDOM;
    }
}
