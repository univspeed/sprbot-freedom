package com.cybercloud.sprbotfreedom.platform.util.encrypt;

import com.cybercloud.sprbotfreedom.platform.enums.SystemErrorCode;
import com.cybercloud.sprbotfreedom.platform.exception.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.asn1.gm.GMNamedCurves;
import org.bouncycastle.asn1.gm.GMObjectIdentifiers;
import org.bouncycastle.asn1.x9.X9ECParameters;
import org.bouncycastle.jcajce.provider.asymmetric.ec.BCECPrivateKey;
import org.bouncycastle.jcajce.provider.asymmetric.ec.BCECPublicKey;
import org.bouncycastle.jcajce.provider.asymmetric.x509.CertificateFactory;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.jce.spec.ECParameterSpec;
import org.bouncycastle.jce.spec.ECPrivateKeySpec;
import org.bouncycastle.jce.spec.ECPublicKeySpec;
import org.bouncycastle.math.ec.ECPoint;
import org.bouncycastle.util.encoders.Hex;

import javax.crypto.Cipher;
import java.io.ByteArrayInputStream;
import java.math.BigInteger;
import java.security.*;
import java.security.cert.X509Certificate;
import java.security.spec.ECGenParameterSpec;
import java.util.Base64;

/**
 * SM2 加密工具
 *
 * @author liuyutang
 */
@Slf4j
public class SM2Util {
    private BouncyCastleProvider provider;
    // 获取SM2相关参数
    private X9ECParameters parameters;
    // 椭圆曲线参数规格
    private ECParameterSpec ecParameterSpec;
    // 获取椭圆曲线KEY生成器
    private KeyFactory keyFactory;
    private static SM2Util sm2Util;

    static {
        sm2Util = new SM2Util();
    }

    public SM2Util() {
        try {
            provider = new BouncyCastleProvider();
            parameters = GMNamedCurves.getByName("sm2p256v1");
            ecParameterSpec = new ECParameterSpec(parameters.getCurve(),
                    parameters.getG(), parameters.getN(), parameters.getH());
            keyFactory = KeyFactory.getInstance("EC", provider);
        } catch (Exception e) {
            log.error("{}",e);
            ServiceException.throwError(SystemErrorCode.ERROR_999);
        }
    }

    /**
     * SM2算法生成密钥对
     *
     * @return 密钥对信息
     */
    public static KeyPair generateSm2KeyPair() throws InvalidAlgorithmParameterException, NoSuchAlgorithmException {
        final ECGenParameterSpec sm2Spec = new ECGenParameterSpec("sm2p256v1");
        // 获取一个椭圆曲线类型的密钥对生成器
        final KeyPairGenerator kpg = KeyPairGenerator.getInstance("EC", sm2Util.provider);
        SecureRandom random = new SecureRandom();
        // 使用SM2的算法区域初始化密钥生成器
        kpg.initialize(sm2Spec, random);
        // 获取密钥对
        KeyPair keyPair = kpg.generateKeyPair();
        return keyPair;
    }

    /**
     * 加密
     *
     * @param input  待加密文本
     * @param pubKey 公钥
     * @return
     */
    public static String encode(String input, String pubKey) {
        try {
            // 获取SM2相关参数
            X9ECParameters parameters = GMNamedCurves.getByName("sm2p256v1");
            // 椭圆曲线参数规格
            ECParameterSpec ecParameterSpec = new ECParameterSpec(parameters.getCurve(), parameters.getG(), parameters.getN(), parameters.getH());
            // 将公钥HEX字符串转换为椭圆曲线对应的点
            ECPoint ecPoint = parameters.getCurve().decodePoint(Hex.decode(pubKey));
            // 获取椭圆曲线KEY生成器
            KeyFactory keyFactory = KeyFactory.getInstance("EC", sm2Util.provider);
            BCECPublicKey key = (BCECPublicKey) keyFactory.generatePublic(new ECPublicKeySpec(ecPoint, ecParameterSpec));
            // 获取SM2加密器
            Cipher cipher = Cipher.getInstance("SM2", sm2Util.provider);
            // 初始化为加密模式
            cipher.init(Cipher.ENCRYPT_MODE, key);
            // 加密并编码为base64格式
            return Base64.getEncoder().encodeToString(cipher.doFinal(input.getBytes()));
        }catch (Exception ex){
            log.error("{}",ex);
            ServiceException.throwError(SystemErrorCode.ERROR_999);
        }
        return null;
    }

    /**
     * 解密
     *
     * @param input  待解密文本
     * @param prvKey 私钥
     * @return
     */
    public static String decoder(String input, String prvKey) {
        try {
            // 获取SM2加密器
            Cipher cipher = Cipher.getInstance("SM2", sm2Util.provider);
            // 将私钥HEX字符串转换为X值
            BigInteger bigInteger = new BigInteger(prvKey, 16);
            BCECPrivateKey privateKey = (BCECPrivateKey) sm2Util.keyFactory.generatePrivate(new ECPrivateKeySpec(bigInteger,
                    sm2Util.ecParameterSpec));
            // 初始化为解密模式
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            // 解密
            return new String(cipher.doFinal(Base64.getDecoder().decode(input)));
        }catch (Exception ex){
            log.error("{}",ex);
            ServiceException.throwError(SystemErrorCode.ERROR_999);
        }
        return null;
    }


