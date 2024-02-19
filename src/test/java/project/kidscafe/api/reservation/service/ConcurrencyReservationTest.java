package project.kidscafe.api.reservation.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import project.kidscafe.domain.center.Center;
import project.kidscafe.domain.center.CenterRepository;
import project.kidscafe.domain.program.Program;
import project.kidscafe.domain.program.ProgramSchedule;
import project.kidscafe.domain.program.ProgramRepository;
import project.kidscafe.domain.program.ProgramScheduleRepository;
import project.kidscafe.api.reservation.dto.request.ReservationRequest;
import project.kidscafe.domain.reservation.ReservationChildRepository;
import project.kidscafe.domain.reservation.ReservationRepository;
import project.kidscafe.domain.user.Child;
import project.kidscafe.domain.user.Parent;
import project.kidscafe.domain.user.ChildRepository;
import project.kidscafe.domain.user.ParentRepository;
import project.kidscafe.global.exception.ErrorCode;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ActiveProfiles("test")
@SpringBootTest
class ConcurrencyReservationTest {

    @Autowired
    ReservationService reservationService;
    @Autowired
    ReservationRepository reservationRepository;
    @Autowired
    ReservationChildRepository reservationChildRepository;
    @Autowired
    CenterRepository centerRepository;
    @Autowired
    ProgramRepository programRepository;
    @Autowired
    ParentRepository parentRepository;
    @Autowired
    ChildRepository childRepository;
    @Autowired
    ProgramScheduleRepository programScheduleRepository;

    @AfterEach
    void tearDown() {
        reservationChildRepository.deleteAllInBatch();
        childRepository.deleteAllInBatch();
        reservationRepository.deleteAllInBatch();
        programScheduleRepository.deleteAllInBatch();
        parentRepository.deleteAllInBatch();
        programRepository.deleteAllInBatch();
        centerRepository.deleteAllInBatch();
    }

    @DisplayName("여러 고객이 하나의 수업에 동시에 예약하는 경우, 수업 인원이 부족하지 않다면 모두 예약이 성공 한다.")
    @Test
    void reserveSuccessInMultiThread() throws InterruptedException, ExecutionException {
        // given
        Center center = createCenter();
        Program program = createProgram(center, 8);
        ProgramSchedule programSchedule = createProgramSchedule(LocalDate.now().plusDays(1), LocalTime.of(17, 0), program);

        Parent parentA = createParent("김부모", "test@naver.com");
        Parent parentB = createParent("박철수", "chulsoo@google.com");
        Parent parentC = createParent("이지훈", "jihoon@google.com");
        List<Child> childrenFromA = createChild(parentA, "김초코", "김민트", "김그린");
        List<Child> childrenFromB = createChild(parentB, "박대한", "박민국", "박만세");
        List<Child> childrenFromC = createChild(parentB, "이예준");

        // when
        CompletableFuture<Void> requestA = CompletableFuture.runAsync(() -> reservationService.createReservation(createReservationRequest(programSchedule, parentA, childrenFromA)));
        CompletableFuture<Void> requestB = CompletableFuture.runAsync(() -> reservationService.createReservation(createReservationRequest(programSchedule, parentB, childrenFromB)));
        CompletableFuture<Void> requestC = CompletableFuture.runAsync(() -> reservationService.createReservation(createReservationRequest(programSchedule, parentC, childrenFromC)));

        // then
        requestA.get();
        requestB.get();
        requestC.get();

        Optional<ProgramSchedule> scheduleAfter = programScheduleRepository.findById(programSchedule.getId());
        assertThat(scheduleAfter).isNotEmpty();
        assertThat(scheduleAfter.get().getReservationCnt()).isEqualTo(7);

        tearDown();
    }

    @DisplayName("여러 고객이 하나의 수업에 동시에 예약하는 경우, 인원이 부족하다면 특정 고객의 예약은 실패하고 에외가 발생한다.")
    @Test
    void overMaxNumOfChildInMultiThread() throws InterruptedException, ExecutionException {
        // given
        Center center = createCenter();
        Program program = createProgram(center, 4);
        ProgramSchedule programSchedule = createProgramSchedule(LocalDate.now().plusDays(1), LocalTime.of(17, 0), program);

        Parent parentA = createParent("김부모", "test@naver.com");
        Parent parentB = createParent("박철수", "chulsoo@google.com");
        Parent parentC = createParent("이지훈", "jihoon@google.com");

        List<Child> childrenFromA = createChild(parentA, "김초코", "김민트", "김그린");
        List<Child> childrenFromB = createChild(parentB, "박대한", "박민국", "박만세");
        List<Child> childrenFromC = createChild(parentB, "이예준");

        // when
        CompletableFuture<Void> requestA = CompletableFuture.runAsync(() -> reservationService.createReservation(createReservationRequest(programSchedule, parentA, childrenFromA)));
        CompletableFuture<Void> requestB = CompletableFuture.runAsync(() -> reservationService.createReservation(createReservationRequest(programSchedule, parentB, childrenFromB)));
        CompletableFuture<Void> requestC = CompletableFuture.runAsync(() -> reservationService.createReservation(createReservationRequest(programSchedule, parentC, childrenFromC)));

        // then
        assertThatThrownBy(() -> {
            requestA.get();
            requestB.get();
            requestC.get();
        }).isInstanceOf(ExecutionException.class)
                .hasMessageContaining(ErrorCode.OVER_THE_MAX_RESERVATION_CNT.getMessage());

        tearDown();
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