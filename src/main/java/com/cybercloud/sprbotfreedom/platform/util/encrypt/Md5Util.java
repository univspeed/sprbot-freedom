package com.cybercloud.sprbotfreedom.platform.util.encrypt;

import lombok.extern.slf4j.Slf4j;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Md5Util
 * @author liuyutang
 * @date 2023/7/10
 */
@Slf4j
public class Md5Util {

    private static final String ALGORITHM = "MD5";

    private static final char[] HEX_DIGITS = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    public static String md5(String str) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance(ALGORITHM);
            messageDigest.update(str.getBytes("UTF-8"));
            byte[] bytes = messageDigest.digest();
            StringBuilder sb = new StringBuilder();
            for (byte b : bytes) {
                sb.append(HEX_DIGITS[(b >> 4) & 0x0f]);
                sb.append(HEX_DIGITS[b & 0x0f]);
            }
            return sb.toString();
        } catch (UnsupportedEncodingException e) {
            log.error("{}",e);
        } catch (NoSuchAlgorithmException e) {
            log.error("{}",e);
        }
        return null;
    }
    public static void main(String[] args) {
        System.out.println(Md5Util.md5("123456"));
    }

}
