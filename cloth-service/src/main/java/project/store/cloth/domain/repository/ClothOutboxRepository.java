package project.store.cloth.domain.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import project.store.cloth.domain.ClothOutbox;

public interface ClothOutboxRepository extends JpaRepository<ClothOutbox, Long> {
  List<ClothOutbox> findByIsSentFalse();

}
