package com.dongfeng.study.basicstudy.algorithms;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author eastFeng
 * @date 2020/6/29 - 15:14
 */
public class BasicAl {
    public static void main(String[] args) {
    }



    private static int[][] memo;
    /**
     * longest common subsequence
     */
    static int lcs(String s1, String s2){
        if (s1==null || s2==null){
            throw new NullPointerException("参数不能为null");
        }
        if (s1.length()==0 || s2.length()==0){
            return 0;
        }

        int i = s1.length();
        int j = s2.length();

        memo = new int[i][j];
        //二维数组每个值初始化为-1
        for (int[] row : memo) {
            Arrays.fill(row, -1);
        }

        return dp(s1, 0, s2, 0);
    }

    /**
     * base case，是i==s1.length 或者j==s2.length
     * 这时候s1或者s2就相当于空串，最长公共子序列长度显然为0
     *
     *
     * int dp(int i, int j) {
     *     dp(i + 1, j + 1); // #1
     *     dp(i, j + 1);     // #2
     *     dp(i + 1, j);     // #3
     * }
     *
     * 假设我想从dp(i, j)转移到dp(i+1, j+1)，有不止一种方式，可以直接走#1，
     * 也可以走#2 -> #3，也可以走#3 -> #2
     */
    static int dp(String s1, int i, String s2, int j){
        //base case
        if (i==s1.length() || j==s2.length()){
            return 0;
        }

        //如果计算过，则直接返回备忘录中的答案
        if (memo[i][j] != -1){
            return memo[i][j];
        }

        if (s1.charAt(i) == s2.charAt(j)){
            //相等, 这个字符一定在lcs中
            memo[i][j] = 1 + dp(s1, i+1, s2, j+1);
        }else {
            //情况1, s1[i]不在lcs中
            memo[i][j] = Math.max(dp(s1, i+1, s2, j),
                    //情况2, s2[j]不在lcs中
                    dp(s1, i, s2, j+1));
        }

        return memo[i][j];
    }


     static int fib(int n){
        if (n==0 ) {
            return 0;
        }
        if (n==1){
            return 1;
        }

        int[] dp = new int[n+1];

        dp[1] = dp[2] = 1;
        for (int i=3; i<=n; i++){
            dp[i] = dp[i-1] + dp[i-2];
        }
        return dp[n];
    }


    /**
     * <b>判断整数a是否是奇数：通过位运算判断</b>
     *
     * <p> 按位与（&）：相同位的两个二进制数字都为1，则为1；若有一个不为1，则为0
     * <p> 1的二进制形式为：00000001
     * <p> 任何偶数的二进制形式的最后一位是：0
     * <p>任何奇数的二进制形式的最后一位是：1
     * <p> 所以，一个整数和1进行按位与操作：结果为1，则一定是奇数；结果为0一定是奇数。
     *
     * @param a 要判断的整数
     * @return true是奇数 false是偶数
     */
    public static boolean isOdd(int a){
        return (a&1)==1;
    }

    /**
     * <b>判断是否是质数</b>
     *
     * <p> 质数又称素数: 一个大于1的自然数，除了1和它自身外，不能被其他自然数整除的数叫做质数；否则称为合数。
     *
     * <p>大于3的素数只有6N-1和6N+1两种形式，我们只需判定这两种数是素数还是合数即可。
     *
     * @param a 要判断的整数
     * @return true是质数，false不是质数
     */
    public static boolean isPrime(int a){
        // 小于等于3的整数中，2和3是素数
        if (a<=3){
            return a>1;
        }

        // 不在6的倍数两侧的一定不是质数
        if ((a%6!=1) && (a%6!=5)){
            return false;
        }

        int sqrt = (int) Math.sqrt(a);
        for (int i=5; i<=sqrt; i+=6){
            if (a%i==0 || a%(i+2)==0){
                return false;
            }
        }
        return true;
    }


    /**
     * <b>获取n以内的素数</b>
     *
     * @param n 范围
     * @return List<Integer> n以内的所有素数的集合
     */
    public static List<Integer> calPrime(int n){
        List<Integer> primeList = new ArrayList<>();
        if (n<=1){
            return primeList;
        }

        byte[] origin = new byte[n+1];
        int count = 0;
        for (int i=2; i<n+1; i++){
        }

        return primeList;
    }

}
