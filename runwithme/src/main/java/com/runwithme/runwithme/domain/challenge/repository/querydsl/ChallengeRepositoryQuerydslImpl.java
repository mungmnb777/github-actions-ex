package com.runwithme.runwithme.domain.challenge.repository.querydsl;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.runwithme.runwithme.domain.challenge.dto.ChallengeResponseDto;
import com.runwithme.runwithme.domain.challenge.dto.QChallengeResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import static com.runwithme.runwithme.domain.challenge.entity.QChallenge.challenge;
import static com.runwithme.runwithme.domain.challenge.entity.QChallengeUser.challengeUser;

@RequiredArgsConstructor
public class ChallengeRepositoryQuerydslImpl implements ChallengeRepositoryQuerydsl{

    private final JPAQueryFactory jpaQueryFactory;

    public Optional<ChallengeResponseDto> findChallengeBySeq(Long userSeq, Long challengeSeq){
        return Optional.ofNullable(jpaQueryFactory.select(new QChallengeResponseDto(
                challenge.seq,
                challenge.manager.seq,
                challenge.manager.nickname,
                challenge.image,
                challenge.name,
                challenge.goalDays,
                challenge.goalType,
                challenge.goalAmount,
                challenge.timeStart,
                challenge.timeEnd,
                challenge.nowMember,
                challenge.maxMember,
                challenge.cost,
                isExistChallengeUserWhereChallengeEqAndUserEq(userSeq)
            )
        ).from(challenge)
                .where(challenge.seq.eq(challengeSeq))
                .fetchOne());
    }
    @Override
    public Page<ChallengeResponseDto> findAllChallengePage(Long cursorSeq, Long userSeq, Pageable pageable){
        QueryResults<ChallengeResponseDto> result = jpaQueryFactory.select(new QChallengeResponseDto(
                        challenge.seq,
                        challenge.manager.seq,
                        challenge.manager.nickname,
                        challenge.image,
                        challenge.name,
                        challenge.goalDays,
                        challenge.goalType,
                        challenge.goalAmount,
                        challenge.timeStart,
                        challenge.timeEnd,
                        challenge.nowMember,
                        challenge.maxMember,
                        challenge.cost,
                        isExistChallengeUserWhereChallengeEqAndUserEq(userSeq)
                        )
                ).from(challenge)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();
        return new PageImpl<>(result.getResults(), pageable, result.getTotal());
    }
    @Override
    public Page<ChallengeResponseDto> findRecruitChallengePage(Long cursorSeq, Long userSeq, LocalDate nowTime, Pageable pageable){
        QueryResults<ChallengeResponseDto> result = jpaQueryFactory.select(new QChallengeResponseDto(
                                challenge.seq,
                                challenge.manager.seq,
                                challenge.manager.nickname,
                                challenge.image,
                                challenge.name,
                                challenge.goalDays,
                                challenge.goalType,
                                challenge.goalAmount,
                                challenge.timeStart,
                                challenge.timeEnd,
                                challenge.nowMember,
                                challenge.maxMember,
                                challenge.cost,
                                isExistChallengeUserWhereChallengeEqAndUserEq(userSeq)
                        )
                ).from(challenge)
                .where(challenge.timeStart.after(nowTime).and(challenge.nowMember.lt(challenge.maxMember))
                        , eqCursorSeq(cursorSeq))
                .orderBy(challenge.timeStart.desc())
                .limit(pageable.getPageSize())
                .fetchResults();
        return new PageImpl<>(result.getResults(), pageable, result.getTotal());
    }

    @Override
    public Page<ChallengeResponseDto> findMyChallengePage(Long cursorSeq, Long userSeq, Pageable pageable){
        QueryResults<ChallengeResponseDto> result = jpaQueryFactory.select(new QChallengeResponseDto(
                        challengeUser.challenge.seq,
                        challengeUser.challenge.manager.seq,
                        challengeUser.challenge.manager.nickname,
                        challengeUser.challenge.image,
                        challengeUser.challenge.name,
                        challengeUser.challenge.goalDays,
                        challengeUser.challenge.goalType,
                        challengeUser.challenge.goalAmount,
                        challengeUser.challenge.timeStart,
                        challengeUser.challenge.timeEnd,
                        challengeUser.challenge.nowMember,
                        challengeUser.challenge.maxMember,
                        challengeUser.challenge.cost,
                        isExistChallengeUserWhereChallengeEqAndUserEq(userSeq)
                        )
                ).from(challengeUser)
                .where(challengeUser.user.seq.eq(userSeq))
                .orderBy(challengeUser.challenge.seq.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();
        return new PageImpl<>(result.getResults(), pageable, result.getTotal());
    }



    private BooleanExpression isExistChallengeUserWhereChallengeEqAndUserEq(Long userSeq){
        return JPAExpressions
                .selectFrom(challengeUser)
                .where(challengeUser.challenge.seq.eq(challenge.seq).and(challengeUser.user.seq.eq(userSeq)))
                .exists();
    }

    private BooleanExpression eqCursorSeq(Long cursorSeq) {
        return cursorSeq == null ? null : challenge.seq.gt(cursorSeq);
    }
}
