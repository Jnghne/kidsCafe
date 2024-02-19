package project.kidscafe.api.reservation.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import project.kidscafe.api.reservation.dto.request.ReservationRequest;
import project.kidscafe.api.reservation.dto.response.ReservationResponse;
import project.kidscafe.api.reservation.service.ReservationService;
import project.kidscafe.api.ApiResponse;

@Tag(name= "Reservation", description = "예약과 관련된 기능을 수행하는 Controller 입니다.")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/reservation")
public class ReservationController {

    private final ReservationService reservationService;

    /**
     * 수업 예약 API
     */
    @Operation(summary = "수업 예약 API", description = "사용자가 특정 매장의 수업 일정을 선택해서 예약을 생성하는 API입니다. " +
                                                    "\n 1. 예약자(부모) ID값, 수업 일정 ID값, 예약 대상(아이)의 ID값 리스트를 받는다. " +
                                                    "\n 2. 각 ID가 DB에 존재하는지 검증한다 " +
                                                    "\n 3. 예약하려는 수업이 현재일 로부터 14일이 남은 일정인지 검증한다. (과거,당일,15일 이후의 수업은 예약 불가) " +
                                                    "\n 4. 수업에 남은 자리가 있는지 검증한다. " +
                                                    "\n 5. 중복 예약 여부를 검증한다. (매장 + 수업 일정 별로 1번만 예약 가능하다) " +
                                                    "\n 6. 검증이 모두 완료되면 예약 데이터를 생성하고 , 아이 수 만큼 수업 인원 수를 증가시킨다. ")
    @PostMapping
    public ApiResponse<ReservationResponse> createReservation(@RequestBody @Valid ReservationRequest reservationRequest) {
        return ApiResponse.ok(reservationService.createReservation(reservationRequest));
    }

    /**
     * 수업 예약 취소 API
     */
    @Operation(summary = "예약 취소 API", description = "사용자가 예약한 수업 일정을 취소하는 API입니다. " +
                                                        "\n 1. 예약 번호로 예약 데이터를 조회한다 (SELECT FOR UPDATE) " +
                                                        "\n 2. 예약 데이터의 예약 상태를 CANCEL로 변경시킨다 (이력 관리 용도와 추후 사용될 여지가 있을 것으로 판단해서 DELETE 처리 X) " +
                                                        "\n 3. 예약 취소한 만큼 수업의 인원수를 감소한다." )
    @PatchMapping("/{reservationId}/cancel")
    public ApiResponse<Void> cancelReservation(@Parameter(description = "예약 번호(PK)") @PathVariable("reservationId") Long reservationId) {
        reservationService.cancelReservation(reservationId);
        return ApiResponse.ok();
    }


}
