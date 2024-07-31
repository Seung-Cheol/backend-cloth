package project.store.order.domain.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import project.store.order.domain.entity.Order;
import project.store.order.domain.entity.OrderCloth;
import reactor.core.publisher.Flux;


@Repository
public interface OrderClothRepository extends ReactiveCrudRepository<OrderCloth, Long> {

  Flux<OrderCloth> findByOrder(Order order);

}
