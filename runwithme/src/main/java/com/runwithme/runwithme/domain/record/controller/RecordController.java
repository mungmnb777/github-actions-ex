package com.runwithme.runwithme.domain.record.controller;

import com.runwithme.runwithme.domain.challenge.dto.ChallengeImageDto;
import com.runwithme.runwithme.domain.record.dto.CoordinateDto;
import com.runwithme.runwithme.domain.record.dto.RunRecordPostDto;
import com.runwithme.runwithme.domain.record.entity.ChallengeTotalRecord;
import com.runwithme.runwithme.domain.record.entity.RunRecord;
import com.runwithme.runwithme.domain.record.service.RecordService;
import com.runwithme.runwithme.global.result.ResultResponseDto;
import io.swagger.v3.oas.annotations.Operation;
//import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

import static com.runwithme.runwithme.global.result.ResultCode.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/challenge")
@Slf4j
public class RecordController {

    private final RecordService recordService;

    //    @ApiOperation(value = "기록 등록")
    @Operation(operationId = "createRunRecord", summary = "기록 등록", description = "기록을 등록한다")
    @PostMapping("/{challengeSeq}/record")
    public ResponseEntity<ResultResponseDto> createRunRecord(
            @PathVariable(value = "challengeSeq") Long challengeSeq,
            @RequestBody RunRecordPostDto runRecordPostDto,
            @Parameter(name = "file", description = "업로드 사진 데이터")
            @RequestParam(name = "file") ChallengeImageDto imgFile
    ){
        try {
            recordService.createRunRecord(challengeSeq, runRecordPostDto, imgFile);
            return ResponseEntity.ok().body(ResultResponseDto.of(CREATE_RECORD_SUCCESS));
        } catch (IOException e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //    @ApiOperation(value = "챌린지내 내 기록 누적 수치")
    @Operation(operationId = "getTotalRecord", summary = "챌린지내 내 기록 누적 수치")
    @GetMapping("/{challengeSeq}/total")
    public ResponseEntity<ResultResponseDto> getTotalRecord(@PathVariable(value = "challengeSeq") Long challengeSeq) {
        final ChallengeTotalRecord myTotals = recordService.getTotalRecord(challengeSeq);
        return ResponseEntity.ok().body(ResultResponseDto.of(GET_MY_TOTAL_RECORD_SUCCESS, myTotals));
    }

    //    @ApiOperation(value = "챌린지내 내 기록 수치 조회")
    @Operation(operationId = "getMyRunRecord", summary = "챌린지내 내 기록 수치 조회")
    @GetMapping("/{challengeSeq}/my")
    public ResponseEntity<ResultResponseDto> getMyRunRecord(@PathVariable(value = "challengeSeq") Long challengeSeq) {
        final List<RunRecord> myRunRecords = recordService.getMyRunRecord(challengeSeq);
        return ResponseEntity.ok().body(ResultResponseDto.of(GET_MY_RECORD_SUCCESS, myRunRecords));
    }

    //    @ApiOperation(value = "챌린지원 기록 수치 조회")
    @Operation(operationId = "getAllRunRecord", summary = "챌린지원 기록 수치 조회")
    @GetMapping("/{challengeSeq}/all")
    public ResponseEntity<ResultResponseDto> getAllRunRecord(@PathVariable(value = "challengeSeq") Long challengeSeq) {
        final List<RunRecord> allRunRecords = recordService.getAllRunRecord(challengeSeq);
        return ResponseEntity.ok().body(ResultResponseDto.of(GET_ALL_RECORD_SUCCESS, allRunRecords));
    }

    // 기록 seq로 상세조회
    @Operation(operationId = "getRecord", summary = "기록 상세조회")
    @GetMapping("/{challengeSeq}/record/{runRecordSeq}")
    public ResponseEntity<ResultResponseDto> getRunRecord(@PathVariable(value = "challengeSeq") Long challengeSeq, @PathVariable(value = "runRecordSeq") Long runRecordSeq) {
        final RunRecord runRecord = recordService.getRunRecord(runRecordSeq);
        return ResponseEntity.ok().body(ResultResponseDto.of(GET_ONE_RECORD_SUCCESS, runRecord));
    }

    @Operation(operationId = "addCoordinate", summary = "Record Coordinate 등록")
    @PostMapping("/{recordSeq}/coordinate")
    public ResponseEntity<ResultResponseDto> addCoordinate(@PathVariable(value = "recordSeq") Long recordSeq, @RequestBody List<CoordinateDto> coordinates) {
        final boolean success = recordService.addCoordinate(recordSeq, coordinates);
        return ResponseEntity.ok().body(ResultResponseDto.of(success ? ADD_COORDINATES_SUCCESS : ADD_COORDINATES_FAIL));
    }
}
