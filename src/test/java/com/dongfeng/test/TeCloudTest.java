package com.dongfeng.test;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.Protocol;
import com.amazonaws.auth.PropertiesCredentials;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.S3ClientOptions;
import com.amazonaws.services.s3.model.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.Calendar;

/**
 * 天翼云 OOS
 *
 * @author eastFeng
 * @date 2020-10-29 16:20
 */
@Slf4j(topic = "------")
public class TeCloudTest {
    private static final String ACCESS_KEY_ID = "ba4ee21764b54068b6d8";
    private static final String SECRET_ACCESS_KEY = "911afb65615b2d121486313cf44f01273adaac23";

    private static final String BUCKET_NAME = "dongfeng.721520.love";

    private static final String ENDPOINT = "oos-cn.ctyunapi.cn";

    public static void main(String[] args) {
        // byte数据类型是8位,有符号的。 最大值是127  最小值是-128
        // byte类型在大型数据中节约空间, 主要代替整数, 因为byte类型变量占用的空间只有int类型的四分之一
        byte i = 126;

        File file = new File("D:\\MyFiles\\Pictures\\微信图片_20200723002024.jpg");
        try {
            putAndGetUrl(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    // 获取文件名称前缀和后缀
    @Test
    public void fileName(){

        String filename1 = "bluSky.jpg";
        String prefix1 = filename1.substring(0, filename1.lastIndexOf("."));
        String suffix1 = filename1.substring(filename1.lastIndexOf("."));
        log.info("filename1 prefix1 :{}", prefix1);
        log.info("filename1 suffix1 :{}", suffix1);

        String filename2 = "bluSky.bull.jpg";
        String prefix2 = filename2.substring(0, filename2.lastIndexOf("."));
        String suffix2 = filename2.substring(filename2.lastIndexOf("."));
        log.info("filename2 prefix2 :{}", prefix2);
        log.info("filename2 suffix2 :{}", suffix2);
    }

    /**
     * Bucket是存储Object的容器。中国电信天翼对象存储系统的每个Object都必须包含在一个Bucket中。
     * <p> 用户存储在OOS上的每个文件都是一个Object。文件可以是文本、图片、音频、视频或者网页。OOS支持的单个文件的大小从1字节到5T字节。
     */
    private static void putAndGetUrl(File file) throws FileNotFoundException {
        // 1. 创建client
        AmazonS3Client client = createClient();

        String fileName = file.getName();
        if (StringUtils.isBlank(fileName)){
            throw new FileNotFoundException();
        }

        String prefix = fileName.substring(0, fileName.lastIndexOf("."));
        String suffix = fileName.substring(fileName.lastIndexOf("."));

        //在容器中的路径
        String filePath = "picture/";

        //每个文件在容器中的名称(要唯一, 格式要正确)
        String key = filePath+prefix+System.currentTimeMillis()+suffix;
        //2. 上传文件
        PutObjectResult putObjectResult = client.putObject(BUCKET_NAME, key, file);
        log.info("putObjectResult: {}", putObjectResult);


        //3. 获取分享连接
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, 1);
        //容器名称 文件在容器中的key url失效时间
        URL url = client.generatePresignedUrl(BUCKET_NAME, key, calendar.getTime());
        log.info("url : {}", url.toString());
    }


    /**
     * 创建AmazonS3Client
     */
    private static AmazonS3Client createClient(){
        ClientConfiguration clientConfig = new ClientConfiguration();
        // 连接超时时间(毫秒)
        clientConfig.setConnectionTimeout(30*1000);
        // socket超时时间(毫秒)
        clientConfig.setSocketTimeout(30*1000);
        // 设置http
        clientConfig.setProtocol(Protocol.HTTPS);

        // 负载是否参与签名 : 设置参与
        S3ClientOptions options = new S3ClientOptions();
        options.setPayloadSigningEnabled(true);

        // 创建client
        AmazonS3Client client =
                new AmazonS3Client(new PropertiesCredentials(ACCESS_KEY_ID, SECRET_ACCESS_KEY), clientConfig);
        // 设置endpoint
        client.setEndpoint(ENDPOINT);
//        // 设置选项
//        client.setS3ClientOptions(options);
        return client;
    }
}
