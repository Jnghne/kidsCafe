package project.kidscafe.api.center.dto.request;

import io.swagger.v3.oas.annotations.Parameter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@AllArgsConstructor
@ToString
@Getter
@Builder
public final class ReservationSearchCondition {

    @Parameter(description = "수업 ID", example = "1", allowEmptyValue = true)
    private final Long programId;

    @Parameter(description = "수업 별 일정 ID", example = "1", allowEmptyValue = true)
    private final Long programScheduleId;

    @Parameter(description = "예약자 조회 시작년월", example = "2024-02-20", allowEmptyValue = true)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private final LocalDate searchStartDt;

    @Parameter(description = "예약자 조회 종료년월", example = "2024-02-25", allowEmptyValue = true)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private final LocalDate searchEndDt;

    @Parameter(description = "예약자(부모) 이름", example = "김부모", allowEmptyValue = true)
    private final String parentName;
}
