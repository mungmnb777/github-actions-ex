package com.runwithme.runwithme.domain.user.service;

import com.runwithme.runwithme.domain.user.MockEntityFactory;
import com.runwithme.runwithme.domain.user.dto.UserCreateDto;
import com.runwithme.runwithme.domain.user.dto.UserProfileDto;
import com.runwithme.runwithme.domain.user.dto.UserProfileViewDto;
import com.runwithme.runwithme.domain.user.entity.User;
import com.runwithme.runwithme.domain.user.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

@ExtendWith(MockitoExtension.class)
@DisplayName("음원 추가 서비스 단위 테스트")
class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

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
        @DisplayName("[성공 케이스]")
        void success() {
            // given
            BDDMockito.given(userRepository.save(any(User.class))).willReturn(MockEntityFactory.user());

            // when
            UserProfileViewDto actual = userService.join(given);

            // then
            Assertions.assertThat(actual).usingRecursiveComparison().isEqualTo(result);

        }

        @Test
        @DisplayName("[실패 케이스 1] 아이디가 중복된 경우")
        void failure() {
            // given
            BDDMockito.given(userRepository.save(any(User.class))).willThrow(IllegalArgumentException.class);

            // when

            // then
            Assertions.assertThatThrownBy(() -> userService.join(given)).isInstanceOf(IllegalArgumentException.class);
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
        @DisplayName("[성공 케이스]")
        void success() {
            // given
            BDDMockito.given(userRepository.findById(eq(1L))).willReturn(Optional.of(MockEntityFactory.user()));

            // when
            UserProfileViewDto actual = userService.setUserProfile(1L, givenDto);

            // then
            Assertions.assertThat(actual).usingRecursiveComparison().isEqualTo(resultDto);

        }

        @Test
        @DisplayName("[실패 케이스 1] 해당 SEQ의 유저가 없는 경우")
        void failure() {
            // given
            BDDMockito.given(userRepository.findById(eq(1L))).willThrow(IllegalArgumentException.class);

            // when

            // then
            Assertions.assertThatThrownBy(() -> userService.setUserProfile(1L, givenDto)).isInstanceOf(IllegalArgumentException.class);
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
        @DisplayName("[성공 케이스]")
        void success() {
            // given
            BDDMockito.given(userRepository.findById(eq(1L))).willReturn(Optional.of(MockEntityFactory.user()));

            // when
            UserProfileViewDto actual = userService.getUserProfile(1L);

            // then
            Assertions.assertThat(actual).usingRecursiveComparison().isEqualTo(resultDto);

        }

        @Test
        @DisplayName("[실패 케이스 1] 해당 SEQ의 유저가 없는 경우")
        void failure() {
            // given
            BDDMockito.given(userRepository.findById(eq(1L))).willThrow(IllegalArgumentException.class);

            // when

            // then
            Assertions.assertThatThrownBy(() -> userService.getUserProfile(1L)).isInstanceOf(IllegalArgumentException.class);
        }
    }
}