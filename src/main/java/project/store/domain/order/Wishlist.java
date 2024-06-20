package project.store.domain.order;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import project.store.domain.cloth.entity.Cloth;
import project.store.domain.member.Member;

public class Wishlist {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "wishlist_id")
  private Long id;

  private int wishlistClothCount;

  private int wishlistClothPrice;

  @ManyToOne
  @JoinColumn(name = "cloth_id")
  private Cloth cloth;

  @ManyToOne
  @JoinColumn(name = "member_id")
  private Member member;
}
