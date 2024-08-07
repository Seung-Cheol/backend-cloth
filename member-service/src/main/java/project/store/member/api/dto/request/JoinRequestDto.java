package project.store.member.api.dto.request;


import jakarta.persistence.Embedded;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import project.store.member.common.util.EncryptUtil;
import project.store.member.domain.Member;
import project.store.member.domain.MemberAddress;

@Data
@NoArgsConstructor
public class JoinRequestDto {

  @Email
  @NotBlank
  private String email;

  @NotBlank
  private String nickname;

  @Embedded
  private MemberAddress memberAddress;

  @NotBlank
  private String password;

  @NotBlank
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
