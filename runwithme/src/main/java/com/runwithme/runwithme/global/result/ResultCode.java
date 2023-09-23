package com.runwithme.runwithme.global.result;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * ResultCode Convention
 * - 도메인 별로 나누어 관리
 * - [동사_목적어_SUCCESS] 형태로 생성
 * - 코드는 도메인명 앞에서부터 1~2글자로 사용
 * - 메시지는 "~~다."로 마무리
 */

@Getter
@AllArgsConstructor
public enum ResultCode {
    // User
    LOGIN_SUCCESS                   (200, 100, "로그인에 성공하였습니다."),
    USER_REQUEST_SUCCESS            (200, 101, "요청을 성공적으로 수행했습니다."),
    INVALID_PARAMETER_FAIL          (400, -100, "잘못된 파라미터입니다."),
    USER_NOT_FOUND                  (400, -101, "존재하지 않는 유저입니다."),
    SEQ_NOT_FOUND                   (400, -102, "해당 SEQ를 가진 유저가 존재하지 않습니다."),
    EMAIL_EXISTS                    (400, -103, "이미 존재하는 이메일입니다."),
    UNAUTHORIZED                    (400, -104, "인증에 실패했습니다."),
    INTERNAL_SERVER_ERROR           (400, -105, "서버 오류입니다."),
    DELETED_USER                    (400, -106, "삭제된 회원입니다."),
    BAD_PASSWORD                    (400, -107, "잘못된 패스워드입니다."),
    UNAUTHORIZED_USER               (400, -108, "권한 없는 사용자입니다."),
    REDIRECT_NOT_FOUND              (400, -109, "리디렉션 URI를 포함해야 합니다"),

    // Record
    CREATE_RECORD_SUCCESS           (200, 200, "기록 등록에 성공하였습니다."),
    GET_MY_TOTAL_RECORD_SUCCESS     (200, 201, "내 기록 누적 수치 조회에 성공하였습니다."),
    GET_MY_RECORD_SUCCESS           (200, 202, "내 기록 수치 조회에 성공하였습니다."),
    GET_ONE_RECORD_SUCCESS          (200, 203, "기록 상세 조회에 성공하였습니다."),
    GET_ALL_RECORD_SUCCESS          (200, 204, "기록 전체 조회에 성공하였습니다."),
    ADD_COORDINATES_SUCCESS         (200, 205, "좌표 등록에 성공하였습니다."),
    ADD_COORDINATES_FAIL            (200, 206, "좌표 등록에 실패하였습니다."),
    RECORD_ALREADY_EXIST            (400, -200, "오늘 이미 기록을 등록 하였습니다."),

    // Challenge
    GET_ONE_CHALLENGE_SUCCESS       (200, 300, "챌린지 상세 조회에 성공하였습니다."),
    GET_ALL_CHALLENGE_SUCCESS       (200, 301, "챌린지 전체 조회에 성공하였습니다."),
    JOIN_CHALLENGE_SUCCESS          (200, 302, "챌린지 가입에 성공하였습니다."),
    JOIN_CHALLENGE_FAIL             (200, 303, "챌린지 가입에 실패하였습니다."),
    CHECK_IN_CHALLENGE_SUCCESS      (200, 304, "챌린지 가입되어 있는 계정입니다."),
    CHECK_IN_CHALLENGE_FAIL         (200, 305, "챌린지 가입되지 않은 계정입니다."),
    GET_MY_CHALLENGE_SUCCESS        (200, 306, "가입한 챌린지 조회에 성공하였습니다."),
    CHALLENGE_NOT_FOUND             (400, -300, "존재하지 않는 챌린지입니다."),
    CHALLENGE_JOIN_ALREADY_EXIST    (400, -301, "이미 가입한 챌린지 입니다."),

    // Board
    CREATE_BOARD_SUCCESS            (200, 400, "게시글 등록에 성공하였습니다."),
    GET_ONE_BOARD_SUCCESS           (200, 401, "게시글 상세 조회에 성공하였습니다."),
    GET_ALL_BOARD_SUCCESS           (200, 402, "게시글 전체 조회에 성공하였습니다."),
    DELETE_BOARD_SUCCESS            (200, 403, "게시글 삭제에 성공하였습니다."),
    WARN_BOARD_SUCCESS              (200, 404, "게시글 신고에 성공하였습니다."),
    WARN_BOARD_FAIL                 (200, 405, "게시글 신고에 실패하였습니다."),
    BOARD_NOT_FOUND                 (400, -400, "존재하지 않는 게시글 입니다."),
    WARN_BOARD_ALREADY_EXIST        (400, -401, "이미 신고한 게시글 입니다."),


    // Image
    IMAGE_NOT_FOUND                 (400, -500, "이미지를 찾을 수 없습니다."),
    FAILED_CONVERT                  (400, -501, "잘못된 파일입니다."),

    // Auth
    FAILED_GENERATE_TOKEN           (400, -600, "토큰을 생성할 수 없습니다."),
    HEADER_NO_TOKEN                 (400, -601, "헤더에 토큰이 존재하지 않습니다."),
    INVALID_JWT_SIGNATURE           (400, -602, "시그니처가 유효하지 않습니다."),
    INVALID_JWT_TOKEN               (400, -603, "JWT Token이 유효하지 않습니다."),
    EXPIRED_JWT_TOKEN               (400, -604, "JWT Token이 만료되었습니다."),
    UNSUPPORTED_JWT_TOKEN           (400, -605, "지원되지 않는 Token입니다."),
    ;

    private final int status;
    private final int code;
    private final String message;
}
