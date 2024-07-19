package project.store.order.domain.entity;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Embeddable
@Getter
public class OrderAddress {

  @NotBlank
  private String address;

  @NotBlank
  private String addressDetail;

}
