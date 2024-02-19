package project.kidscafe.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    DUPLICATE_RESERVATION(BAD_REQUEST, "수업을 중복해서 예약할 수 없습니다."),
    NOT_FOUNDED_CHILD(BAD_REQUEST, "자식 데이터가 존재하지 않습니다."),
    NOT_FOUNDED_PARENT(BAD_REQUEST, "부모 데이터가 존재하지 않습니다."),
    NOT_FOUNDED_PROGRAM_SCHEDULE(BAD_REQUEST, "수업 일정 데이터가 존재하지 않습니다."),
    NOT_FOUNDED_RESERVATION(BAD_REQUEST, "예약 데이터가 존재하지 않습니다."),
    NOT_AVAILABLE_PROGRAM_SCHEDULE(BAD_REQUEST, "유효하지 않은 수업 일정입니다."),
    OVER_THE_MAX_RESERVATION_CNT(BAD_REQUEST, "수업의 최대 예약 인원 수를 초과했습니다."),
    DECREASE_RESERVATION_CNT_UNDER_THE_ZERO(INTERNAL_SERVER_ERROR, "예약 인원 수는 0보다 작을 수 없다.");
    private final HttpStatus status;
    private final String message;

}

