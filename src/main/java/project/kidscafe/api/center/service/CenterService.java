package project.kidscafe.api.center.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.kidscafe.api.center.dto.request.ReservationSearchCondition;
import project.kidscafe.api.center.dto.response.ReservationSearchResponse;
import project.kidscafe.domain.center.CenterRepository;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class CenterService {
    private final CenterRepository centerRepository;
    public Page<ReservationSearchResponse> getReservationUserList(Long centerId, ReservationSearchCondition reservationSearchCondition, Pageable pageable) {
        return centerRepository.findReservationsByCondition(centerId, reservationSearchCondition,pageable);
    }
}
