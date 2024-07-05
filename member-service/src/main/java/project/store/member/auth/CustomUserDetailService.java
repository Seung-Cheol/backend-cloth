package project.store.member.auth;

import java.util.Collections;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import project.store.member.common.CustomMember;
import project.store.member.common.util.EncryptUtil;
import project.store.member.domain.Member;
import project.store.member.domain.repository.MemberRepository;

@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {

  private final MemberRepository memberRepository;
  private final EncryptUtil encryptUtil;
  @Override
  public UserDetails loadUserByUsername(String email) {
    Member member = memberRepository.findByEmail(encryptUtil.encrypt(email))
      .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));
    return creaetUserDetails(member);
  }

  private UserDetails creaetUserDetails(Member member) {
    return new CustomMember(member.getId(), member.getEmail(), member.getPassword(),
      Collections.singleton(new SimpleGrantedAuthority("ROLE_" + member.getRole())));
  }
}
