package project.store.member.domain.repository;


import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.store.member.domain.Member;

@Repository
public interface MemberRepository extends JpaRepository<Member,Long> {

  Optional<Member> findByEmail(String email);


}
