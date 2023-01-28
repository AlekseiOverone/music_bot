package com.smuraha.telegram.bot.handler;

import com.smuraha.telegram.bot.Command;
import com.smuraha.telegram.model.MyAudio;
import com.smuraha.telegram.model.repo.MyAudioRepo;
import com.smuraha.telegram.util.BotUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Service
public class ListMusicHandler implements Handler {

    private final MyAudioRepo myAudioRepo;
    private final static int LIST_SIZE = 4;

    public ListMusicHandler(MyAudioRepo myAudioRepo) {
        this.myAudioRepo = myAudioRepo;
    }

    @Override
    public List<PartialBotApiMethod<? extends Serializable>> handle(Update update) {
        List<PartialBotApiMethod<? extends Serializable>> list = new ArrayList<>();
        String chatId;
        DeleteMessage deleteMessage = null;
        if (update.getMessage() == null && update.hasCallbackQuery()) {
            CallbackQuery callbackQuery = update.getCallbackQuery();
            chatId = callbackQuery.getFrom().getId() + "";
            deleteMessage = new DeleteMessage();
            deleteMessage.setChatId(chatId);
            deleteMessage.setMessageId(callbackQuery.getMessage().getMessageId());
        } else
            chatId = update.getMessage().getChatId() + "";
        int page = Integer.parseInt(BotUtil.getParamRequest(update).get("/list"));
        String search = BotUtil.getParamRequest(update).get("search");
        Page<MyAudio> myAudios;
        boolean isSearched = search != null;
        if (!isSearched)
            myAudios = myAudioRepo.findAll(PageRequest.of(page, LIST_SIZE));
        else
            myAudios = myAudioRepo.findAllBySearchTitleContains(search.toLowerCase(),PageRequest.of(page, LIST_SIZE));

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        setPagination(sendMessage, page,myAudios.getTotalPages(),search);

        StringBuilder stringList = new StringBuilder();
        myAudios.forEach(myAudio -> stringList.append("Название - ").append(myAudio.getTitle()).append("\n")
                .append("Исполнитель - ").append(myAudio.getPerformer()).append("\n")
                .append("Длительность - ").append(myAudio.getDuration()).append(" сек").append("\n")
                .append("Скачать - ").append("/download_").append(myAudio.getId()).append("\n\n")
        );
        final String listMessage = stringList.toString();
        sendMessage.setText(isSearched?"Показаны результаты поиска по "+search+"\n\n"+listMessage:listMessage);
        list.add(deleteMessage);
        list.add(sendMessage);
        return list;
    }

    private void setPagination(SendMessage sendMessage, int page,int countOfPages,String search) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<InlineKeyboardButton> pager = new ArrayList<>();

        boolean isSearched = search != null;
        String searchParam = "";
        if(isSearched)
            searchParam="&search_"+search;
        if (page > 0) {
            InlineKeyboardButton inlineKeyboardButton = new InlineKeyboardButton();
            inlineKeyboardButton.setText("1");
            inlineKeyboardButton.setCallbackData("/list_0"+searchParam);
            pager.add(inlineKeyboardButton);
        }
        if (page > 1) {
            InlineKeyboardButton inlineKeyboardButton = new InlineKeyboardButton();
            int prev = page - 1;
            inlineKeyboardButton.setText(prev + 1 + "");
            inlineKeyboardButton.setCallbackData("/list_" + prev+searchParam);
            pager.add(inlineKeyboardButton);
        }
        InlineKeyboardButton inlineKeyboardButton = new InlineKeyboardButton();
        inlineKeyboardButton.setText("->" + (page + 1) + "<-");
        inlineKeyboardButton.setCallbackData("/list_" + page+searchParam);
        pager.add(inlineKeyboardButton);
        if (page < countOfPages - 1) {
            InlineKeyboardButton inlineKeyboardButtonNext = new InlineKeyboardButton();
            long next = page + 1;
            inlineKeyboardButtonNext.setText(next + 1 + "");
            inlineKeyboardButtonNext.setCallbackData("/list_" + next+searchParam);
            pager.add(inlineKeyboardButtonNext);
        }
        if (page < (countOfPages - 2)) {
            InlineKeyboardButton inlineKeyboardButtonNext = new InlineKeyboardButton();
            long last = countOfPages - 1;
            inlineKeyboardButtonNext.setText(last + 1 + "");
            inlineKeyboardButtonNext.setCallbackData("/list_" + last+searchParam);
            pager.add(inlineKeyboardButtonNext);
        }
        List<List<InlineKeyboardButton>> finalPager = new ArrayList<>();
        finalPager.add(pager);
        inlineKeyboardMarkup.setKeyboard(finalPager);
        sendMessage.setReplyMarkup(inlineKeyboardMarkup);
    }

    @Override
    public Command getCommand() {
        return Command.LIST_MUSIC;
    }
}
