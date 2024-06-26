package project.store.order.service.usecase;


import jakarta.transaction.Transactional;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import project.store.cloth.api.dto.response.ClothDetailResponseDto;
import project.store.cloth.domain.ClothDetail;
import project.store.cloth.service.ClothService;
import project.store.member.domain.Member;
import project.store.member.service.MemberService;
import project.store.order.api.dto.request.OrderFromWishListRequestDto;
import project.store.order.api.dto.request.OrderRequestDto;
import project.store.order.domain.entity.Order;
import project.store.order.domain.entity.OrderStatus;
import project.store.order.domain.entity.WishList;
import project.store.order.service.OrderService;
import project.store.order.service.WishListService;
import project.store.order.service.dto.OrderClothDto;
import project.store.order.service.dto.OrderDto;

@RequiredArgsConstructor
@Component
public class OrderUseCase {
  private final WishListService wishListService;
  private final MemberService memberService;
  private final ClothService clothService;
  private final OrderService orderService;


  @Transactional
  public String orderFromWishList(OrderFromWishListRequestDto dto, Long memberId) {
    //유저 정보 추출
    Member member = memberService.findMemberById(memberId);

    // 주문 생성
    OrderDto orderInfo = OrderDto.builder()
      .orderAddress(dto.getOrderAddress())
      .member(member)
      .orderStatus(OrderStatus.PAID)
      .build();
    Order order = orderService.createOrder(orderInfo);

    //카트에서 상품 정보 추출
    List<WishList> wishLists = wishListService.getWishListByIds(dto.getWishListIds());

    //재고가 충분한지 확인한다
    for (WishList wishList : wishLists) {
      if (wishList.getClothDetail().getInventory() < wishList.getWishlistClothCount()) {
        return "재고가 부족합니다.";
      }
    }

    //재고 처리한다
    for (WishList wishList : wishLists) {
      clothService.updateInventory(wishList.getClothDetail().getId(), wishList.getWishlistClothCount());
    }

    //주문 상세 넣는다 (OrderClothTable)
    List<OrderClothDto> orderClothDtos = wishLists.stream()
      .map(wishList -> OrderClothDto.builder()
        .quantity(wishList.getWishlistClothCount())
        .price(wishList.getClothDetail().getCloth().getPrice() * wishList.getWishlistClothCount())
        .order(order)
        .clothDetail(wishList.getClothDetail())
        .build())
      .toList();
    orderService.createOrderCloth(orderClothDtos);


    //카트에서 제거
    wishListService.deleteWishListByIds(dto.getWishListIds());


    return "주문 추가 성공";
  }

  @Transactional
  public String orderFromDetail(OrderRequestDto dto, Long memberId) {

    //유저 정보 추출
    Member member = memberService.findMemberById(memberId);
    //주문 생성
    OrderDto orderInfo = OrderDto.builder()
      .orderAddress(dto.getOrderAddress())
      .member(member)
      .orderStatus(OrderStatus.PAID)
      .build();
    Order order = orderService.createOrder(orderInfo);

    //재고 확인
    if (clothService.getDetailEntity(dto.getClothDetailId()).getInventory() < dto.getQuantity()) {
      return "재고가 부족합니다.";
    }

    //재고 처리한다
    clothService.updateInventory(dto.getClothDetailId(), dto.getQuantity());

    //주문 상세 넣는다 (OrderClothTable)
    OrderClothDto orderClothDto = OrderClothDto.builder()
      .quantity(dto.getQuantity())
      .price(clothService.getClothDetail(dto.getClothDetailId()).getPrice() * dto.getQuantity())
      .order(order)
      .clothDetail(clothService.getDetailEntity(dto.getClothDetailId()))
      .build();

    return "주문 추가 성공";
  }

}
