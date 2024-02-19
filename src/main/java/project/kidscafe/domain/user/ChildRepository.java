package project.kidscafe.domain.user;

import org.springframework.data.jpa.repository.JpaRepository;
import project.kidscafe.domain.user.Child;

public interface ChildRepository extends JpaRepository<Child, Long> {
}
