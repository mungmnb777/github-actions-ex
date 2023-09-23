package com.runwithme.runwithme.domain.user.controller;

import com.runwithme.runwithme.domain.user.dto.UserCreateDto;
import com.runwithme.runwithme.domain.user.dto.UserProfileDto;
import com.runwithme.runwithme.domain.user.dto.UserProfileViewDto;
import com.runwithme.runwithme.domain.user.service.UserService;
import com.runwithme.runwithme.utils.JacksonUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc
@DisplayName("유저 컨트롤러 단위 테스트")
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Nested
    @DisplayName("[회원가입] 사용자는 이메일을 이용하여 회원가입할 수 있습니다.")
    class Join {

        UserCreateDto given;
        UserProfileViewDto result;

        @BeforeEach
        void beforeEach() {
            given = new UserCreateDto("mungmnb777@gmail.com", "asd12345", "명범", 173, 60);
            result = UserProfileViewDto.builder()
                    .seq(1L)
                    .email("mungmnb777@gmail.com")
                    .height(173)
                    .weight(60)
                    .nickname("명범")
                    .point(0)
                    .build();
        }

        @Test
        @WithMockUser(roles = "USER")
        @DisplayName("[성공 케이스]")
        void success() throws Exception {
            // given
            BDDMockito.given(userService.join(any(UserCreateDto.class))).willReturn(result);

            // when
            ResultActions actions = mockMvc.perform(MockMvcRequestBuilders.post("/users/join")
                    .content(JacksonUtils.convertToJson(given))
                    .contentType(MediaType.APPLICATION_JSON)
                    .with(SecurityMockMvcRequestPostProcessors.csrf()));

            // then
            actions.andExpect(jsonPath("$.data.seq").value(1L));
            actions.andExpect(jsonPath("$.data.email").value("mungmnb777@gmail.com"));
            actions.andExpect(jsonPath("$.data.height").value(173));
            actions.andExpect(jsonPath("$.data.weight").value(60));
            actions.andExpect(jsonPath("$.data.nickname").value("명범"));
            actions.andExpect(jsonPath("$.data.point").value(0));
            actions.andExpect(jsonPath("$.code").value("U002"));
            actions.andExpect(status().isCreated());
        }

        @Test
        @WithMockUser(roles = "USER")
        @DisplayName("[실패 케이스 1] 해당 이메일을 가진 유저가 이미 있는 경우")
        void failure() throws Exception {
            // given
            BDDMockito.given(userService.join(any(UserCreateDto.class))).willThrow(IllegalArgumentException.class);

            // when
            ResultActions actions = mockMvc.perform(MockMvcRequestBuilders.post("/users/join")
                    .content(JacksonUtils.convertToJson(given))
                    .contentType(MediaType.APPLICATION_JSON)
                    .with(SecurityMockMvcRequestPostProcessors.csrf()));

            // then
            actions.andExpect(jsonPath("$.code").value("U101"));
            actions.andExpect(status().isBadRequest());
        }
    }

    @Nested
    @DisplayName("[프로필 수정] 사용자는 유저 프로필(닉네임, 키, 몸무게)를 수정할 수 있습니다.")
    class SetUserProfile {

        UserProfileDto givenDto;
        UserProfileViewDto resultDto;

        @BeforeEach
        void beforeEach() {
            givenDto = new UserProfileDto("명범", 173, 60);
            resultDto = UserProfileViewDto.builder()
                    .seq(1L)
                    .email("mungmnb777@gmail.com")
                    .height(173)
                    .weight(60)
                    .nickname("명범")
                    .point(0)
                    .build();
        }

        @Test
        @WithMockUser(roles = "USER")
        @DisplayName("[성공 케이스]")
        void success() throws Exception {
            // given
            BDDMockito.given(userService.setUserProfile(eq(1L), any(UserProfileDto.class))).willReturn(resultDto);

            // when
            ResultActions actions = mockMvc.perform(MockMvcRequestBuilders.put("/users/1/profile")
                    .content(JacksonUtils.convertToJson(givenDto))
                    .contentType(MediaType.APPLICATION_JSON)
                    .with(SecurityMockMvcRequestPostProcessors.csrf()));

            // then
            actions.andExpect(jsonPath("$.data.seq").value(1L));
            actions.andExpect(jsonPath("$.data.email").value("mungmnb777@gmail.com"));
            actions.andExpect(jsonPath("$.data.height").value(173));
            actions.andExpect(jsonPath("$.data.weight").value(60));
            actions.andExpect(jsonPath("$.data.nickname").value("명범"));
            actions.andExpect(jsonPath("$.data.point").value(0));
            actions.andExpect(jsonPath("$.code").value("U002"));
            actions.andExpect(status().isOk());
        }

        @Test
        @WithMockUser(roles = "USER")
        @DisplayName("[실패 케이스 1] 해당 SEQ의 유저가 없는 경우")
        void failure() throws Exception {
            // given
            BDDMockito.given(userService.setUserProfile(eq(1L), any(UserProfileDto.class))).willThrow(IllegalArgumentException.class);

            // when
            ResultActions actions = mockMvc.perform(MockMvcRequestBuilders.put("/users/1/profile")
                    .content(JacksonUtils.convertToJson(givenDto))
                    .contentType(MediaType.APPLICATION_JSON)
                    .with(SecurityMockMvcRequestPostProcessors.csrf()));

            // then
            actions.andExpect(jsonPath("$.code").value("U101"));
            actions.andExpect(status().isBadRequest());
        }
    }

    @Nested
    @DisplayName("[프로필 조회] 사용자는 SEQ를 이용해 유저 프로필을 조회할 수 있습니다.")
    class GetUserProfile {
        UserProfileViewDto resultDto;

        @BeforeEach
        void beforeEach() {
            resultDto = UserProfileViewDto.builder()
                    .seq(1L)
                    .email("mungmnb777@gmail.com")
                    .height(173)
                    .weight(60)
                    .nickname("명범")
                    .point(0)
                    .build();
        }

        @Test
        @WithMockUser(roles = "USER")
        @DisplayName("[성공 케이스]")
        void success() throws Exception {
            // given
            BDDMockito.given(userService.getUserProfile(eq(1L))).willReturn(resultDto);

            // when
            ResultActions actions = mockMvc.perform(MockMvcRequestBuilders.get("/users/1")
                    .with(SecurityMockMvcRequestPostProcessors.csrf()));

            // then
            actions.andExpect(jsonPath("$.data.seq").value(1L));
            actions.andExpect(jsonPath("$.data.email").value("mungmnb777@gmail.com"));
            actions.andExpect(jsonPath("$.data.height").value(173));
            actions.andExpect(jsonPath("$.data.weight").value(60));
            actions.andExpect(jsonPath("$.data.nickname").value("명범"));
            actions.andExpect(jsonPath("$.data.point").value(0));
            actions.andExpect(jsonPath("$.code").value("U002"));
            actions.andExpect(status().isOk());
        }

        @Test
        @WithMockUser(roles = "USER")
        @DisplayName("[실패 케이스 1] 해당 SEQ의 유저가 없는 경우")
        void failure() throws Exception {
            // given
            BDDMockito.given(userService.getUserProfile(eq(1L))).willThrow(IllegalArgumentException.class);

            // when
            ResultActions actions = mockMvc.perform(MockMvcRequestBuilders.get("/users/1")
                    .with(SecurityMockMvcRequestPostProcessors.csrf()));

            // then
            actions.andExpect(jsonPath("$.code").value("U101"));
            actions.andExpect(status().isBadRequest());
        }
    }
}