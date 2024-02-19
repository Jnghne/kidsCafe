package project.kidscafe.api.user.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import project.kidscafe.ControllerTestEnvironment;
import project.kidscafe.api.user.dto.request.ReservationHistoryCondition;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class UserControllerTest extends ControllerTestEnvironment {

    @DisplayName("사용자 별 예약 이력을 조회한다.")
    @Test
    void getReservationHistory() throws Exception {
        // given
        ReservationHistoryCondition request = ReservationHistoryCondition.builder()
                .searchStartDt(LocalDate.now())
                .searchEndDt(LocalDate.now().plusDays(1))
                .build();

        // when / then
        mockMvc.perform(
                        get("/api/v1/user/1/reservation-history")
                                .queryParam("parentId","1")
                                .queryParam("searchStartDt",LocalDate.now().toString())
                                .queryParam("searchEndDt",LocalDate.now().plusDays(1).toString())
                )
                .andDo(print())
                .andExpect(status().isOk());
    }

}