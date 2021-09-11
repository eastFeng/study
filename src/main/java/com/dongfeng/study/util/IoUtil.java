package com.dongfeng.study.util;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ObjectUtil;
import com.dongfeng.study.bean.base.Response;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * @author eastFeng
 * @date 2020/8/15 - 14:27
 */
@Slf4j
public class IoUtil {
    /**
     * 4096、8192都可以
     */
    private static final int COPY_DEFAULT_BUF_SIZE = 8192;
    /**
     * 数据流末尾
     */
    public static final int EOF = -1;

    public Response<String> uploadImage(MultipartFile file){
        try {
            InputStream inputStream = file.getInputStream();
            File tempFile = File.createTempFile("hhhhh", ".jpg");
            copyInputStreamToFile(inputStream, tempFile, false, true);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
        }
        return Response.successInstance("success");
    }

    /**
     * 将流的内容写入文件
     * @param source 输入流
     * @param destination 目标文件
     * @param append 是否以追加的方式写入到文件，true是追加，false是覆盖
     * @param isCloseIn 是否关闭输入流
     */
    public static void copyInputStreamToFile(InputStream source, File destination, boolean append, boolean isCloseIn) throws IOException {
        if (ObjectUtil.hasNull(source, destination)){
            return;
        }

        // 文件输出流
        FileOutputStream output = null;
        try {
            output = new FileOutputStream(destination, append);
            // 拷贝输入流的内容到输出流
            copy(source, output);
        } finally {
            close(output);
            if (isCloseIn){
                close(source);
            }
        }
    }

    /**
     * 将字符串写入到文件中
     *
     * @param data 文本
     * @param file 文件
     * @throws IOException io异常
     */
    public static void writeString(String data, File file) throws IOException {
        if (data == null){
            return;
        }
        FileUtil.writeString(data, file, StandardCharsets.UTF_8);

        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(new FileOutputStream(file));
//        outputStreamWriter.write(data);
//        outputStreamWriter.flush();

        // 输出流：输出流（OutputStream或Writer）是和目标媒介相关的，将数据写入到目标媒介中
        OutputStream out = null;
        try {
            // 初始化输出流
            out = new FileOutputStream(file, true);
            // 将字节数组写入到输出流
            out.write(data.getBytes());
            out.flush();
        } finally {
            // 释放资源
            close(out);
        }
    }

    /* ---------------- writeString -------------- */

    /**
     * 将String写入文件，追加模式，字符集（编码方式）为UTF-8
     * @param data 写入的内容
     * @param file 文件
     * @throws IOException IO异常
     */
    public static void appendString(String data, File file) throws IOException{
        writeString(data, file, StandardCharsets.UTF_8, true);
    }

    /**
     * 将String写入文件，追加模式
     * @param data 写入的内容
     * @param file 文件
     * @param charset 字符集（编码方式）
     * @throws IOException IO异常
     */
    public static void appendString(String data, File file, Charset charset) throws IOException{
        writeString(data, file, charset, true);
    }


    /**
     * 将String写入文件，覆盖模式，字符集（编码方式）为UTF-8
     * @param data 写入的内容
     * @param file 文件
     * @throws IOException IO异常
     */
    public static void writeUtf8String(String data, File file) throws IOException{
        writeString(data, file, StandardCharsets.UTF_8, false);
    }

    /**
     * 将String写入文件，覆盖模式
     * @param data 写入的内容
     * @param file 文件
     * @param charset 字符集（编码方式）
     * @throws IOException IO异常
     */
    public static void writeString(String data, File file, Charset charset) throws IOException{
        writeString(data, file, charset, false);
    }

