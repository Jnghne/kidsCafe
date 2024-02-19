package project.kidscafe.api.user.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import project.kidscafe.domain.reservation.ReservationStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@ToString
@NoArgsConstructor
public class ReservationHistoryResponse {
    // 매장 정보
    private String centerName;
    // 수업 정보
    private String programName;
    // 수업 일정 정보
    private LocalDate scheduleStartDate;
    private LocalTime scheduleStartTime;
    // 예약 정보
    private Long reservationId;
    private String parentName;
    private Long childCnt;
    private ReservationStatus reservationStatus;
    private LocalDateTime reservationDt;
    private LocalDateTime cancelDt;

}
