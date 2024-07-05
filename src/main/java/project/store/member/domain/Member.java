package project.store.member.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.bind.DefaultValue;

@Entity
@Builder
@Getter
@Setter
public class Member {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "member_id")
  private Long id;
  private String email;
  private String nickname;
  private String password;
  @Embedded
  private MemberAddress memberAddress;
  private String phone;
  private int point;
  @Enumerated(EnumType.STRING)
  private MemberRole role;

  public Member() {
  }

  public Member(Long id, String email, String nickname, String password, MemberAddress memberAddress, String phone,
    int point, MemberRole role) {
    this.id = id;
    this.email = email;
    this.nickname = nickname;
    this.password = password;
    this.memberAddress = memberAddress;
    this.phone = phone;
    this.role = role;
    this.point = point;
  }
}
