package project.kidscafe.api.reservation.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import project.kidscafe.IntegrateTestEnvironment;
import project.kidscafe.domain.center.Center;
import project.kidscafe.domain.program.Program;
import project.kidscafe.domain.program.ProgramSchedule;
import project.kidscafe.api.reservation.dto.request.ReservationRequest;
import project.kidscafe.api.reservation.dto.response.ReservationResponse;
import project.kidscafe.domain.reservation.Reservation;
import project.kidscafe.domain.reservation.ReservationStatus;
import project.kidscafe.domain.user.Child;
import project.kidscafe.domain.user.Parent;
import project.kidscafe.global.exception.CustomException;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static project.kidscafe.global.exception.ErrorCode.*;


class ReservationServiceTest extends IntegrateTestEnvironment {
    @Autowired
    ReservationService reservationService;
    final int defaultMaxReservationCnt = 20;

    @DisplayName("예약 정보를 받아 수업에 대한 예약을 생성 한다.")
    @Test
    void reservation() {
        // given
        Center center = createCenter();
        Program program = createProgram(center, defaultMaxReservationCnt);
        ProgramSchedule programSchedule = createProgramSchedule(LocalDate.now().plusDays(1), LocalTime.of(17, 0), program);
        Parent parent = createParent("김부모", "test@naver.com");
        List<Child> children = createChild(parent, "김초코");

        ReservationRequest request = createReservationRequest(programSchedule, parent, children);

        // when
        ReservationResponse response = reservationService.createReservation(request);

        // then
        assertThat(response)
                .extracting("reservationId", "reservationDt")
                .isNotNull();

        assertThat(response)
                .extracting("centerName", "programName", "parentName", "reservationCnt")
                .containsExactly(program.getCenter().getCenterName(), program.getProgramName(), parent.getParentName(), children.size());
    }

    @DisplayName("수업을 예약할 때 여러명의 아이를 한번에 예약할 수 있다.")
    @Test
    void reservationWithMultipleChild() {
        // given
        Center center = createCenter();
        Program program = createProgram(center, defaultMaxReservationCnt);
        ProgramSchedule programSchedule = createProgramSchedule(LocalDate.now().plusDays(1), LocalTime.of(17, 0), program);
        Parent parent = createParent("김부모", "test@naver.com");
        List<Child> children = createChild(parent, "김초코", "김민트");

        ReservationRequest request = createReservationRequest(programSchedule, parent, children);

        // when
        ReservationResponse response = reservationService.createReservation(request);

        // then
        assertThat(response)
                .extracting("reservationId", "reservationDt")
                .isNotNull();

        assertThat(response)
                .extracting("centerName", "programName", "parentName", "reservationCnt")
                .containsExactly(center.getCenterName(), program.getProgramName(), parent.getParentName(), children.size());
    }

    @DisplayName("예약이 완료되면 신청하는 아이 수 만큼 수업의 예약 인원 수가 증가한다.")
    @Test
    void increaseReservationCount() {
        // given
        Center center = createCenter();
        Program program = createProgram(center, defaultMaxReservationCnt);
        ProgramSchedule programSchedule = createProgramSchedule(LocalDate.now().plusDays(1), LocalTime.of(17, 0), program);
        Parent parent = createParent("김부모", "test@naver.com");
        List<Child> children = createChild(parent, "김초코", "김민트", "김그린");

        ReservationRequest request = createReservationRequest(programSchedule, parent, children);

        // when
        reservationService.createReservation(request);

        // then
        ProgramSchedule programScheduleAfterReserve = programScheduleRepository.findById(programSchedule.getId()).orElseThrow();
        assertThat(programScheduleAfterReserve.getReservationCnt()).isEqualTo(3);

    }

    @DisplayName("예약하려는 수업의 잔여 자리가 부족한 경우 예외가 발생한다.")
    @Test
    void overMaxNumOfChild() {
        // given
        Center center = createCenter();
        Program program = createProgram(center, 2);
        ProgramSchedule programSchedule = createProgramSchedule(LocalDate.now().plusDays(1), LocalTime.of(17, 0), program);
        Parent parent = createParent("김부모", "test@naver.com");
        List<Child> children = createChild(parent, "김초코", "김민트", "김그린");

        ReservationRequest request = createReservationRequest(programSchedule, parent, children);

        // when / then
        assertThatThrownBy(() -> reservationService.createReservation(request))
                .isInstanceOf(CustomException.class)
                .hasMessage(OVER_THE_MAX_RESERVATION_CNT.getMessage());
    }

