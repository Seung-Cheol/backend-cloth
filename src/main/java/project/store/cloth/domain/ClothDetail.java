package project.store.cloth.domain;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.Builder;
import lombok.Getter;

@Entity
@Builder
@Getter
public class ClothDetail {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "cloth_detail_id")
  private Long id;

  private int inventory;

  @Enumerated(EnumType.STRING)
  private ClothSize size;

  @ManyToOne
  @JoinColumn(name = "cloth_id")
  private Cloth cloth;

  public void updateInventory(int count) {
    this.inventory -= count;
  }

}
