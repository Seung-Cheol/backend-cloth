package project.store.member.common;

import java.util.Collection;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

public class CustomMember extends User {
  private Long id;
  private String email;
  private String role;

  public CustomMember(Long id,String email,String password, Collection<SimpleGrantedAuthority> role) {
    super(email, password, role);
    this.id = id;
    this.email = email;
  }

  public Long getId() {
    return id;
  }

  public String getEmail() {
    return email;
  }

  public String getRole() {
    return role;
  }

}
