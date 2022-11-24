/**
 * @author eastFeng
 * @date 2022-11-24 20:11
 */
package com.dongfeng.study.basicstudy.cmd;

// CMD常用命令
/*
 * CMD是Windows操作系统的命令提示符，CMD是command的缩写，即命令提示符（CMD），位于C:\Windows\System32的目录下。
 * CMD常用命令：
 *
 * 一、启动cmd方式
 * 1. 用户启动: Win+r ——> 输入cmd ——> Enter
 * 2. 管理员启动: Win+r ——> 输入cmd ——> Ctrl+Shift+Enter
 *
 * 二、修改背景
 * 打开cmd，右击窗口上边框点击属性，可以为cmd命令窗口设置文字与背景样式。
 *
 * cmd快捷键（注意不是命令，是快捷键，键盘上的按键）
 * ↑ 和 ↓   # 快速查看历史命令记录
 *
 * 三、文件夹命令
 * cd..     # 返回到上一级目录
 * cd \     # 跳转到当前盘的根目录
 * D:       # 切换到D盘
 *
 * cd 文件夹名字      # 进入当前目录（文件夹）下的某一个目录（文件夹）中
 *
 * cd D:\CodingStudy\UploadFile\springboot-study  # 进入到当前盘的任意一个文件夹
 * （不局限于当前目录（文件夹）下的文件，只要是当前盘中的文件夹就行，但是需要完整路径）
 * 注意：只能进入到当前盘的任意一个存在的文件夹下，不能进入其他盘的某一个文件夹。
 * 例如，当前在D盘，执行C:\ProgramData 命令就不会成功，不会有任何反应。
 *
 * start 文件夹名字   # 打开当前目录（文件夹）下的某个文件夹，比如：start springboot-study
 *
 * start D:\CodingStudy\UploadFile\springboot-study  #打开（当前电脑，不局限于当前盘）任意一个文件夹（需要完整路径）
 *
 * start 文件名字     # 打开当前目录（文件夹）下的某个文件，必须是文件的完整名称，包括文件名称+文件拓展名。
 * 例如: start fileUpload-20221124004700048-英语单词.docx
 *
 * start 带完整路径的文件名字 # 打开（当前电脑，不局限于当前盘）任意一个文件（需要完整路径）
 * 例如: start C:\ProgramData\DisplaySessionContainer2.log
 *
 * dir     # 遍历/查看当前路径下的所有文件和文件夹
 *
 * 四、网络相关
 * ipconfig            # 查看IP地址
 * ipconfig /all       # 查看详细的IP地址信息
 * ping www.baidu.com  # 测试网络连接, 可以是网址也可以是ip地址
 * netstat -ano        # 查看网络连接、状态以及对应的进程id
 *
 * 五、其它常用命令
 * cls                    # （cmd）清屏
 * exit                   # 退出cmd
 * shutdown -s            # 关机
 * shutdown -s -t 60      # 定时关机，定时60s,时间自定
 * shutdown -r            # 关机并重启
 * shutdown -r -t 60      # 60s后重启,时间自定
 *
 *
 * 其他比较实用的，但使用频率不高的命令:
 * notepad+路径	打开记事本
 * dxdiag	检查DirectX信息
 * winver	检查Windows版本
 * wmimgmt.msc		打开windows管理体系结构（WMI）
 * wupdmgr			windows	更新程序
 * wscript			windows脚本设置
 * write		写字板
 * winmsd		系统信息
 * wiaacmgr	扫描仪和相机
 * calc		计算器
 * mplayer2	打开windows media player
 * mspaint		画图板
 * mstsc		远程桌面连接
 * mmc			打开控制台
 * dxdiag		检查Directx信息
 * drwtsn32	系统医生
 * devmgmt.msc	设备管理器
 * notepad		记事本
 * ntbackup	系统备份和还原
 * sndrec32	录音机
 * Sndovl32	音量控制程序
 * tsshutdn	60秒倒计时关机
 * taskmgr		任务管理器
 * explorer	资源管理器
 * progman		程序管理器
 * regedit.exe	注册表
 * perfmon.msc	计算机性能监测
 * eventvwr	事件查看器
 * net user  	查看用户
 * whoami		查看当前用户
 *
 * 七、JAVA相关
 * java -version     # 查看JDK版本
 * java Test.java    # 运行Test.java程序
 *
 * 八、电脑快捷键
 * 既然无鼠标办公，那必然也离不开电脑快捷键
 * win+E 打开文件管器
 * win+D 显示桌面
 * win+L 锁计算机
 * alt+F4 关闭当前程序\文件
 * ctrl+shift+Esc 打开任务管理器（或者ctrl+alt+delete）
 * ctrl+F 在一个文本或者网页里面查找，相当实用（退出一般按ESC）
 * ctrl+A 选中所有文本,或所有文件
 * crtl+alt+tab 选中窗口但不打开，使用回车打开。按tab或←→切换
 * alt+tab 选中窗口并打开
 * win+tab 任务视图
 * ctrl+tab 切换窗口(仅同一软件内多个窗口有效，如浏览器开了许多个网页)
 *
 */
