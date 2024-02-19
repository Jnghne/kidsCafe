package project.kidscafe.domain.center;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CenterRepository extends JpaRepository<Center, Long>, CenterRepositoryCustom {
}
