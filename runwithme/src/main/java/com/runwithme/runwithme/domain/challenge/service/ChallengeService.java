package com.runwithme.runwithme.domain.challenge.service;

import com.runwithme.runwithme.domain.challenge.dto.*;
import com.runwithme.runwithme.domain.challenge.entity.Challenge;
import com.runwithme.runwithme.domain.challenge.entity.ChallengeBoard;
import com.runwithme.runwithme.domain.challenge.entity.ChallengeBoardWarn;
import com.runwithme.runwithme.domain.challenge.entity.ChallengeUser;
import com.runwithme.runwithme.domain.challenge.repository.ChallengeBoardRepository;
import com.runwithme.runwithme.domain.challenge.repository.ChallengeBoardWarnRepository;
import com.runwithme.runwithme.domain.challenge.repository.ChallengeRepository;
import com.runwithme.runwithme.domain.challenge.repository.ChallengeUserRepository;
import com.runwithme.runwithme.domain.user.entity.User;
import com.runwithme.runwithme.domain.user.repository.UserRepository;
import com.runwithme.runwithme.global.dto.PagingResultDto;
import com.runwithme.runwithme.global.error.CustomException;
import com.runwithme.runwithme.global.entity.Image;
import com.runwithme.runwithme.global.repository.ImageRepository;
import com.runwithme.runwithme.global.service.ImageService;
import com.runwithme.runwithme.global.utils.AuthUtils;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

import static com.runwithme.runwithme.global.result.ResultCode.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChallengeService {
    private final ChallengeRepository challengeRepository;
    private final ChallengeBoardRepository challengeBoardRepository;
    private final ChallengeUserRepository challengeUserRepository;
    private final ChallengeBoardWarnRepository challengeBoardWarnRepository;
    private final ImageService imageService;
    private final ImageRepository imageRepository;

    private final AuthUtils authUtils;

    @Transactional
    public void createBoard(Long challengeSeq, ChallengeBoardPostDto challengeBoardPostDto) {
        final User user = authUtils.getLoginUser();

        final Image savedImage = imageService.save(challengeBoardPostDto.getImage());

        final LocalDateTime challengeBoardRegTime = LocalDateTime.now();
        final ChallengeBoard challengeBoard = ChallengeBoard.builder()
                .user(user)
                .challengeSeq(challengeSeq)
                .image(savedImage)
                .challengeBoardContent(challengeBoardPostDto.getChallengeBoardContent())
                .challengeBoardRegTime(challengeBoardRegTime).build();
        challengeBoardRepository.save(challengeBoard);
    }

    @Transactional
    public PagingResultDto<ChallengeBoardResponseDto> getBoardList(Long cursorSeq, Long challengeSeq, Pageable pageable){
        final Long userSeq = authUtils.getLoginUserSeq();

        final Page<ChallengeBoardResponseDto> allBoards = challengeBoardRepository.findAllBoardPage(cursorSeq, userSeq, challengeSeq, pageable);
        return new PagingResultDto<>(allBoards.getContent());
    }

    @Transactional
    public void deleteBoard(Long boardSeq){
        final User user = authUtils.getLoginUser();
        challengeBoardRepository.deleteById(boardSeq);
    }

    @Transactional
    public void createChallenge(ChallengeCreateDto challengeCreateDto) {
        final User user = authUtils.getLoginUser();

        final Image savedImage = imageService.save(challengeCreateDto.getImage());

        final Challenge challenge = Challenge.builder()
                .manager(user)
                .image(savedImage)
                .name(challengeCreateDto.getName())
                .description(challengeCreateDto.getDescription())
                .goalDays(challengeCreateDto.getGoalDays())
                .goalType(challengeCreateDto.getGoalType())
                .goalAmount(challengeCreateDto.getGoalAmount())
                .timeStart(challengeCreateDto.getTimeStart())
                .timeEnd(challengeCreateDto.getTimeEnd())
                .password(challengeCreateDto.getPassword())
                .cost(challengeCreateDto.getCost())
                .maxMember(challengeCreateDto.getMaxMember())
                .build();

        challengeRepository.save(challenge);
    }

    @Transactional
    public Challenge getChallengeData(Long challengeSeq) {
        return challengeRepository.findById(challengeSeq).get();
    }

    @Transactional
    public boolean joinChallengeUser(Long challengeSeq, String password) {
        final User user = authUtils.getLoginUser();

        if (challengeUserRepository.existsByUserSeqAndChallengeSeq(user.getSeq(), challengeSeq)) {
            throw new CustomException(CHALLENGE_JOIN_ALREADY_EXIST);
        }
        final Challenge challenge = challengeRepository.findById(challengeSeq).get();
        if (Objects.equals(challenge.getPassword(), password)) {
            challengeUserRepository.save(new ChallengeUser(user, challenge));
            return true;
        }
        return false;
    }

    @Transactional
    public boolean isChallengeUser(Long challengeSeq) {
        final Long userSeq = authUtils.getLoginUserSeq();
        if (challengeUserRepository.existsByUserSeqAndChallengeSeq(userSeq, challengeSeq)) {
            throw new CustomException(CHALLENGE_JOIN_ALREADY_EXIST);
        }
        return true;
    }

    @Transactional
    public PagingResultDto getAllChallengeList(Long cursorSeq, Pageable pageable) {
        final Long userSeq = authUtils.getLoginUserSeq();
        final Page<ChallengeResponseDto> challenges = challengeRepository.findAllChallengePage(cursorSeq, userSeq, pageable);
        return new PagingResultDto<>(challenges.getContent());
    }

    @Transactional
    public PagingResultDto getRecruitChallengeList(Long cursorSeq, Pageable pageable) {
        final Long userSeq = authUtils.getLoginUserSeq();
        final LocalDate localDate = LocalDate.now();
        final Page<ChallengeResponseDto> challenges = challengeRepository.findRecruitChallengePage(cursorSeq, userSeq, localDate, pageable);
        return new PagingResultDto<>(challenges.getContent());
    }

    @Transactional
    public PagingResultDto getMyChallengeList(Long cursorSeq, Pageable pageable) {
        final Long userSeq = authUtils.getLoginUserSeq();
        final Page<ChallengeResponseDto> challenges = challengeRepository.findMyChallengePage(cursorSeq, userSeq, pageable);
        return new PagingResultDto<>(challenges.getContent());
    }

    @Transactional
    public boolean boardWarn(Long boardSeq) {
        final User user = authUtils.getLoginUser();
        final ChallengeBoard challengeBoard = challengeBoardRepository.findById(boardSeq)
                .orElseThrow(()->new CustomException(BOARD_NOT_FOUND));

        if(challengeBoardWarnRepository.existsByUserAndChallengeBoard(user, challengeBoard)) {
            throw new CustomException(WARN_BOARD_ALREADY_EXIST);
        }

        final ChallengeBoardWarn challengeBoardWarn = new ChallengeBoardWarn(user, challengeBoard);
        challengeBoardWarnRepository.save(challengeBoardWarn);

        return true;
    }
}
