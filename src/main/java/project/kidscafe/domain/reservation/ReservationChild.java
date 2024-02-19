package project.kidscafe.domain.reservation;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;
import project.kidscafe.domain.user.Child;
import project.kidscafe.domain.BaseEntity;

@Entity
@Table(name = "reservation_child")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReservationChild extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reservation_child_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reservation_id")
    private Reservation reservation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "child_id")
    private Child child;

    @Builder
    private ReservationChild(Reservation reservation, Child child) {
        this.reservation = reservation;
        this.child = child;
    }

    public static ReservationChild create(Reservation reservation, Child child) {
        return ReservationChild.builder()
                .reservation(reservation)
                .child(child)
                .build();
    }
}
