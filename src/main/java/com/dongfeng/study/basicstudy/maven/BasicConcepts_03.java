package com.dongfeng.study.basicstudy.maven;

/**
 * Maven的核心/基础概念：仓库，坐标，仓库配置
 *
 * @author eastFeng
 * @date 2022-11-30 14:01
 */
public class BasicConcepts_03 {
    /**
     * 一.仓库
     * <p> 1.仓库：用于存储资源，包含各种jar包。
     *
     * <p> 2.仓库分类
     * <p> 本地仓库：自己电脑上存储资源的仓库，连接远程仓库获取资源。
     * <p> 远程仓库：非本机电脑上的仓库，为本地仓库提供资源。
     * <p> 远程仓库又分为：
     * <ul>
     *     <li>中央仓库：Maven团队维护，存储所有资源的仓库。</li>
     *     <li>私服：部门/公司范围内存储资源的仓库，从中央仓库获取资源。</li>
     * </ul>
     *
     * <p> 3.私服的作用
     * <ul>
     *     <li>保存具有版权的资源，包含购买或自主研发的jar包。中央仓库中的iar都是开源的，不能存储具有版权的资源。</li>
     *     <li>一定范围内共享资源，仅对内部开放，不对外共享。</li>
     * </ul>
     *
     * <p> 二. 坐标
     * <p> 1.什么是坐标？
     * <p> Maven中的坐标用于描述仓库中资源的位置。
     * <p> Maven中央仓库：https://repo1.maven.org/maven2/，
     *     https://mvnrepository.com/
     *
     * <p> 2.Maven坐标主要组成
     * <ul>
     *     <li>groupId：定义当前Maven项目隶属组织名称 （通常是域名反写，例如:org.mybatis）。</li>
     *     <li>artifactId：定义当前Maven项目名称（通常是模块名称，例如CRM、SMS），（该组织下）项目的唯一ID。</li>
     *     <li>version：定义当前项目版本号。</li>
     *     <li>packaging：定义该项目的打包方式</li>
     * </ul>
     *
     * 3.Maven坐标的作用
     * <p> 使用唯一标识，唯一性【定位资源位置】，通过该标识可以将资源的识别与下载工作交由机器完成。
     *
     * <p> 三.仓库配置
     *
     * <p> 1.本地仓库配置（资源下载到哪）
     * <p> Maven启动后，会自动保存下载的资源到本地仓库。
     * <p> 本地仓库默认位置：
     * <localRepository>$(user.home)/.m2/repository</localRepository>
     * 当前目录位置为登录用户名所在目录下的m2文件夹中。
     * <p> 在settings配置文件自定义本地仓库位置：
     * <localRepository>D:/WorkSoftware/maven-repository</localRepository>
     * 当前目录位置为D:\WorkSoftware\maven-repository文件夹中。
     *
     * <p> 2.远程仓库配置
     * <p> Maven默认连接的仓库位置
     * <p>
     * <repositories>
     * 	<repository>
     * 		<id>central</id>
     * 		<name>Central Repository</name>
     * 		<url>https://repo.maven.apache.org/maven2</ur1>
     * 		<layout>default</layout>
     * 		<snapshots>
     * 			<enabled>false</enabled>
     * 		</snapshots>
     * 	</repository>
     * </repositories>
     *
     * <p> 3.镜像仓库配置（资源从哪来）
     * <p> 在settings配置文件中配置阿里云镜像仓库
     * <mirror>
     *    <id>nexus-aliyun</id>
     *    <mirrorOf>central</mirrorOf>
     *    <name>Nexus aliyun</name>
     *    <url>http://maven.aliyun.com/nexus/content/groups/public</url>
     * </mirror>
     *
     * <p> 4.全局setting与用户setting区别
     * <p> 全局settting定义了当前计算器中Maven的公共配置。
     * <p> 用户settting定义了当前用户的配置。
     */

    public static void main(String[] args) {
    }
}
