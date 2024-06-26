package project.store.order.domain;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.store.member.domain.Member;


@Repository
public interface WishListRepository extends JpaRepository<WishList, Long> {

  List<WishList> findAllByMember(Member member);

  Optional<WishList> findByIdAndMemberId(Long wishListId, Long memberId);

}
