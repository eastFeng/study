package com.dongfeng.study.util;

/**
 * 文件类型工具类
 *
 *
 * @author eastFeng
 * @date 2022-11-23 22:50
 */
public class FileTypeUtil {
    public static final String IMAGE_PNG = "image/png";

    public static final String IMAGE_JPG = "image/jpg";

    public static final String IMAGE_JPEG = "image/jpeg";

    public static final String IMAGE_BMP = "image/bmp";

    public static final String IMAGE_GIF = "image/gif";

    public static final String[] IMAGE_SUFFIX = { "bmp", "gif", "jpg", "jpeg", "png" };

    public static final String[] FLASH_SUFFIX = { "swf", "flv" };

    public static final String[] MEDIA_SUFFIX = { "swf", "flv", "mp3", "wav", "wma", "wmv", "mid", "avi", "mpg",
            "asf", "rm", "rmvb" };

    public static final String[] VIDEO_SUFFIX = { "mp4", "avi", "rmvb" };

    // 默认允许的文件类型/后缀
    public static final String[] DEFAULT_ALLOWED_SUFFIX = {
            // 图片
            "bmp", "gif", "jpg", "jpeg", "png",
            // word excel powerpoint
            "doc", "docx", "xls", "xlsx", "ppt", "pptx", "html", "htm", "txt",
            // 压缩文件
            "rar", "zip", "gz", "bz2",
            // 视频格式
            "mp4", "avi", "rmvb",
            // pdf
            "pdf" };

    public static String getExtension(String prefix)
    {
        switch (prefix)
        {
            case IMAGE_PNG:
                return "png";
            case IMAGE_JPG:
                return "jpg";
            case IMAGE_JPEG:
                return "jpeg";
            case IMAGE_BMP:
                return "bmp";
            case IMAGE_GIF:
                return "gif";
            default:
                return "";
        }
    }
}
