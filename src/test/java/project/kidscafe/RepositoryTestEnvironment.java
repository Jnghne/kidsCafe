package project.kidscafe;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import project.kidscafe.domain.center.CenterRepository;
import project.kidscafe.domain.program.ProgramRepository;
import project.kidscafe.domain.program.ProgramScheduleRepository;
import project.kidscafe.domain.reservation.ReservationChildRepository;
import project.kidscafe.domain.reservation.ReservationRepository;
import project.kidscafe.domain.user.ChildRepository;
import project.kidscafe.domain.user.ParentRepository;
import project.kidscafe.global.config.JpaAuditingConfig;
import project.kidscafe.global.config.P6SpyConfig;
import project.kidscafe.global.config.QuerydslConfig;

@Import({JpaAuditingConfig.class, P6SpyConfig.class, QuerydslConfig.class})
@ActiveProfiles("test")
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) // 기존 dataSource를 교체하지 않는 옵션
public abstract class RepositoryTestEnvironment {
    @Autowired
    protected ReservationRepository reservationRepository;
    @Autowired
    protected ReservationChildRepository reservationChildRepository;
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
}
