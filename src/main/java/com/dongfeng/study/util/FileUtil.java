package com.dongfeng.study.util;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.CharUtil;
import cn.hutool.core.util.StrUtil;
import com.dongfeng.study.bean.base.BaseResponse;
import com.dongfeng.study.bean.enums.ResponseCodeEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 文件工具类
 *
 * @author eastFeng
 * @date 2022-11-23 22:02
 */
@Slf4j
public class FileUtil {
    public static void main(String[] args) {
        String suffix = getSuffix("zlahgagh");
        System.out.println(null == suffix);
        System.out.println(StrUtil.isBlank(suffix));
        System.out.println("suffix="+suffix+"======");
    }

    /**
     * 默认的文件名最大长度为100
     */
    public static final int DEFAULT_MAX_FILE_NAME_LENGTH = 100;

    /**
     * 默认的文件大小最大为50M
     */
    public static final long DEFAULT_MAX_FILE_SIZE = 50 * 1024 * 1024;

    /**
     * 特殊的后缀
     */
    public static final CharSequence[] SPECIAL_SUFFIX = new CharSequence[]{"tar.bz2", "tar.Z", "tar.gz", "tar.xz"};

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

        // 获取文件后缀/拓展名
        String suffix = getSuffix(originalFilename);
        // 当文件无后缀名时(如C盘下的hosts文件就没有后缀名),就不用判断后缀名
        if (!StringUtil.EMPTY.equals(suffix)){
            // 判断文件的后缀/拓展名是否是允许上传文件后缀/拓展名
            boolean containsSuffix = ArrayUtil.containsIgnoreCase(FileTypeUtil.DEFAULT_ALLOWED_SUFFIX, suffix);
            if (!containsSuffix){
                return BaseResponse.errorInstance(4004,
                        "文件[" + originalFilename + "]后缀[" + suffix + "]不正确，请上传" +
                                Arrays.toString(FileTypeUtil.DEFAULT_ALLOWED_SUFFIX) + "格式");
            }
        }

        // 文件类型
        String type = multipartFile.getContentType();
        // 判断文件的类型是否是允许上传文件类型
        boolean containsType = ArrayUtil.containsIgnoreCase(FileTypeUtil.DEFAULT_ALLOWED_SUFFIX, suffix);
        if (!containsType){
            return BaseResponse.errorInstance(4005,
                    "文件[" + originalFilename + "]类型[" + type + "]不正确，请上传" +
                            Arrays.toString(FileTypeUtil.DEFAULT_ALLOWED_SUFFIX) + "类型");
        }
        // 注意：其实可以只根据文件类型判断，不用判断文件名后缀/拓展名，因为有的程序会恶意篡改文件名后缀/拓展名


        log.info("----检查要上传的文件是否符合要求——检查结束----");
        return BaseResponse.successInstance(originalFilename);
    }


    /**
     * 检查文件是否可/允许下载
     *
     * @param fileName 需要下载的文件
     * @return true：正常/允许， false：非法/不允许
     */
    public static BaseResponse<Boolean> checkAllowDownload(String fileName){
        BaseResponse<Boolean> response = new BaseResponse<>();
        response.setData(Boolean.FALSE);

        Map<String, String> map = new HashMap<>();
        map.put("fileName", fileName);

        // 检查参数是否为空
        if (StrUtil.isBlank(fileName)){
            log.info("文件名称:{}为空，无法下载。", fileName);
            String format = StrUtil.format("文件名称{fileName}为空，无法下载。", map);
            BaseResponse.setError(response, 4003, format);
            return response;
        }

        // 禁止目录上跳级别
        if (StrUtil.contains(fileName, "..")){
            log.info("文件名称:{}为禁止目录上跳级别，不允许下载。", fileName);
            String format = StrUtil.format("文件名称{fileName}为禁止目录上跳级别，不允许下载。", map);
            BaseResponse.setError(response, 4004, format);
            return response;
        }

        // 获取文件后缀/拓展名
        String suffix = getSuffix(fileName);
        // 检查要下载的文件后缀/拓展名 是否 包含在默认允许的文件后缀/拓展名中
        if (ArrayUtil.containsIgnoreCase(FileTypeUtil.DEFAULT_ALLOWED_SUFFIX, suffix)){
            // 包含在里面，可以下载
            response.setData(Boolean.TRUE);
            return response;
        }

        log.info("文件名称:{}非法，不允许下载。", fileName);
        String format = StrUtil.format("文件名称{fileName}非法，不允许下载。", map);
        BaseResponse.setError(response, 4006, format);
        return  response;
    }


    /**
     * 根据完整文件名获取主文件名
     *
     * @param fileName 完整文件名
     * @return 主文件名
     */
    public static String getPrefix(String fileName) {
        if (null == fileName) {
            return null;
        } else {
            int len = fileName.length();
            if (0 == len) {
                return fileName;
            } else {
                CharSequence[] var2 = SPECIAL_SUFFIX;
                int end = var2.length;

                for(int var4 = 0; var4 < end; ++var4) {
                    CharSequence specialSuffix = var2[var4];
                    if (StrUtil.endWith(fileName, "." + specialSuffix)) {
                        return StrUtil.subPre(fileName, len - specialSuffix.length() - 1);
                    }
                }

                if (CharUtil.isFileSeparator(fileName.charAt(len - 1))) {
                    --len;
                }

                int begin = 0;
                end = len;

                for(int i = len - 1; i >= 0; --i) {
                    char c = fileName.charAt(i);
                    if (len == end && '.' == c) {
                        end = i;
                    }

                    if (CharUtil.isFileSeparator(c)) {
                        begin = i + 1;
                        break;
                    }
                }

                return fileName.substring(begin, end);
            }
        }
    }

    /**
     * 根据完整文件名（文件名称+文件拓展名）获取文件后缀/拓展名
     * <p>获得文件后缀名，扩展名不带“.”</p>
     *
     * @param fileName 整文件名（文件名称+文件拓展名）
     * @return 文件后缀名，扩展名不带“.”
     */
    public static String getSuffix(String fileName) {
        if (fileName == null) {
            return null;
        } else {
            int index = fileName.lastIndexOf(".");
            if (index == -1) {
                return "";
            } else {
                int secondToLastIndex = fileName.substring(0, index).lastIndexOf(".");
                String substr = fileName.substring(secondToLastIndex == -1 ? index : secondToLastIndex + 1);
                if (StrUtil.containsAny(substr, SPECIAL_SUFFIX)) {
                    return substr;
                } else {
                    String ext = fileName.substring(index + 1);
                    return StrUtil.containsAny(ext, new char[]{'/', '\\'}) ? "" : ext;
                }
            }
        }

          // 获取fileName名称的前缀(prefix)和后缀(suffix)简单方法：但是不准确
//        // substring(int beginIndex, int endIndex) : 以beginIndex起始索引(包含)，以endIndex为终止索(不包含)
//        String prefix = fileName.substring(0, fileName.lastIndexOf("."));
//        String suffix = fileName.substring(fileName.lastIndexOf("."));
    }

    /**
     * 删除文件
     *
     * @param filePath 文件路径
     * @return true:删除成功，false:删除失败
     */
    public static boolean deleteFile(String filePath) {
        boolean flag = false;
        try {
            File file = new File(filePath);
            // 路径为文件且不为空则进行删除
            if (file.isFile() && file.exists())
            {
                file.delete();
                flag = true;
            }
        } catch (Exception e) {
            // 删除失败也不往外抛出异常
            log.error("文件:{}删除失败", filePath, e);
            e.printStackTrace();
        }
        return flag;
    }


}
