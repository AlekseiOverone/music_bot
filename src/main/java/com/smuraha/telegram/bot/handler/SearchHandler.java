package com.smuraha.telegram.bot.handler;

import com.smuraha.telegram.bot.Command;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.io.Serializable;
import java.util.List;

@Service
public class SearchHandler implements Handler{

    private final ListMusicHandler listMusicHandler;

    public SearchHandler(ListMusicHandler listMusicHandler) {
        this.listMusicHandler = listMusicHandler;
    }

    @Override
    public List<PartialBotApiMethod<? extends Serializable>> handle(Update update) {
        update.getMessage().setText("/list_0&search_"+update.getMessage().getText());
        return listMusicHandler.handle(update);
    }

    @Override
    public Command getCommand() {
        return Command.SEARCH;
    }
}
