package com.dongfeng.study.basicstudy.algorithms;

/**
 * <b>字符串相关算法</b>
 *
 * @author eastFeng
 * @date 2020-09-26 21:21
 */
public class String_03 {
    public static void main(String[] args) {
        System.out.println(reverse("lgd是不可战胜的"));
    }

    /**
     * <b>字符串反转</b>
     *
     * @param str 要被反转的字符串
     * @return 反转之后的字符串
     */
    public static String reverse(String str){
        if (str==null){
            throw new NullPointerException("str is null");
        }

        int length = str.length();
        if (length==0 || length==1){
            return str;
        }

        char[] chars = str.toCharArray();

        // >>: 位移操作
        // 如果为偶数，右移一位表示除以2
        // 如果是奇数，右移一位并非完全相当于除以2，是近似于除以2，如果35右移一位之后是17

        // length-1: 数组最大下标
        int n = length - 1;
        // j是从中间下标递减到0，中间遍历到最左边
        for (int j=(n-1)>>1; j>=0; j--){
            int k = n - j;
            char jChar = chars[j];
            char kChar = chars[k];
            chars[j] = kChar;
            chars[k] = jChar;
        }

        return new String(chars);
    }

    /**
     * 反转数组，会变更原数组
     *
     * @param array 数组，会变更
     * @param startIndexInclusive 其实位置（包含）
     * @param endIndexExclusive 结束位置（不包含）
     * @return 变更后的原数组
     */
    public static char[] reverse(char[] array, final int startIndexInclusive, final int endIndexExclusive){
        if (array==null || array.length==0 || array.length==1){
            return array;
        }

        int i = Math.max(startIndexInclusive, 0);
        int j = Math.min(array.length, endIndexExclusive) - 1;
        while (j > i) {
            exchange(array, i, j);
            j--;
            i++;
        }
        return array;
    }

    /**
     * <b>是否包含指定字符串</b>
     *
     * @param source 源字符串
     * @param target 要查找的字符串
     * @return true:包含 false:不包含
     */
    public static boolean contains(String source, String target){
        if (source==null || target==null){
            throw new NullPointerException("source or target is null");
        }
        char[] sourceChars = source.toCharArray();
        char[] targetChars = target.toCharArray();
        int index = indexOf(sourceChars, 0, sourceChars.length,
                targetChars, 0, targetChars.length, 0);
        System.out.println("【"+index+"】");
        return index > -1;
    }

    /**
     * source是要被搜索的字符串，target是搜索的字符串
     *
     * @param source       要被搜索的字符串(源字符串的字符数组)
     * @param sourceOffset 源字符串的偏移量---->确定真正要被搜索的字符串(确定真正的source)，真正的是从sourceOffset下标之后(包含下标)的部分源字符串
     *                     仅当sourceOffset=0时，真正要被搜索的字符串就是source本身
     * @param sourceCount  源字符串的长度(字符个数)
     * @param target       要搜索的字符串(目标字符串的字符数组)
     * @param targetOffset 目标字符串的偏移量---->含义和sourceOffset相同
     * @param targetCount  目标字符串的长度(字符个数)
     * @param fromIndex    要开始搜索的索引(数组下标)---->从真正开始查找的源字符串哪个索引下的字符开始搜索
     * @return 查找到的第一个下标(小于0: 不包含, 大于等于0: source中第一个匹配的下标)
     */
    public static int indexOf(char[] source, int sourceOffset, int sourceCount,
                              char[] target, int targetOffset, int targetCount,
                              int fromIndex) {
        // 如果要开始搜索的索引大于等于源字符串的长度
        if (fromIndex >= sourceCount){
            //如果目标字符串是空的,则包含,返回源字符串最后一个下标
            //如果目标字符串不是空的,则不包含,返回-1
            return targetCount==0 ? sourceCount : -1;
        }
        // 下标最小值为0
        if (fromIndex < 0){
            fromIndex = 0;
        }
        // 如果目标字符串是空的,包含,返回开始索引fromIndex
        if (targetCount == 0){
            return fromIndex;
        }

        // 目标真正的第一个字符：要查找的第一个字符
        char first = target[targetOffset];
        // 循环查找的最大下标
        // sourceCount - targetCount : 最大下标之后必须有targetCount个字符
        int max = sourceOffset + (sourceCount - targetCount);

        // 从源字符串的下标为(sourceOffset+fromIndex)的字符开始搜索
        for (int i=sourceOffset+fromIndex; i<=max; i++){
            // 查找第一个字符
            while (i<=max && source[i]!=first){
                i++;
            }

            // 已经找到了第一个字符串，现在查找剩余部分
            if (i <= max){
                int j = i+1;
                int end = j+targetCount-1;

                for (int k=targetOffset+1; j<end && source[j]==target[k]; j++,k++){
                    // 不需要做任何事情
                }

                if (j==end){
                    // 找到整个字符串
                    return i-sourceOffset;
                }
            }
        }
        return -1;
    }


    /**
     * <b>字符替换</b>
     *
     * @param source 要进行字符替换的字符串
     * @param oldChar 老字符
     * @param newChar 新字符
     * @return 替换之后的字符串
     */
    public static String replace(String source, char oldChar, char newChar){
        if (source==null){
            return null;
        }

        // 不一样的情况下才进行替换操作
        if (newChar != oldChar){
            int length = source.length();
            int i = 0;
            char[] chars = source.toCharArray();

            while (i < length){
                if (chars[i] == oldChar){
                    //遇到第一个oldChar就会跳出循环
                    break;
                }
                i++;
            }

            // i = length 说明source里面没有字符oldChar
            // i < length 说明source里面有字符oldChar
            if (i < length){
                //此时i就是source里面第一个出现的oldChar的chars[]数组下标
                //新建buf数组用来存放替换之后的char
                char[] buf = new char[length];

                //把遇到的第一个oldChar之前的字符先复制
                for (int j=0; j<i; j++){
                    buf[j] = chars[j];
                }

                //处理i之后的字符
                while (i < length){
                    char aChar = chars[i];
                    buf[i] = (aChar==oldChar) ? newChar : aChar;
                    i++;
                }
                return new String(buf);
            }
        }
        return source;
    }


    public static void exchange(char[] c, int i, int j){
        if (isEmpty(c)){
            throw new IllegalArgumentException("Number array must not empty !");
        }

        char temp = c[i];
        c[i] = c[j];
        c[j] = temp;
    }

    public static Boolean isEmpty(char[] c){
        return c==null || c.length==0;
    }

    public static <T> boolean isEmpty(T[] t){
        return false;
    }
}
