package project.store.domain.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import project.store.domain.PaymentOutbox;

public interface PaymentOutboxRepository extends JpaRepository<PaymentOutbox, Long> {
  List<PaymentOutbox> findByIsSentFalse();

}
