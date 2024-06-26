package project.store.order.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.store.order.domain.entity.OrderCloth;

public interface OrderClothRepository extends JpaRepository<OrderCloth, Long> {

}
