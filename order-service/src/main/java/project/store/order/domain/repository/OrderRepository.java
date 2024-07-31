package project.store.order.domain.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import project.store.order.domain.entity.Order;
import project.store.order.domain.entity.WishList;
import reactor.core.publisher.Flux;


@Repository
public interface OrderRepository extends ReactiveCrudRepository<Order, Long> {

  Flux<Order> findAllByMemberId(Long memberId);

}
