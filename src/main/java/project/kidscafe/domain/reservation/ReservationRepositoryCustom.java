package project.kidscafe.domain.reservation;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.Lock;
import project.kidscafe.domain.reservation.Reservation;
import project.kidscafe.domain.reservation.ReservationStatus;

import java.util.Optional;

public interface ReservationRepositoryCustom {
    /**
     * 예약자(부모)의 ID와 프로그램 일정 ID로 예약된 수업을 조회합니다.
     * @param parentId
     * @param scheduleId
     * @param reservationStatus
     * @return
     */
    Optional<Reservation> findByParentAndProgramSchedule(Long parentId, Long scheduleId, ReservationStatus reservationStatus);

    /**
     * 예약 번호 + 예약 상태로 예약 데이터 조회 (PESSIMISTIC_WRITE LOCK)
     * @param reservationId : 예약 번호
     * @param reservationStatus : 예약 상태
     * @return Reservation : 예약 데이터
     */
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<Reservation> findReservationForUpdate(Long reservationId, ReservationStatus reservationStatus);

}
