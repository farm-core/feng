package com.yun.common.encrypt;

import org.apache.tomcat.util.codec.binary.Base64;

import javax.crypto.Cipher;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

public class RSAUtil {
    private static Map<Integer, String> keyMap = new HashMap<Integer, String>();  //用于封装随机产生的公钥与私钥

    public final static String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCCPaD/nyqfCaOQ76/yalJ0Iw+bVR2XirgmtuIkasvsdPgPHRxITmTGQgW0urFPDbOZtxiDi7DNfWvADcj8cOqIXfSO8zjDmz/KhYjqiluQy65incxcDTzbw/IRou6tSgeOGMuEcHE3zHCfBr76Fh5LOpwHWCDr5UYYsBhnXHlCHQIDAQAB";
    public final static String privateKey = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAII9oP+fKp8Jo5Dvr/JqUnQjD5tVHZeKuCa24iRqy+x0+A8dHEhOZMZCBbS6sU8Ns5m3GIOLsM19a8ANyPxw6ohd9I7zOMObP8qFiOqKW5DLrmKdzFwNPNvD8hGi7q1KB44Yy4RwcTfMcJ8GvvoWHks6nAdYIOvlRhiwGGdceUIdAgMBAAECgYAolpw+DZRyy8Y1vPzQR2I631JVzkjSmWCacBQ51S6IGkvUKfZOcl/liV3N3QC06kg2d2EkXbxTzepuh91DDs2ID64K/JpjCPzT2EllfCBM9UIlJBPem0J8S6ljNJ71cKc3PbHzvDfAWtIsN4fNgt5Iw6++0ZXw19wqNvjjJwvkAQJBAMiw3l4lTk7OGKLkkF/9kQ1nzHmmyjmfyZIybCxMxq8pB2qLijLjpcAg/yoWw/SS/g9GxL7Cj/Ps6r9VXYnWueECQQCmIl2Fqq0b6cNE2FJ5tjoIfWgJj7nye/x1Qx64SO6ID0K7xaAEQLQCQH7AB073Lk0+rfiuKnyo4zsjKTDuOOe9AkEAtsYN3qQPK0obuc57hlCCWDkeSfSS2+QgSdtOFRJfKIsvS+OFm08kV1Q1zvkowYdVhkgnSqwbOkWcjWCLbct3QQJBAJgWhb9ETE1tZxGzZ2184Qu8om1+YiAWWTuQQqM6QQPV1KXJeX/rK9+T43UwO54ViSmEYlAvHQgPXagGoYC0H5ECQDZnddNjin43eWh2128vX8Tc2lydkTtnbMQY0m1Z1itJ0CMkO8XA+v9P7xpUvX4St8RPVHoX+x5eZflKNbOGTH4=";
    public static void main(String[] args) throws Exception {
        //生成公钥和私钥
        genKeyPair();
        //加密字符串
        String message = "df723820";
        System.out.println("随机生成的公钥为:" + keyMap.get(0));
        System.out.println("随机生成的私钥为:" + keyMap.get(1));
        String messageEn = encrypt(message, keyMap.get(0));
        System.out.println(message + "\t加密后的字符串为:" + messageEn);
        String messageDe = decrypt(messageEn, keyMap.get(1));
        System.out.println("还原后的字符串为:" + messageDe);
    }

    /**
     * 随机生成密钥对
     *
     * @throws NoSuchAlgorithmException
     */
    public static void genKeyPair() throws NoSuchAlgorithmException {
        // KeyPairGenerator类用于生成公钥和私钥对，基于RSA算法生成对象
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
        // 初始化密钥对生成器，密钥大小为96-1024位
        keyPairGen.initialize(1024, new SecureRandom());
        // 生成一个密钥对，保存在keyPair中
        KeyPair keyPair = keyPairGen.generateKeyPair();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();   // 得到私钥
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();  // 得到公钥
        String publicKeyString = new String(Base64.encodeBase64(publicKey.getEncoded()));
        // 得到私钥字符串
        String privateKeyString = new String(Base64.encodeBase64((privateKey.getEncoded())));
        // 将公钥和私钥保存到Map
        keyMap.put(0, publicKeyString);  //0表示公钥
        keyMap.put(1, privateKeyString);  //1表示私钥
    }

    /**
     * RSA公钥加密
     *
     * @param str       加密字符串
     * @param publicKey 公钥
     * @return 密文
     * @throws Exception 加密过程中的异常信息
     */
    public static String encrypt(String str, String publicKey) throws Exception {
        //base64编码的公钥
        byte[] decoded = Base64.decodeBase64(publicKey);
        RSAPublicKey pubKey = (RSAPublicKey) KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(decoded));
        //RSA加密
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, pubKey);
        String outStr = Base64.encodeBase64String(cipher.doFinal(str.getBytes("UTF-8")));
        return outStr;
    }

    /**
     * RSA私钥解密
     *
     * @param str        加密字符串
     * @param privateKey 私钥
     * @return 铭文
     * @throws Exception 解密过程中的异常信息
     */
    public static String decrypt(String str, String privateKey) throws Exception {
        //64位解码加密后的字符串
        byte[] inputByte = Base64.decodeBase64(str.getBytes("UTF-8"));
        //base64编码的私钥
        byte[] decoded = Base64.decodeBase64(privateKey);
        RSAPrivateKey priKey = (RSAPrivateKey) KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(decoded));
        //RSA解密
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, priKey);
        String outStr = new String(cipher.doFinal(inputByte));
        return outStr;
    }

}