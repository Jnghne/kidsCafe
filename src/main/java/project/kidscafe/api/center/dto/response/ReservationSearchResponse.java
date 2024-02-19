package project.kidscafe.api.center.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import project.kidscafe.domain.reservation.ReservationStatus;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@ToString
@NoArgsConstructor
public class ReservationSearchResponse {
    private Long reservationId;
    private String parentName;
    private String email;
    private Long childCnt;
    private ReservationStatus reservationStatus;
    private LocalDateTime reservationDt;
    private LocalDateTime cancelDt;
}

