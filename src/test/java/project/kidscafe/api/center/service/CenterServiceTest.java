package project.kidscafe.api.center.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import project.kidscafe.IntegrateTestEnvironment;
import project.kidscafe.api.center.dto.request.ReservationSearchCondition;
import project.kidscafe.api.center.dto.response.ReservationSearchResponse;
import project.kidscafe.domain.center.Center;
import project.kidscafe.domain.center.CenterRepositoryImpl;
import project.kidscafe.domain.program.Program;
import project.kidscafe.domain.program.ProgramSchedule;
import project.kidscafe.api.reservation.dto.request.ReservationRequest;
import project.kidscafe.api.reservation.dto.response.ReservationResponse;
import project.kidscafe.domain.user.Child;
import project.kidscafe.domain.user.Parent;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

class CenterServiceTest extends IntegrateTestEnvironment {
    @Autowired
    CenterRepositoryImpl centerRepositoryImpl;
    Center defaultCenter;
    Program defaultProgram;
    ProgramSchedule defaultProgramSchedule;

    @BeforeEach
    void setUp() {
        defaultCenter = centerRepository.save(Center.create("잠실점", "서울 송파구 XX동 YY"));
        defaultProgram = programRepository.save(Program.create("퍼즐놀이", defaultCenter, 20));
        defaultProgramSchedule = programScheduleRepository.save(ProgramSchedule.create(LocalDate.now().plusDays(1)
                , LocalTime.of(17, 0)
                , defaultProgram));
    }


    @DisplayName("매장 ID로 예약자 현황을 조회할 수 있다.")
    @Test
    void findReservationUserByCenter() {
        // given
        Parent parentA = parentRepository.save(Parent.create("김부모", "test@naver.com"));
        Parent parentB = parentRepository.save(Parent.create("박철수", "chulsoo@google.com"));
        Parent parentC = parentRepository.save(Parent.create("이지훈", "jihoon@google.com"));

        Child childrenA = childRepository.save(Child.create("김초코", parentA));
        List<Child> childrenB = childRepository.saveAll(List.of(Child.create("김민트", parentB), Child.create("김땡땡", parentB)));
        Child childrenC = childRepository.save(Child.create("김그린", parentC));

        ReservationResponse reservationA = reservationService.createReservation(new ReservationRequest(defaultProgramSchedule.getId(), parentA.getId(), List.of(childrenA.getId())));
        ReservationResponse reservationB = reservationService.createReservation(new ReservationRequest(defaultProgramSchedule.getId(), parentB.getId(), childrenB.stream().map(Child::getId).toList()));
        ReservationResponse reservationC = reservationService.createReservation(new ReservationRequest(defaultProgramSchedule.getId(), parentC.getId(), List.of(childrenC.getId())));

        // when
        ReservationSearchCondition reservationSearchCondition = ReservationSearchCondition.builder().build();
        Page<ReservationSearchResponse> response = centerRepositoryImpl.findReservationsByCondition(defaultCenter.getId(), reservationSearchCondition, PageRequest.of(0, 10));

        // then
        assertThat(response).hasSize(3);

        assertThat(response.getContent()).extracting("reservationId", "parentName", "email", "childCnt")
                .containsExactlyInAnyOrder(tuple(reservationA.getReservationId(), parentA.getParentName(), parentA.getEmail(), 1L)
                        , tuple(reservationB.getReservationId(), parentB.getParentName(), parentB.getEmail(), 2L)
                        , tuple(reservationC.getReservationId(), parentC.getParentName(), parentC.getEmail(), 1L));
    }

