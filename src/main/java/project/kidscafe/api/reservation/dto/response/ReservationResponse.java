package project.kidscafe.api.reservation.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import project.kidscafe.domain.reservation.Reservation;

import java.time.LocalDateTime;

@ToString
@Getter
public final class ReservationResponse {
    private final Long reservationId;
    private final String CenterName;
    private final String programName;
    private final String parentName;
    private final int reservationCnt;
    private final LocalDateTime reservationDt;
    @Builder
    private ReservationResponse(Long reservationId, String centerName, String programName, String parentName, int reservationCnt, LocalDateTime reservationDt) {
        this.reservationId = reservationId;
        this.CenterName = centerName;
        this.programName = programName;
        this.parentName = parentName;
        this.reservationCnt = reservationCnt;
        this.reservationDt = reservationDt;
    }
    public static ReservationResponse of(Reservation reservation) {
        return ReservationResponse.builder()
                .reservationId(reservation.getId())
                .centerName(reservation.getProgramSchedule().getProgram().getCenter().getCenterName())
                .programName(reservation.getProgramSchedule().getProgram().getProgramName())
                .parentName(reservation.getParent().getParentName())
                .reservationCnt(reservation.getReservationChildren().size())
                .reservationDt(reservation.getCreatedDt())
                .build();
    }
}
