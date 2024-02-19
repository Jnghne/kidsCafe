package project.kidscafe;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import project.kidscafe.api.center.controller.CenterController;
import project.kidscafe.api.center.service.CenterService;
import project.kidscafe.api.reservation.controller.ReservationController;
import project.kidscafe.api.reservation.service.ReservationService;
import project.kidscafe.api.user.controller.UserController;
import project.kidscafe.api.user.service.UserService;

@WebMvcTest(controllers = {ReservationController.class, CenterController.class, UserController.class})
public abstract class ControllerTestEnvironment {
    @Autowired
    protected MockMvc mockMvc;
    @Autowired
    protected ObjectMapper objectMapper;
    @MockBean
    protected ReservationService reservationService;
    @MockBean
    protected CenterService centerService;
    @MockBean
    protected UserService userService;
}
