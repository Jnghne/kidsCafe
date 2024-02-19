package project.kidscafe.domain.user;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ParentRepository extends JpaRepository<Parent, Long>, ParentRepositoryCustom {
}
