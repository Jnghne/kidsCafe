package project.kidscafe.api.reservation.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import project.kidscafe.ControllerTestEnvironment;
import project.kidscafe.api.reservation.dto.request.ReservationRequest;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


class ReservationControllerTest extends ControllerTestEnvironment {

    @DisplayName("수업 일정을 예약할 수 있다.")
    @Test
    void createReservation() throws Exception {
        // given
        ReservationRequest reservationRequest = ReservationRequest.builder()
                .programScheduleId(1L)
                .parentId(1L)
                .childIdList(List.of(1L,2L))
                .build();

        // when / then
        mockMvc.perform(
                        post("/api/v1/reservation")
                                .content(objectMapper.writeValueAsString(reservationRequest))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk());
    }
    @DisplayName("수업 예약 시 부모 ID값은 필수 값이다")
    @Test
    void createReservationWithoutParentId() throws Exception {
        // given
        ReservationRequest reservationRequest = ReservationRequest.builder()
                .programScheduleId(1L)
                .childIdList(List.of(1L,2L))
                .build();

        // when / then
        mockMvc.perform(
                        post("/api/v1/reservation")
                                .content(objectMapper.writeValueAsString(reservationRequest))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value("부모 ID값은 필수입니다."))
                .andExpect(jsonPath("$.data").isEmpty());
    }
    @DisplayName("수업 예약 시 수업 일정 ID값은 필수 값이다")
    @Test
    void createReservationWithoutProgramScheduleId() throws Exception {
        // given
        ReservationRequest reservationRequest = ReservationRequest.builder()
                .parentId(1L)
                .childIdList(List.of(1L,2L))
                .build();
        // when / then
        mockMvc.perform(
                        post("/api/v1/reservation")
                                .content(objectMapper.writeValueAsString(reservationRequest))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value("수업 일정 ID값은 필수입니다."))
                .andExpect(jsonPath("$.data").isEmpty());
    }
    @DisplayName("수업 예약 시 아이 ID값 리스트 값은 필수 값이다")
    @Test
    void createReservationWithoutChildIdList() throws Exception {
        // given
        ReservationRequest reservationRequest = ReservationRequest.builder()
                .programScheduleId(1L)
                .parentId(1L)
                .build();

        // when / then
        mockMvc.perform(
                        post("/api/v1/reservation")
                                .content(objectMapper.writeValueAsString(reservationRequest))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value("아이 ID값 리스트는 필수입니다."))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @DisplayName("수업 예약을 취소할 수 있다.")
    @Test
    void cancelReservation() throws Exception {
        // when / then
        mockMvc.perform(
                        patch("/api/v1/reservation/1/cancel")
                )
                .andDo(print())
                .andExpect(status().isOk());
    }
}