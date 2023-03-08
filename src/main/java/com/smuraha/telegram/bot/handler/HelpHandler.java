package com.smuraha.telegram.bot.handler;

import com.smuraha.telegram.bot.Command;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Service
public class HelpHandler implements Handler{

    @Override
    public List<PartialBotApiMethod<? extends Serializable>> handle(Update update) {
        List<PartialBotApiMethod<? extends Serializable>> list = new ArrayList<>();
        SendMessage message = new SendMessage();
        message.setChatId(update.getMessage().getChatId()+"");
        message.setText("Это сборник музыки, любой может добавить свою. Достаточно лишь прикрепить файл \n" +
                "Используя команду /random вы получите случайную музыку. \n" +
                "Для получения списка доступной музыки введите /list \n" +
                "Для поиска конкретной музыки введите часть названия.");
        list.add(message);
        return list;
    }

    @Override
    public Command getCommand() {
        return Command.HELP;
    }
}
