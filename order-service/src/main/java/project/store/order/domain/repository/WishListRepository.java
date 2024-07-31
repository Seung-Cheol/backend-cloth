package project.store.order.domain.repository;

import java.util.List;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import project.store.order.domain.entity.WishList;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface WishListRepository extends ReactiveCrudRepository<WishList, Long> {

  Flux<WishList> findAllByMemberId(Long memberId);

  Mono<WishList> findByIdAndMemberId(Long wishListId, Long memberId);

  Flux<WishList> findByIdIn(List<Long> wishListIds);

}
