package project.kidscafe.api.user.dto.request;

import io.swagger.v3.oas.annotations.Parameter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;
import project.kidscafe.domain.reservation.ReservationStatus;

import java.time.LocalDate;

@Getter
@ToString
@Builder
@AllArgsConstructor
public final class ReservationHistoryCondition {
    // 사용자 ID
    @Parameter(description = "매장 ID")
    private final Long centerId;

    @Parameter(description = "수업 ID")
    private final Long programId;

    // 조회 날짜
    @Parameter(description = "조회 시작년월")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private final LocalDate searchStartDt;

    @Parameter(description = "조회 종료년월")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private final LocalDate searchEndDt;

    @Parameter(description = "예약 상태 (OK : 예약 , CANCEL : 취소, NULL : 전체) ")
    private final ReservationStatus reservationStatus;
}
