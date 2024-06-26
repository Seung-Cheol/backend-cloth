package project.store.order.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Builder;
import lombok.Getter;
import project.store.cloth.domain.ClothDetail;
import project.store.member.domain.Member;

@Getter
@Builder
@Entity
public class WishList {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "wishlist_id")
  private Long id;

  private int wishlistClothCount;

  @ManyToOne
  @JoinColumn(name = "cloth_detail_id")
  private ClothDetail clothDetail;

  @ManyToOne
  @JoinColumn(name = "member_id")
  private Member member;

  public void updateWishList(int wishlistClothCount) {
    this.wishlistClothCount = wishlistClothCount;
  }

}
