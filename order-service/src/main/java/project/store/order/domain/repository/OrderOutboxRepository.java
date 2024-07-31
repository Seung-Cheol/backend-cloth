package project.store.order.domain.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import project.store.order.domain.entity.Order;
import project.store.order.domain.entity.OrderOutbox;
import reactor.core.publisher.Flux;


@Repository
public interface OrderOutboxRepository extends ReactiveCrudRepository<OrderOutbox, Long> {
  Flux<OrderOutbox> findByIsSentFalse();

}
