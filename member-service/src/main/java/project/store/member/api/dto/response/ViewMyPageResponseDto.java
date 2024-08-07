package project.store.member.api.dto.response;

import jakarta.persistence.Embedded;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import project.store.member.common.util.EncryptUtil;
import project.store.member.domain.Member;
import project.store.member.domain.MemberAddress;

@Data
@Builder
@NoArgsConstructor
public class ViewMyPageResponseDto {

  @Email
  @NotBlank
  private String email;

  @NotBlank
  private String nickname;

  @Embedded
  private MemberAddress memberAddress;

  @NotBlank
  private String phone;

  private int point;

  public ViewMyPageResponseDto(String email, String nickname, MemberAddress memberAddress,
    String phone, int point) {
    this.email = email;
    this.nickname = nickname;
    this.memberAddress = memberAddress;
    this.phone = phone;
    this.point = point;
  }

  public static ViewMyPageResponseDto toDto(Member member, EncryptUtil encryptUtil) {
    return ViewMyPageResponseDto.builder()
      .email(encryptUtil.decrypt(member.getEmail()))
      .nickname(member.getNickname())
      .memberAddress(member.getMemberAddress())
      .phone(encryptUtil.decrypt(member.getPhone()))
      .point(member.getPoint())
      .build();
  }

}
