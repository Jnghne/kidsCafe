package project.kidscafe.api.user.controller;

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
import project.kidscafe.api.user.dto.request.ReservationHistoryCondition;
import project.kidscafe.api.user.dto.response.ReservationHistoryResponse;
import project.kidscafe.api.user.service.UserService;
import project.kidscafe.api.ApiResponse;

@Tag(name= "User", description = "사용자(부모/아이)와 관련된 기능을 수행하는 Controller입니다.")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
public class UserController {
    private final UserService userService;

    @Operation(summary = "예약 이력 조회 API", description = "사용자가 직접 매장, 수업 별로 예약한 이력을 조회하는 API 입니다. " +
                                            "\n 1. 매장 ID, 수업 ID를 조합해서 예약 이력을 검색할 수 있다." +
                                            "\n 2. 시작년월, 종료년월을 설정해서 기간 내의 이력만 검색할 수 있다." +
                                            "\n 3. 예약 상태별로 예약할 수 있다 (예약 완료 : OK , 취소 : CANCEL, 전체 : Null) " +
                                            "\n 4. 페이징 처리 되어있다.")
    @GetMapping("/{parentId}/reservation-history")
    public ApiResponse<Page<ReservationHistoryResponse>> getReservationHistory(@Parameter(description = "예약자(부모) ID") @PathVariable("parentId") Long parentId, @Valid ReservationHistoryCondition reservationHistoryCondition, Pageable pageable) {
        return ApiResponse.ok(userService.getReservationHistory(parentId, reservationHistoryCondition, pageable));
    }

}
