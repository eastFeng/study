package com.dongfeng.test;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dongfeng.study.bean.vo.Good;
import com.dongfeng.study.bean.vo.UserPunch;
import com.github.liaochong.myexcel.core.DefaultExcelBuilder;
import com.github.liaochong.myexcel.core.DefaultStreamExcelBuilder;
import com.github.liaochong.myexcel.utils.FileExportUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Workbook;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author eastFeng
 * @date 2020-10-22 10:31
 */
@Slf4j(topic = "------")
public class ExcelTest {
    public static void main(String[] args) {
//        List<Good> goodList = new ArrayList<>(10);
//        goodList.add(new Good("10006","手机","小米10",10,6500));
//        goodList.add(new Good("10007","电脑","联想小新Air14",20,6500));
//        goodList.add(new Good("10007","电脑","联想拯救者Y7000P",20,12999));
//        goodList.add(new Good("10008","键盘","阿米洛中国花旦娘机械键盘 德国cherry静音红轴",30,1149.00));
//        goodList.add(new Good("10009","键盘","阿米洛中国花旦娘机械键盘 德国cherry红轴",30,102.00));
//        goodList.add(new Good("10010","鼠标","戴尔MS116有线商务办公鼠标",40,30.90));
//        exportFile(goodList);
//
//        dynamicExport(goodList);
//
//        HashMap<String, String> hashMap = new HashMap<>();
//        Set<Map.Entry<String, String>> entries = hashMap.entrySet();


//        bookToExcel("乔家大院11-18","5f11729dd6920c45eab22253", "20201118");

        System.out.println(File.separator);
    }

    //----------------------------------MyExcel 工具 start------------------------------------------
    /**
     * 文件导出
     * 默认导出
     * @param goodList 要导出到Excel表格的数据
     */
    public static void exportFile(List<Good> goodList){
        Workbook workbook = DefaultExcelBuilder.of(Good.class)
                .build(goodList);
        try {
            FileExportUtil.export(workbook, new File("D:\\MyFiles\\myExcel.xlsx"));
            log.info("导出完成");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 文件加密导出
     * 默认导出
     * @param goodList 要导出到Excel表格的数据
     */
    public static void exportFileEncrypt(List<Good> goodList){
        Workbook workbook = DefaultExcelBuilder.of(Good.class)
                .build(goodList);
        try {
            FileExportUtil.encryptExport(workbook, new File("D:\\MyFiles\\encryptMyExcel.xlsx"), "123456");
            log.info("导出完成");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Excel流式导出 : 适合大数据量场景----10万+
     * 流式导出采用生产者消费者模式, 允许分批获取数据,分批写入Excel
     * 且默认采用SXSSF模式, 内存占用量极低, 真正意义上实现海量数据导出, 另外流式导出支持zip压缩包等独有特性
     */
    @Test
    public void streamExportFile(){
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(1600840759000L));
        Workbook workbook;
        //1. 配置
        try (DefaultStreamExcelBuilder<UserPunch> streamExcelBuilder = DefaultStreamExcelBuilder
                //如导出Map类型数据, 使用of(Map.class)
                .of(UserPunch.class)
                //容量设定 在主动划分excel使用, 可选
                .capacity(100000)
                .start()) {


            for (int i=0; i<=3; i++){
                if (i!=0){
                    calendar.add(Calendar.DATE, 1);
                }
                String date = format.format(calendar.getTime());
                log.info(date);

                StringBuilder sb = new StringBuilder("http://one.joyuai.com/city-marketing-api-mgt-app/hym/batch" +
                        "/getPunchDays?secret=ksmujw29023@j3jrf8929$3ijfj9203@jsj0!2&formId=5eaa1de8d6920c45ea6c67b7&dateStartStr=");

                sb.append(date);
                sb.append("&dateEndStr=");
                sb.append(date);
                //请求打卡数据
                String result = HttpUtil.get(sb.toString());

                JSONObject jsonObject = JSON.parseObject(result);
                List<UserPunch> punchList = JSON.parseArray(jsonObject.getString("data"), UserPunch.class);
                //2. 数据追加
                streamExcelBuilder.append(punchList);
            }
            //3. 完成构建
            workbook = streamExcelBuilder.build();
            FileExportUtil.export(workbook, new File("D:\\MyFiles\\streamMyExcel.xlsx"));
            log.info("导出完成");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 动态导出:
     * 1. 动态指定标题、字段顺序；
     * 2. 字段分组；
     * 3. Map导出；
     * @param goodList 要导出到Excel表格的数据
     */
    public static void dynamicExport(List<Good> goodList){
        //标题
        List<String> titles = new ArrayList<>(5);
        titles.add("商品ID");
        titles.add("商品名称");
        titles.add("类型");
        titles.add("数量");
        titles.add("价格");

        //field display order
        List<String> order = new ArrayList<>(5);
        order.add("id");
        order.add("name");
        order.add("type");
        order.add("number");
        order.add("price");

        try {
            Workbook workbook = DefaultExcelBuilder.of(Good.class)
                    .sheetName("title example")
                    .titles(titles)
                    .fieldDisplayOrder(order)
                    .build(goodList);

            FileExportUtil.export(workbook, new File("D:\\MyFiles\\titleMyExcel.xlsx"));
            log.info("导出完成");
        } catch (Exception e) {
            log.error("dynamicExport error:{}", e.getMessage(), e);
        }
    }
    //----------------------------------MyExcel 工具 end  ------------------------------------------

}
