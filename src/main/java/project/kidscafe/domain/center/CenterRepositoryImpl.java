package project.kidscafe.domain.center;

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
import project.kidscafe.api.center.dto.request.ReservationSearchCondition;
import project.kidscafe.api.center.dto.response.ReservationSearchResponse;

import java.time.LocalDate;
import java.util.List;

import static project.kidscafe.domain.program.QProgram.program;
import static project.kidscafe.domain.reservation.QReservation.reservation;
import static project.kidscafe.domain.reservation.QReservationChild.reservationChild;
import static project.kidscafe.domain.user.QParent.parent;
import static project.kidscafe.domain.program.QProgramSchedule.programSchedule;
import static project.kidscafe.domain.center.QCenter.center;


@Repository
@RequiredArgsConstructor
public class CenterRepositoryImpl implements CenterRepositoryCustom {
    private final JPQLQueryFactory jpqlQueryFactory;

    @Override
    public Page<ReservationSearchResponse> findReservationsByCondition(Long centerId, ReservationSearchCondition condition, Pageable pageable) {
        List<ReservationSearchResponse> reservationPersonList = getReservationPersonList(centerId, condition, pageable);
        Long count = getReservationPersonCount(centerId, condition);
        return new PageImpl<>(reservationPersonList, pageable, count);
    }

    private List<ReservationSearchResponse> getReservationPersonList(Long centerId, ReservationSearchCondition condition, Pageable pageable) {
        return jpqlQueryFactory.select(Projections.fields(ReservationSearchResponse.class
                                        , reservation.id.as("reservationId")
                                        , parent.parentName
                                        , parent.email
                                        , reservationChild.count().as("childCnt")
                                        , reservation.reservationStatus
                                        , reservation.createdDt.as("reservationDt")
                                        , reservation.cancelDt))
                                .from(reservation)
                                    .join(reservation.programSchedule, programSchedule)
                                    .join(programSchedule.program, program)
                                    .join(program.center, center)
                                    .join(reservation.parent, parent)
                                    .join(reservation.reservationChildren, reservationChild)
                                .where(centerEq(centerId),
                                        programEq(condition.getProgramId()),
                                        programScheduleEq(condition.getProgramScheduleId()),
                                        programScheduleBetween(condition.getSearchStartDt(), condition.getSearchEndDt()),
                                        parentNameEq(condition.getParentName()))
                                .groupBy(reservation.id)
                                .offset(pageable.getOffset())
                                .limit(pageable.getPageSize())
                                .orderBy(reservation.id.desc())
                                .fetch();
    }
    private Long getReservationPersonCount(Long centerId, ReservationSearchCondition condition) {
        return jpqlQueryFactory.select(reservation.count())
                                .from(reservation)
                                    .join(reservation.programSchedule, programSchedule)
                                    .join(reservation.parent, parent)
                                    .join(reservation.reservationChildren, reservationChild)
                                    .join(programSchedule.program, program)
                                    .join(program.center, center)
                                .where(centerEq(centerId),
                                        programEq(condition.getProgramId()),
                                        programScheduleEq(condition.getProgramScheduleId()),
                                        programScheduleBetween(condition.getSearchStartDt(), condition.getSearchEndDt()),
                                        parentNameEq(condition.getParentName()))
                                .groupBy(reservation.id).fetchCount();
    }
    private BooleanExpression parentNameEq(String parentName) {
        return parentName!=null ? parent.parentName.eq(parentName) : null;
    }

    private BooleanExpression programScheduleBetween(LocalDate searchStartDt, LocalDate searchEndDt) {
        if(searchStartDt == null || searchEndDt==null) {
            return null;
        }
        DateTemplate<LocalDate> formattedStartDt = Expressions.dateTemplate(LocalDate.class, "DATE_FORMAT({0}, {1})", searchStartDt, "%Y-%m-%d");
        DateTemplate<LocalDate> formattedEndDt = Expressions.dateTemplate(LocalDate.class, "DATE_FORMAT({0}, {1})", searchEndDt, "%Y-%m-%d");

        return Expressions.dateTemplate(LocalDate.class, "DATE_FORMAT({0}, {1})", programSchedule.startDate, "%Y-%m-%d")
                .between(formattedStartDt, formattedEndDt);
    }

    private BooleanExpression programScheduleEq(Long programScheduleId) {
        return programScheduleId != null ? programSchedule.id.eq(programScheduleId) : null;
    }

    private BooleanExpression programEq(Long programId) {
        return programId != null ? program.id.eq(programId) : null;
    }

    private BooleanExpression centerEq(@NotNull Long centerId) {
        return center.id.eq(centerId);
    }
}
