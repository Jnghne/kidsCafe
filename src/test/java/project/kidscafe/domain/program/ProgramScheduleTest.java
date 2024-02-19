package project.kidscafe.domain.program;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import project.kidscafe.domain.center.Center;
import project.kidscafe.global.exception.CustomException;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.*;
import static project.kidscafe.global.exception.ErrorCode.*;

class ProgramScheduleTest {

    @DisplayName("수업 시작 일자, 수업 시작 시간, 수업 Entity를 통해 수업 일정 Entity를 생성할 수 있다.")
    @Test
    void createProgramSchedule() {
        // given
        LocalDate startDate = LocalDate.of(2023,11,10);
        LocalTime startTime = LocalTime.of(14,0);
        Center center = Center.create("인천점", "인천시 미추홀구 XX로 XX번지");
        Program program = Program.create("물감놀이",center,20);

        // when
        ProgramSchedule programSchedule = ProgramSchedule.create(startDate, startTime, program);

        // then
        assertThat(programSchedule).isNotNull()
                .extracting("startDate", "startTime", "reservationCnt")
                .contains(startDate,startTime,0);
    }

    @DisplayName("수업 일정 별 예약 인원 수를 증가 시킬 수 있다.")
    @Test
    void increaseReservationCnt() {
        // given
        LocalDate startDate = LocalDate.of(2023,11,10);
        LocalTime startTime = LocalTime.of(14,0);
        Center center = Center.create("인천점", "인천시 미추홀구 XX로 XX번지");
        Program program = Program.create("물감놀이",center,20);
        ProgramSchedule programSchedule = ProgramSchedule.create(startDate, startTime, program);

        // when
        int beforeReservationCnt = programSchedule.getReservationCnt();
        programSchedule.increaseReservationCnt(5);
        int afterReservationCnt = programSchedule.getReservationCnt();

        // then
        assertThat(beforeReservationCnt).isEqualTo(0);
        assertThat(afterReservationCnt).isEqualTo(5);
    }
    @DisplayName("예약 인원 수를 증가 시킬 때, 수업 별 최대 인원 수를 초과 하는 경우 예외가 발생한다.")
    @Test
    void increaseReservationCntOverProgramMaxCnt() {
        // given
        LocalDate startDate = LocalDate.of(2023,11,10);
        LocalTime startTime = LocalTime.of(14,0);
        Center center = Center.create("인천점", "인천시 미추홀구 XX로 XX번지");
        Program program = Program.create("물감놀이",center,4);
        ProgramSchedule programSchedule = ProgramSchedule.create(startDate, startTime, program);

        // when // then
        assertThatThrownBy(() -> programSchedule.increaseReservationCnt(5))
                .isInstanceOf(CustomException.class)
                .hasMessage(OVER_THE_MAX_RESERVATION_CNT.getMessage());
    }

    @DisplayName("수업 일정 별 예약 인원 수를 변경할 수 있다 (관리자 용)")
    @Test
    void updateReservationCnt() {
        // given
        LocalDate startDate = LocalDate.of(2023,11,10);
        LocalTime startTime = LocalTime.of(14,0);
        Center center = Center.create("인천점", "인천시 미추홀구 XX로 XX번지");
        Program program = Program.create("물감놀이",center,20);
        ProgramSchedule programSchedule = ProgramSchedule.create(startDate, startTime, program);

        // when
        int beforeReservationCnt = programSchedule.getReservationCnt();
        programSchedule.updateReservationCnt(10);
        int afterReservationCnt = programSchedule.getReservationCnt();

        // then
        assertThat(beforeReservationCnt).isEqualTo(0);
        assertThat(afterReservationCnt).isEqualTo(10);
    }


    @DisplayName("수업 일정 별 예약 인원 수를 감소 시킬 수 있다.")
    @Test
    void decreaseReservationCnt() {
        // given
        LocalDate startDate = LocalDate.of(2023,11,10);
        LocalTime startTime = LocalTime.of(14,0);
        Center center = Center.create("인천점", "인천시 미추홀구 XX로 XX번지");
        Program program = Program.create("물감놀이",center,20);
        ProgramSchedule programSchedule = ProgramSchedule.create(startDate, startTime, program);

        // when
        int firstReservationCnt = programSchedule.getReservationCnt();
        programSchedule.updateReservationCnt(5);
        int updatedReservationCnt = programSchedule.getReservationCnt();
        programSchedule.decreaseReservationCnt(3);
        int afterReservationCnt = programSchedule.getReservationCnt();

        // then
        assertThat(firstReservationCnt).isEqualTo(0);
        assertThat(updatedReservationCnt).isEqualTo(5);
        assertThat(afterReservationCnt).isEqualTo(2);
    }

    @DisplayName("예약 인원 수를 감소 시킬 때, 예약 인원 수가 음수가 된다면 예외가 발생 한다.")
    @Test
    void decreaseReservationCntUnderTheZero() {
        // given
        LocalDate startDate = LocalDate.of(2023,11,10);
        LocalTime startTime = LocalTime.of(14,0);
        Center center = Center.create("인천점", "인천시 미추홀구 XX로 XX번지");
        Program program = Program.create("물감놀이",center,20);
        ProgramSchedule programSchedule = ProgramSchedule.create(startDate, startTime, program);

        // when
        programSchedule.updateReservationCnt(1);

        // then
        assertThatThrownBy(() -> programSchedule.decreaseReservationCnt(2))
                .isInstanceOf(CustomException.class)
                .hasMessage(DECREASE_RESERVATION_CNT_UNDER_THE_ZERO.getMessage());
    }

