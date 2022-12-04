package com.dongfeng.study.basicstudy.spring.ioc;

/**
 * @author eastFeng
 * @date 2022-12-04 14:45
 */
public class Book {
    /**
     * 书本名称
     */
    private String name;

    /**
     * 作者名称
     */
    private String author;

    /**
     * 简介
     */
    private String briefIntroduction;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getBriefIntroduction() {
        return briefIntroduction;
    }

    public void setBriefIntroduction(String briefIntroduction) {
        this.briefIntroduction = briefIntroduction;
    }

    @Override
    public String toString() {
        return "Book{" +
                "name='" + name + '\'' +
                ", author='" + author + '\'' +
                ", briefIntroduction='" + briefIntroduction + '\'' +
                '}';
    }
}
