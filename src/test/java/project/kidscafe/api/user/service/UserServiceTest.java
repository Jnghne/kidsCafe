package project.kidscafe.api.user.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import project.kidscafe.IntegrateTestEnvironment;
import project.kidscafe.domain.center.Center;
import project.kidscafe.domain.program.Program;
import project.kidscafe.domain.program.ProgramSchedule;
import project.kidscafe.api.reservation.dto.request.ReservationRequest;
import project.kidscafe.api.reservation.dto.response.ReservationResponse;
import project.kidscafe.domain.reservation.ReservationStatus;
import project.kidscafe.api.user.dto.request.ReservationHistoryCondition;
import project.kidscafe.api.user.dto.response.ReservationHistoryResponse;
import project.kidscafe.domain.user.Child;
import project.kidscafe.domain.user.Parent;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

class UserServiceTest extends IntegrateTestEnvironment {
    @Autowired
    UserService userService;
    Parent defaultParent;
    Center jamsilCenter;
    Center pangyoCenter;
    Program jamsilCityFarm;
    Program jamsilDrawing;
    Program pangyoCityFarm;
    Program pangyoDrawing; // 예약 X
    ReservationResponse reservation;

    @BeforeEach
    void setUp() {
        jamsilCenter = centerRepository.save(Center.create("잠실점", "서울 송파구 XX동 YY"));
        pangyoCenter = centerRepository.save(Center.create("수원점", "경기 수원시 XX구 YY"));

        jamsilCityFarm = programRepository.save(Program.create("퍼즐놀이", jamsilCenter, 20));
        jamsilDrawing = programRepository.save(Program.create("물감놀이", jamsilCenter, 20));

        pangyoCityFarm = programRepository.save(Program.create("퍼즐놀이", pangyoCenter, 20));
        pangyoDrawing = programRepository.save(Program.create("물감놀이", pangyoCenter, 20));

        // 잠실점 센터 일정
        ProgramSchedule scheduleOfJamsilCityFarm = programScheduleRepository.save(ProgramSchedule.create(LocalDate.now().plusDays(5)
                , LocalTime.of(17, 0)
                , jamsilCityFarm));
        ProgramSchedule scheduleOfJamsilDrawing = programScheduleRepository.save(ProgramSchedule.create(LocalDate.now().plusDays(2)
                , LocalTime.of(17, 0)
                , jamsilDrawing));

        // 수원점 센터 일정
        ProgramSchedule scheduleOfPangyoCityFarm = programScheduleRepository.save(ProgramSchedule.create(LocalDate.now().plusDays(3)
                , LocalTime.of(17, 0)
                , pangyoCityFarm));

        defaultParent = parentRepository.save(Parent.create("김부모", "test@naver.com"));

        List<Child> defaultChildren = childRepository.saveAll(List.of(Child.create("김민트", defaultParent)
                , Child.create("김초코", defaultParent)
                , Child.create("김그린", defaultParent)));

        // 수업 예약
        reservation = reservationService.createReservation(new ReservationRequest(scheduleOfJamsilCityFarm.getId(), defaultParent.getId(), defaultChildren.stream().map(Child::getId).toList()));
        reservationService.createReservation(new ReservationRequest(scheduleOfJamsilDrawing.getId(), defaultParent.getId(), defaultChildren.stream().map(Child::getId).toList()));
        reservationService.createReservation(new ReservationRequest(scheduleOfPangyoCityFarm.getId(), defaultParent.getId(), defaultChildren.stream().map(Child::getId).toList()));
    }

    @DisplayName("사용자의 모든 예약 이력을 조회할 수 있다.")
    @Test
    void findReservationHistoryAll() {
        // given
        ReservationHistoryCondition reservationHistoryCondition = ReservationHistoryCondition.builder()
                .build();

        // when
        Page<ReservationHistoryResponse> reseponse = userService.getReservationHistory(defaultParent.getId(), reservationHistoryCondition, PageRequest.of(0, 10));

        // then
        assertThat(reseponse).hasSize(3);
        assertThat(reseponse.getContent())
                .extracting("centerName","programName", "parentName", "childCnt")
                .containsExactlyInAnyOrder(
                        tuple("잠실점","퍼즐놀이",defaultParent.getParentName(), 3L)
                        ,tuple("잠실점","물감놀이",defaultParent.getParentName(), 3L)
                        ,tuple("수원점","퍼즐놀이",defaultParent.getParentName(), 3L));
    }

