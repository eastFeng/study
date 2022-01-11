package com.dongfeng.test;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import com.dongfeng.study.StudyApplication;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.util.FileCopyUtils;

import java.io.*;

/**
 * @author eastFeng
 * @date 2020-11-02 16:16
 */
@Slf4j(topic = "------")
public class IOTest {
    public static void main(String[] args) {
        // 流从概念上来说是一个连续的数据流 【可以从流中读取数据 也可以往流里面写数据】 流与数据源或者目标媒介相关联//
        // InputStream和Reader与数据源相关联， //程序需要InputStream或者Reader从数据源读取数据。
        // OutputStream和Write与目标媒介相关联， //程序需要OutputStream或者Write将数据写到目标媒介中。

        // 资源加载
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(StudyApplication.class);
        Resource resource = context.getResource("D:\\MyFiles\\Pictures\\blueSky.jpg");

        String filename = resource.getFilename();
        try {
            InputStream inputStream = resource.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }



    // java IO中的管道为运行在同一个JVM中的两个线程提供了通信能力。
    // 一个PipedOutputStream流应该和一个PipedInputStream流相关联。
    // 一个线程通过PipedOutputSteam写入的数据可以被另一个线程通过相关联的PipedInputStream读取出来。
    @Test
    public void io_piped(){
        try {
            PipedOutputStream pipedOutputStream = new PipedOutputStream();
            PipedInputStream pipedInputStream = new PipedInputStream(pipedOutputStream);

            Thread t1 = new Thread(() -> {
                try {
                    //PipedOutputStream写入数据
                    pipedOutputStream.write("Hello world, pipe!".getBytes());
                    //关闭流
                    pipedOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

            Thread t2 = new Thread(() -> {
                try {
                    // PipedInputStream读取数据
                    int read = pipedInputStream.read();
                    while (read!=-1 && pipedOutputStream!=null){
                        System.out.println((char)read);
                        read = pipedInputStream.read();
                    }

                    //关闭流
                    pipedInputStream.close();;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

            t1.start();
            t2.start();
        } catch (Exception e) {
            log.error("io_piped error:{}",e.getMessage(), e);
        }
    }


    //字节数组(byte[])和字符数组(char[])
    @Test
    public void io_byteArray(){
        try {
            //字节数组 1024就是1KB
            byte[] bytes = new byte[1024];

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            //把数据写入字符数组
            outputStream.write("this text is converted to bytes".getBytes());
            bytes = outputStream.toByteArray();

            ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
            // 读取第一个字节
            int read = inputStream.read();
            while (read != -1){
                System.out.println((char)read);

                // 读取下一个字节
                read = inputStream.read();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