    @DisplayName("내일 수업 일정은 예약할 수 있다.")
    @Test
    void isAvailableSchedule() {
        // given
        LocalDate startDate = LocalDate.of(2023,11,10);
        LocalTime startTime = LocalTime.of(14,0);
        Center center = Center.create("인천점", "인천시 미추홀구 XX로 XX번지");
        Program program = Program.create("물감놀이",center,20);
        ProgramSchedule programSchedule = ProgramSchedule.create(startDate, startTime, program);

        // when
        LocalDate now = LocalDate.of(2023, 11, 9);
        boolean availableDateYn = programSchedule.isAvailableDate(now);

        // then
        assertThat(availableDateYn).isTrue();
    }
    @DisplayName("14일 이후의 수업 일정은 예약할 수 있다.")
    @Test
    void isAvailableScheduleWithAfter14Day() {
        // given
        LocalDate startDate = LocalDate.of(2023,11,24);
        LocalTime startTime = LocalTime.of(14,0);
        Center center = Center.create("인천점", "인천시 미추홀구 XX로 XX번지");
        Program program = Program.create("물감놀이",center,20);
        ProgramSchedule programSchedule = ProgramSchedule.create(startDate, startTime, program);

        // when
        LocalDate now = LocalDate.of(2023, 11, 10);
        boolean availableDateYn = programSchedule.isAvailableDate(now);

        // then
        assertThat(availableDateYn).isTrue();
    }
    @DisplayName("15일 이후의 수업 일정은 예약할 수 없다. ")
    @Test
    void isAvailableScheduleWithAfter15Day() {
        // given
        LocalDate startDate = LocalDate.of(2023,11,25);
        LocalTime startTime = LocalTime.of(14,0);
        Center center = Center.create("인천점", "인천시 미추홀구 XX로 XX번지");
        Program program = Program.create("물감놀이",center,20);
        ProgramSchedule programSchedule = ProgramSchedule.create(startDate, startTime, program);

        // when
        LocalDate now = LocalDate.of(2023, 11, 10);
        boolean availableDateYn = programSchedule.isAvailableDate(now);

        // then
        assertThat(availableDateYn).isFalse();
    }
    @DisplayName("과거 수업 일정은 예약할 수 없다.")
    @Test
    void isAvailableScheduleWithPastSchedule() {
        // given
        LocalDate startDate = LocalDate.of(2023,11,10);
        LocalTime startTime = LocalTime.of(14,0);
        Center center = Center.create("인천점", "인천시 미추홀구 XX로 XX번지");
        Program program = Program.create("물감놀이",center,20);
        ProgramSchedule programSchedule = ProgramSchedule.create(startDate, startTime, program);

        // when
        LocalDate now = LocalDate.of(2023, 11, 11);
        boolean isAvailableSchedule = programSchedule.isAvailableDate(now);

        // then
        assertThat(isAvailableSchedule).isFalse();
    }
    @DisplayName("당일 수업 일정은 예약할 수 없다.")
    @Test
    void isAvailableScheduleWithSameDay() {
        // given
        LocalDate startDate = LocalDate.of(2023,11,10);
        LocalTime startTime = LocalTime.of(14,0);
        Center center = Center.create("인천점", "인천시 미추홀구 XX로 XX번지");
        Program program = Program.create("물감놀이",center,20);
        ProgramSchedule programSchedule = ProgramSchedule.create(startDate, startTime, program);

        // when
        LocalDate now = LocalDate.of(2023, 11, 10);
        boolean availableDateYn = programSchedule.isAvailableDate(now);

        // then
        assertThat(availableDateYn).isFalse();
    }

    @DisplayName("최대 예약 인원 수를 초과하는 경우, isExceedMaxReservationCnt() 메서드는 true를 리턴한다.")
    @Test
    void exceedMaxReservationCnt() {
        // given
        LocalDate startDate = LocalDate.of(2023,11,10);
        LocalTime startTime = LocalTime.of(14,0);
        Center center = Center.create("인천점", "인천시 미추홀구 XX로 XX번지");
        Program program = Program.create("물감놀이",center,5);
        ProgramSchedule programSchedule = ProgramSchedule.create(startDate, startTime, program);

        // when
        boolean exceedYn = programSchedule.isExceedMaxReservationCnt(6);

        // then
        assertThat(exceedYn).isTrue();
    }

    @DisplayName("최대 예약 인원 수를 초과하지 않는 경우, isExceedMaxReservationCnt() 메서드는 false를 리턴한다.")
    @Test
    void notExceedMaxReservationCnt() {
        // given
        LocalDate startDate = LocalDate.of(2023,11,10);
        LocalTime startTime = LocalTime.of(14,0);
        Center center = Center.create("인천점", "인천시 미추홀구 XX로 XX번지");
        Program program = Program.create("물감놀이",center,5);
        ProgramSchedule programSchedule = ProgramSchedule.create(startDate, startTime, program);

        // when
        boolean exceedYn = programSchedule.isExceedMaxReservationCnt(4);

        // then
        assertThat(exceedYn).isFalse();
    }
}