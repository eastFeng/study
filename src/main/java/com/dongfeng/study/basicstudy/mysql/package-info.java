/**
 * @author eastFeng
 * @date 2022-11-24 20:07
 */
package com.dongfeng.study.basicstudy.mysql;

// Windows10系统的CMD（命令提示符）中MySQL数据库常见命令
/*
 * ====== Windows10系统的CMD（命令提示符）中MySQL数据库常见命令 ======
 *
 * ====== 首先，以管理员身份启动cmd： Win+r ——> 输入cmd ——> Ctrl+Shift+Enter
 *
 * 一、在下载安装好MySQL并配置好环境变量之后
 * mysql -V          # 查看MySQL数据库版本信息，注意字母V需要大写
 * mysql -Version    # 查看MySQL数据库版本信息，注意单词Version开头字母需要大写
 *
 * 二、启动和停止MySQL服务, 需要以管理员的身份启动cmd（命令提示符）
 * net start mysql80  # 启动MySQL，mysql80是服务名
 * net stop mysql80   # 停止MySQL，mysql80是服务名
 *
 * 三、登录和退出MySQL
 * 注：需要在MySQL服务启动了之后才能登录
 *
 * mysql -hlocalhost -uroot -pzdf123   #登录MySQL方式1
 * -h：主机名，表示要连接的数据库主机名或IP,以上的localhost表示本地的主机。
 * -u：用户名，一般默认root。
 * -p:密码
 * 注：主机名/ip,用户名,密码都要分别紧挨着前面的字母h,u,p（这三个字母都要小写）
 *
 * mysql -hlocalhost -uroot -p        #登录MySQL方式2
 * 这种方式是在p后面没有输入密码，回车之后再输入密码。
 *
 * 登录成功之后就进入了mysql，进入mysql之后所有的命令/语句都要以;结尾
 *
 * exit;     # 退出MySQL
 *
 * 三、成功登录MySQL并进入后
 * 注意：进入mysql之后所有的命令/语句都要以;结尾
 * SHOW databases;    # 查看所有数据库。SQL语句不区分大小写，show databases; 一样
 * show databases;    # SQL语句不区分大小写，不过建议大写
 *
 * use dongfeng_test; # 选择数据库，该语句是选择dongfeng_test这个数据库
 * show tables;       # 查看已经选择的数据库的所有数据表
 *
 * describe tablename;  # 表的详细描述
 * select version(),current_date;  # 显示当前mysql版本和当前日期
 *
 *
 *
 *
 */
