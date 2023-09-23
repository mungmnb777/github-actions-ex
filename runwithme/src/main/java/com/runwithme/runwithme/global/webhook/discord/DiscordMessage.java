package com.runwithme.runwithme.global.webhook.discord;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public class DiscordMessage {

    private final List<Embed> embeds;

    public DiscordMessage() {
        embeds = new ArrayList<>();
    }

    public DiscordMessage(Embed embed) {
        this();
        embeds.add(embed);
    }

    public DiscordMessage(List<Embed> embeds) {
        this.embeds = embeds;
    }

    @Getter
    public static class Embed {
        String title;
        String description;
        Footer footer;
        int color;

        public Embed(String title, String footer, int color) {
            this.title = title;
            this.footer = new Footer(footer);
            this.color = color;
        }

        public void addExceptionInfo(Exception e) {
            title = e.getClass().getSimpleName();
            description = """
                    **Error Message**
                    `%s`
                    """.formatted(e.getMessage());
        }

        public void addExceptionInfo(Exception e, String uri) {
            addExceptionInfo(e);
            description = """
                    %s
                        
                    **Request URL**
                    `%s`
                    """.formatted(description, uri);
        }

        public void addExceptionInfo(Exception e, String uri, String params) {
            addExceptionInfo(e, uri);
            description = """
                    %s
                        
                    **Parameters**
                    %s
                    """.formatted(description, params.isEmpty() ? "`파라미터가 없습니다.`" : params);
        }
    }

    record Footer(String text) {
    }
}
