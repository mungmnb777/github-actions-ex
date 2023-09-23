package com.runwithme.runwithme.domain.record.service;

import com.runwithme.runwithme.domain.challenge.dto.ChallengeImageDto;
import com.runwithme.runwithme.domain.record.dto.CoordinateDto;
import com.runwithme.runwithme.domain.record.dto.RunRecordPostDto;
import com.runwithme.runwithme.domain.record.entity.ChallengeTotalRecord;
import com.runwithme.runwithme.domain.record.entity.RunRecord;
import com.runwithme.runwithme.domain.record.repository.ChallengeTotalRecordRepository;
import com.runwithme.runwithme.domain.record.repository.RunRecordRepository;
import com.runwithme.runwithme.global.entity.Image;
import com.runwithme.runwithme.global.service.ImageService;
import com.runwithme.runwithme.global.error.CustomException;
import com.runwithme.runwithme.global.utils.AuthUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import static com.runwithme.runwithme.global.result.ResultCode.RECORD_ALREADY_EXIST;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RecordService {

    private final RunRecordRepository runRecordRepository;
    private final ChallengeTotalRecordRepository challengeTotalRecordRepository;

    private final AuthUtils authUtils;
    private final ImageService imageService;

    @Transactional
    public void createRunRecord(Long challengeSeq, RunRecordPostDto runRecordPostDto, ChallengeImageDto imgFile)  throws IOException {
        final Long userSeq = authUtils.getLoginUserSeq();

        final Image savedImage = imageService.save(imgFile.image());

        if (runRecordRepository.existsByUserSeqAndChallengeSeqAndRegTime(userSeq, challengeSeq, LocalDate.now())) {
            throw new CustomException(RECORD_ALREADY_EXIST);
        }

        final RunRecord runRecord = RunRecord.builder()
                .userSeq(userSeq)
                .challengeSeq(challengeSeq)
                .startTime(runRecordPostDto.getStartTime())
                .endTime(runRecordPostDto.getEndTime())
                .runningTime(runRecordPostDto.getRunningTime())
                .runningDistance(runRecordPostDto.getRunningDistance())
                .image(savedImage).build();
        runRecordRepository.save(runRecord);

        final ChallengeTotalRecord myTotals = challengeTotalRecordRepository.findByUserSeqAndChallengeSeq(userSeq, challengeSeq);

        myTotals.setTotalTime(myTotals.getTotalTime() + runRecordPostDto.getRunningTime());
        myTotals.setTotalDistance(myTotals.getTotalDistance() + runRecordPostDto.getRunningDistance());
    }

    @Transactional
    public ChallengeTotalRecord getTotalRecord(Long challengeSeq){
        final Long userSeq = authUtils.getLoginUserSeq();

        final ChallengeTotalRecord myTotals = challengeTotalRecordRepository.findByUserSeqAndChallengeSeq(userSeq, challengeSeq);

        return myTotals;
    }

    @Transactional
    public List<RunRecord> getMyRunRecord(Long challengeSeq){
        final Long userSeq = authUtils.getLoginUserSeq();

        final List<RunRecord> myRunRecords = runRecordRepository.findAllByUserSeqAndChallengeSeq(userSeq, challengeSeq);

        return myRunRecords;
    }

    @Transactional
    public List<RunRecord> getAllRunRecord(Long challengeSeq){
        final Long userSeq = authUtils.getLoginUserSeq();

        final List<RunRecord> allRunRecords = runRecordRepository.findAllByChallengeSeq(challengeSeq);

        return allRunRecords;
    }

    @Transactional
    public RunRecord getRunRecord(Long runRecordSeq){
        final Long userSeq = authUtils.getLoginUserSeq();

        final RunRecord runRecord = runRecordRepository.findById(runRecordSeq).get();

        return runRecord;
    }

    @Transactional
    public boolean addCoordinate(Long recordSeq, List<CoordinateDto> coordinates) {
        int[] results = runRecordRepository.coordinatesInsertBatch(recordSeq, coordinates);
        int success = 0;

        for (int result : results) {
            if (result == -2) {
                success++;
            }
        }
        return results.length == success;
    }
}
