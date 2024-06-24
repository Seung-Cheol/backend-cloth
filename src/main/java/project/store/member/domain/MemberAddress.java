package project.store.member.domain;


import jakarta.persistence.Embeddable;
import lombok.Data;
import lombok.NoArgsConstructor;
import project.store.common.util.EncryptUtil;

@Data
@Embeddable
public class MemberAddress {

  private String address;
  private String addressDetail;
}
