package com.zhuzhe.integrationmqtt.mqtt.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/*SSL安全连接工具类*/
public class SslUtil {

  private static final Logger log = LoggerFactory.getLogger(SslUtil.class);

  // 提供指定证书生成秘钥下的SSL安全连接
  public static SSLSocketFactory getSslSocket(String caPath) {
    try {
      // 加载证书
      var caF = CertificateFactory.getInstance("X.509");
      var caIn = new FileInputStream(caPath);
      var ca = (X509Certificate) caF.generateCertificate(caIn);
      // 创建秘钥库,将证书添加到里面
      var caKs = KeyStore.getInstance("JKS");
      caKs.load(null,null);
      caKs.setCertificateEntry("ca-certificate", ca);
      // 创建信任管理器, 添加信任秘钥, 并设置客户端不用证书验证
      var tmf = TrustManagerFactory.getInstance("PKIX");
      tmf.init(caKs);
      var context = SSLContext.getInstance("TLSv1.2");
      context.init(null, tmf.getTrustManagers(), new SecureRandom());
      return context.getSocketFactory();
    } catch (CertificateException | KeyStoreException | IOException | NoSuchAlgorithmException |
             KeyManagementException e) {
      log.info("构建网络连接安全通信失败, 原因: " + e);
    }
    return null;
  }
}
