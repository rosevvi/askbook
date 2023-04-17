package com.rosevvi.tools;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;

/**
 * @author: rosevvi
 * @date: 2023/3/26 16:22
 * @version: 1.0
 * @description:
 */
public class AESUtils {

    private static final String Key_ALGORITHM="AES";

    private static final String DEFAULT_CIPHER_ALGORITHM="AES/ECB/PKCS5Padding";

    private static final String KEY="rosevvi!#$%^&*()";

    /**
     * 加密操作
     * @param content 要加密的内容
     * @return
     */
    public static String encrypt(String content){
        try {
            //根据指定算法AES创建密码器
            Cipher cipher= Cipher.getInstance(DEFAULT_CIPHER_ALGORITHM);
            //始化密码器，第一个参数未加密或者解密操作，第二个参数为使用的KEY
            cipher.init(Cipher.ENCRYPT_MODE,getSecretKey());
            //获取加密内容的字节数组（这里要设置utf-8）不然内容中如果有中文和英文混合中文会被解码成乱码
            byte[] bytes = content.getBytes(StandardCharsets.UTF_8);
            //加密
            byte[] result = cipher.doFinal(bytes);
            //通过base64转码返回
            return Base64.encodeBase64String(result).replaceAll("[+]",".");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 解密操作
     * @param content 要加密的内容
     * @return
     */
    public static String decrypt(String content){
        try {
            //实例化，根据指定算法AES创建密码器
            Cipher cipher=Cipher.getInstance(DEFAULT_CIPHER_ALGORITHM);
            //使用密钥初始化设置为解密模式
            cipher.init(Cipher.DECRYPT_MODE,getSecretKey());
            //执行操作
            byte[] result = cipher.doFinal(Base64.decodeBase64(content.replaceAll("[.]", "+")));
            return new String(result,StandardCharsets.UTF_8);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 生成加密密钥
     * @return
     */
    public static SecretKeySpec getSecretKey(){
        //返回生成指定算法密钥生成器的KeyGenerator对象
        KeyGenerator kg=null;

        try {
            //构造密钥生成器，指定为AES算法，不区分大小写
            kg=KeyGenerator.getInstance(Key_ALGORITHM);
            //初始化SecureRandom，使用SHA1PRNG算法，SecureRandom类提供加密的强随机数生成器
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
            //设置种子
            random.setSeed(KEY.getBytes());
            //AES要求密钥长度为128
            kg.init(128,random);

            //生成一个密钥
            SecretKey secretKey = kg.generateKey();
            //转换为AES专用密钥
            return  new SecretKeySpec(secretKey.getEncoded(),Key_ALGORITHM);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
