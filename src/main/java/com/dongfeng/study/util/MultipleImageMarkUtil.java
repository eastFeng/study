package com.dongfeng.study.util;

import cn.hutool.core.util.ObjectUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;

/**
 * <b> 多图片水印
 * <p> 给图片添加多个图片水印
 *
 * @author eastFeng
 * @date 2021-04-30 2:01
 */
@Slf4j
public class MultipleImageMarkUtil {

    public static void main(String[] args) {
        File sourceFile = new File("D:\\MyFiles\\Pictures\\blueSky.jpg");
        File targetFile = new File("D:\\MyFiles\\Pictures\\work\\imageTest1.jpg");

        InputStream logoInputStream = null;
        try {
            // 图片水印
            ClassPathResource classPathResource = new ClassPathResource("img" + "/logo1.png");
            // 输入流：输入流和数据源相关联，从数据源中读取数据
            logoInputStream = classPathResource.getInputStream();

            boolean success = watermark(logoInputStream, sourceFile, targetFile);
            System.out.println("添加水印是否成功 : " +success);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            IoUtil.close(logoInputStream);
        }

    }

    /**
     * 水印之间的距离默认值
     */
    private static final int DEFAULT_DISTANCE = 350;

    /**
     * 从文件中获取图片并添加文字水印
     * @param logo 水印
     * @param sourceFile 原始图片文件
     * @param targetFile 存放添加了水印的图片的文件
     * @return 添加水印成功返回true，否则返回false
     */
    public static boolean watermark(InputStream logo, File sourceFile, File targetFile){
        // 参数校验
        if (ObjectUtil.hasNull(logo, sourceFile, targetFile)){
            return false;
        }

        try {
            BufferedImage logoImage = ImageIO.read(logo);
            BufferedImage sourceImage = ImageIO.read(sourceFile);
            return watermarkImp(logoImage, sourceImage, targetFile);
        }catch (Exception e){
            log.error("从文件中获取图片并添加图片水印失败 error:{}", e.getMessage(), e);
        }
        return false;
    }

    /**
     * 从InputStream中获取图片并添加水印
     *
     * @param logo 水印图片输入流
     * @param source 原始图片输入流
     * @param targetFile 存放添加了水印的图片的文件
     * @return 添加水印成功返回true，否则返回false
     */
    public static boolean watermark(InputStream logo, InputStream source, File targetFile){
        // 参数校验
        if (ObjectUtil.hasNull(logo, source, targetFile)){
            return false;
        }

        try {
            BufferedImage logoImage = ImageIO.read(logo);
            BufferedImage sourceImage = ImageIO.read(source);
            return watermarkImp(logoImage, sourceImage, targetFile);
        }catch (Exception e){
            log.error("从InputStream中获取图片并添加图片水印失败 error:{}", e.getMessage(), e);
        }
        return false;
    }

    /**
     * 从URL中获取图片并添加水印
     *
     * @param logo 水印图片输入流
     * @param url URL
     * @param targetFile 存放添加了水印的图片的文件
     * @return 添加水印成功返回true，否则返回false
     */
    public static boolean watermark(InputStream logo, URL url, File targetFile) {
        // 参数校验
        if (ObjectUtil.hasNull(logo, url, targetFile)){
            return false;
        }

        try {
            BufferedImage logoImage = ImageIO.read(logo);
            BufferedImage sourceImage = ImageIO.read(url);
            return watermarkImp(logoImage, sourceImage, targetFile);
        } catch (Exception e) {
            log.error("从URL中获取图片并添加图片水印失败 url:{}, error:{}", url, e.getMessage(), e);
        }
        return false;
    }

    /**
     * 给图片添加多个图片水印
     *
     * @param logoImage 水印（图片）
     * @param sourceImage 原始图片
     * @param file 存放添加了水印的图片的文件
     * @return 添加水印成功返回true，否则返回false
     */
    private static boolean watermarkImp(Image logoImage, Image sourceImage, File file){
        // 参数校验
        if (ObjectUtil.hasNull(logoImage, sourceImage, file)){
            return false;
        }

        try {
            // 获取原图像的高度宽度
            int width = sourceImage.getWidth(null);
            int height = sourceImage.getHeight(null);
            // 获取水印图片以及高度宽度
            int logoImageWidth = logoImage.getWidth(null);
            int logoImageHeight = logoImage.getHeight(null);

            // 1. 创建图片缓存对象（这个对象存储在内存中，提高效率）
            // 构造器三个参数：图像宽度，图像高度，颜色的设置
            BufferedImage bufferedImage = new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);

            // 2. 创建Java绘图工具对象
            Graphics2D graphics2D = bufferedImage.createGraphics();

            // 3. 使用Java绘图工具对象将原图绘制到缓存图片对象
            graphics2D.drawImage(sourceImage,0,0,width,height,null);

            // 设置水印的透明度
            graphics2D.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP,1));
            // 设置水印旋转角度
            graphics2D.rotate(Math.toRadians(30),(double) bufferedImage.getWidth()/2,
                    (double) bufferedImage.getHeight()/2);

            // 开始添加水印

            // 水印之间的距离
            int distance = width/4;
            int x = -width/2;
            int y = -height/2;
            while (x < width*1.5){
                y = -height/2;
                while (y < height*1.5){
                    graphics2D.drawImage(logoImage, x, y,null);
                    y += logoImageHeight+distance;
                }
                x += logoImageWidth+distance;
            }

            // 工具使用完之后释放掉
            graphics2D.dispose();
            // 添加完水印后的图片写入到文件file中
            ImageIO.write(bufferedImage,"JPEG",file);
            return true;
        }catch (Exception e){
            log.error("添加多个图片水印失败 error:{}", e.getMessage(), e);
        }
        return false;
    }
}
