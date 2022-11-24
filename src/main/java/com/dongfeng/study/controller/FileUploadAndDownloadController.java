package com.dongfeng.study.controller;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.dongfeng.study.bean.base.BaseResponse;
import com.dongfeng.study.bean.base.BizException;
import com.dongfeng.study.bean.base.Constants;
import com.dongfeng.study.bean.enums.ResponseCodeEnum;
import com.dongfeng.study.util.FileUtil;
import com.dongfeng.study.util.IOUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 文件上传和下载功能
 *
 * @author eastFeng
 * @date 2022-11-23 15:45
 */
@Slf4j
@RequestMapping("/file")
@RestController
public class FileUploadAndDownloadController {


    /**
     * 文件上传到本地（单个文件）
     * <p>
     * {@link MultipartFile}为org.springframework.web.multipart包下的一个接口，也就是说如果想使用这个接口就必须引入spring框架，
     * 换句话说，如果想在项目中使用MultipartFile这个接口，那么项目必须要使用spring框架才可以，否则无法引入这个接口。
     * </p>
     * <p>
     * Multipart的意思是：多部件的；多元件的；由几部分组成的； adj
     * </p>
     *
     * <p>一般来讲使用{@link MultipartFile}这个接口主要是来实现以表单的形式进行文件上传功能。</p>
     *
     * @param multipartFile 要上传的文件
     * @return 上传到本地之后的文件名称
     */
    @PostMapping("/upload")
    public BaseResponse<String> uploadFile(HttpServletRequest request,
                                           @RequestParam("file") MultipartFile multipartFile){
        String token = request.getHeader(Constants.TOKEN);
        log.info("【文件开始上传, token:{}】", token);

        return FileUtil.uploadFile(multipartFile, Constants.UPLOAD_FILE_STORAGE_PATH);
    }

    /**
     * 文件上传到本地（多个文件）
     *
     * @param multipartFileList 要上传的多个文件
     * @return 上传到本地之后的文件名称列表
     */
    @PostMapping("/uploads")
    public BaseResponse<List<String>> uploadFiles(HttpServletRequest request,
                                           @RequestParam("file") List<MultipartFile> multipartFileList){
        String token = request.getHeader(Constants.TOKEN);
        log.info("【文件开始上传, token:{}】", token);

        // 判断文件列表是否为空
        if (CollectionUtil.isEmpty(multipartFileList)){
            log.error(ResponseCodeEnum.FILE_IS_EMPTY.getMsg());
            return BaseResponse.errorInstance(ResponseCodeEnum.FILE_IS_EMPTY);
        }

        List<String> fileNames = new ArrayList<>();
        // for循环上传每一个文件
        for (MultipartFile multipartFile : multipartFileList){
            BaseResponse<String> uploadFileResponse =
                    FileUtil.uploadFile(multipartFile, Constants.UPLOAD_FILE_STORAGE_PATH);
            if (uploadFileResponse.getCode() != ResponseCodeEnum.SUCCESS.getCode()){
                return BaseResponse.errorInstance(uploadFileResponse);
            }
            fileNames.add(uploadFileResponse.getData());
        }

        return BaseResponse.successInstance(fileNames);
    }

    /**
     * 文件下载请求：从上传的文件夹中下载文件
     * <p>使用response输出流下载，这种方式方法的返回值必须为void</p>
     *
     * @param fileName 文件名称
     * @param delete 下载之后是否删除
     * @param response 响应
     * @param request 请求
     */
    @GetMapping("/download")
    public void fileDownload(String fileName, Boolean delete,
                             HttpServletResponse response,
                             HttpServletRequest request) {

        if (delete == null){
            // 默认不删除原始文件
            delete = Boolean.FALSE;
        }

        // 首先检测文件是否允许下载
        BaseResponse<Boolean> allowDownloadResponse = FileUtil.checkAllowDownload(fileName);

        try {
            if (!allowDownloadResponse.getData()){
                throw new Exception(allowDownloadResponse.getMsg());
            }

            // 存储路径+文件名
            String filePath = Constants.UPLOAD_FILE_STORAGE_PATH + fileName;
            log.info("filePath:{}", filePath);
            // 告诉浏览器发送的数据属于什么文件类l型
            response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
            String contentDisposition = "attachment; filename=" + fileName;
            // 设置响应头
            response.setHeader("Content-disposition", contentDisposition);
            // 将文件写入响应的输出流中
            IOUtil.writeToStream(filePath, response.getOutputStream(), true);

            if (delete){
                FileUtil.deleteFile(filePath);
            }
        } catch (Exception e) {
            log.error("下载文件失败:", e);
            e.printStackTrace();
        }
    }






    public void MultipartFileMethod(MultipartFile multipartFile){
        try {
            // 获取的是文件的完整名称，包括文件名称+文件拓展名
            String originalFilename = multipartFile.getOriginalFilename();

            // 获取的是文件的类型，注意是文件的类型，不是文件的拓展名
            String contentType = multipartFile.getContentType();

            // 判断传入的文件是否为空，如果为空则表示没有传入任何文件
            boolean empty = multipartFile.isEmpty();

            // 获取文件的大小，单位是字节
            long size = multipartFile.getSize();

            // 将文件转换成一种字节数组的方式进行传输，会抛出IOException异常
            byte[] bytes = multipartFile.getBytes();

            // 将文件转换成输入流的形式来传输文件，会抛出IOException异常
            InputStream inputStream = multipartFile.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
