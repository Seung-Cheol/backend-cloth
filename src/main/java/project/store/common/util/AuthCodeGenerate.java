package project.store.common.util;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Random;

public class AuthCodeGenerate {
  public String create() throws Exception {
    int lenth = 6;
      Random random = SecureRandom.getInstanceStrong();
      StringBuilder builder = new StringBuilder();
      for (int i = 0; i < lenth; i++) {
        builder.append(random.nextInt(10));
      }
      return builder.toString();
  }
}
