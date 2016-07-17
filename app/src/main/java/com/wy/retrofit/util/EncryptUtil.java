package com.wy.retrofit.util;

import android.annotation.SuppressLint;
import android.util.Base64;
import com.wy.retrofit.util.L;
import javax.crypto.Cipher;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/**
 * 加密工具
 */
public class EncryptUtil {
  public static final String KEY_ALGORITHM = "AES";
  public static final String CIPHER_ALGORITHM = "AES/ECB/PKCS5Padding";
  public static final String CRYPT_KEY = "2014090958623592";
  public static final String CHARSET_NAME = "utf-8";

  public static final String HMAC_SHA1 = "HmacSHA1";

  /**
   * 本 app AES 加密,然后结果 Base64 编码,调用这个
   */
  public static String getAESBase64(String raw) {
    try {
      byte[] bytes = encryptByte(raw.getBytes(CHARSET_NAME), CRYPT_KEY);
      return encryptBase64(bytes);
    } catch (Exception e) {
      e.printStackTrace();
      L.e("AESBase64 error", e.getMessage());
    }
    return null;
  }

  /**
   * AES AES/ECB/PKCS5Padding 加密
   *
   * @param src 要加密的字符串
   * @return result AES 加密后的字节数组
   * @throws Exception AES 加密异常
   */
  @SuppressLint("GetInstance") public static byte[] encryptByte(byte[] src, String key)
      throws Exception {
    Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
    SecretKeySpec secretkey = new SecretKeySpec(key.getBytes(), KEY_ALGORITHM);
    cipher.init(Cipher.ENCRYPT_MODE, secretkey);// 设置密钥和加密形式
    return cipher.doFinal(src);
  }

  /**
   * HmacSHA1 加密
   */
  public static String getSignature(String data, String key) throws Exception {
    byte[] keyBytes = key.getBytes();
    SecretKeySpec signingKey = new SecretKeySpec(keyBytes, HMAC_SHA1);
    Mac mac = Mac.getInstance(HMAC_SHA1);
    mac.init(signingKey);
    byte[] rawHmac = mac.doFinal(data.getBytes());
    return new String(rawHmac);
  }

  /**
   * Base64 编码
   *
   * @param bytes 要编码的byte[]
   * @return Base64 编码后的结果
   * @throws Exception Base64 编码异常
   */
  public static String encryptBase64(byte[] bytes) {
    try {
      return Base64.encodeToString(bytes, Base64.DEFAULT);
    } catch (Exception e) {
      e.printStackTrace();
      L.e("encryptBase64 error", e.getMessage());
    }
    return null;
  }

  /**
   * 解密
   *
   * @param content 待解密的字符串
   * @return 解密结果
   */
  @SuppressLint("GetInstance") public static String aesDecrypt(String content) {
    try {
      byte[] bytes = Base64.decode(content, Base64.DEFAULT);
      SecretKeySpec key = new SecretKeySpec(CRYPT_KEY.getBytes(), KEY_ALGORITHM);
      Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
      cipher.init(Cipher.DECRYPT_MODE, key);
      byte[] decryptedData = cipher.doFinal(bytes);
      return new String(decryptedData, CHARSET_NAME);
    } catch (Exception e) {
      e.printStackTrace();
      L.e("aesDecrypt error", e.getMessage());
    }
    return null;
  }

  /**
   * @param data bytes
   * @return 十六进制
   */
  public static String toHex(byte[] data) {

    char[] HEX_DIGITS_UPPER_CASE =
        { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };

    char[] HEX_DIGITS_LOWER_CASE =
        { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
    // 用来构建字符串（1 byte 是 8 位二进制，也就是 2 位十六进制字符（2的8次方等于16的2次方）） 
    char[] result = new char[data.length * 2];
    int c = 0;
    for (byte b : data) {
      result[c++] = HEX_DIGITS_LOWER_CASE[(b >> 4) & 0xf];
      result[c++] = HEX_DIGITS_LOWER_CASE[b & 0xf];
    }
    return new String(result);
  }
}
