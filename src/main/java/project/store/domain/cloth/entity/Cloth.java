package project.store.domain.cloth.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToOne;
import java.time.LocalDate;

@Entity
public class Cloth {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "member_id")
  private Long id;

  private String clothName;
  @Lob
  private String clothContent;

  private int price;

  private int inventory;

  @Enumerated(EnumType.STRING)
  private ClothSize size;

  private LocalDate created_at;

  @OneToOne
  @JoinColumn(name = "type_id")
  private ClothType clothType;


}
