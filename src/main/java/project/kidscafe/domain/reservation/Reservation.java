package project.kidscafe.domain.reservation;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import project.kidscafe.domain.program.ProgramSchedule;
import project.kidscafe.domain.user.Child;
import project.kidscafe.domain.user.Parent;
import project.kidscafe.domain.BaseEntity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Reservation extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reservation_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "program_schedule_id")
    private ProgramSchedule programSchedule;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Parent parent;

    @OneToMany(mappedBy = "reservation", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ReservationChild> reservationChildren = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private ReservationStatus reservationStatus;

    private LocalDateTime cancelDt;

    @Builder
    private Reservation(ProgramSchedule programSchedule, Parent parent, List<ReservationChild> reservationChildren, ReservationStatus reservationStatus){
        this.programSchedule = programSchedule;
        this.parent = parent;
        this.reservationChildren = reservationChildren;
        this.reservationStatus = reservationStatus;
    }

    public static Reservation create(ProgramSchedule programSchedule, Parent parent, List<Child> children) {
        Reservation reservation = Reservation.builder()
                .programSchedule(programSchedule)
                .parent(parent)
                .reservationStatus(ReservationStatus.OK)
                .build();
        reservation.addReservationChildren(children);
        return reservation;
    }

    // -- 편의 메서드
    public void addReservationChildren(List<Child> children) {
        this.reservationChildren = children.stream()
                .map((child) -> ReservationChild.create(this, child))
                .toList();
    }

    // -- 비즈니스 로직
    public void cancel() {
        this.reservationStatus = ReservationStatus.CANCEL;
        this.cancelDt = LocalDateTime.now();
    }
}
