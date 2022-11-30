package com.dongfeng.study.basicstudy.maven;

/**
 * Maven简介
 *
 * @author eastFeng
 * @date 2020-12-26 19:22
 */
public class Introduction_01 {


    /**
     * 传统项目管理状态：
     * <ol>
     *     <li>jar包不统一，jar包不兼容。</li>
     *     <li>工程升级维护过程操作繁琐。</li>
     *     <li>等等问题...</li>
     * </ol>
     *
     * <p> Maven是什么？
     * <p> Maven本质是一个项目管理工具，将项目开发和管理过程抽象成一个项目对象模型（POM）。
     * <p> POM（Project Object Model）：项目对象模型。
     * <p> ps：Maven是用Java写的，所以它管理的东西都是以面向对象的形式进行设计，
     *         最终它把一个【项目看成一个对象】，而这个对象叫做POM。
     *
     * <p> Maven的作用
     * <ol>
     *     <li>项目构建：提供标准的、跨平台的自动化项目构建方式。</li>
     *     <li>依赖管理（Dependency）：方便快捷的管理项目依赖的资源 (jar包)，避免资源间的版本冲突问题。</li>
     *     <li>统一开发结构：提供标准的、统一的项目结构。</li>
     * </ol>
     *
     *
     *
     *
     *
     *
     *
     */

    public static void main(String[] args) {

    }








    /**
     * Maven 构建声明周期：
     * Maven 构建生命周期定义了一个项目构建跟发布的过程。
     * 一个典型的Maven构建（build）生命周期是由以下几个阶段的序列组成的：
     * 开始--->validate--->compile--->test--->package--->verify-->install-->deploy--->结束
     *
     * validate : 验证项目，验证项目是否正确且所有必须信息是可用的。
     * compile : 执行编译，源代码编译在此阶段完成。
     * test : 测试，使用适当的单元测试框架（例如JUnit）运行测试。
     * package : 打包，创建JAR/WAR包，如在pom.xml中定义提及的包。
     * verify: 检查，对集成测试的结果进行检查，以保证质量达标。
     * install : 安装，安装打包的项目到本地仓库，以供其他项目使用。
     * deploy : 拷贝最终的工程包到远程仓库中，以共享给其他开发人员和工程。
     *
     */
}
