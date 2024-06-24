package project.store.member.api.dto.request;


import jakarta.persistence.Embedded;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import project.store.member.domain.Member;
import project.store.member.domain.MemberAddress;
import project.store.common.util.EncryptUtil;

@Data
@NoArgsConstructor
public class JoinRequestDto {

  @Email
  @NotNull
  private String email;

  @NotNull
  private String nickname;

  @Embedded
  private MemberAddress memberAddress;

  @NotNull
  private String password;

  @NotNull
  private String phone;

  @Builder
  public JoinRequestDto(String email, String nickname, MemberAddress memberAddress, String password,
    String phone) {
    this.email = email;
    this.nickname = nickname;
    this.memberAddress = memberAddress;
    this.password = password;
    this.phone = phone;
  }

  public Member toEntity() {
    return Member.builder()
      .email(email)
      .nickname(nickname)
      .memberAddress(memberAddress)
      .password(password)
      .phone(phone)
      .build();
  }

  public void passwordEncoding(PasswordEncoder passwordEncoder) {
    this.password = passwordEncoder.encode(password);
  }

  public void userInfoEncrypt(EncryptUtil encryptUtil) {
    this.email = encryptUtil.encrypt(email);
    this.phone = encryptUtil.encrypt(phone);
  }
}
