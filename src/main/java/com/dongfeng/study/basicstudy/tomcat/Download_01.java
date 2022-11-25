package com.dongfeng.study.basicstudy.tomcat;

/**
 * Tomcat服务器下载, 解压, 配置环境变量等
 *
 * @author eastFeng
 * @date 2022-11-26 0:36
 */
public class Download_01 {
    /*
     * 教程:
     * https://blog.csdn.net/weixin_47700137/article/details/116055222
     *
     * 一、下载Tomcat及解压
     * 官网：http://tomcat.apache.org/
     * 1、选择下载版本（本文选择tomcat 9.0.69版本为例）
     *    下载64-bit Windows zip
     * 2、之后选择解压到任意一个盘，我是解压到D盘，解压的路径一定要记住，后面系统环境变量配置的时候要用到。
     *   D:\WorkSoftware\tomcat\apache-tomcat-9.0.69
     *
     * 二、配置环境变量
     * windows10步骤：
     * 1. 新建系统变量，变量名为CATALINA_HOME
     * 右键点击“此电脑”——>属性——>高级系统设置——>环境变量——>系统变量——>新建
     * 变量名为CATALINA_HOME
     * 变量值为解压文件夹的路径D:\WorkSoftware\tomcat\apache-tomcat-9.0.69
     *
     * 2. 找到系统变量Path，双击空白处或新建即可在末尾加上%CATALINA_HOME%\bin
     * 右键点击“此电脑”——>属性——>高级系统设置——>环境变量——>系统变量——>Path——>编辑——>新建
     * %CATALINA_HOME%\bin
     *
     * 三、验证是否配置成功
     * 上面操作已经完成配置了，但不代表着就已经成功完成配置了
     * 1. 进入Windows命令行窗口/cmd（win+R，输入cmd，回车）中输入startup.bat然后回车，
     * 可依次看到tomcat启动窗口（和双击D:\WorkSoftware\tomcat\apache-tomcat-9.0.69\bin\startup.bat文件一样的窗口），
     * 但很明显弹出的Tomcat出现了乱码。
     * 2. 上面说的乱码解决方案
     * （1）用Notepad++或者记事本打开D:\WorkSoftware\tomcat\apache-tomcat-9.0.69\conf\logging.properties文件。
     * （2）找到: java.util.logging.ConsoleHandler.encoding = UTF-8 这一行。
     * （3）把UTF-8改为GBK
     * 在cmd中重新输入startup.bat然后回车，可以看到Tomcat的启动窗口已经正常显示，不乱码了。
     * 3. Tomcat的启动窗口不关闭的情况下，在浏览器中输入http://localhost:8080/
     * 成功显示Tomcat的页面，说明环境变量配置成功。
     * 【注意】Tomcat的默认端口号是8080，一定要确保8080端口没有被占用。
     * 如果被占用，可以在D:\WorkSoftware\tomcat\apache-tomcat-9.0.69\conf\server.xml配置文件中更改Tomcat的端口，
     * 改成9571，9528等。
     *
     *
     */
}
