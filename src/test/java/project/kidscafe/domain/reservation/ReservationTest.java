package project.kidscafe.domain.reservation;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import project.kidscafe.domain.user.Child;
import project.kidscafe.domain.user.Parent;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

class ReservationTest {


    @DisplayName("예약 Entity 생성 시, 최초 상태값은 OK이다.")
    @Test
    void createReservation() {
        // given
        Parent parent = Parent.create("김부모", "test@gmail.com");
        List<Child> childList = List.of(Child.create("김아이", parent));

        // when
        Reservation reservation = Reservation.create(null, parent, childList);

        // then
        assertThat(reservation.getReservationStatus()).isEqualTo(ReservationStatus.OK);
    }

    @DisplayName("예약 취소 시 예약 Entity 상태가 CANCEL로 변경된다.")
    @Test
    void reservationCancel() {
        // given
        Parent parent = Parent.create("김부모", "test@gmail.com");
        List<Child> childList = List.of(Child.create("김아이", parent));
        Reservation reservation = Reservation.create(null, parent, childList);

        // when
        reservation.cancel();

        // then
        assertThat(reservation.getReservationStatus()).isEqualTo(ReservationStatus.CANCEL);
    }
}