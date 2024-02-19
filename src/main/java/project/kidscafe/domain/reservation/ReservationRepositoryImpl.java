package project.kidscafe.domain.reservation;

import com.querydsl.jpa.JPQLQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import static project.kidscafe.domain.reservation.QReservation.reservation;

@Repository
@RequiredArgsConstructor
public class ReservationRepositoryImpl implements ReservationRepositoryCustom {
    private final JPQLQueryFactory jpqlQueryFactory;

    @Override
    public Optional<Reservation> findByParentAndProgramSchedule(Long programScheduleId, Long parentId, ReservationStatus reservationStatus) {
        return Optional.ofNullable(jpqlQueryFactory.selectFrom(reservation)
                .where(reservation.parent.id.eq(parentId)
                        .and(reservation.programSchedule.id.eq(programScheduleId))
                        .and(reservation.reservationStatus.eq(reservationStatus))
                ).fetchFirst());
    }

    public Optional<Reservation> findReservationForUpdate(Long reservationId, ReservationStatus reservationStatus) {
        return Optional.ofNullable(jpqlQueryFactory.selectFrom(reservation)
                                                    .join(reservation.programSchedule).fetchJoin()
                                                    .where(reservation.id.eq(reservationId)
                                                        .and(reservation.reservationStatus.eq(reservationStatus)))
                                                    .fetchOne());
    }
}
