package com.zhuzhe.integrationmqtt;

import java.util.Random;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
@SpringBootTest
class IntegrationMqttApplicationTests {

  @Test
  void contextLoads() {
    
  }

  /*测试工具: 随机mac和sn*/
  @Test
  public void generateMacAndSn() {
    System.out.println("---mac---");
    for (int i = 0; i < 10; i++) {
      String mac = generateRandomMac();
      System.out.println(mac.toUpperCase());
    }
    System.out.println("---sn---");
    for (int i = 0; i < 10; i++) {
      String sn = generateRandomSN();
      System.out.println(sn);
    }
  }

  public String generateRandomMac() {
    Random random = new Random();
    byte[] macBytes = new byte[6];
    random.nextBytes(macBytes);

    macBytes[0] |= (byte) 0b00000010;

    StringBuilder sb = new StringBuilder(18);
    for (byte b : macBytes) {
      sb.append(String.format("%02x", b));
    }
    return sb.toString();
  }

  public String generateRandomSN() {
    String prefix = "202200000000000";
    var nextInt = new Random().nextInt(1111, 9999);
    return prefix + nextInt;
  }
}
