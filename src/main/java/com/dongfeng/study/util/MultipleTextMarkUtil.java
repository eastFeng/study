package com.dongfeng.study.util;

import cn.hutool.core.util.ObjectUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.util.Objects;

/**
 * <b> 多文字水印 </b>
 *
 * @author eastFeng
 * @date 2021-04-30 2:58
 */
@Slf4j
public class MultipleTextMarkUtil {

    public static void main(String[] args) {
        File sourceFile = new File("D:\\MyFiles\\Pictures\\wugui1.jpg");
        File targetFile = new File("D:\\MyFiles\\Pictures\\work\\textWuGui1.jpg");

        boolean watermark = watermark("beautiful", sourceFile, targetFile);
        System.out.println("添加水印是否成功 : " +watermark);
    }

    /**
     * 水印之间的距离默认值
     */
    private static final int DEFAULT_DISTANCE = 350;

    /**
     * 从文件中获取图片并添加文字水印
     *
     * @param text 水印
     * @param sourceFile 原始图片文件
     * @param targetFile 存放添加了水印的图片的文件
     * @return 添加水印成功返回true，否则返回false
     */
    public static boolean watermark(String text, File sourceFile, File targetFile){
        // 参数校验
        if (ObjectUtil.hasNull(sourceFile, targetFile) || StringUtils.isBlank(text)){
            return false;
        }

        try {
            BufferedImage sourceImage = ImageIO.read(sourceFile);
            return watermarkImp(text, sourceImage, targetFile);
        } catch (Exception e) {
            log.error("从文件中获取图片并添加文字水印失败 error:{}", e.getMessage(), e);
        }
        return false;
    }

    /**
     * 从InputStream中获取图片并添加水印
     *
     * @param text 水印
     * @param source 输入流InputStream
     * @param targetFile 存放添加了水印的图片的文件
     * @return 添加水印成功返回true，否则返回false
     */
    public static boolean watermark(String text, InputStream source, File targetFile){
        // 参数校验
        if (ObjectUtil.hasNull(source, targetFile) || StringUtils.isBlank(text)){
            return false;
        }

        try {
            Image sourceImage = ImageIO.read(source);
            return watermarkImp(text, sourceImage, targetFile);
        }catch (Exception e){
            log.error("从InputStream中获取图片并添加文字水印失败 error:{}", e.getMessage(), e);
        }
        return false;
    }

    /**
     * 从URL中获取图片并添加文字水印
     * @param url URL
     * @param targetFile 存放添加了水印的图片的文件
     * @return 添加水印成功返回true，否则返回false
     */
    public static boolean watermark(String text, URL url, File targetFile){
        // 参数校验
        if (ObjectUtil.hasNull(url, targetFile) || StringUtils.isBlank(text)){
            return false;
        }

        try {
            Image sourceImage = ImageIO.read(url);
            return watermarkImp(text, sourceImage, targetFile);
        }catch (Exception e){
            log.error("从URL中获取图片并添加文字水印失败 error:{}", e.getMessage(), e);
        }
        return false;
    }

    /**
     * 给图片添加多个文字水印
     *
     * @param text 水印内容
     * @param sourceImage 原始图片
     * @param file 存放添加了水印的图片的文件
     * @return 添加水印成功返回true，否则返回false
     */
    private static boolean watermarkImp(String text, Image sourceImage, File file){
        try {
            // 获取原图像的高度宽度
            int width = sourceImage.getWidth(null);
            int height = sourceImage.getHeight(null);
            log.info("添加文字水印 原图像的高度宽度 width:{},height:{}", width, height);

            // 1.创建图片缓存对象(这个对象存储在内存中,提高效率)
            // 构造器三个参数:图像宽度,图像高度,颜色的设置
            BufferedImage bufferedImage = new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);

            // 2.创建Java绘图工具对象
            Graphics2D graphics2D = bufferedImage.createGraphics();

            // 3.使用Java绘图工具对象将原图绘制到缓存图片对象
            graphics2D.drawImage(sourceImage,0,0,width,height,null);

            int fontSize = 120;
            log.info("添加文字水印 fontSize:{}", fontSize);
            // 设置水印文字信息(设置文字样式、粗细、(单个文字)大小)
            graphics2D.setFont(new Font(null,Font.BOLD,fontSize));
            // 设置水印颜色
            graphics2D.setColor(Color.white);
            //设置文字水印的透明度
            graphics2D.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP,0.5f));
            //设置水印旋转角度
            graphics2D.rotate(Math.toRadians(30),(double) bufferedImage.getWidth()/2,
                    (double) bufferedImage.getHeight()/2);

            // 文字水印宽度
            int textWidth = fontSize*3;
            // 文字水印高度
            int textHeight = fontSize;
            // 水印之间的距离
            int distance = DEFAULT_DISTANCE;
            int x = -width/2;
            int y = -height/2;
            while (x < width*1.5){
                y = -height/2;
                while (y < height*1.5){
                    graphics2D.drawString(text, x, y);
                    y += textHeight+distance;
                }
                x += textWidth+distance;
            }

            // 工具使用完之后释放掉
            graphics2D.dispose();
            // 添加完水印后的图片写入到文件file中
            ImageIO.write(bufferedImage,"JPEG",file);
            return true;
        }catch (Exception e){
            log.error("添加多个文字水印失败 error:{}", e.getMessage(), e);
        }
        return false;
    }

    /**
     * 获取文字的长度（不是字符串的长度）
     *
     * @param text 文字
     * @return 文字的长度
     */
    public static int getTextLength(String text){
        int length = text.length();
        for(int i=0; i<text.length(); i++){
            String s = String.valueOf(text.charAt(i));
            if (s.getBytes().length > 1){
                length++;
            }
        }
        length = length%2==0 ? length/2 : length/2+1;
        return length;
    }

}