    @DisplayName("매장 ID + 수업 ID로 예약자 현황을 조회할 수 있다.")
    @Test
    void findReservationUserByProgram() {
        // given
        Parent parentA = parentRepository.save(Parent.create("김부모", "test@naver.com"));
        Parent parentB = parentRepository.save(Parent.create("박철수", "chulsoo@google.com"));
        Parent parentC = parentRepository.save(Parent.create("이지훈", "jihoon@google.com"));

        Child childrenA = childRepository.save(Child.create("김초코", parentA));
        List<Child> childrenB = childRepository.saveAll(List.of(Child.create("김민트", parentB), Child.create("김땡땡", parentB)));
        Child childrenC = childRepository.save(Child.create("김그린", parentC));

        ReservationResponse reservationA = reservationService.createReservation(new ReservationRequest(defaultProgramSchedule.getId(), parentA.getId(), List.of(childrenA.getId())));
        ReservationResponse reservationB = reservationService.createReservation(new ReservationRequest(defaultProgramSchedule.getId(), parentB.getId(), childrenB.stream().map(Child::getId).toList()));
        ReservationResponse reservationC = reservationService.createReservation(new ReservationRequest(defaultProgramSchedule.getId(), parentC.getId(), List.of(childrenC.getId())));

        // when
        ReservationSearchCondition reservationSearchCondition = ReservationSearchCondition.builder().programId(defaultProgram.getId()).build();
        Page<ReservationSearchResponse> response = centerRepositoryImpl.findReservationsByCondition(defaultCenter.getId(), reservationSearchCondition, PageRequest.of(0, 10));

        // then
        assertThat(response).hasSize(3);

        assertThat(response.getContent()).extracting("reservationId", "parentName", "email", "childCnt")
                .containsExactlyInAnyOrder(tuple(reservationA.getReservationId(), parentA.getParentName(), parentA.getEmail(), 1L)
                        , tuple(reservationB.getReservationId(), parentB.getParentName(), parentB.getEmail(), 2L)
                        , tuple(reservationC.getReservationId(), parentC.getParentName(), parentC.getEmail(), 1L));
    }

    @DisplayName("매장 ID + 수업 ID + 스케줄 ID로 예약자 현황을 조회할 수 있다.")
    @Test
    void findReservationUserByProgramSchedule() {
        // given
        Parent parentA = parentRepository.save(Parent.create("김부모", "test@naver.com"));
        Parent parentB = parentRepository.save(Parent.create("박철수", "chulsoo@google.com"));
        Parent parentC = parentRepository.save(Parent.create("이지훈", "jihoon@google.com"));

        Child childrenA = childRepository.save(Child.create("김초코", parentA));
        List<Child> childrenB = childRepository.saveAll(List.of(Child.create("김민트", parentB), Child.create("김땡땡", parentB)));
        Child childrenC = childRepository.save(Child.create("김그린", parentC));

        ReservationResponse reservationA = reservationService.createReservation(new ReservationRequest(defaultProgramSchedule.getId(), parentA.getId(), List.of(childrenA.getId())));
        ReservationResponse reservationB = reservationService.createReservation(new ReservationRequest(defaultProgramSchedule.getId(), parentB.getId(), childrenB.stream().map(Child::getId).toList()));
        ReservationResponse reservationC = reservationService.createReservation(new ReservationRequest(defaultProgramSchedule.getId(), parentC.getId(), List.of(childrenC.getId())));

        // when
        ReservationSearchCondition reservationSearchCondition = ReservationSearchCondition.builder()
                .programId(defaultProgram.getId())
                .programScheduleId(defaultProgramSchedule.getId())
                .build();
        Page<ReservationSearchResponse> response = centerRepositoryImpl.findReservationsByCondition(defaultCenter.getId(), reservationSearchCondition, PageRequest.of(0, 10));

        // then
        assertThat(response).hasSize(3);
        assertThat(response.getContent()).extracting("reservationId", "parentName", "email", "childCnt")
                .containsExactlyInAnyOrder(tuple(reservationA.getReservationId(), parentA.getParentName(), parentA.getEmail(), 1L)
                        , tuple(reservationB.getReservationId(), parentB.getParentName(), parentB.getEmail(), 2L)
                        , tuple(reservationC.getReservationId(), parentC.getParentName(), parentC.getEmail(), 1L));
    }