    @DisplayName("과거 일의 수업을 예약하는 경우 예외가 발생한다.")
    @Test
    void reserveInPastSchedule() {
        // given
        Center center = createCenter();
        Program program = createProgram(center, defaultMaxReservationCnt);
        ProgramSchedule programSchedule = createProgramSchedule(LocalDate.now().minusDays(1), LocalTime.of(17, 0), program);
        Parent parent = createParent("김부모", "test@naver.com");
        List<Child> children = createChild(parent, "김초코", "김민트", "김그린");

        ReservationRequest request = createReservationRequest(programSchedule, parent, children);

        // when / then
        assertThatThrownBy(() -> reservationService.createReservation(request))
                .isInstanceOf(CustomException.class)
                .hasMessage(NOT_AVAILABLE_PROGRAM_SCHEDULE.getMessage());
    }

    @DisplayName("당일 수업을 예약하는 경우 예외가 발생한다.")
    @Test
    void reserveInSameDay() {
        // given
        Center center = createCenter();
        Program program = createProgram(center, defaultMaxReservationCnt);
        ProgramSchedule programSchedule = createProgramSchedule(LocalDate.now(), LocalTime.of(17, 0), program);
        Parent parent = createParent("김부모", "test@naver.com");
        List<Child> children = createChild(parent, "김초코", "김민트", "김그린");

        ReservationRequest request = createReservationRequest(programSchedule, parent, children);

        // when / then
        assertThatThrownBy(() -> reservationService.createReservation(request))
                .isInstanceOf(CustomException.class)
                .hasMessage(NOT_AVAILABLE_PROGRAM_SCHEDULE.getMessage());
    }

    @DisplayName("수업 일정이 현재 일로부터 14일 이전 수업은 예약이 가능하다.")
    @Test
    void reserveBefore14day() {
        // given
        Center center = createCenter();
        Program program = createProgram(center, defaultMaxReservationCnt);
        ProgramSchedule programSchedule = createProgramSchedule(LocalDate.now().plusDays(14), LocalTime.of(17, 0), program);
        Parent parent = createParent("김부모", "test@naver.com");
        List<Child> children = createChild(parent, "김초코", "김민트", "김그린");

        ReservationRequest request = createReservationRequest(programSchedule, parent, children);

        // when
        ReservationResponse response = reservationService.createReservation(request);

        // then
        assertThat(response)
                .extracting("reservationId", "reservationDt")
                .isNotNull();

        assertThat(response)
                .extracting("centerName", "programName", "parentName", "reservationCnt")
                .containsExactly(center.getCenterName(), program.getProgramName(), parent.getParentName(), children.size());
    }

    @DisplayName("수업 일정이 현재 일로부터 15일 이후 수업인 경우 예외가 발생한다.")
    @Test
    void reserveAfter15day() {
        // given
        Center center = createCenter();
        Program program = createProgram(center, defaultMaxReservationCnt);
        ProgramSchedule programSchedule = createProgramSchedule(LocalDate.now().plusDays(15), LocalTime.of(17, 0), program);
        Parent parent = createParent("김부모", "test@naver.com");
        List<Child> children = createChild(parent, "김초코", "김민트", "김그린");

        ReservationRequest request = createReservationRequest(programSchedule, parent, children);

        // when / then
        assertThatThrownBy(() -> reservationService.createReservation(request))
                .isInstanceOf(CustomException.class)
                .hasMessage(NOT_AVAILABLE_PROGRAM_SCHEDULE.getMessage());
    }


