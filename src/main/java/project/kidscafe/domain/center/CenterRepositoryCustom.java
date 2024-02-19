package project.kidscafe.domain.center;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import project.kidscafe.api.center.dto.request.ReservationSearchCondition;
import project.kidscafe.api.center.dto.response.ReservationSearchResponse;

public interface CenterRepositoryCustom {
    /**
     * 매장 별로 조회 조건을 설정해서 예약자 리스트 조회
     * @param centerId : 매장 ID (필수값)
     * @param reservationSearchCondition : 수업 ID, 수업 일정 ID, 조회 시작년월, 조회 종료년월, 예약자명
     * @param pageable : 페이징 정보
     * @return ReservationSearchResponse : 예약자 정보
     */
    Page<ReservationSearchResponse> findReservationsByCondition(Long centerId, ReservationSearchCondition reservationSearchCondition, Pageable pageable);
}