    @DisplayName("특정 매장에 대한 예약 이력을 조회할 수 있다.")
    @Test
    void findReservationHistoryByCenter() {
        // given
        ReservationHistoryCondition reservationHistoryCondition = ReservationHistoryCondition.builder()
                .centerId(jamsilCenter.getId())
                .build();

        // when
        Page<ReservationHistoryResponse> reseponse = userService.getReservationHistory(defaultParent.getId(), reservationHistoryCondition, PageRequest.of(0, 10));

        // then
        assertThat(reseponse).hasSize(2);
        assertThat(reseponse.getContent())
                .extracting("centerName","programName", "parentName", "childCnt")
                .containsExactlyInAnyOrder(
                        tuple("잠실점","퍼즐놀이",defaultParent.getParentName(), 3L)
                        ,tuple("잠실점","물감놀이",defaultParent.getParentName(), 3L));
    }

    @DisplayName("특정 매장의 특정 수업에 대한 예약 이력을 조회할 수 있다.")
    @Test
    void findReservationHistoryByProgram() {
        // given
        ReservationHistoryCondition reservationHistoryCondition = ReservationHistoryCondition.builder()
                .centerId(jamsilCenter.getId())
                .programId(jamsilCityFarm.getId())
                .build();

        // when
        Page<ReservationHistoryResponse> reseponse = userService.getReservationHistory(defaultParent.getId(), reservationHistoryCondition, PageRequest.of(0, 10));

        // then
        assertThat(reseponse).hasSize(1);
        assertThat(reseponse.getContent())
                .extracting("centerName","programName", "parentName", "childCnt")
                .containsExactly(tuple("잠실점","퍼즐놀이",defaultParent.getParentName(), 3L));
    }
    
    @DisplayName("조회 기간을 통해 예약 이력을 조회할 수 있다.")
    @Test
    void findReservationHistoryByPeriod() {
        // given
        ReservationHistoryCondition reservationHistoryCondition = ReservationHistoryCondition.builder()
                .searchStartDt(LocalDate.now().plusDays(1))
                .searchEndDt(LocalDate.now().plusDays(3))
                .build();

        // when
        Page<ReservationHistoryResponse> reseponse = userService.getReservationHistory(defaultParent.getId(), reservationHistoryCondition, PageRequest.of(0, 10));

        // then
        assertThat(reseponse).hasSize(2);
        assertThat(reseponse.getContent())
                .extracting("centerName","programName", "parentName", "childCnt")
                .containsExactlyInAnyOrder(tuple("잠실점","물감놀이",defaultParent.getParentName(), 3L)
                                        , tuple("수원점","퍼즐놀이",defaultParent.getParentName(), 3L));
    }
    @DisplayName("취소한 예약만 조회할 수 있다.")
    @Test
    void findReservationHistoryStatusCancel() {
        // given
        reservationService.cancelReservation(reservation.getReservationId());

        ReservationHistoryCondition reservationHistoryCondition = ReservationHistoryCondition.builder()
                .reservationStatus(ReservationStatus.CANCEL)
                .build();

        // when
        Page<ReservationHistoryResponse> reseponse = userService.getReservationHistory(defaultParent.getId(), reservationHistoryCondition, PageRequest.of(0, 10));

        // then
        assertThat(reseponse).hasSize(1);
        assertThat(reseponse.getContent())
                .extracting("centerName","programName", "parentName", "childCnt")
                .containsExactly(tuple("잠실점","퍼즐놀이",defaultParent.getParentName(), 3L));
    }
    @DisplayName("조건에 해당하는 예약 이력이 없으면 빈 값이 반환된다.")
    @Test
    void notFoundReservationHistory() {
        // given
        ReservationHistoryCondition reservationHistoryCondition = ReservationHistoryCondition.builder()
                .centerId(pangyoCenter.getId())
                .programId(pangyoDrawing.getId())
                .build();
        // when
        Page<ReservationHistoryResponse> reseponse = userService.getReservationHistory(defaultParent.getId(), reservationHistoryCondition, PageRequest.of(0, 10));

        // then
        assertThat(reseponse).hasSize(0);
    }
}