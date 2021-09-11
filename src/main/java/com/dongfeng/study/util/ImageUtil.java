package com.dongfeng.study.util;

import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * @author eastFeng
 * @date 2020/8/15 - 14:37
 */
@Slf4j
public class ImageUtil {
    public static void main(String[] args) {
        String urlPath1 = "http://image.baidu.com/search/detail?z=0&word=%E6" +
                "%91%84%E5%BD%B1%E5%B8%88%E7%9F%B3%E8%80%80%E8%87%A3&hs=0&pn=4&spn=1&di=0&pi=45273135686&tn=baiduimagedetail&is=1%2C17742&ie=utf-8&oe=utf-8&cs=873970885%2C3141044368&os=&simid=&adpicid=0&lpn=0&fm=&sme=&cg=&bdtype=-1&oriquery=&objurl=http%3A%2F%2Fg.hiphotos.baidu.com%2Fimage%2Fpic%2Fitem%2Fd52a2834349b033b95e7b4601fce36d3d539bd19.jpg&fromurl=&gsm=&catename=pcindexhot&islist=&querylist=";
        String urlPath2 = "https://pics.lvjs.com.cn/uploads/pc/place2/mind/2018-12-14/zjtThumbnail1570688053354.jpg";
        String urlPath3 = "http://113.100.143" +
                ".90:6780/ifsrc/engine1/eng1store1_0/ImgWareHouse/src_0_3/20190909/20190909T205848_16_3.jpg";
        String urlPath4 = "http://image.baidu.com/search/detail?ct=503316480&z=0&ipn=false&word=%E5%A4%A7%E5%9B%BE%E5%A3%81%E7%BA%B8&step_word=&hs=2&pn=3&spn=0&di=157890&pi=0&rn=1&tn=baiduimagedetail&is=0%2C0&istype=2&ie=utf-8&oe=utf-8&in=&cl=2&lm=-1&st=-1&cs=3301271374%2C3033297907&os=2815398910%2C1433691355&simid=3537379634%2C696305877&adpicid=0&lpn=0&ln=2710&fr=&fmq=1570774473431_R&fm=result&ic=&s=undefined&hd=&latest=&copyright=&se=&sme=&tab=0&width=&height=&face=undefined&ist=&jit=&cg=wallpaper&bdtype=11&oriquery=&objurl=http%3A%2F%2Fi0.hdslb.com%2Fbfs%2Farticle%2Fbd3383f69f2b09c2fd00f62d09f181da8ce9adc9.jpg&fromurl=ippr_z2C%24qAzdH3FAzdH3Fooo_z%26e3Bktstktst_z%26e3Bv54AzdH3F6jw1AzdH3Fvenm9mc98AzdH3F&gsm=&rpstart=0&rpnum=0&islist=&querylist=&force=undefined";
        String urlPath5 = "http://image.baidu.com/search/detail?ct=503316480&z=0&ipn=d&word=%E5%A4%A7%E5%9B%BE%E5%A3%81%E7%BA%B8%20%20%20%E7%BD%AA%E6%81%B6%E7%8E%8B%E5%86%A0&step_word=&hs=2&pn=34&spn=0&di=170170&pi=0&rn=1&tn=baiduimagedetail&is=0%2C0&istype=2&ie=utf-8&oe=utf-8&in=&cl=2&lm=-1&st=-1&cs=3445938065%2C533980697&os=1036976075%2C893415991&simid=4108672705%2C532459177&adpicid=0&lpn=0&ln=547&fr=&fmq=1570774652558_R&fm=result&ic=0&s=undefined&hd=0&latest=0&copyright=0&se=&sme=&tab=0&width=&height=&face=undefined&ist=&jit=&cg=&bdtype=0&oriquery=&objurl=http%3A%2F%2Fgss0.baidu.com%2F9fo3dSag_xI4khGko9WTAnF6hhy%2Fzhidao%2Fpic%2Fitem%2F0eb30f2442a7d933325d3f4dac4bd11373f00113.jpg&fromurl=ippr_z2C%24qAzdH3FAzdH3Fr5oj6_z%26e3Bkwt17_z%26e3Bv54AzdH3Fq7jfpt5gAzdH3Fcd9dbmm8d_z%26e3Bip4s%3Fqks%3D6jswpj_q7jfpt5g_c&gsm=&rpstart=0&rpnum=0&islist=&querylist=&force=undefined";
        String urlPaht6 = "http://image.baidu.com/search/detail?ct=503316480&z=0&ipn=d&word=%E5%A4%A7%E5%9B%BE%E5%A3%81%E7%BA%B8%20%20%20%E7%BD%AA%E6%81%B6%E7%8E%8B%E5%86%A0&step_word=&hs=2&pn=94&spn=0&di=61270&pi=0&rn=1&tn=baiduimagedetail&is=0%2C0&istype=2&ie=utf-8&oe=utf-8&in=&cl=2&lm=-1&st=-1&cs=3888158230%2C4260561724&os=1639686813%2C548396169&simid=4224968773%2C793740428&adpicid=0&lpn=0&ln=547&fr=&fmq=1570774652558_R&fm=result&ic=0&s=undefined&hd=0&latest=0&copyright=0&se=&sme=&tab=0&width=&height=&face=undefined&ist=&jit=&cg=&bdtype=0&oriquery=&objurl=http%3A%2F%2Fhbimg.b0.upaiyun.com%2Fd391dbe237a75d772f43b8a7c3c00b905d44610c581a6-P03sV9_fw658&fromurl=ippr_z2C%24qAzdH3FAzdH3Fi7wkwg_z%26e3Bv54AzdH3FrtgfAzdH3F9cdn0lmmnAzdH3F&gsm=&rpstart=0&rpnum=0&islist=&querylist=&force=undefined";
        String urlPaht7 = "http://image.baidu.com/search/detail?ct=503316480&z=0&ipn=d&word=%E5%A4%A7%E5%9B%BE%E5%A3%81%E7%BA%B8%20%20%20%E7%BD%AA%E6%81%B6%E7%8E%8B%E5%86%A0&step_word=&hs=2&pn=97&spn=0&di=19470&pi=0&rn=1&tn=baiduimagedetail&is=0%2C0&istype=2&ie=utf-8&oe=utf-8&in=&cl=2&lm=-1&st=-1&cs=2803443262%2C22505183&os=1942415432%2C3938156680&simid=3410430376%2C277627553&adpicid=0&lpn=0&ln=547&fr=&fmq=1570774652558_R&fm=result&ic=0&s=undefined&hd=0&latest=0&copyright=0&se=&sme=&tab=0&width=&height=&face=undefined&ist=&jit=&cg=&bdtype=0&oriquery=&objurl=http%3A%2F%2Fpic1.win4000.com%2Fwallpaper%2F7%2F58ed8df801e09.jpg&fromurl=ippr_z2C%24qAzdH3FAzdH3Fooo_z%26e3Botg9aaa_z%26e3Bv54AzdH3F45ktsj_1jpwts_8dc0ad_n_z%26e3Bip4s&gsm=&rpstart=0&rpnum=0&islist=&querylist=&force=undefined";
        String urlPaht8 = "http://image.baidu.com/search/detail?ct=503316480&z=0&ipn=d&word=%E5%A4%A7%E5%9B%BE%E5%A3%81%E7%BA%B8%20%20%20%E7%BD%AA%E6%81%B6%E7%8E%8B%E5%86%A0&step_word=&hs=2&pn=103&spn=0&di=137830&pi=0&rn=1&tn=baiduimagedetail&is=0%2C0&istype=2&ie=utf-8&oe=utf-8&in=&cl=2&lm=-1&st=-1&cs=3900719567%2C559477663&os=2195078235%2C3204559839&simid=0%2C0&adpicid=0&lpn=0&ln=547&fr=&fmq=1570774652558_R&fm=result&ic=0&s=undefined&hd=0&latest=0&copyright=0&se=&sme=&tab=0&width=&height=&face=undefined&ist=&jit=&cg=&bdtype=0&oriquery=&objurl=http%3A%2F%2Fwww.ouliu.net%2Fuploadfile%2F2012%2F0906%2F20120906095623625.jpg&fromurl=ippr_z2C%24qAzdH3FAzdH3Fooo_z%26e3B4wsj451jsfrtvp76j_z%26e3BgjpAzdH3F%25E0%25BD%25AA%25Em%25b8%25Bm%25E0%25bE%25bB%25Ec%25bm%25Aa%25Ec%25bA%25Ab%25Em%25BC%25AB%25El%25AB%25lb%25Em%25Bb%25bc%25Ec%25An%25b8%25E0%25BA%25Bb%25En%25ba%25l8%25El%25AB%25lb%25Em%25Bb%25bcAzdH3F%25E0%25BD%25AA%25Em%25b8%25Bm%25E0%25bE%25bB%25Ec%25bm%25Aa%25Ec%25bA%25Ab%25Em%25BC%25AB%25El%25AB%25lb%25Em%25Bb%25bc%25Ec%25An%25b8%25E0%25BA%25Bb%25En%25ba%25l8%25El%25AB%25lb%25Em%25Bb%25bc-%25E0%25BD%25AA%25Em%25b8%25Bm%25E0%25bE%25bB%25Ec%25bm%25Aa%25Ec%25bA%25Ab%25Em%25BC%25AB%25El%25AB%25lb%25Em%25Bb%25bc_z%26e3Bip4s&gsm=&rpstart=0&rpnum=0&islist=&querylist=&force=undefined";
        String urlPath9 = "http://image.baidu.com/search/detail?ct=503316480&z=0&ipn=d&word=%E5%A4%A7%E5%9B%BE%E5%A3%81%E7%BA%B8%20%20%20%E7%BD%AA%E6%81%B6%E7%8E%8B%E5%86%A0&step_word=&hs=2&pn=14&spn=0&di=127710&pi=0&rn=1&tn=baiduimagedetail&is=0%2C0&istype=2&ie=utf-8&oe=utf-8&in=&cl=2&lm=-1&st=-1&cs=3918583735%2C571364005&os=763956056%2C1871413877&simid=3285227589%2C389750346&adpicid=0&lpn=0&ln=547&fr=&fmq=1570774652558_R&fm=result&ic=0&s=undefined&hd=0&latest=0&copyright=0&se=&sme=&tab=0&width=&height=&face=undefined&ist=&jit=&cg=&bdtype=0&oriquery=&objurl=http%3A%2F%2Fuploads.5068.com%2Fallimg%2F151022%2F48-1510221T643-50.jpg&fromurl=ippr_z2C%24qAzdH3FAzdH3Fdp_z%26e3Bcamb_z%26e3Bv54AzdH3FrtvAzdH3F8m88m8_8m_z%26e3Bip4s&gsm=&rpstart=0&rpnum=0&islist=&querylist=&force=undefined";

        List<String> urlList = new ArrayList<>();
        urlList.add(urlPath1);
        urlList.add(urlPath2);
        urlList.add(urlPath3);
        urlList.add(urlPath4);
        urlList.add(urlPath5);
        urlList.add(urlPaht6);
        urlList.add(urlPaht7);
        urlList.add(urlPaht8);
        urlList.add(urlPath9);

        long totalBytes = 0;
        for (String urlPath : urlList){
            byte[] imageFromURL = imageURL2Bytes(urlPath);
            totalBytes += imageFromURL.length;
        }

        String imageMB = bytes2kb(totalBytes);

        //输出
        System.out.println("图片的字节数：" + totalBytes + ", 图片的大小："+imageMB);
    }


