package com.zhejian.utils;

import com.google.common.collect.Maps;  
import sun.misc.BASE64Decoder;  
import sun.misc.BASE64Encoder;  
  
import javax.crypto.Cipher;

import org.junit.Test;

import java.io.UnsupportedEncodingException;
import java.security.*;  
import java.security.interfaces.RSAPrivateKey;  
import java.security.interfaces.RSAPublicKey;  
import java.security.spec.PKCS8EncodedKeySpec;  
import java.security.spec.X509EncodedKeySpec;  
import java.util.Map; 
public class RsaUtil {
	/** 
     * 定义加密方式 
     */  
    private final static String KEY_RSA = "RSA";  
    /** 
     * 定义签名算法 
     */  
    private final static String KEY_RSA_SIGNATURE = "sha256withrsa";
    /** 
     * 定义公钥算法 
     */  
    private final static String KEY_RSA_PUBLICKEY = "RSAPublicKey";  //RSAPublicKey
    /** 
     * 定义私钥算法 
     */  
    private final static String KEY_RSA_PRIVATEKEY = "RSAPrivateKey";//RSAPrivateKey
  
    /** 
     * 初始化密钥 
     * @return 
     */  
    public static Map<String, Object> init() {  
        Map<String, Object> map = null;  
        try {  
            KeyPairGenerator generator = KeyPairGenerator.getInstance(KEY_RSA);  
            generator.initialize(2048);  
            KeyPair keyPair = generator.generateKeyPair();  
            // 公钥  
            RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();  
            // 私钥  
            RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();  
            // 将密钥封装为map  
            map = Maps.newHashMap();  
            map.put(KEY_RSA_PUBLICKEY, publicKey);  
            map.put(KEY_RSA_PRIVATEKEY, privateKey);  
        } catch (NoSuchAlgorithmException e) {  
            e.printStackTrace();  
        }  
        return map;  
    }  
    
