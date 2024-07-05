package project.store.domain.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import project.store.domain.Payment;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
  Optional<Payment> findByOrderId(Long orderId);
}
