package project.store.order.domain.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import project.store.order.domain.entity.Order;
import project.store.order.domain.entity.OrderCloth;

public interface OrderClothRepository extends JpaRepository<OrderCloth, Long> {

  List<OrderCloth> findByOrder(Order order);

}