    /**
     * 根据图片地址获取图片字节数组
     *
     * @param urlPath 图片地址
     * @return 字节数组
     */
    public static byte[] imageURL2Bytes(String urlPath) {
        // 字节数组
        byte[] data = null;
        // 输入流
        InputStream is = null;
        // Http连接对象
        HttpURLConnection conn = null;
        try {
            // Url对象
            URL url = new URL(urlPath);
            // 打开连接
            conn = (HttpURLConnection) url.openConnection();
            // 打开读取 写入是setDoOutput
            conn.setDoInput(true);
            // 设置请求方式
            conn.setRequestMethod("GET");
            // 设置超时时间
            conn.setConnectTimeout(6000);
            // 得到访问的数据流
            is = conn.getInputStream();
            // 验证访问状态是否是200 正常
            if (conn.getResponseCode() == 200) {
                data = inputStream2Bytes(is);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (is != null) {
                    // 关闭流
                    is.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (conn!=null){
                // 关闭连接
                conn.disconnect();
            }
        }
        return data;
    }

    /**
     * 将流转换为字节数组
     *
     * @param is InputStream
     * @return byte[]
     */
    public static byte[] inputStream2Bytes(InputStream is) {
        /*
         * 捕获内存缓冲区的数据，转换成字节数组
         * ByteArrayOutputStream类是在创建它的实例时，程序内部创建一个byte型别数组的缓冲区，然后利用ByteArrayOutputStream和ByteArrayInputStream的实例向数组中写入或读出byte型数据。
         * 在网络传输中我们往往要传输很多变量，我们可以利用ByteArrayOutputStream把所有的变量收集到一起，然后一次性把数据发送出去。
         */
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        // 创建字节数组 1024个字节 也就是1KB
        byte[] buffer = new byte[1024];
        // 防止无限循环
        int length = -1;
        try {
            // 循环写入数据到字节数组
            while ((length = is.read(buffer)) != -1) {
                baos.write(buffer, 0, length);
            }
            // 强制刷新，扫尾工作，主要是为了，让数据流在管道的传输工程中全部传输过去，防止丢失数据
            baos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 字节流转换字节数组
        byte[] data = baos.toByteArray();
        try {
            // 关闭读取流
            is.close();
            // 关闭写入流
            baos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }

    /**
     * 将获取到的字节数转换为KB，MB模式
     *
     * @param bytes 字节数
     * @return KB, MB
     */
    public static String bytes2kb(long bytes) {
        BigDecimal filesize = new BigDecimal(bytes);
        BigDecimal megabyte = new BigDecimal(1024 * 1024);
        float returnValue = filesize.divide(megabyte, 2, BigDecimal.ROUND_UP).floatValue();
        if (returnValue > 1){
            return (returnValue + "MB");
        }
        BigDecimal kilobyte = new BigDecimal(1024);
        returnValue = filesize.divide(kilobyte, 2, BigDecimal.ROUND_UP).floatValue();
        return (returnValue + "KB");
    }
}
