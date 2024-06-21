package project.store.member.service;


import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import project.store.common.util.AuthCodeGenerate;
import project.store.common.util.EmailUtil;
import project.store.common.util.RedisStringUtil;
import project.store.member.domain.MemberRepository;

@Service
@RequiredArgsConstructor
public class MemberService {

  @Value("${auth.mail.mark}")
  private String authMark;

  private final EmailUtil emailUtil;
  private final RedisStringUtil redisStringUtil;
  public String createNumber(){
    return String.valueOf((int)(Math.random() * (90000) + 100000));// (int) Math.random() * (최댓값-최소값+1) + 최소값
  }
  @Async
  public void sendEmailCode(String email)  {
    String number = this.createNumber();
    redisStringUtil.setDataExpire(email,number,300);
    String title = "인증번호 메일입니다.";
    String body = "";
    body += "요청하신 인증 번호는 " + number + " 입니다.";
    emailUtil.sendEmail(email,title,body);
  }

  public String verifyAuthCode(String email, String authCode) {
    String message = "";
    if(redisStringUtil.getData(email) != null && redisStringUtil.getData(email).equals(authCode)) {
      message += "이메일 인증이 완료되었습니다.";
      redisStringUtil.setDataExpire(email,authMark,1800);
    } else {
      message += "인증번호가 유효하지 않습니다.";
    }
    return message;
  }
}
