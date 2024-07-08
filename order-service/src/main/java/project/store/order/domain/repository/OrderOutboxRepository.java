package project.store.order.domain.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import project.store.order.domain.entity.OrderOutbox;

public interface OrderOutboxRepository extends JpaRepository<OrderOutbox, Long> {
  List<OrderOutbox> findByIsSentFalse();

}
