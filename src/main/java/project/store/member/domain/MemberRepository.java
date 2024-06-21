package project.store.member.domain;


import org.springframework.data.jpa.repository.JpaRepository;
import project.store.member.domain.Member;

public interface MemberRepository extends JpaRepository<Member,Long> {



}
