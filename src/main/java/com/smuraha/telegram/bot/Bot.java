package com.smuraha.telegram.bot;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendAudio;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.Serializable;
import java.util.List;

@Component
@Slf4j
public class Bot extends TelegramLongPollingBot {

    private final UpdateReceiver updateReceiver;

    @Value("${bot.name}")
    private String botUserName;

    @Value("${bot.token}")
    private String botToken;

    public Bot(UpdateReceiver updateReceiver) {
        this.updateReceiver = updateReceiver;
    }

    @Override
    public String getBotUsername() {
        return botUserName;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @SneakyThrows
    @Override
    public void onUpdateReceived(Update update) {
        List<PartialBotApiMethod<? extends Serializable>> messagesToSend = updateReceiver.handle(update);

        if (messagesToSend != null && !messagesToSend.isEmpty()) {
            messagesToSend.forEach(response -> {
                if (response instanceof SendMessage) {
                    executeWithExceptionCheck((SendMessage) response);
                }
                else if(response instanceof SendAudio){
                    executeWithExceptionCheck((SendAudio) response);
                }
                else if(response instanceof DeleteMessage){
                    executeWithExceptionCheck((DeleteMessage) response);
                }
            });
        }
    }
    public void executeWithExceptionCheck(SendMessage sendMessage) {
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
            log.error("Ошибка при отправке сообщения пользователю");
        }
    }
    public void executeWithExceptionCheck(DeleteMessage deleteMessage) {
        try {
            execute(deleteMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
            log.error("Ошибка при удалении сообщения");
        }
    }
    public void executeWithExceptionCheck(SendAudio sendAudio) {
        try {
            final long x = System.currentTimeMillis();
            System.out.println(x);
            execute(sendAudio);
            System.out.println(x-System.currentTimeMillis());
        } catch (TelegramApiException e) {
            log.error("Ошибка при отправке аудио пользователю");
        }
    }
}
