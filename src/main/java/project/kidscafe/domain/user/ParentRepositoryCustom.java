package project.kidscafe.domain.user;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import project.kidscafe.api.user.dto.request.ReservationHistoryCondition;
import project.kidscafe.api.user.dto.response.ReservationHistoryResponse;

public interface ParentRepositoryCustom {
    /**
     * 사용자 별 예약 이력 조회
     * @param parentId : 부모ID (필수값)
     * @param reservationHistoryCondition : 매장, 수업, 조회 날짜 등 조회 조건
     * @param pageable : 조회 페이지
     * @return 매장 정보, 수업 정보, 예약정보, 페이징 정보
     */
    Page<ReservationHistoryResponse> findReservationHistory(Long parentId, ReservationHistoryCondition reservationHistoryCondition, Pageable pageable);
}
