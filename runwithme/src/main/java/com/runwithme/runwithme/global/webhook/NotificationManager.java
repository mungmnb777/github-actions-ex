package com.runwithme.runwithme.global.webhook;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class NotificationManager {
    private final NotificationSender notificationSender;

    public void sendNotification(Exception e, String uri, String params) {
        notificationSender.send(e, uri, params);
    }
}
