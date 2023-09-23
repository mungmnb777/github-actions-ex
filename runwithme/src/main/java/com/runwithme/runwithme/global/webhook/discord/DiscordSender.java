package com.runwithme.runwithme.global.webhook.discord;

import com.nimbusds.jose.shaded.gson.Gson;
import com.runwithme.runwithme.global.webhook.NotificationSender;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static com.runwithme.runwithme.global.webhook.discord.DiscordMessage.Embed;

@Slf4j
@RequiredArgsConstructor
public class DiscordSender implements NotificationSender {

    @Value("#{new Boolean('${notification.discord.enabled}')}")
    private boolean enabled;

    @Value("${notification.discord.webhook-id}")
    private String webhookId;

    @Value("${notification.discord.webhook-token}")
    private String webhookToken;

    @Override
    public void send(Exception e, String uri, String params) {
        if (!enabled) return;

        Embed embed = new Embed(
        "Error",
            LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
            16515072
        );
        embed.addExceptionInfo(e, uri, params);

        DiscordMessage message = new DiscordMessage(embed);
        String payload = new Gson().toJson(message);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-type", MediaType.APPLICATION_JSON_VALUE);

        String requestUri = "https://discord.com/api/webhooks/%s/%s".formatted(webhookId, webhookToken);
        HttpEntity<String> entity = new HttpEntity<>(payload, headers);

        new RestTemplate().postForEntity(
                requestUri,
                entity,
                String.class
        );
    }
}
