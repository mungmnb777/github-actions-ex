package com.runwithme.runwithme.domain.user.service;

import com.runwithme.runwithme.domain.user.dto.*;
import com.runwithme.runwithme.domain.user.dto.converter.UserConverter;
import com.runwithme.runwithme.domain.user.entity.User;
import com.runwithme.runwithme.domain.user.repository.UserRepository;
import com.runwithme.runwithme.global.error.CustomException;
import com.runwithme.runwithme.global.service.ImageService;
import com.runwithme.runwithme.global.utils.CacheUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import static com.runwithme.runwithme.global.result.ResultCode.*;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final ImageService imageService;

    public UserProfileViewDto join(UserCreateDto dto) {
        if (userRepository.existsByEmail(dto.email())) {
            throw new CustomException(EMAIL_EXISTS);
        }

        User joinUser = UserConverter.toEntity(dto);
        return UserConverter.toViewDto(userRepository.save(joinUser));
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new CustomException(USER_NOT_FOUND));
    }

    public UserProfileViewDto setUserProfile(Long userSeq, UserProfileDto dto) {
        User findUser = userRepository.findById(userSeq).orElseThrow(() -> new CustomException(SEQ_NOT_FOUND));
        findUser.setProfile(dto);
        return UserConverter.toViewDto(findUser);
    }

    public UserProfileViewDto getUserProfile(Long userSeq) {
        User findUser = userRepository.findById(userSeq).orElseThrow(() -> new CustomException(SEQ_NOT_FOUND));
        return UserConverter.toViewDto(findUser);
    }

    public DuplicatedViewDto isDuplicatedEmail(String email) {
        return new DuplicatedViewDto(userRepository.existsByEmail(email));
    }

    public DuplicatedViewDto isDuplicatedNickname(String nickname) {
        return new DuplicatedViewDto(userRepository.existsByNickname(nickname));
    }

    public Resource getUserImage(Long userSeq) {
        User user = userRepository.findById(userSeq).orElseThrow(() -> new CustomException(SEQ_NOT_FOUND));
        return imageService.getImage(user.getImage().getSeq());
    }

    public void changeImage(Long userSeq, UserProfileImageDto dto) {
        User user = userRepository.findById(userSeq).orElseThrow(() -> new CustomException(SEQ_NOT_FOUND));

        if (!ObjectUtils.nullSafeEquals(user.getImage(), CacheUtils.get("defaultImage"))) {
            imageService.delete(user.getImage().getSeq());
        }
        user.changeImage(imageService.save(dto.image()));
    }
}
