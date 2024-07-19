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
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
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

  private boolean limited;

  private LocalDateTime startAt;

  @CreatedDate
  private LocalDate created_at;

  @ManyToOne
  @JoinColumn(name = "type_id")
  private ClothType clothType;

  @OneToMany(mappedBy = "cloth")
  private List<ClothDetail> clothDetails;

  @OneToMany(mappedBy = "cloth")
  private List<ClothPicture> clothPictures;
}
