package project.store.order.domain.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import project.store.member.domain.Member;
import project.store.order.domain.entity.WishList;


@Repository
public interface WishListRepository extends JpaRepository<WishList, Long> {

  List<WishList> findAllByMember(Member member);

  Optional<WishList> findByIdAndMember(Long wishListId, Member member);

  @Query("select w from WishList w where w.id in :wishListIds")
  List<WishList> findByIds(Long[] wishListIds);

}
