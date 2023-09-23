package com.runwithme.runwithme.global.config;

import com.runwithme.runwithme.global.webhook.NotificationManager;
import com.runwithme.runwithme.global.webhook.NotificationSender;
import com.runwithme.runwithme.global.webhook.discord.DiscordSender;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HttpConfiguration {

    @Bean
    public NotificationSender notificationSender() {
        return new DiscordSender();
    }

    @Bean
    public NotificationManager notificationManager() {
        return new NotificationManager(notificationSender());
    }
}
