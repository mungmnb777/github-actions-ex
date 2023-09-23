package com.runwithme.runwithme.domain.user;

import com.runwithme.runwithme.domain.user.entity.User;

public class MockEntityFactory {

    public static User user() {
        return User.builder()
                .seq(1L)
                .email("mungmnb777@gmail.com")
                .height(173)
                .weight(60)
                .nickname("명범")
                .point(0)
                .build();
    }
}
