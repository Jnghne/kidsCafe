package project.kidscafe;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import project.kidscafe.domain.center.CenterRepository;
import project.kidscafe.domain.program.ProgramRepository;
import project.kidscafe.domain.program.ProgramScheduleRepository;
import project.kidscafe.domain.reservation.ReservationRepository;
import project.kidscafe.api.reservation.service.ReservationService;
import project.kidscafe.domain.user.ChildRepository;
import project.kidscafe.domain.user.ParentRepository;

@ActiveProfiles("test")
@Transactional
@SpringBootTest
public abstract class IntegrateTestEnvironment {

    @Autowired
    protected ReservationRepository reservationRepository;
    @Autowired
    protected CenterRepository centerRepository;
    @Autowired
    protected ProgramRepository programRepository;
    @Autowired
    protected ParentRepository parentRepository;
    @Autowired
    protected ChildRepository childRepository;
    @Autowired
    protected ProgramScheduleRepository programScheduleRepository;
    @Autowired
    protected ReservationService reservationService;

}
