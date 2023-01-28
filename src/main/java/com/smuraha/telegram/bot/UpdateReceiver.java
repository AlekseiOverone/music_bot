package com.smuraha.telegram.bot;

import com.smuraha.telegram.bot.handler.Handler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.io.Serializable;
import java.util.List;

@Component
public class UpdateReceiver {

    private final List<Handler> handlers;

    @Autowired
    public UpdateReceiver(List<Handler> handlers) {
        this.handlers = handlers;
    }

    public List<PartialBotApiMethod<? extends Serializable>> handle(Update update) {

        if (update.getMessage()!=null) {
            if (update.getMessage().hasAudio()) {
                return getHandlerByCommand(Command.AUTO_SAVE).handle(update);
            } else if (update.getMessage().hasText() && update.getMessage().getText().startsWith("/")) {
                String textCommand = update.getMessage().getText();
                Command command = getCommand(textCommand);
                return getHandlerByCommand(command).handle(update);
            } else if(update.getMessage().hasText() && !update.getMessage().getText().startsWith("/")){
                return getHandlerByCommand(Command.SEARCH).handle(update);
            }
        }
        else if (update.hasCallbackQuery()) {
            final CallbackQuery callbackQuery = update.getCallbackQuery();
            String textCommand = callbackQuery.getData();
            Command command = getCommand(textCommand);
            return getHandlerByCommand(command).handle(update);
        }
        throw new UnsupportedOperationException();
    }

    private Command getCommand(String textCommand) {
        if (textCommand.contains("_"))
            textCommand = textCommand.substring(0, textCommand.indexOf("_"));
        Command command;
        try {
            command = Command.getCommandByName(textCommand);
        } catch (Exception e) {
            command = Command.HELP;
        }
        return command;
    }

    private Handler getHandlerByCommand(Command command) {
        return handlers.stream().filter(handler -> handler.getCommand()
                .equals(command)).findAny().orElseThrow(UnsupportedOperationException::new);
    }
}
