package project.kidscafe.api.center.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.kidscafe.api.center.dto.request.ReservationSearchCondition;
import project.kidscafe.api.center.dto.response.ReservationSearchResponse;
import project.kidscafe.api.center.service.CenterService;
import project.kidscafe.api.ApiResponse;


@Tag(name= "Center", description = "매장과 매장에 소속된 수업에 대한 기능을 수행하는 Controller입니다. ")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/center")
public class CenterController {
    private final CenterService centerService;


    @Operation(summary = "매장 별 수업 별 예약자 현황 조회 API", description = "매장의 수업/날짜/이름 조건으로 예약자 현황을 조회하는 기능입니다." +
                                                                "\n - 수업 ID, 수업 일정 ID 를 조합해서 조회할 수 있다." +
                                                                "\n - 시작년월, 종료년월을 설정해서 기간 내의 예약자만 검색할 수 있다. " +
                                                                "\n - 예약자명을 추가해서 검색할 수 있다." +
                                                                "\n - 페이징 처리 되어있다.")
    @GetMapping("/{centerId}/reservations")
    public ApiResponse<Page<ReservationSearchResponse>> getReservationParentList(@Parameter(description = "매장 ID") @PathVariable("centerId") Long centerId
                                                                                , @Valid ReservationSearchCondition reservationSearchCondition
                                                                                , Pageable pageable) {
        return ApiResponse.ok(centerService.getReservationUserList(centerId, reservationSearchCondition, pageable));
    }


}
