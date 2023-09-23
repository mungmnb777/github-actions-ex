package com.runwithme.runwithme.global.webhook;

public interface NotificationSender {

    void send(Exception e, String uri, String params);
}
