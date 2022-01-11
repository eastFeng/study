package com.dongfeng.study.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.codec.binary.Base64;

import java.io.File;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * RSA非对称加密算法工具类
 *
 * <p> 非对称加密算法需要两个密钥：公开密钥（publickey:简称公钥）和私有密钥（privatekey:简称私钥）。
 * 公钥与私钥是一对，如果用公钥对数据进行加密，只有用对应的私钥才能解密。
 * 因为加密和解密使用的是两个不同的密钥，所以这种算法叫作非对称加密算法。
 *
 * <p> RSA公开密钥密码体制的原理是：根据数论，寻求两个大素数比较简单，而将它们的乘积进行因式分解却极其困难，因此可以将乘积公开作为加密密钥
 *
 * @author eastFeng
 * @date 2020/8/15 - 14:21
 */
@Slf4j
public class RSAUtil {
    private static final String RSA_ALGORITHM = "RSA";

    /**
     * 长度可以根据业务需求指定
     */
    private static final int KEY_SIZE = 1024;

    public static void main(String[] args) {
        File publicFile = new File("C:\\Users\\eastFeng\\Desktop" +
                "\\watermarkTes\\publicKeyStr.txt");

        File privateFile = new File("C:\\Users\\eastFeng\\Desktop" +
                "\\watermarkTes\\privateKeyStr.txt");

        KeyPair keyPair = generateKeyPair();
        PublicKey aPublic = keyPair.getPublic();
        PrivateKey aPrivate = keyPair.getPrivate();
        saveKeyForEncodedBase64(aPublic,publicFile);
        saveKeyForEncodedBase64(aPrivate,privateFile);
    }

    /**
     * 将 公钥/私钥 编码后以 Base64 的格式保存到指定文件
     */
    public static void saveKeyForEncodedBase64(Key key, File keyFile) {
        try {
            // 获取密钥编码后的格式
            byte[] encBytes = key.getEncoded();

            // 转换为 Base64 文本
            String encBase64 = Base64.encodeBase64String(encBytes);

            // 保存到文件
            IOUtil.writeUtf8String(encBase64, keyFile);
        }catch (Exception e){
            log.error("saveKeyForEncodedBase64 error:{}",e.getMessage(),e);
        }
    }

    /**
     * @return 随机生成密钥对（包含公钥和私钥）
     */
    public static KeyPair generateKeyPair() {
        try {
            // 获取指定算法的密钥对生成器
            KeyPairGenerator gen = KeyPairGenerator.getInstance(RSA_ALGORITHM);

            // 初始化密钥对生成器（指定密钥长度, 使用默认的安全随机数源）
            gen.initialize(KEY_SIZE);

            // 随机生成一对密钥（包含公钥和私钥）
            return gen.generateKeyPair();
        }catch (Exception e){
            log.error("generateKeyPair error:{}",e.getMessage(),e);
        }
        return null;
    }

    /**
     * 根据公钥的Base64文本创建公钥对象
     * @param pubKeyBase64 公钥的Base64文本
     * @return 公钥对象
     */
    public static PublicKey getPublicKey(String pubKeyBase64){
        // 把公钥的Base64文本 转换为 已编码的公钥bytes
        byte[] keyBytes = Base64.decodeBase64(pubKeyBase64);

        // 创建 已编码的公钥规格
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);

        // 获取指定算法的密钥工厂, 根据 已编码的公钥规格 生成公钥对象
        PublicKey publicKey = null;
        try {
            publicKey = KeyFactory.getInstance(RSA_ALGORITHM).generatePublic(keySpec);
        } catch (Exception e) {
            log.error("getPublicKey error:{}", e.getMessage(), e);
        }

        return publicKey;
    }

    /**
     * 根据私钥的Base64文本创建私钥对象
     * @param priKeyBase64 私钥的Base64文本
     * @return 私钥对象
     */
    public static PrivateKey getPrivateKey(String priKeyBase64){
        // 把私钥的Base64文本 转换为 已编码的私钥bytes
        byte[] keyBytes = Base64.decodeBase64(priKeyBase64);

        // 创建 已编码的私钥规格
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);

        // 获取指定算法的密钥工厂, 根据 已编码的私钥规格 生成私钥对象
        PrivateKey privateKey = null;
        try {
            privateKey = KeyFactory.getInstance(RSA_ALGORITHM).generatePrivate(keySpec);
        } catch (Exception e) {
            log.error("getPublicKey error:{}", e.getMessage(), e);
        }

        return privateKey;
    }
}