    @DisplayName("매장 ID + 수업 ID + 스케줄 ID + 예약자 명으로 특정 예약자를 조회할 수 있다.")
    @Test
    void findReservationUserByName() {
        // given
        Parent parentA = parentRepository.save(Parent.create("김부모", "test@naver.com"));
        Parent parentB = parentRepository.save(Parent.create("박철수", "chulsoo@google.com"));
        Parent parentC = parentRepository.save(Parent.create("이지훈", "jihoon@google.com"));

        Child childrenA = childRepository.save(Child.create("김초코", parentA));
        List<Child> childrenB = childRepository.saveAll(List.of(Child.create("김민트", parentB), Child.create("김땡땡", parentB)));
        Child childrenC = childRepository.save(Child.create("김그린", parentC));

        ReservationResponse reservationA = reservationService.createReservation(new ReservationRequest(defaultProgramSchedule.getId(), parentA.getId(), List.of(childrenA.getId())));
        ReservationResponse reservationB = reservationService.createReservation(new ReservationRequest(defaultProgramSchedule.getId(), parentB.getId(), childrenB.stream().map(Child::getId).toList()));
        ReservationResponse reservationC = reservationService.createReservation(new ReservationRequest(defaultProgramSchedule.getId(), parentC.getId(), List.of(childrenC.getId())));

        // when
        ReservationSearchCondition reservationSearchCondition = ReservationSearchCondition.builder()
                .programId(defaultProgram.getId())
                .programScheduleId(defaultProgramSchedule.getId())
                .parentName(parentB.getParentName())
                .build();

        Page<ReservationSearchResponse> response = centerRepositoryImpl.findReservationsByCondition(defaultCenter.getId(), reservationSearchCondition, PageRequest.of(0, 10));

        // then
        assertThat(response).hasSize(1);
        assertThat(response.getContent()).extracting("reservationId", "parentName", "email", "childCnt")
                .containsExactly(tuple(reservationB.getReservationId(), parentB.getParentName(), parentB.getEmail(), 2L));

    }

    @DisplayName("매장 ID + 수업 ID + 조회 일자로 예약자를 조회할 수 있다.")
    @Test
    void findReservationUserByScheduleDate() {
        // given
        ProgramSchedule programScheduleA = programScheduleRepository.save(ProgramSchedule.create(LocalDate.now().plusDays(1), LocalTime.of(17, 0), defaultProgram));
        ProgramSchedule programScheduleB = programScheduleRepository.save(ProgramSchedule.create(LocalDate.now().plusDays(5), LocalTime.of(17, 0), defaultProgram));
        ProgramSchedule programScheduleC = programScheduleRepository.save(ProgramSchedule.create(LocalDate.now().plusDays(7), LocalTime.of(17, 0), defaultProgram));

        Parent parentA = parentRepository.save(Parent.create("김부모", "test@naver.com"));
        Parent parentB = parentRepository.save(Parent.create("박철수", "chulsoo@google.com"));
        Parent parentC = parentRepository.save(Parent.create("이지훈", "jihoon@google.com"));

        Child childrenA = childRepository.save(Child.create("김초코", parentA));
        List<Child> childrenB = childRepository.saveAll(List.of(Child.create("김민트", parentB), Child.create("김땡땡", parentB)));
        Child childrenC = childRepository.save(Child.create("김그린", parentC));

        ReservationResponse reservationA = reservationService.createReservation(new ReservationRequest(programScheduleA.getId(), parentA.getId(), List.of(childrenA.getId())));
        ReservationResponse reservationB = reservationService.createReservation(new ReservationRequest(programScheduleB.getId(), parentB.getId(), childrenB.stream().map(Child::getId).toList()));
        ReservationResponse reservationC = reservationService.createReservation(new ReservationRequest(programScheduleC.getId(), parentC.getId(), List.of(childrenC.getId())));

        // when
        ReservationSearchCondition reservationSearchCondition = ReservationSearchCondition.builder()
                .programId(defaultProgram.getId())
                .searchStartDt(LocalDate.now().plusDays(2))
                .searchEndDt(LocalDate.now().plusDays(6))
                .build();

        Page<ReservationSearchResponse> response = centerRepositoryImpl.findReservationsByCondition(defaultCenter.getId(), reservationSearchCondition, PageRequest.of(0, 10));

        // then
        assertThat(response).hasSize(1);
        assertThat(response.getContent()).extracting("reservationId", "parentName", "email", "childCnt")
                .containsExactly(tuple(reservationB.getReservationId(), parentB.getParentName(), parentB.getEmail(), 2L));
    }
}