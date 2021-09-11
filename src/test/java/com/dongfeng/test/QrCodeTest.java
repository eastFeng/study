package com.dongfeng.test;

import cn.hutool.core.io.FileUtil;
import cn.hutool.extra.qrcode.QrCodeUtil;
import cn.hutool.extra.qrcode.QrConfig;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import lombok.extern.slf4j.Slf4j;

import java.awt.*;
import java.io.File;

/**
 * @author eastFeng
 * @date 2021-01-26 15:43
 */
@Slf4j
public class QrCodeTest {
    public static void main(String[] args) {
//        generate();
        json();
    }


    public static void json(){
        JSONObject param = new JSONObject();
        param.put("formId", "5fea9e8fb9ed38003e80bbce");
        param.put("bookId", "5ff6af883f54bb006cd76900");
        param.put("userId", "153702");

        System.out.println(param.toJSONString());
    }



    public static void generate(){
        log.info("generate qrCode start ----------------------");

        // 默认长宽都是300
        QrConfig config = new QrConfig();
//        // 设置边距，既二维码和背景之间的边距
//        config.setMargin(3);
        // 设置前景色，既二维码颜色（红色）
        config.setForeColor(Color.GREEN);
//        // 设置背景色（灰色）
//        config.setBackColor(Color.GRAY);
        // 附带log小图标
        config.setImg("D:\\MyFiles\\Pictures\\005O0Eg8ly1g3je0efkcfj32yo1o04qp.jpg");

        // 很多时候，二维码无法识别，这时就要调整纠错级别。纠错级别使用zxing的ErrorCorrectionLevel枚举封装，包括：L、M、Q、H几个参数，由低到高。
        // 低级别的像素块更大，可以远距离识别，但是遮挡就会造成无法识别。高级别则相反，像素块小，允许遮挡一定范围，但是像素块更密集。
        config.setErrorCorrection(ErrorCorrectionLevel.H);

        JSONObject content = new JSONObject();
        content.put("formId", "5fea9e8fb9ed38003e80bbce");
        content.put("userId", "153741");
        content.put("userRegInfoId", "600941a3a1f18121190610b1");
        content.put("punchId", "5ff6af883f54bb006cd76902");

        // 生成的二维码图片存放路径
        String path = "C:\\Users\\46037\\Desktop\\qrcode.jpg";
        // 生成二维码到文件，也可以到流
        QrCodeUtil.generate(content.toJSONString(), config, FileUtil.file(path));

        log.info("generate qrCode end ----------------------");
    }
}
