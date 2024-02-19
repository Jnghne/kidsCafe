package project.kidscafe.api.center.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import project.kidscafe.ControllerTestEnvironment;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class CenterControllerTest extends ControllerTestEnvironment {
    final int centerId = 1;

    @DisplayName("예약자 현황을 조회할 수 있다.")
    @Test
    void getReservationParentList() throws Exception {
        // when / then
        mockMvc.perform(
                        get("/api/v1/center/"+centerId+"/reservations")
                                .queryParam("programScheduleId","1")
                                .queryParam("searchStartDt",LocalDate.now().toString())
                                .queryParam("searchEndDt",LocalDate.now().plusDays(1).toString())
                                .queryParam("parentName","김부모")
                )
                .andDo(print())
                .andExpect(status().isOk());
    }

}