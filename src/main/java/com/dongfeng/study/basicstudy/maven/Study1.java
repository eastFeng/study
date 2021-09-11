package com.dongfeng.study.basicstudy.maven;

/**
 * Maven 构建声明周期
 * @author eastFeng
 * @date 2020-12-26 19:22
 */
public class Study1 {

    /**
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
     */
}
