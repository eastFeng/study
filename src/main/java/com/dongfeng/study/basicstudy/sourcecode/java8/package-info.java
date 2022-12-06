/**
 * @author Zhang Dongfeng
 * @date 2021-01-09 17:27
 */
package com.dongfeng.study.basicstudy.sourcecode.java8;

// java 8 源码学习


// 计算机中的有符号数有三种表示方法，即原码、反码和补码。三种表示方法均有符号位和数值位两部分，符号位都是用0表示“正”，用1表示“负”，而数值位，三种表示方法各不相同

// 原码(true form)是一种计算机中对数字的二进制定点表示方法。原码表示法在数值前面增加了一位符号位（即最高位为符号位）：正数该位为0，负数该位为1（0有两种表示：+0和-0），其余位表示数值的大小。
// 原码是计算机机器数中最简单的一种形式，数值位就是真值的绝对值，符号位位“0”时表示正数，符号位为“1”时表示负数，原码又称带符号的绝对值。

// 反码通常是用来由原码求补码或者由补码求原码的过渡码。 正数的反码跟原码一样；负数时，反码就是原码符号位除外，其他位按位取反。

// 补码是计算机把减法运算转化为加法运算的关键编码。 就是取反后加1。

// 已知一个数的补码，求原码的操作其实就是对该补码再求补码。
// (1): 如果补码的符号位为“0”，表示是一个正数，其原码就是补码。
// (2): 如果补码的符号位为“1”，表示是一个负数，那么求给定的这个补码的补码就是要求的原码。


// 位移运算符 : 位移位运算符是将数据看成二进制数，对其进行向左或向右移动若干位的运算。
// 位移位运算符分为左移和右移两种，均为双目运算符。第一运算对象是移位对象，第二个运算对象是所移的二进制位数。

// <<< (左移运算符) : 将一个数的各二进制位全部左移若干位，移动的位数由右操作数指定，右操作数必须是非负值，其右边空出的位用0填补，高位左移溢出则舍弃该高位。
// 左移一位相当于该数乘以2，左移2位相当于该数乘以2^2=4。但此结论只适用于该数左移时被溢出舍弃的高位中不包含1的情况。

// >> (带符号右移运算符) : 按二进制形式把所有的数字向右移动对应的位数，低位移出(舍弃)，高位的空位补符号位，即正数补零，负数补1。
// 正数右移一位相当于除2，右移n位相当于除以2的n次方。这里是取商哈，余数就不要了。

// >>> (无符号右移运算符) : 按二进制形式把所有的数字向右移动对应位数，低位移出(舍弃)，高位的空位补零。对于正数来说和带符号右移相同，对于负数来说不同。