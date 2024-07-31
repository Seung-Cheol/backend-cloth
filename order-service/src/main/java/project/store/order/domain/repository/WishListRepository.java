package project.store.order.domain.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import project.store.order.domain.entity.WishList;


public interface WishListRepository extends JpaRepository<WishList, Long> {

  List<WishList> findAllByMemberId(Long memberId);

  Optional<WishList> findByIdAndMemberId(Long wishListId, Long memberId);

  @Query("select w from WishList w where w.id in :wishListIds")
  List<WishList> findByIds(Long[] wishListIds);

}
