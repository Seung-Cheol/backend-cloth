package project.store.cloth.domain;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import java.time.LocalDate;
import java.util.List;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;

@Entity
@Getter
public class Cloth {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "cloth_id")
  private Long id;

  private String clothName;

  @Column(columnDefinition = "TEXT")
  private String clothContent;

  private String thumbnail;

  private int price;

  @CreatedDate
  private LocalDate created_at;

  @ManyToOne
  @JoinColumn(name = "type_id")
  private ClothType clothType;

  @OneToMany(mappedBy = "clothDetail")
  private List<ClothDetail> clothDetails;

  @OneToMany(mappedBy = "clothPicture")
  private List<ClothPicture> clothPictures;
}