    @DisplayName("동일 매장, 동일 수업에 중복 예약하는 경우 예외가 발생한다.")
    @Test
    void duplicateReservation() {
        // given
        Center center = createCenter();
        Program program = createProgram(center, defaultMaxReservationCnt);
        ProgramSchedule programSchedule = createProgramSchedule(LocalDate.now().plusDays(10), LocalTime.of(17, 0), program);
        Parent parent = createParent("김부모", "test@naver.com");
        List<Child> children = createChild(parent, "김초코", "김민트", "김그린");

        ReservationRequest request = createReservationRequest(programSchedule, parent, children);

        // when
        reservationService.createReservation(request);

        // then
        assertThatThrownBy(() -> reservationService.createReservation(request))
                .isInstanceOf(CustomException.class)
                .hasMessage(DUPLICATE_RESERVATION.getMessage());
    }

    @DisplayName("존재하지 않은 ChildId로 예약하는 경우 예외가 발생한다.")
    @Test
    void reserveWithNotExistChildId() {
        // given
        Center center = createCenter();
        Program program = createProgram(center, defaultMaxReservationCnt);
        ProgramSchedule programSchedule = createProgramSchedule(LocalDate.now().plusDays(3), LocalTime.of(17, 0), program);
        Parent parent = createParent("김부모", "test@naver.com");
        List<Child> children = createChild(parent, "김초코", "김민트", "김그린");

        ReservationRequest request = new ReservationRequest(programSchedule.getId()
                , parent.getId()
                , List.of(1L, 10441L));

        // when / then
        assertThatThrownBy(() -> reservationService.createReservation(request))
                .isInstanceOf(CustomException.class)
                .hasMessage(NOT_FOUNDED_CHILD.getMessage());
    }

    @DisplayName("존재하지 않은 ScheduleID로 예약하는 경우 예외가 발생한다.")
    @Test
    void reserveWithNotExistScheduleId() {
        // given
        Center center = createCenter();
        Program program = createProgram(center, defaultMaxReservationCnt);
        ProgramSchedule programSchedule = createProgramSchedule(LocalDate.now().plusDays(3), LocalTime.of(17, 0), program);
        Parent parent = createParent("김부모", "test@naver.com");
        List<Child> children = createChild(parent, "김초코", "김민트", "김그린");

        ReservationRequest request = new ReservationRequest(12099L
                , parent.getId()
                , children.stream().map(Child::getId).toList());

        // when / then
        assertThatThrownBy(() -> reservationService.createReservation(request))
                .isInstanceOf(CustomException.class)
                .hasMessage(NOT_FOUNDED_PROGRAM_SCHEDULE.getMessage());
    }

    @DisplayName("존재하지 않은 ParentId로 예약하는 경우 예외가 발생한다.")
    @Test
    void reserveWithNotExistParentId() {
        // given
        Center center = createCenter();
        Program program = createProgram(center, defaultMaxReservationCnt);
        ProgramSchedule programSchedule = createProgramSchedule(LocalDate.now().plusDays(3), LocalTime.of(17, 0), program);
        Parent parent = createParent("김부모", "test@naver.com");
        List<Child> children = createChild(parent, "김초코", "김민트", "김그린");

        ReservationRequest request = new ReservationRequest(programSchedule.getId()
                , 1010110L
                , children.stream().map(Child::getId).toList());

        // when / then
        assertThatThrownBy(() -> reservationService.createReservation(request))
                .isInstanceOf(CustomException.class)
                .hasMessage(NOT_FOUNDED_PARENT.getMessage());
    }


    @DisplayName("예약 취소 시 예약 상태가 CANCEL로 변경된다.")
    @Test
    void cancel() {
        // given
        Center center = createCenter();
        Program program = createProgram(center, defaultMaxReservationCnt);
        ProgramSchedule programSchedule = createProgramSchedule(LocalDate.now().plusDays(1), LocalTime.of(17, 0), program);
        Parent parent = createParent("김부모", "test@naver.com");
        List<Child> children = createChild(parent, "김초코");

        ReservationRequest request = createReservationRequest(programSchedule, parent, children);
        ReservationResponse response = reservationService.createReservation(request);

        // when
        reservationService.cancelReservation(response.getReservationId());

        // then
        Reservation findReservation = reservationRepository.findById(response.getReservationId()).orElseThrow();
        assertThat(findReservation.getReservationStatus()).isEqualTo(ReservationStatus.CANCEL);
    }

