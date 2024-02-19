package project.kidscafe.api.reservation.dto.request;

import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
@AllArgsConstructor
@Builder
public final class ReservationRequest {
    @Parameter(description = "예약하려는 수업 일정의 PK값 입니다.")
    @NotNull(message = "수업 일정 ID값은 필수입니다.")
    private final Long programScheduleId;

    @Parameter(description = "예약자(부모)의 PK값 입니다.")
    @NotNull(message = "부모 ID값은 필수입니다.")
    private final Long parentId;

    @Parameter(description = "예약 대상(아이)의 PK값 리스트 입니다.")
    @NotEmpty(message = "아이 ID값 리스트는 필수입니다.")
    private final List<Long> childIdList;

}
