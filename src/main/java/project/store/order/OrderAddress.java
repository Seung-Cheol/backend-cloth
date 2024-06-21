package project.store.order;

import jakarta.persistence.Embeddable;

@Embeddable
public class OrderAddress {
  private String address;
  private String addressDetail;

}
