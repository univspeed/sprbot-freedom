package com.cybercloud.sprbotfreedom.platform.util.encrypt;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @version V1.0 * @desc AES 加密工具类
 */
public class AESUtil {
    private static final String KEY_ALGORITHM = "AES";
    //默认的加密算法
    private static final String DEFAULT_CIPHER_ALGORITHM = "AES/ECB/PKCS5Padding";

    /**
     * AES 加密操作
     * @param content
     * @param password
     */
    public static String encrypt(String content, String password) {
        try {
            // 创建密码器
            Cipher cipher = Cipher.getInstance(DEFAULT_CIPHER_ALGORITHM);
            byte[] byteContent = content.getBytes("utf-8");
            // 初始化为加密模式的密码器
            cipher.init(Cipher.ENCRYPT_MODE, getSecretKey(password));
            // 加密
            byte[] result = cipher.doFinal(byteContent);
            return Base64.getEncoder().encodeToString(result);//通过Base64转码返回
        } catch (Exception ex) {
            Logger.getLogger(AESUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /**
     * 解码
     * @param content
     * @param password
     * @return
     */
    public static String decrypt(String content, String password) {
        try {
            //实例化
            Cipher cipher = Cipher.getInstance(DEFAULT_CIPHER_ALGORITHM);
            //使用密钥初始化，设置为解密模式
            cipher.init(Cipher.DECRYPT_MODE, getSecretKey(password));
            //执行操作
            byte[] result = cipher.doFinal(Base64.getDecoder().decode(content));
            String s = new String(result, "utf-8");
            return s;
        } catch (Exception ex) {
            ex.printStackTrace();
            Logger.getLogger(AESUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /**
     * 生成密钥
     * @param password
     * @return
     */
    private static SecretKeySpec getSecretKey(String password) {
        //返回生成指定算法密钥生成器的 KeyGenerator 对象
        /*KeyGenerator kg = null;
        try {
            kg = KeyGenerator.getInstance(KEY_ALGORITHM);
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
            random.setSeed(password.getBytes());
            //AES 要求密钥长度为 128
            kg.init(128, random);
            //生成一个密钥
            SecretKey secretKey = kg.generateKey();
            return new SecretKeySpec(secretKey.getEncoded(), KEY_ALGORITHM);// 转换为AES专用密钥
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(AESUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
         */
        return new SecretKeySpec(password.getBytes(),"AES");
    }

    public static void main(String[] args) throws UnsupportedEncodingException {
        String password = "IfnW@@tgsSDvdsAD3400fdsJFjefnccS";
        System.out.println(encrypt("123123123","IfnW@@tgsSDvdsAD3400fdsJFjefnccS"));
        //String decrypt = AESUtil.decrypt("J+TtQCtEI6wwPMLHtwX48RSWkxc4Pr1PFm3t7D4IzKg597MpNCmzfdTQ+3jWCFy6lwN8ThfLTEoz8SpTrbgamw==", password);
        //System.out.println(decrypt);
        System.out.println(decrypt("8abnMoj4R18p0+wg3P42Ag==","IfnW@@tgsSDvdsAD3400fdsJFjefnccS"));
    }
}