    /**
     * 签名
     *
     * @param plainText 待签名文本
     * @param prvKey    私钥
     * @return
     */
    public static String sign(String plainText, String prvKey) {
        try {
            // 创建签名对象
            Signature signature = Signature.getInstance(GMObjectIdentifiers.sm2sign_with_sm3.toString(), sm2Util.provider);
            // 将私钥HEX字符串转换为X值
            BigInteger bigInteger = new BigInteger(prvKey, 16);
            BCECPrivateKey privateKey = (BCECPrivateKey) sm2Util.keyFactory.generatePrivate(new ECPrivateKeySpec(bigInteger,
                    sm2Util.ecParameterSpec));
            // 初始化为签名状态
            signature.initSign(privateKey);
            // 传入签名字节
            signature.update(plainText.getBytes());
            // 签名
            return Base64.getEncoder().encodeToString(signature.sign());
        }catch (Exception ex){
            log.error("{}",ex);
            ServiceException.throwError(SystemErrorCode.ERROR_999);
        }
        return null;
    }

    public static boolean verify(String plainText, String signatureValue, String pubKey) {
        try {
            // 创建签名对象
            Signature signature = Signature.getInstance(GMObjectIdentifiers.sm2sign_with_sm3.toString(), sm2Util.provider);
            // 将公钥HEX字符串转换为椭圆曲线对应的点
            ECPoint ecPoint = sm2Util.parameters.getCurve().decodePoint(Hex.decode(pubKey));
            BCECPublicKey key = (BCECPublicKey) sm2Util.keyFactory.generatePublic(new ECPublicKeySpec(ecPoint, sm2Util.ecParameterSpec));
            // 初始化为验签状态
            signature.initVerify(key);
            signature.update(plainText.getBytes());
            return signature.verify(Base64.getDecoder().decode(signatureValue));

        }catch (Exception ex){
            log.error("{}",ex);
            ServiceException.throwError(SystemErrorCode.ERROR_999);
        }
        return false;
    }

    /**
     * 证书验签
     *
     * @param certStr      证书串
     * @param plaintext    签名原文
     * @param signValueStr 签名产生签名值 此处的签名值实际上就是 R和S的sequence
     * @return
     */
    public static boolean certVerify(String certStr, String plaintext, String signValueStr) {
        try {
            byte[] signValue = Base64.getDecoder().decode(signValueStr);
            /*
             * 解析证书
             */
            CertificateFactory factory = new CertificateFactory();
            X509Certificate certificate = (X509Certificate) factory
                    .engineGenerateCertificate(new ByteArrayInputStream(Base64.getDecoder().decode(certStr)));
            // 验证签名
            Signature signature = Signature.getInstance(certificate.getSigAlgName(), sm2Util.provider);
            signature.initVerify(certificate);
            signature.update(plaintext.getBytes());
            return signature.verify(signValue);
        } catch (Exception ex) {
            log.error("{}",ex);
            ServiceException.throwError(SystemErrorCode.ERROR_999);
        }
        return false;
    }

    /*public static void main(String[] args) throws InvalidAlgorithmParameterException, NoSuchAlgorithmException {
        String str = "看看能不能一次通过";
        KeyPair keyPair = SM2Util.generateSm2KeyPair();
        BCECPrivateKey privateKey = (BCECPrivateKey) keyPair.getPrivate();
        BCECPublicKey publicKey = (BCECPublicKey) keyPair.getPublic();

        // 拿到密钥
        String pubKey = new String(Hex.encode(publicKey.getQ().getEncoded(true)));
        String prvKey = privateKey.getD().toString(16);
        System.out.println("Private Key: " + prvKey);
        System.out.println("Public Key: " + pubKey);
        // 加解密测试
        try {
            System.out.println("加密前：" + str);
            String encode = SM2Util.encode(str, pubKey);
            System.out.println("加密后：" + encode);
            String decoder = new String(SM2Util.decoder(encode, prvKey));
            System.out.println("解密后：" + decoder);
        } catch (Exception e) {
            System.out.println("加解密测试错误");
        }
        // 签名和验签测试
        try {
            System.out.println("签名源数据：" + str);
            String signStr = SM2Util.sign(str, prvKey);
            System.out.println("签名后数据：" + signStr);
            boolean verify = SM2Util.verify(str, signStr, pubKey);
            System.out.println("签名验证结果：" + verify);
        } catch (Exception e) {
            System.out.println("签名和验签测试错误");
        }
    }*/
    public static void main(String[] args) {
        String username = SM2Util.encode("testUser", "025c508eb2286db6c61be377b19598c8650752a60abd7be36492821555d68c94e7");
        String password = SM2Util.encode("W0ShiMiM@", "025c508eb2286db6c61be377b19598c8650752a60abd7be36492821555d68c94e7");
        System.out.println(username);
        System.out.println(password);
    }
}