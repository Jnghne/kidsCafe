package project.kidscafe.domain.program;

import org.springframework.data.jpa.repository.JpaRepository;
import project.kidscafe.domain.program.Program;

public interface ProgramRepository extends JpaRepository<Program, Long> {
}
