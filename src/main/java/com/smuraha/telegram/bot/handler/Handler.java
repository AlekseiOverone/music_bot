package com.smuraha.telegram.bot.handler;

import com.smuraha.telegram.bot.Command;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.io.Serializable;
import java.util.List;

public interface Handler {
    List<PartialBotApiMethod<? extends Serializable>> handle(Update update);

    Command getCommand();
}