    @DisplayName("예약 취소 시 예약 취소 일시가 저장 된다.")
    @Test
    void cancelDate() {
        // given
        Center center = createCenter();
        Program program = createProgram(center, defaultMaxReservationCnt);
        ProgramSchedule programSchedule = createProgramSchedule(LocalDate.now().plusDays(1), LocalTime.of(17, 0), program);
        Parent parent = createParent("김부모", "test@naver.com");
        List<Child> children = createChild(parent, "김초코");

        ReservationRequest request = createReservationRequest(programSchedule, parent, children);
        ReservationResponse response = reservationService.createReservation(request);

        // when
        reservationService.cancelReservation(response.getReservationId());

        // then
        Reservation findReservation = reservationRepository.findById(response.getReservationId()).orElseThrow();
        assertThat(findReservation.getCancelDt()).isNotNull();
    }
    @DisplayName("예약 취소 후 수업 예약 인원이 예약한 만큼 감소한다.")
    @Test
    void decreaseReservationCnt() {
        // given
        Center center = createCenter();
        Program program = createProgram(center, defaultMaxReservationCnt);
        ProgramSchedule programSchedule = createProgramSchedule(LocalDate.now().plusDays(1), LocalTime.of(17, 0), program);
        Parent parent = createParent("김부모", "test@naver.com");
        List<Child> children = createChild(parent, "김초코");

        ReservationRequest request = createReservationRequest(programSchedule, parent, children);
        ReservationResponse response = reservationService.createReservation(request);

        // when
        reservationService.cancelReservation(response.getReservationId());

        // then
        ProgramSchedule programScheduleAfterCancel = programScheduleRepository.findById(programSchedule.getId()).orElseThrow();
        assertThat(programScheduleAfterCancel.getReservationCnt()).isEqualTo(0);
    }

    @DisplayName("취소한 예약에 대해 다시 예약이 가능하다.")
    @Test
    void reservationAfterCancel() {
        // given
        Center center = createCenter();
        Program program = createProgram(center, defaultMaxReservationCnt);
        ProgramSchedule programSchedule = createProgramSchedule(LocalDate.now().plusDays(1), LocalTime.of(17, 0), program);
        Parent parent = createParent("김부모", "test@naver.com");
        List<Child> children = createChild(parent, "김초코");

        ReservationRequest request = createReservationRequest(programSchedule, parent, children);
        ReservationResponse response = reservationService.createReservation(request);

        // when
        reservationService.cancelReservation(response.getReservationId());

        // then
        ReservationResponse reservationAgain = reservationService.createReservation(request);
        assertThat(reservationAgain.getReservationId()).isNotNull();
        assertThat(reservationAgain).extracting("centerName", "programName", "parentName", "reservationCnt")
                .containsExactly(program.getCenter().getCenterName(), program.getProgramName(), parent.getParentName(), children.size());
    }

    // --- 생성자 메서드

    private List<Child> createChild(Parent parent, String... childNames) {
        List<Child> children = new ArrayList<>();
        for (String childName : childNames) {
            children.add(Child.create(childName, parent));
        }
        return childRepository.saveAll(children);
    }

    private Parent createParent(String parentName, String email) {
        Parent parent = Parent.create(parentName, email);
        return parentRepository.save(parent);
    }

    private ProgramSchedule createProgramSchedule(LocalDate scheduleStartDate, LocalTime scheduleStartTime, Program program) {
        ProgramSchedule programSchedule = ProgramSchedule.create(scheduleStartDate, scheduleStartTime, program);
        return programScheduleRepository.save(programSchedule);
    }

    private Program createProgram(Center center, int maxReservationCnt) {
        Program program = Program.create("퍼즐놀이", center, maxReservationCnt);
        return programRepository.save(program);
    }

    private Center createCenter() {
        Center center = Center.create("잠실점", "서울 송파구 XX동 YY");
        return centerRepository.save(center);
    }

    private ReservationRequest createReservationRequest(ProgramSchedule programSchedule, Parent parentA, List<Child> childrenA) {
        return new ReservationRequest(programSchedule.getId()
                , parentA.getId()
                , childrenA.stream().map(Child::getId).toList());
    }
}