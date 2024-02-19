package project.kidscafe.api.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.kidscafe.api.user.dto.request.ReservationHistoryCondition;
import project.kidscafe.api.user.dto.response.ReservationHistoryResponse;
import project.kidscafe.domain.user.ParentRepository;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class UserService {
    private final ParentRepository parentRepository;
    public Page<ReservationHistoryResponse> getReservationHistory(Long parentId, ReservationHistoryCondition reservationHistoryCondition, Pageable pageable) {
        return parentRepository.findReservationHistory(parentId, reservationHistoryCondition, pageable);
    }
}
