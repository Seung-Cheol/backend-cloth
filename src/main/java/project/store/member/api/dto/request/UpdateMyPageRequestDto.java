package project.store.member.api.dto.request;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import project.store.common.util.EncryptUtil;
import project.store.member.domain.Member;
import project.store.member.domain.MemberAddress;

@Data
@Builder
@NoArgsConstructor
public class UpdateMyPageRequestDto {

  private String nickname;
  private MemberAddress memberAddress;
  private String phone;

  public UpdateMyPageRequestDto(String nickname, MemberAddress memberAddress, String phone) {
    this.nickname = nickname;
    this.memberAddress = memberAddress;
    this.phone = phone;
  }

  public Member toUpdateEntity(Member member, EncryptUtil encryptUtil) {
    member.setNickname(nickname);
    member.setMemberAddress(memberAddress);
    member.setPhone(encryptUtil.encrypt(phone));
    return member;
  }
}
