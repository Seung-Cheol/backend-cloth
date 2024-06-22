package project.store.member.auth;

public class TokenInfo {
  private String grantType;
  private String accessToken;

  public TokenInfo(String grantType, String accessToken) {
    this.grantType = grantType;
    this.accessToken = accessToken;
  }

}