    /**
     * 将String写入文件
     * @param data 写入的内容
     * @param file 文件
     * @param charset 字符集（编码方式）
     * @param isAppend  是否追加
     * @throws IOException IO异常
     */
    public static void writeString(String data, File file, Charset charset, boolean isAppend) throws IOException{
        if (StringUtils.isBlank(data) || file==null || charset==null){
            return;
        }

        BufferedWriter bufferedWriter = null;
        try {
            OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(file, isAppend), charset);
            bufferedWriter = new BufferedWriter(writer);
            bufferedWriter.write(data);
            bufferedWriter.flush();
        } finally {
            close(bufferedWriter);
        }
    }

    /* ---------------- readLines -------------- */
    public static List<String> readLines(File file, Charset charset) throws IOException{
        List<String> list = new ArrayList<>();
        if (file!=null || charset!=null){
            readLines(file, charset, list);
        }
        return list;
    }

    public static void readLines(File file, Charset charset, Collection<String> collection) throws IOException {
        if (file==null || charset==null || collection==null){
            return ;
        }

        BufferedReader bufferedReader = null;
        try {
            InputStreamReader inputStreamReader = new InputStreamReader(new FileInputStream(file), charset);
            bufferedReader = new BufferedReader(inputStreamReader);

            while (true){
                String line = bufferedReader.readLine();
                if (line == null){
                    break;
                }
                collection.add(line);
            }
        } finally {
            close(bufferedReader);
        }

    }


    /* ---------------- readString -------------- */
    /**
     * 从文件中读取字符串
     * @param file 文件
     * @return String 字符串
     * @throws IOException io异常
     */
    public static String readFile(File file) throws IOException {
        if (file == null){
            return null;
        }

        // 输入流：输入流和数据源相关，从数据源中读取数据
        InputStream in = null;
        // ByteArrayOutputStream：将数据写入到动态的字节数组中
        ByteArrayOutputStream out = null;
        try {
            in = new FileInputStream(file);
            out = new ByteArrayOutputStream();
            copy(in, out);
            return out.toString();
        } finally {
            close(in);
            close(out);
        }
    }

    /**
     * 将文件全部内容读入到一个字符串
     *
     * @param file 文件
     * @param charset 编码类型
     * @return 文件中的字符串
     * @throws IOException 没找到文件
     */
    public static String readFile(File file, Charset charset) throws IOException {
        if (file == null){
            return null;
        }

        InputStreamReader reader = null;
        StringWriter writer = new StringWriter();
        try {
            if (charset == null){
                reader = new InputStreamReader(new FileInputStream(file));
            }else {
                reader = new InputStreamReader(new FileInputStream(file), charset);
            }
            copy(reader, writer);
        } finally {
            close(reader);
            close(writer);
        }

        return writer.toString();
    }

    /* ---------------- copy -------------- */

    /**
     * 拷贝输入流的内容到输出流，拷贝后不关闭流，
     *
     * @param input 输入流
     * @param output 输出流
     * @return 传输的byte数
     */
    public static long copy(InputStream input, OutputStream output) throws IOException{
        return copy(input, output, COPY_DEFAULT_BUF_SIZE);
    }

    /**
     * 拷贝输入流的内容到输出流，拷贝后不关闭流
     *
     * @param input 输入流
     * @param output 输出流
     * @param bufferSize 数组大小（缓存大小）
     * @return 传输的byte数
     */
    public static long copy(InputStream input, OutputStream output, int bufferSize) throws IOException {
        Objects.requireNonNull(input, "InputStream is null!");
        Objects.requireNonNull(output, "OutputStream is null");
        if (bufferSize <= 0){
            bufferSize = COPY_DEFAULT_BUF_SIZE;
        }

        // 总共拷贝的字节数量
        long transferred = 0;

        // 4096、8192都可以
        byte[] buf = new byte[bufferSize];
        // 每次从输入流中读取的字节数量
        int readBytes;
        while ((readBytes=input.read(buf)) != EOF){
            // 将充输入流中读取到的数据（字节）写入到输出流中
            output.write(buf, 0, readBytes);
            transferred += readBytes;
            output.flush();
        }

        return transferred;
    }

    public static long copy(FileInputStream input, FileOutputStream output) throws IOException{
        if (input == null || output==null){
            return 0;
        }

        cn.hutool.core.io.IoUtil.copy(input, output);
        return 0;
    }

    /**
     * 拷贝输入流的内容到输出流，拷贝后不关闭流，使用默认Buffer大小
     *
     * @param reader 字符输入流
     * @param writer 字符输出流
     * @throws IOException IO异常
     * @return 传输的char数量
     */
    public static long copy(Reader reader, Writer writer) throws IOException{
        Objects.requireNonNull(reader, "reader is null");
        Objects.requireNonNull(writer, "writer is null");

        return copy(reader, writer, COPY_DEFAULT_BUF_SIZE);
    }

    /**
     * 拷贝输入流的内容到输出流，拷贝后不关闭流
     *
     * @param reader 字符输入流
     * @param writer 字符输出流
     * @throws IOException IO异常
     * @return 传输的char数量
     */
    public static long copy(Reader reader, Writer writer, int bufferSize) throws IOException {
        Objects.requireNonNull(reader, "reader is null");
        Objects.requireNonNull(writer, "writer is null");

        if (bufferSize <= 0){
            bufferSize = COPY_DEFAULT_BUF_SIZE;
        }

        // 总共拷贝的字节数量
        long transferred = 0;

        // 每次读到的字符（char）放入本char数组
        char[] charBuf = new char[bufferSize];
        // 每次读到的字符（char）数
        int readChars;
        while ((readChars=reader.read(charBuf)) != EOF){
            // 从字符输入流中读取的数据写入字符输出流
            writer.write(charBuf, 0, readChars);
            writer.flush();
            transferred += readChars;
        }
        return transferred;
    }


    /**
     * 关闭流
     * <p> 关闭失败不会抛出异常
     */
    public static void close(Closeable c) {
        if (c != null) {
            try {
                c.close();
            } catch (IOException e) {
                log.error("IO close error:{}", e.getMessage(), e);
            }
        }
    }
}
