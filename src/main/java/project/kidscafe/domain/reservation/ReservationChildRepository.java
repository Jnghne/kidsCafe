package project.kidscafe.domain.reservation;

import org.springframework.data.jpa.repository.JpaRepository;
import project.kidscafe.domain.reservation.ReservationChild;

public interface ReservationChildRepository extends JpaRepository<ReservationChild,Long> {
}