    /** 
     * 用私钥对信息生成数字签名 
     * @param data 加密数据 
     * @param privateKey 私钥 
     * @return 
     */  
    public static String sign(byte[] data, String privateKey) {
        String str = "";  
        try {  
            // 解密由base64编码的私钥  
            byte[] bytes = decryptBase64(privateKey);  
            // 构造PKCS8EncodedKeySpec对象  
            PKCS8EncodedKeySpec pkcs = new PKCS8EncodedKeySpec(bytes);  
            // 指定的加密算法  
            KeyFactory factory = KeyFactory.getInstance(KEY_RSA);  
            // 取私钥对象  
            PrivateKey key = factory.generatePrivate(pkcs);  
            // 用私钥对信息生成数字签名  
            Signature signature = Signature.getInstance(KEY_RSA_SIGNATURE);  
            signature.initSign(key);  
            signature.update(data);  
            str = encryptBase64(signature.sign());  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
        return str;  
    }  
  
    /** 
     * 校验数字签名 
     * @param data 加密数据 
     * @param publicKey 公钥 
     * @param sign 数字签名 
     * @return 校验成功返回true，失败返回false 
     */  
    public static boolean verify(byte[] data, String publicKey, String sign) {  
        boolean flag = false;  
        try {  
            // 解密由base64编码的公钥  
            byte[] bytes = decryptBase64(publicKey);  
            // 构造X509EncodedKeySpec对象  
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(bytes);  
            // 指定的加密算法  
            KeyFactory factory = KeyFactory.getInstance(KEY_RSA);  
            // 取公钥对象  
            PublicKey key = factory.generatePublic(keySpec);  
            // 用公钥验证数字签名  
            Signature signature = Signature.getInstance(KEY_RSA_SIGNATURE);  
            signature.initVerify(key);  
            signature.update(data);  
            flag = signature.verify(decryptBase64(sign));  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
        return flag;  
    }  
  
    /** 
     * 私钥解密 
     * @param data 加密数据 
     * @param key 私钥 
     * @return 
     */  
    public static byte[] decryptByPrivateKey(byte[] data, String key) {  
        byte[] result = null;  
        try {  
            // 对私钥解密  
            byte[] bytes = decryptBase64(key);  
            // 取得私钥  
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(bytes);  
            KeyFactory factory = KeyFactory.getInstance(KEY_RSA);  
            PrivateKey privateKey = factory.generatePrivate(keySpec);  
            // 对数据解密  
            Cipher cipher = Cipher.getInstance(factory.getAlgorithm());  
            cipher.init(Cipher.DECRYPT_MODE, privateKey);  
            result = cipher.doFinal(data);  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
        return result;  
    }  
  
    /** 
     * 私钥解密 
     * @param data 加密数据 
     * @param key 公钥 
     * @return 
     */  
    public static byte[] decryptByPublicKey(byte[] data, String key) {  
        byte[] result = null;  
        try {  
            // 对公钥解密  
            byte[] bytes = decryptBase64(key);  
            // 取得公钥  
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(bytes);  
            KeyFactory factory = KeyFactory.getInstance(KEY_RSA);  
            PublicKey publicKey = factory.generatePublic(keySpec);  
            // 对数据解密  
            Cipher cipher = Cipher.getInstance(factory.getAlgorithm());  
            cipher.init(Cipher.DECRYPT_MODE, publicKey);  
            result = cipher.doFinal(data);  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
        return result;  
    }  
  
    /** 
     * 公钥加密 
     * @param data 待加密数据 
     * @param key 公钥 
     * @return 
     */  
    public static byte[] encryptByPublicKey(byte[] data, String key) {  
        byte[] result = null;  
        try {  
            byte[] bytes = decryptBase64(key);  
            // 取得公钥  
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(bytes);  
            KeyFactory factory = KeyFactory.getInstance(KEY_RSA);  
            PublicKey publicKey = factory.generatePublic(keySpec);  
            // 对数据加密  
            Cipher cipher = Cipher.getInstance(factory.getAlgorithm());  
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);  
            result = cipher.doFinal(data);  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
        return result;  
    }  
  
    /** 
     * 私钥加密 
     * @param data 待加密数据 
     * @param key 私钥 
     * @return 
     */  
    public static byte[] encryptByPrivateKey(byte[] data, String key) {  
        byte[] result = null;  
        try {  
            byte[] bytes = decryptBase64(key);
            // 取得私钥  
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(bytes);  
            KeyFactory factory = KeyFactory.getInstance(KEY_RSA);  
            PrivateKey privateKey = factory.generatePrivate(keySpec);  
            // 对数据加密  
            Cipher cipher = Cipher.getInstance(factory.getAlgorithm());  
            cipher.init(Cipher.ENCRYPT_MODE, privateKey);  
            result = cipher.doFinal(data);  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
        return result;  
    }  
  
    /** 
     * 获取公钥 
     * @param map 
     * @return 
     */  
    public static String getPublicKey(Map<String, Object> map) {  
        String str = "";  
        try {  
            Key key = (Key) map.get(KEY_RSA_PUBLICKEY);
            str = encryptBase64(key.getEncoded());  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
        return str;  
    }  
  
    /** 
     * 获取私钥 
     * @param map 
     * @return 
     */  
    public static String getPrivateKey(Map<String, Object> map) {  
        String str = "";  
        try {  
            Key key = (Key) map.get(KEY_RSA_PRIVATEKEY);  
            str = encryptBase64(key.getEncoded());  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
        return str;  
    }  
  
    /** 
     * BASE64 解密 
     * @param key 需要解密的字符串 
     * @return 字节数组 
     * @throws Exception 
     */  
    public static byte[] decryptBase64(String key) throws Exception {
        return (new BASE64Decoder()).decodeBuffer(key);  
    }  
  
    /** 
     * BASE64 加密 
     * @param key 需要加密的字节数组 
     * @return 字符串 
     * @throws Exception 
     */  
    public static String encryptBase64(byte[] key) throws Exception {
        return (new BASE64Encoder()).encodeBuffer(key);  
    }  
  
    /** 
     * 测试方法 
     * @param args 
     * @throws UnsupportedEncodingException 
     */  
    public static void main(String[] args) {
        String privateKey = "";  
        String publicKey = "";  
        // 生成公钥私钥  
        Map<String, Object> map = init();
        publicKey = getPublicKey(map);
        privateKey = getPrivateKey(map);  
        System.out.println("公钥: \n\r" + publicKey);  
        System.out.println("私钥： \n\r" + privateKey);  
        System.out.println("公钥加密--------私钥解密");  
        String word = "你好，世界！";  
        byte[] encWord = encryptByPublicKey(word.getBytes(), publicKey);  
        String decWord = new String(decryptByPrivateKey(encWord, privateKey));
        System.out.println("加密前: " + word + "\n\r" + "解密后: " + decWord);  
        System.out.println("私钥加密--------公钥解密");  
        String english = "Hello, World!";  
        byte[] encEnglish = encryptByPrivateKey(english.getBytes(), privateKey);  
        String decEnglish = new String(decryptByPublicKey(encEnglish, publicKey));  
        System.out.println("加密前: " + english + "\n\r" + "解密后: " + decEnglish);  
        System.out.println("私钥签名——公钥验证签名");  
        // 产生签名  
        String sign = sign(encEnglish, privateKey);
        System.out.println("签名:\r" + sign);  
        // 验证签名  
        boolean status = verify(encEnglish, publicKey, sign);  
        System.out.println("状态:\r" + status);
    } 
    
    @Test
    public void test() throws UnsupportedEncodingException{
    	String privateKey ="MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQCeH7t1GMFbzcoN8njMoCfco8acH3ICB5+LdE3iXAVbt6xsINJAAesVurxynebUMtdutVkiN/wnOvxhYeL2PG4PvSj2L78MCi9zvZ9wYg/WlgBS57u4YC3Yv1YyNYhDhgpabua2cHv7NjGSZVaka+QYnpb7ZFtck5i/e1mEoqrxZ6ZDuocq2PAJm+EU8Ab0EMyzb8BUg1WwmHxZD/vNKZvZIh1nMbsibij9TjtimOUJJtQbDeBP+gppCPs//bNgm3qryf8z7P3qOgBJ3ScfLgIBPN6l3MofGhf3O1F2pdnmagXnur6LQ4TNP93bVxuwkQCZBUtv8fMLi/7d/ySXyt2PAgMBAAECggEBAITFVkzzdWBVgoSFhYMkM3RjnF/5U2x65C9fgApChElIB6JfSMtwEOoU+ykuhuTEr/oIkghWhAaY09QdMkrZ4PycM6nL25oLb1666QafgHcJSspSRGCDeduWhZTxODKieU0H8/daqe/8yFEJgF/dLBC0EUiTMtegI1EdDMR/JhQ+RhaUEylHJDAl4qoXuQkpfTv9o4ud/cNm8u6MRZ6OxLG9wGLKeOYNYQsdcXRObXNaTZcUPwK/0VmIO+JnNxa1MUGo+ObNIH0j8NTk0cRURowRYCghjB+g1mojBpY+RVcrQMv1L+0KYYn4mIrcBmRIpUfPCmI+q4T+OKipvwAxuRECgYEA2Q1d2xcjHFq8oWDxuFkCDIZHYEFxU0I+xwEIk3Kuvx65hr6MLpm7/kfOD68+txRvbM2PlcWzLa/ZVFLq4Z6ZF4PLSsWiBnvWnBBSE725rPYqnT/+mlciQ4SUkbliOvD6adA21kw3Fv1kPLvjDGcPgCT9n2hpHJTv+Q6KvRF2V0cCgYEAun9nW/dgqwzlKZsTMcEC+EFQ2cRzv4lq6jecuZ3yinCH/7SE/FqXqdWcnskEgflG0Fhds+29RX3U/9turDJ8fuD02CtUmrRW+td5YEZA4XEZ8vAiVq+N2H/8ox9/lp2Qgm4QGApzWDEpFGZMJMJ/UAqcr9ok0G+O33Mi/ozv+3kCgYAg5aq9AyO6L3J5jyTzyOHLniqBR0ny0Y6MQPbYyj/8DZ8W8hhzE6/vmVh8/hWmT/2XYwyJKb6/hPCOnEanTY9shfgw7AVjsC9V1g9qmI3kzialUMa5A83Yp+aQs6VUw6NgmENHdaO/40XnAp8mCRTLWk/qcjvELjq05+RVRalHDwKBgQCgHvrTDxXERpIKosZcFWdDVnDgR7dJX+aXqZ+6Dk5LZkqd1JPJRs6oVmreOaDRJwERqIzrCtdSbrMN6Yyf88jH9sTBRJKjMOrs+0uOyUKzTLQGzP3uh1O16k0ykLyuyCFmmrDeszpNIggvJ5WAii1cv3+/y/+HpgB5ARWuj7sLoQKBgC67qmmHdr8MTKIM5uR1QpZN1YZr7ZhX+AqI03vwM6d6ZNKUnl1DgRABvJmyjGjIlR9g/UDfwJJjF4Wc2mRFmXHsbOp4ipQsKPLJtap3LolIc/VgQksj74L8j8iT/hqsX6ZqO2N5Sxy9wnFNy6dfD/bdpi1JPPjX+iQxBwVz7/c6";
    	String publicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAnh+7dRjBW83KDfJ4zKAn3KPGnB9yAgefi3RN4lwFW7esbCDSQAHrFbq8cp3m1DLXbrVZIjf8Jzr8YWHi9jxuD70o9i+/DAovc72fcGIP1pYAUue7uGAt2L9WMjWIQ4YKWm7mtnB7+zYxkmVWpGvkGJ6W+2RbXJOYv3tZhKKq8WemQ7qHKtjwCZvhFPAG9BDMs2/AVINVsJh8WQ/7zSmb2SIdZzG7Im4o/U47YpjlCSbUGw3gT/oKaQj7P/2zYJt6q8n/M+z96joASd0nHy4CATzepdzKHxoX9ztRdqXZ5moF57q+i0OEzT/d21cbsJEAmQVLb/HzC4v+3f8kl8rdjwIDAQAB";
    	
    	String english = "hello world!";  
        byte[] encEnglish = encryptByPrivateKey(english.getBytes(), privateKey);  
        String decEnglish = new String(decryptByPublicKey(encEnglish, publicKey));  
        System.out.println("加密前: " + english + "\n\r" + "解密后: " + decEnglish);  
        System.out.println("私钥签名——公钥验证签名");  
        // 产生签名  
        String sign = sign(encEnglish, privateKey);
        System.out.println("签名:\r" + sign);  
        sign = "CbwMvj3Q358lBQB6s/8EPHznL/OwKkN0dC8BYKUmTbNIE67oC6UgLpJeAIDR7IueGPY/ZibFAYJ5"+
"ipbAG7Kon1CQal5J1PFBBlfWOtJKf9ppKnlUULbAizSxi7+b28H+C6+zTZTqAWCgzCQREODa0jiu"+
"PdDfPWfOhJQ4NNaYKL6iMVR2RnNBQsHBRmv0iG0B12DD4sFSmc2xkRXMnEglM9Z/g4ir5YnN0z1w"+
"hjrFubbEH7OWnp3k9SBLffLi8Fp++2+iwlPWFw==";
        // 验证签名  
        boolean status = verify(encEnglish, publicKey, sign);  
        System.out.println("状态:\r" + status);
    }
    @Test
    public void test1(){
    	String privateKey = "";  
        String publicKey = "";  
        // 生成公钥私钥  
        Map<String, Object> map = init();
        publicKey = getPublicKey(map);
        privateKey = getPrivateKey(map);  
        System.out.println("公钥: \n\r" + publicKey);  
        System.out.println("私钥： \n\r" + privateKey);  
        System.out.println("私钥： \n\r" + privateKey.length());  
    }
}
