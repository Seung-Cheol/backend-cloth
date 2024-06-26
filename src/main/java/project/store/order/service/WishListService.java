package project.store.order.service;


import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import project.store.cloth.domain.repository.ClothDetailRepository;
import project.store.member.domain.Member;
import project.store.member.domain.repository.MemberRepository;
import project.store.order.api.dto.request.WishListAddRequestDto;
import project.store.order.api.dto.response.WishListResponseDto;
import project.store.order.api.dto.request.WishListUpdateRequestDto;
import project.store.order.domain.entity.WishList;
import project.store.order.domain.repository.WishListRepository;

@Service
@RequiredArgsConstructor
public class WishListService {

  private final WishListRepository wishListRepository;
  private final MemberRepository memberRepository;
  private final ClothDetailRepository clothDetailRepository;

  public String addWishList(Long memberId, WishListAddRequestDto wishListAddRequestDto) {
    Member member = memberRepository.findById(memberId)
      .orElseThrow(() -> new IllegalArgumentException("해당 회원이 없습니다."));

    WishList wishList = WishList.builder()
      .wishlistClothCount(wishListAddRequestDto.getQuantity())
      .clothDetail(clothDetailRepository.findById(wishListAddRequestDto.getClothDetailId())
        .orElseThrow(() -> new IllegalArgumentException("해당 상품이 없습니다.")))
      .member(member)
      .build();

    wishListRepository.save(wishList);

    return "위시리스트에 추가되었습니다.";
  }

  public List<WishListResponseDto> getWishList(Long memberId) {
    Member member = memberRepository.findById(memberId)
      .orElseThrow(() -> new IllegalArgumentException("해당 회원이 없습니다."));
    List<WishList> list = wishListRepository.findAllByMember(member);
    List<WishListResponseDto> data = list.stream()
      .map(WishListResponseDto::toDto)
      .toList();
    return data;
  }

  public List<WishList> getWishListByIds(Long[] wishListIds) {
    return wishListRepository.findByIds(wishListIds);
  }

  public String updateWishList(WishListUpdateRequestDto wishListUpdateRequestDto, Long memberId) {
    Member member = memberRepository.findById(memberId)
      .orElseThrow(() -> new IllegalArgumentException("해당 회원이 없습니다."));
    WishList wishList = wishListRepository.findByIdAndMember(wishListUpdateRequestDto.getWishListId(), member)
      .orElseThrow(() -> new IllegalArgumentException("해당 위시리스트가 없습니다."));
    wishList.updateWishList(wishListUpdateRequestDto.getQuantity());
    wishListRepository.save(wishList);
    return "위시리스트가 수정되었습니다.";
  }

  public String deleteWishList(Long wishListId, Long memberId) {
    Member member = memberRepository.findById(memberId)
      .orElseThrow(() -> new IllegalArgumentException("해당 회원이 없습니다."));
    WishList wishList = wishListRepository.findByIdAndMember(wishListId, member)
      .orElseThrow(() -> new IllegalArgumentException("해당 위시리스트가 없습니다."));
    wishListRepository.delete(wishList);
    return "위시리스트가 삭제되었습니다.";
  }

  public void deleteWishListByIds(Long[] wishListIds) {
    List<WishList> wishLists = wishListRepository.findByIds(wishListIds);
    wishListRepository.deleteAll(wishLists);
  }
}
