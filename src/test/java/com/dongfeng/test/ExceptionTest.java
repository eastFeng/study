package com.dongfeng.test;

import cn.hutool.core.exceptions.ExceptionUtil;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * @author eastFeng
 * @date 2021-01-28 16:38
 */
public class ExceptionTest {
    public static void main(String[] args) {
        String stacktrace = null;
        try {
            division(0);
        } catch (Exception e) {
            stacktrace = ExceptionUtil.stacktraceToString(e);
        }

        System.out.println("异常堆栈信息："+stacktrace);
    }

    public static void division(int i){
        int i1 = 7 / i;
        System.out.println(i1);
    }

    private static String getStackError(Exception exception) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw, true);
        exception.printStackTrace(pw);
        return sw.getBuffer().toString();
    }
}
