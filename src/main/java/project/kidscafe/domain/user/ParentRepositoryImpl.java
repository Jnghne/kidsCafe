package project.kidscafe.domain.user;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.DateTemplate;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPQLQueryFactory;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import project.kidscafe.domain.reservation.ReservationStatus;
import project.kidscafe.api.user.dto.request.ReservationHistoryCondition;
import project.kidscafe.api.user.dto.response.ReservationHistoryResponse;

import java.time.LocalDate;
import java.util.List;

import static project.kidscafe.domain.center.QCenter.center;
import static project.kidscafe.domain.program.QProgram.program;
import static project.kidscafe.domain.program.QProgramSchedule.programSchedule;
import static project.kidscafe.domain.reservation.QReservation.reservation;
import static project.kidscafe.domain.reservation.QReservationChild.reservationChild;
import static project.kidscafe.domain.user.QParent.parent;
import static project.kidscafe.domain.user.QChild.child;

@Repository
@RequiredArgsConstructor
public class ParentRepositoryImpl implements ParentRepositoryCustom {
    private final JPQLQueryFactory jpqlQueryFactory;

    @Override
    public Page<ReservationHistoryResponse> findReservationHistory(Long parentId, ReservationHistoryCondition condition, Pageable pageable) {
        List<ReservationHistoryResponse> reservationHistoryList = getReservationHistoryList(parentId, condition, pageable);

        Long count = getReservationHistoryCount(parentId, condition);

        return new PageImpl<>(reservationHistoryList, pageable, count);
    }

    private List<ReservationHistoryResponse> getReservationHistoryList(Long parentId, ReservationHistoryCondition condition, Pageable pageable) {
        return jpqlQueryFactory.select(Projections.fields(ReservationHistoryResponse.class
                                        , center.centerName
                                        , program.programName
                                        , programSchedule.startDate.as("scheduleStartDate")
                                        , programSchedule.startTime.as("scheduleStartTime")
                                        , reservation.id.as("reservationId")
                                        , parent.parentName
                                        , child.childName.count().as("childCnt")
                                        , reservation.reservationStatus
                                        , reservation.createdDt.as("reservationDt")
                                        , reservation.cancelDt))
                                .from(reservation)
                                    .join(reservation.parent, parent)
                                    .join(reservation.programSchedule, programSchedule)
                                    .join(programSchedule.program, program)
                                    .join(program.center, center)
                                    .join(reservation.reservationChildren, reservationChild)
                                .where(parentEq(parentId)
                                    , centerEq(condition.getCenterId())
                                    , programEq(condition.getProgramId())
                                    , statusIn(condition.getReservationStatus())
                                    , programScheduleBetween(condition.getSearchStartDt(), condition.getSearchEndDt()))
                                .groupBy(reservation.id)
                                .offset(pageable.getOffset())
                                .limit(pageable.getPageSize())
                                .orderBy(reservation.id.desc())
                                .fetch();
    }

    private Long getReservationHistoryCount(Long parentId, ReservationHistoryCondition condition) {
        return jpqlQueryFactory.select(reservation.count())
                                .from(reservation)
                                    .join(reservation.parent, parent)
                                    .join(reservation.programSchedule, programSchedule)
                                    .join(programSchedule.program, program)
                                    .join(program.center, center)
                                    .join(reservation.reservationChildren, reservationChild)
                                .where(parentEq(parentId)
                                    , centerEq(condition.getCenterId())
                                    , programEq(condition.getProgramId())
                                    , programScheduleBetween(condition.getSearchStartDt(), condition.getSearchEndDt()))
                                .groupBy(reservation.id).fetchCount();
    }

    private BooleanExpression statusIn(ReservationStatus reservationStatus) {
        if(reservationStatus == ReservationStatus.OK) {
            return reservation.reservationStatus.in(ReservationStatus.OK);
        } else if (reservationStatus == ReservationStatus.CANCEL) {
            return reservation.reservationStatus.in(ReservationStatus.CANCEL);
        } else {
            return reservation.reservationStatus.in(ReservationStatus.OK, ReservationStatus.CANCEL);
        }
    }

    private BooleanExpression parentEq(@NotNull Long parentId) {
        return parentId != null ? parent.id.eq(parentId) : null;
    }

    private BooleanExpression programScheduleBetween(LocalDate searchStartDt, LocalDate searchEndDt) {
        if(searchStartDt == null || searchEndDt==null) {
            return null;
        }

        DateTemplate<LocalDate> formattedStartDt = Expressions.dateTemplate(LocalDate.class
                                                                            , "DATE_FORMAT({0}, {1})"
                                                                            , searchStartDt
                                                                            , "%Y-%m-%d");

        DateTemplate<LocalDate> formattedEndDt = Expressions.dateTemplate(LocalDate.class
                                                                            , "DATE_FORMAT({0}, {1})"
                                                                            , searchEndDt
                                                                            , "%Y-%m-%d");

        return Expressions.dateTemplate(LocalDate.class
                                        , "DATE_FORMAT({0}, {1})"
                                        , programSchedule.startDate
                                        , "%Y-%m-%d")
                        .between(formattedStartDt, formattedEndDt);
    }

    private BooleanExpression programEq(Long programId) {
        return programId != null ? program.id.eq(programId) : null;
    }

    private BooleanExpression centerEq(@NotNull Long centerId) {
        return centerId != null ? center.id.eq(centerId) : null;
    }
}
