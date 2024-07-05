package project.store.order.common.util;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.encrypt.AesBytesEncryptor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EncryptUtil {

  private final AesBytesEncryptor encryptor;

  public String encrypt(String str) {
    byte[] encrypt = encryptor.encrypt(str.getBytes(StandardCharsets.UTF_8));
    return byteArrayToString(encrypt);
  }

  public String decrypt(String str) {
    byte[] decryptBytes = stringToByteArray(str);
    byte[] decrypt = encryptor.decrypt(decryptBytes);
    return new String(decrypt, StandardCharsets.UTF_8);
  }

  public String byteArrayToString(byte[] bytes) {
    StringBuilder sb = new StringBuilder();
    for (byte abyte :bytes){
      sb.append(abyte);
      sb.append(" ");
    }
    return sb.toString();
  }

  public byte[] stringToByteArray(String byteString) {
    String[] split = byteString.split("\\s");
    ByteBuffer buffer = ByteBuffer.allocate(split.length);
    for (String s : split) {
      buffer.put((byte) Integer.parseInt(s));
    }
    return buffer.array();
  }
}