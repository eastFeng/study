package com.dongfeng.study.basicstudy.maven;

/**
 * @author eastFeng
 * @date 2022-12-01 17:05
 */
public class PomExtends_07 {

    /**
     * 模块聚合与继承
     *
     * <p> 多个Maven项目之间难免有重复的pom配置，重复的配置没必要重复写，
     * Maven提供了pom的父子继承机制，类似Java的父子类。
     * 将公用的pom配置定义在父pom中，子pom继承后就等价持有父pom中的内容，实现pom复用。
     *
     * <p> 父pom的打包方式（packing元素）必须是pom。
     *
     */
    public static void main(String[] args) {
    }
}
