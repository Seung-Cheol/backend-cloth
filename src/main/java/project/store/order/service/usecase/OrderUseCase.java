package project.store.order.service.usecase;


import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
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
      clothService.minusInventory(wishList.getClothDetail().getId(), wishList.getWishlistClothCount());
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

    //포인트 차감
    int totalPrice = orderClothDtos.stream().mapToInt(OrderClothDto::getPrice).sum();
    if(totalPrice > member.getPoint()) {
      return "포인트가 부족합니다.";
    } else {
      memberService.updatePoint(member.getId(), member.getPoint() - totalPrice);
    }


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
    clothService.minusInventory(dto.getClothDetailId(), dto.getQuantity());

    //주문 상세 넣는다 (OrderClothTable)
    OrderClothDto orderClothDto = OrderClothDto.builder()
      .quantity(dto.getQuantity())
      .price(clothService.getClothDetail(dto.getClothDetailId()).getPrice() * dto.getQuantity())
      .order(order)
      .clothDetail(clothService.getDetailEntity(dto.getClothDetailId()))
      .build();

    //멤버 포인트를 낮춘다
    if(orderClothDto.getPrice() > member.getPoint()) {
      return "포인트가 부족합니다.";
    } else {
      memberService.updatePoint(member.getId(), member.getPoint() - orderClothDto.getPrice());
    }

    return "주문 추가 성공";
  }


  @Transactional
  public String cancelOrder(Long orderId, Long memberId) {

    Order order = orderService.getOrderById(orderId);
    if(order.getOrderStatus()!=OrderStatus.PAID) {
      return "취소는 배송 전에만 가능합니다.";
    }

    //주문 상태 변경
    orderService.updateOrderStatus(orderId, OrderStatus.CANCEL);

    //Order Detail 갯수 가져오고 재고증가
    orderService.getOrderClothByOrderId(orderId).forEach(orderCloth -> {
      clothService.plusInventory(orderCloth.getClothDetail().getId(), orderCloth.getOrderClothCount());
      memberService.updatePoint(memberId, memberService.findMemberById(memberId).getPoint() + orderCloth.getOrderClothPrice());
    });

    return "주문이 취소되었습니다.";
  }

  @Transactional
  public String refundOrder(Long orderId, Long memberId) {
    Order order = orderService.getOrderById(orderId);
    if(order.getOrderStatus()!=OrderStatus.COMPLETE) {
      return "환불은 배송 후 가능합니다.";
    }

    if (LocalDateTime.now().isBefore(order.getDeliveryDate().plusDays(1))) {
      return "환불은 배송 완료 후 1일 이내에만 가능합니다.";
    }

    //주문 상태 변경
    orderService.updateOrderStatus(orderId, OrderStatus.REFUND);

    //Order Detail 갯수 가져오고 재고증가
    orderService.getOrderClothByOrderId(orderId).forEach(orderCloth -> {
      clothService.plusInventory(orderCloth.getClothDetail().getId(), orderCloth.getOrderClothCount());
      memberService.updatePoint(memberId, memberService.findMemberById(memberId).getPoint() + orderCloth.getOrderClothPrice());
    });

    return "주문이 환불되었습니다.";
  }



}
