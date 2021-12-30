package com.dongfeng.study.util;

import com.dongfeng.study.bean.base.Constants;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.codec.binary.Base64;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * 数字签名工具类（MD5 和 RSA）
 *
 * <p> MD5 : 哈希函数
 * <p> MD5信息摘要算法（英语：MD5 Message-Digest Algorithm），一种被广泛使用的密码散列函数，
 * 可以产生出一个128位（16字节）的散列值（hash value），用于确保信息传输完整一致。
 *
 * <p> RSA : 一种非对称加密算法
 *
 * <p> 发送报文时：发送方用md5函数从报文文本（content）中生成内容摘要（哈希值），再用RSA的私钥（发送方私钥）对摘要进行加密，进而生成数字签名（加密之后的值）
 *     发送方会将报文文本（content）和 数字签名一起发送给接收方。
 * <p> 接收报文时（验证时）：接收方首先用与发送方一样的哈希函数（MD5）从接收到的原始报文（content）中计算出内容摘要（哈希值），
 *     接着用发送方的公钥对报文附加的数字签名进行解密，解密出摘要，如果两个摘要相同，那么接收方就能确认该报文时发送方的，并且没有进行篡改。
 *
 * @author eastFeng
 * @date 2020/8/15 - 14:30
 */
@Slf4j
public class MD5WithRSAUtil {
    private static final String RSA_ALGORITHM = "RSA";

    private static final String MD5_WITH_RSA = "MD5withRSA";

    private static final String CHARSET = "UTF-8";


    /**
     * 用md5从报文文本（content）中生成内容摘要（哈希值），再用RSA的私钥（发送方私钥）加密，进而生成数字签名
     *
     * @param content 要生成摘要的内容（报文）
     * @return 数字签名
     */
    public static String getMd5Sign(String content) {
        try {
            byte[] bytes = content.getBytes(CHARSET);
            // 返回MD5withRSA签名算法的 Signature对象
            Signature md5WithRSA = Signature.getInstance(MD5_WITH_RSA);
            md5WithRSA.initSign(getPrivateKey(Constants.MY_PRIVATE_KEY));
            md5WithRSA.update(bytes);
            // 生成数字签名
            byte[] sign = md5WithRSA.sign();
            // 两次编码
            return Base64.encodeBase64String(Base64.encodeBase64(sign));
        } catch (Exception e) {
            log.error("MD5withRSAUtil.getMd5Sign error:{}", e.getMessage(), e);
        }
        return "";
    }

    /**
     * 用公钥对内容进行验证:
     * 接收方首先 用与发送方一样的哈希函数从接收到的原始报文中计算出报文摘要，
     * 接着再用发送方的公用密钥来对报文附加的数字签名进行解密，解密出摘要，
     * 如果这两个摘要相同、那么接收方就能确认报文是从发送方发送且没有被遗漏和修改过！
     *
     * @param content 要验证的内容
     * @param sign 数字签名
     * @return true:验证通过  false:验证失败
     */
    public static boolean verify(String content, String sign) {
        try {
            byte[] bytes = content.getBytes(CHARSET);
            // 返回MD5withRSA签名算法的 Signature对象
            Signature signature = Signature.getInstance(MD5_WITH_RSA);
            signature.initVerify(getPublicKey(Constants.CEBBANK_PUBLIC_KEY));
            signature.update(bytes);
            return signature.verify(Base64.decodeBase64(Base64.decodeBase64(sign)));
        } catch (Exception e) {
            log.error("MD5withRSAUtil.verify error:{}", e.getMessage(), e);
        }
        return false;
    }

    /**
     * 根据公钥的Base64文本创建公钥对象
     */
    public static PublicKey getPublicKey(String pubKeyBase64) {
        // 把公钥的Base64文本 转换为 已编码的公钥bytes
        byte[] keyBytes = Base64.decodeBase64(pubKeyBase64);

        // 创建 已编码的公钥规格
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);

        // 获取指定算法的密钥工厂, 根据 已编码的公钥规格 生成公钥对象
        PublicKey publicKey = null;
        try {
            publicKey = KeyFactory.getInstance(RSA_ALGORITHM).generatePublic(keySpec);
        } catch (Exception e) {
            log.error("MD5withRSAUtil.getPublicKey error:{}", e.getMessage(), e);
        }
        return publicKey;
    }

    /**
     * 根据私钥的Base64文本创建私钥对象
     * @param priKeyBase64 私钥的Base64文本
     * @return 私钥对象
     */
    public static PrivateKey getPrivateKey(String priKeyBase64) {
        // 把私钥的Base64文本 转换为 已编码的私钥bytes
        byte[] keyBytes = Base64.decodeBase64(priKeyBase64);

        // 创建 已编码的私钥规格
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);

        // 获取指定算法的密钥工厂, 根据 已编码的公钥规格 生成私钥对象
        PrivateKey privateKey = null;
        try {
            privateKey = KeyFactory.getInstance(RSA_ALGORITHM).generatePrivate(keySpec);
        } catch (Exception e) {
            log.error("MD5withRSAUtil.getPrivateKey error:{}", e.getMessage(), e);
        }
        return privateKey;
    }
}
