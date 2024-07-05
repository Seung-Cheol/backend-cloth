package project.store;

import java.util.Collection;
import java.util.Collections;
import lombok.Builder;
import lombok.Getter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

@Getter
public class CustomMember extends User {
  private String id;
  private String email;
  private String role;

  public CustomMember(String id,String email,String password, Collection<SimpleGrantedAuthority> role) {
    super(id, password, role);
    this.id = id;
    this.email = email;
  }

  public String getId() {
    return id;
  }

  public String getEmail() {
    return email;
  }

  public String getRole() {
    return role;
  }

}

