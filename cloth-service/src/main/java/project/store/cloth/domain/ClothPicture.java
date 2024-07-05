package project.store.cloth.domain;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class ClothPicture {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "cloth_picture_id")
  private Long id;

  private String clothPicture;

  @ManyToOne
  @JoinColumn(name = "cloth_id")
  private Cloth cloth;

}
