package project.store.order.domain.entity;

import jakarta.persistence.Embeddable;

@Embeddable
public class OrderAddress {
  private String address;
  private String addressDetail;

}
