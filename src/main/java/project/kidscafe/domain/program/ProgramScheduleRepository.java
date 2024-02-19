package project.kidscafe.domain.program;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import project.kidscafe.domain.program.ProgramSchedule;

import java.util.Optional;

public interface ProgramScheduleRepository extends JpaRepository<ProgramSchedule, Long> {
    /**
     * 수업 일정 ID로 수업 일정 조회 (PESSIMISTIC_WRITE LOCK)
     * @param programScheduleId : 수업 일정 ID
     * @return
     */
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select s from ProgramSchedule s where s.id = :programScheduleId")
    Optional<ProgramSchedule> findByIdForUpdate(@Param("programScheduleId") Long programScheduleId);
}