package project.store.order.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Entity
public class WishList {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "wishlist_id")
  private Long id;

  private int wishlistClothCount;

  private Long clothDetailId;

  private Long memberId;

  public void updateWishList(int wishlistClothCount) {
    this.wishlistClothCount = wishlistClothCount;
  }

}
