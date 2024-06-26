package project.store.order.domain.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import project.store.member.domain.Member;
import project.store.order.domain.entity.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {

  Optional<Order> findByMember(Member member);

}
