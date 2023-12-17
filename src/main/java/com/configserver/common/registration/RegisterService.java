package com.configserver.common.registration;

import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

public class RegisterService {

    public static void registers() {
        registerTelegram();
    }

    private static void registerTelegram() {
        try {
            TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
            telegramBotsApi.registerBot(new com.configserver.common.alerts.TelegramBot());
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

}
