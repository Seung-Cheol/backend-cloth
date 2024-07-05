package project.store.order.domain.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import project.store.member.domain.Member;

@Entity
@Table(name="orders")
@Builder
@Getter
@NoArgsConstructor
public class Order {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "order_id")
  private Long id;

  @CreationTimestamp
  private LocalDateTime orderDate;

  @Enumerated(EnumType.STRING)
  private OrderStatus orderStatus;

  @Embedded
  private OrderAddress orderAddress;

  private LocalDateTime deliveryDate;

  private LocalDateTime refundDate;

  @ManyToOne
  @JoinColumn(name = "member_id")
  private Member member;

  @OneToMany(mappedBy = "order")
  @Builder.Default
  private List<OrderCloth> orderCloths = new ArrayList<>();

  public void updateStatus(OrderStatus status) {
    this.orderStatus = status;
  }

  public Order(Long id, LocalDateTime orderDate, OrderStatus orderStatus, OrderAddress orderAddress,
    LocalDateTime deliveryDate, LocalDateTime refundDate, Member member,
    List<OrderCloth> orderCloths) {
    this.id = id;
    this.orderDate = orderDate;
    this.orderStatus = orderStatus;
    this.orderAddress = orderAddress;
    this.deliveryDate = deliveryDate;
    this.refundDate = refundDate;
    this.member = member;
  }
}
