package project.store.order.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Builder;
import project.store.cloth.domain.ClothDetail;
import project.store.order.domain.entity.Order;

@Entity
@Builder
public class OrderCloth {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "order_cloth_id")
  private Long id;

  private int orderClothCount;

  private int orderClothPrice;

  @ManyToOne
  @JoinColumn(name = "order_id")
  private Order order;

  @ManyToOne
  @JoinColumn(name = "cloth_detail_id")
  private ClothDetail clothDetail;


}
