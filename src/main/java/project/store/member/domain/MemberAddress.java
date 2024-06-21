package project.store.member.domain;


import jakarta.persistence.Embeddable;

@Embeddable
public class MemberAddress {

  private String address;
  private String addressDetail;

}
