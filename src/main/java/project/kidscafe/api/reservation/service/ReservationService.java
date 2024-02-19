package project.kidscafe.api.reservation.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.kidscafe.domain.program.ProgramSchedule;
import project.kidscafe.domain.program.ProgramScheduleRepository;
import project.kidscafe.api.reservation.dto.request.ReservationRequest;
import project.kidscafe.api.reservation.dto.response.ReservationResponse;
import project.kidscafe.domain.reservation.Reservation;
import project.kidscafe.domain.reservation.ReservationStatus;
import project.kidscafe.domain.reservation.ReservationRepository;
import project.kidscafe.domain.user.Child;
import project.kidscafe.domain.user.Parent;
import project.kidscafe.domain.user.ChildRepository;
import project.kidscafe.domain.user.ParentRepository;
import project.kidscafe.global.exception.CustomException;

import java.time.LocalDate;
import java.util.List;

import static project.kidscafe.global.exception.ErrorCode.*;

@Transactional
@RequiredArgsConstructor
@Slf4j
@Service
public class ReservationService {
    private final ProgramScheduleRepository programScheduleRepository;
    private final ParentRepository parentRepository;
    private final ChildRepository childRepository;
    private final ReservationRepository reservationRepository;

    /**
     * 수업 예약
     * @param request 예약 데이터
     * @return ReservationResponse - 예약 완료 정보
     */
    public ReservationResponse createReservation(ReservationRequest request) {
        // 부모(예약자) 조회
        Parent parent = parentRepository.findById(request.getParentId())
                                        .orElseThrow(() -> new CustomException(NOT_FOUNDED_PARENT));

        // 아이 목록 조회
        List<Child> children = childRepository.findAllById(request.getChildIdList());
        if(children.size() != request.getChildIdList().size()) {
            throw new CustomException(NOT_FOUNDED_CHILD);
        }

        // 수업 일정 조회
        ProgramSchedule programSchedule = programScheduleRepository.findByIdForUpdate(request.getProgramScheduleId())
                                                .orElseThrow(() -> new CustomException(NOT_FOUNDED_PROGRAM_SCHEDULE));

        // 예약 날짜 검증
        if(!programSchedule.isAvailableDate(LocalDate.now())) {
            throw new CustomException(NOT_AVAILABLE_PROGRAM_SCHEDULE);
        }

        // 동일 수업 중복 예약 여부 검증
        validateDuplicationReserve(programSchedule, parent);

        // 수업 일정의 예약 수 증가
        increaseReservationCnt(programSchedule, children);

        // 수업 예약 처리
        Reservation reservation = Reservation.create(programSchedule, parent, children);
        Reservation savedReservation = reservationRepository.save(reservation);

        return ReservationResponse.of(savedReservation);
    }


    /**
     * 수업 예약 취소
     * @param reservationId 예약 ID
     */
    public void cancelReservation(Long reservationId) {
        // 예약 데이터 조회
        Reservation reservation = reservationRepository.findReservationForUpdate(reservationId, ReservationStatus.OK)
                                                            .orElseThrow(() -> new CustomException(NOT_FOUNDED_RESERVATION));

        // 수업 일정의 예약 인원 수 감소
        decreaseReservationCnt(reservation);

        // 예약 취소 처리
        reservation.cancel();
    }

    /**
     * 수업 중복 예약 유효성 검사
     * @param programSchedule 수업 일정
     * @param parent 예약자
     */
    private void validateDuplicationReserve(ProgramSchedule programSchedule, Parent parent) {

        Long programScheduleId = programSchedule.getId();
        Long parentId = parent.getId();

        if(reservationRepository.findByParentAndProgramSchedule(programScheduleId, parentId, ReservationStatus.OK).isPresent()) {
            throw new CustomException(DUPLICATE_RESERVATION);
        }
    }

    /**
     * 수업 일정 별 예약 인원 수 증가
     * @param programSchedule 수업 일정
     * @param children 예약 대상(아이) 목록
     */
    private void increaseReservationCnt(ProgramSchedule programSchedule, List<Child> children) {
        // 수업 최대 인원 초과 여부 검증
        if(programSchedule.isExceedMaxReservationCnt(children.size())){
            throw new CustomException(OVER_THE_MAX_RESERVATION_CNT);
        }
        programSchedule.increaseReservationCnt(children.size());
    }

    /**
     * 수업 일정 별 예약 인원 수 감소
     * @param reservation 예약 데이터
     */
    private void decreaseReservationCnt(Reservation reservation) {
        int reservationCnt = reservation.getReservationChildren().size();

        ProgramSchedule programSchedule = reservation.getProgramSchedule();
        if (programSchedule.isReservationCntLessThanZero(reservationCnt)) {
            throw new CustomException(DECREASE_RESERVATION_CNT_UNDER_THE_ZERO);
        }

        programSchedule.decreaseReservationCnt(reservationCnt);
    }
}
