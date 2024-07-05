package project.store.order.client;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.PutMapping;
import project.store.order.common.CommonResponseDto;

@FeignClient(name = "member-service")
public interface MemberServiceClient {

  @GetMapping("/member/mypage")
  CommonResponseDto<?> myPage(Long memberId);
}
