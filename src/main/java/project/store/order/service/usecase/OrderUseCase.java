package project.store.order.service.usecase;


import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import project.store.cloth.service.ClothService;
import project.store.order.service.OrderService;
import project.store.order.service.WishListService;

@RequiredArgsConstructor
public class OrderUseCase {
  private final WishListService wishListService;
  private final ClothService clothService;
  private final OrderService orderService;


  @Transactional
  public String orderFromWishList() {
    //주문 넣는다 (OrderTable)

    //주문 상세 넣는다 (OrderClothTable)

    //재고 처리한다

    //카트에서 제외한다

    return "주문 추가 성공";
  }

  @Transactional
  public String orderFromDetail() {
    //주문 넣는다 (OrderTable)

    //주문 상세 넣는다 (OrderClothTable)

    //재고 처리한다

    return "주문 추가 성공";
  }

}
