package project.store.member.domain;


import jakarta.persistence.Embeddable;
import lombok.Data;

@Data
@Embeddable
public class MemberAddress {

  private String address;
  private String addressDetail;

}
