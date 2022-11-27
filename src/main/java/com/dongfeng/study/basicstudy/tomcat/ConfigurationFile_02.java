package com.dongfeng.study.basicstudy.tomcat;

import java.lang.invoke.SerializedLambda;

/**
 * Tomcat 中用到的配置文件的作用
 *
 * @author eastFeng
 * @date 2022-11-27 23:49
 */
public class ConfigurationFile_02 {
    public static void main(String[] args) {
        // 1、server.xml配置文件
        server();

        // 2、web.xml配置文件
        web();

        // 3、context.xml配置文件
        context();

        // 4、logging.properties配置文件
        logging();

        // 5、tomcat-users.xml配置文件
        tomcatUsers();

        //

        //


    }

    public static void server(){
        /*
         *
         * 1、server.xml配置文件
         * server.xml是Tomcat的主配置文件，位于$TOMCAT_HOME/conf目录下，
         * 包含Service, Connector, Engine, Realm, Valve, Hosts主组件的相关配置信息。
         *
         */
    }

    public static void web(){
        /*
         * 2、web.xml配置文件
         * web.xml遵循Servlet规范标准的配置文件，用于配置servlet，
         * 并为所有的Web应用程序提供包括MIME映射，配置默认servlet，Jsp处理器和一些其他的filter，
         * 能设置欢迎页面以及一些默认配置信息，分别位于$TOMCAT_HOME/conf目录下，和项目中WEB-INF目录下，
         * 两个配置文件的功能相同，只是$TOMCAT_HOME/conf下面的这个配置文件用来配置所有应用通用的配置。
         *
         *
         */
    }

    public static void context(){
        /*
         * 3、context.xml配置文件
         * context.xml配置所有host的默认配置信息，context组件是host组件的子组件。
         * Tomcat的conf目录下的context.xml的内容，位于$TOMCAT_HOME/conf目录下。
         * 一般情况下这个配置文件不需要修改，使用默认的就可以。
         *
         */
    }

    public static void logging(){
        /*
         * 4、logging.properties配置文件
         * Tomcat的日志相关配置，位于$CATALINA_BASE/conf/logging.properties目录下，
         * 如果日志不够详细，可以在Tomcat下面的classes目录下，logging.properties中添加内容如下：
         * handlers = org.apache.juli.FileHandler, java.util.logging.ConsoleHandler
         * org.apache.juli.FileHandler.level = FINE
         * org.apache.juli.FileHandler.directory = ${catalina.base}/logs
         * org.apache.juli.FileHandler.prefix = error-debug.
         * java.util.logging.ConsoleHandler.level = FINE
         * java.util.logging.ConsoleHandler.formatter=java.util.logging.SimpleFormatter
         *
         * 【注意】：如没有logging.properties配置文件，可以直接新建一个，在添加上述内容。然后重启Tomcat。
         *
         */
    }


    public static void tomcatUsers(){
        /*
         * 5、tomcat-users.xml配置文件
         * Tomcat提供了一个管理控制台，在控制台的manager的管理页面，
         * 能够查看到所有部署的应用的运行状态、也能管理应用的运行。
         * 也能通过这个界面进行应用部署。如要通过这个界面进行应用管理和部署，需要用户进行登陆。
         * 相关的配置就是在tomcat-users.xml中进行配置的。
         *
         *
         */
    }
}
