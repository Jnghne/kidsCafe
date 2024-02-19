package project.kidscafe.domain.program;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import project.kidscafe.domain.BaseEntity;
import project.kidscafe.global.exception.CustomException;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

import static project.kidscafe.global.exception.ErrorCode.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "program_schedule")
public class ProgramSchedule extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "prgoram_schedule_id")
    private Long id;

    private LocalDate startDate;
    private LocalTime startTime;

    @ColumnDefault("0")
    private int reservationCnt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "program_id")
    private Program program;

    // -- 생성자
    @Builder
    private ProgramSchedule(LocalDate startDate, LocalTime startTime, Program program) {
        this.startDate = startDate;
        this.startTime = startTime;
        this.program = program;
    }

    public static ProgramSchedule create(LocalDate startDate, LocalTime startTime, Program program) {
        return ProgramSchedule.builder()
                .startDate(startDate)
                .startTime(startTime)
                .program(program)
                .build();
    }

    // -- 비즈니스 로직
    public boolean isExceedMaxReservationCnt(int reservationCnt) {
        if (this.program.getMaxReservationCnt() < this.reservationCnt + reservationCnt) {
            return true;
        }
        return false;
    }
    public void increaseReservationCnt(int increaseCnt) {
        if (isExceedMaxReservationCnt(increaseCnt)) {
            throw new CustomException(OVER_THE_MAX_RESERVATION_CNT);
        }
        this.reservationCnt += increaseCnt;
    }

    public boolean isReservationCntLessThanZero(int decreaseCnt){
        if (this.reservationCnt - decreaseCnt < 0) {
            return true;
        }
        return false;
    }
    public void decreaseReservationCnt(int reservationCnt) {
        if (isReservationCntLessThanZero(reservationCnt)) {
            throw new CustomException(DECREASE_RESERVATION_CNT_UNDER_THE_ZERO);
        }
        this.reservationCnt -= reservationCnt;
    }

    public void updateReservationCnt(int reservationCnt) {
        this.reservationCnt = reservationCnt;
    }

    public boolean isAvailableDate(LocalDate now) {

        // 과거 수업 일정인 경우
        if (this.startDate.isBefore(now)) {
            return false;
        }

        // 당일 수업 일정인 경우
        if (this.startDate.isEqual(now)) {
            return false;
        }

        // 15일 이후의 수업 일정인 경우
        if (ChronoUnit.DAYS.between(now, this.startDate) >= 15) {
            return false;
        }

        return true;
    }
}
