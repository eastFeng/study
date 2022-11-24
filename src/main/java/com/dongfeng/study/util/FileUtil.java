package com.dongfeng.study.util;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ArrayUtil;
import com.dongfeng.study.bean.base.BaseResponse;
import com.dongfeng.study.bean.enums.ResponseCodeEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;

/**
 * 文件工具类
 *
 * @author eastFeng
 * @date 2022-11-23 22:02
 */
@Slf4j
public class FileUtil {

    /**
     * 默认的文件名最大长度为100
     */
    public static final int DEFAULT_MAX_FILE_NAME_LENGTH = 100;

    /**
     * 默认的文件大小最大为50M
     */
    public static final long DEFAULT_MAX_FILE_SIZE = 50 * 1024 * 1024;

    /**
     * 文件上传到本地
     *
     * @param multipartFile 原始文件（要上传的文件）
     * @param filePath 上传成功之后文件所在目录
     * @return 返回上传成功后的文件名
     */
    public static BaseResponse<String> uploadFile(MultipartFile multipartFile, String filePath){

        // 检查要上传的文件是否符合要求
        BaseResponse<String> checkResponse = checkMultipartFile(multipartFile);
        if (checkResponse.getCode() != ResponseCodeEnum.SUCCESS.getCode()){
            return checkResponse;
        }

        // 获取当前时间，拼接到文件名中，避免文件名重复。时间格式为yyyyMMddHHmmssSSS
        String nowTimeString =
                DateUtil.format(new Date(System.currentTimeMillis()), "yyyyMMddHHmmssSSS");
        // 保存的文件新名称
        String fileName = "fileUpload-"+nowTimeString+"-"+checkResponse.getData();

        // 新建文件
        File file = new File(filePath, fileName);

        // 判断上传目录/文件夹是否存在
        if (!file.getParentFile().exists()){
            // 目录/文件夹不存在，创建文件夹
            file.getParentFile().mkdirs();
        }

        try {
            // 写入文件
            multipartFile.transferTo(file);
            // 上传成功
            log.info("文件上传成功:{}",filePath+fileName);
            return BaseResponse.successInstance(fileName);
        } catch (IOException e) {
            e.printStackTrace();
            log.error(ResponseCodeEnum.FILE_UPLOAD_FIELD.getMsg());
        }

        // 上传失败
        return BaseResponse.errorInstance(ResponseCodeEnum.FILE_UPLOAD_FIELD);

        //        // 获取原文件（上传的文件）名称的前缀(prefix)和后缀(suffix)
//        // substring(int beginIndex, int endIndex) : 以beginIndex起始索引(包含)，以endIndex为终止索(不包含)
//        String prefix = originalFilename.substring(0, originalFilename.lastIndexOf("."));
//        String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
//        String fileName = "springBootStudy-"+prefix+"-fileUpload-"+nowTimeString+"."+suffix;
    }

    /**
     * 检查要上传的文件是否符合要求
     *
     * @param multipartFile 原始文件（要上传的文件）
     * @return 原始文件（要上传的文件）的完整名称（包括文件名称+文件拓展名）
     */
    public static BaseResponse<String> checkMultipartFile(MultipartFile multipartFile){
        log.info("----开始检查要上传的文件是否符合要求----");
        // 首先判断上传的文件是否为空
        if (null == multipartFile || multipartFile.isEmpty()){
            log.info(ResponseCodeEnum.FILE_IS_EMPTY.getMsg());
            return BaseResponse.errorInstance(ResponseCodeEnum.FILE_IS_EMPTY);
        }

        // 获取原始文件的完整名称，包括文件名称+文件拓展名
        String originalFilename = multipartFile.getOriginalFilename();
        // 判断文件名是否为空
        if (originalFilename == null || StringUtils.isBlank(originalFilename)){
            return BaseResponse.errorInstance(ResponseCodeEnum.FILE_NAME_IS_EMPTY);
        }
        // 判断文件名是否超过设置的最大长度
        if (originalFilename.length() > DEFAULT_MAX_FILE_NAME_LENGTH){
            return BaseResponse.errorInstance(4002, "文件名太大，允许最长文件名为100字");
        }

        // 判断文件的大小是否超过设置的最大值
        long size = multipartFile.getSize();
        if (size > DEFAULT_MAX_FILE_SIZE) {
            return BaseResponse.errorInstance(4003, "文件太大，允许上传文件最大为50M");
        }

        // 获取文件名后缀
        String suffix = cn.hutool.core.io.FileUtil.getSuffix(originalFilename);
        // 判断文件的类型/后缀是否超出允许上传文件类型/后缀
        boolean contains = ArrayUtil.containsIgnoreCase(FileTypeUtil.DEFAULT_ALLOWED_SUFFIX, suffix);
        if (!contains){
            return BaseResponse.errorInstance(4004,
                    "文件[" + originalFilename + "]后缀[" + suffix + "]不正确，请上传" +
                            Arrays.toString(FileTypeUtil.DEFAULT_ALLOWED_SUFFIX) + "格式");
        }

        log.info("----检查要上传的文件是否符合要求——检查结束----");
        return BaseResponse.successInstance(originalFilename);
    }



}
