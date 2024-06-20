package project.store.domain.review;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;
import project.store.domain.cloth.entity.Cloth;
import project.store.domain.member.Member;

public class Review {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "review_id")
  private Long id;
  @Lob
  private String reviewContent;

  private LocalDateTime createdAt;

  private String reviewPicture;

  private int point;

  @ManyToOne
  @JoinColumn(name="cloth_id")
  private Cloth cloth;

  @ManyToOne
  @JoinColumn(name="member_id")
  private Member member;
}
