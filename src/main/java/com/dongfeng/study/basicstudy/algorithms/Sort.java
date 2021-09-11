package com.dongfeng.study.basicstudy.algorithms;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * <b> 排序相关算法
 *
 * <p> 冒泡排序: {@link #bubble(int[])}
 * <p> 选择排序: {@link #selection(int[])}
 * <p> 插入排序: {@link #insertion(int[])}, {@link #insertionOptimize(int[])}}
 * <p> 希尔排序: {@link #shell(int[])}
 * <p> 归并排序: {@link #bottomUpMerger(int[])}
 * <p> 快速排序: {@link #quick(int[])}
 * <p> 堆排序: {@link #heapSort(ArrayList)}
 *
 * <p> 排序如此有用的一个主要原因是，在一个有序的数组中查找一个元素要比在一个无序的数组中查找简单得多。
 * 人们用了一个多世纪发现在一本按姓氏排序的电话黄页中查找某个人的电话号码最容易
 *
 * <p> 如果一个排序算法能够保留数组中重复元素的相对位置则可以被称为是稳定的。这个性质在许多情况下很重要。
 * <p> 一部分算法是稳定的（插入排序和归并排序），但很多不是（选择排序、希尔排序、快速排序和堆排序）
 * <p> 快速排序是最快的通用排序算法。
 *
 * @author eastFeng
 * @date 2020/6/5 - 15:28
 */
public class Sort {
    public static void main(String[] args) {
        int[] arr = {4, 22, 1, 2, 7, 5, 2, 8, 77, 10};

        String s = "s";
        int s1 = s.compareTo("s");
        boolean hhh = s.equals("hhh");
        // 复制测试
//        int[] copy = Arrays.copyOfRange(arr, 1, 5);
//        Arrays.sort(copy);
//        Arrays.stream(copy).forEach(t-> System.out.print(t+","));
//        System.out.println();
//        for (int k=1; k<5; k++){
//            arr[k] = copy[k-1];
//        }

//        bubble(arr);
//        insertionOptimize2(arr);
        insertionOptimize(arr, 3, arr.length-1, false);
//        selection(arr, 1, 5);
//        bubble(arr, 1, 5);
        Arrays.stream(arr).forEach(System.out::println);
    }


    /**
     * <b>冒泡排序（Bubble Sort）</b>
     *
     * <p> 稳定排序
     * <p> 时间复杂度： O（n²）
     * <p> 空间复杂度：O（1）
     * <p> 升序：把小（大）的元素往前（后）调
     *
     * <p>冒泡排序（Bubble Sort）也是一种简单直观的排序算法。它重复地走访过要排序的数列，
     * 一次比较两个元素，如果他们的顺序错误就把他们交换过来。
     * 走访数列的工作是重复地进行直到没有再需要交换，也就是说该数列已经排序完成。
     * 这个算法的名字由来是因为越小的元素会经由交换慢慢"浮"到数列的顶端。
     *
     * <p> 原理 :
     * <p> 1. 比较相邻的元素。如果第一个比第二个大，就交换他们两个。
     * <p> 2. 对每一对相邻元素做同样的工作，从开始第一对到结尾的最后一对。在这一点，最后的元素应该会是最大的数。
     * <p> 3. 针对所有的元素重复以上的步骤，除了最后一个。
     * <p> 4. 持续每次对越来越少的元素重复上面的步骤，直到没有任何一对数字需要比较。
     *
     * <p> 冒泡排序第1次遍历后会将最大值放到最右边，这个最大值也是全局最大值。
     *
     * <p>冒泡排序就是把小的元素往前调或者把大的元素往后调。比较是相邻的两个元素比较，交换也发生在这两个元素之间。所以，如果两个元素相等，是不会再交换的；
     * 如果两个相等的元素没有相邻，那么即使通过前面的两两交换把两个相邻起来，这时候也不会交换，所以相同元素的前后顺序并没有改变，所以冒泡排序是一种稳定排序算法。</p>
     *
     * @param a 要排序的数组
     */
    public static void bubble(int[] a){
        final int length = a.length;

        // 需要遍历intArray.length-1次，每次确定一个位置上面的数字
        for (int i=0; i<length-1; i++){
            /*
             * 第一次遍历之后确定最后一个位置（length-1）的数字：需要比较（length-1-0）次，本次i=0
             * 第二次遍历之后确定倒数第二个位置（length-2）的数字：需要比较（length-1-1）次，本次i=1
             * 第二次遍历之后确定倒数第三个位置（length-3）的数字：需要比较（length-1-2）次，本次i=2
             * ...
             * 所以每次遍历需要比较的次数是length-1-i次 （确定下标是x位置的数字就需要遍历x次）。
             * 最后一次（第length-1-i次）比较时左边数字的下标integerArray.length-1-i-1
             *
             * 之前遍历已经确定的位置就不用再遍历进行比较了
             */
            for (int j=0; j<length-1-i; j++){
                // 比较相邻元素：如果前面的元素比后面的元素大，则交换位置
                if (a[j]>a[j+1]){
                    exchange(a, j, j+1);
                }
            } // 该for循环对每一对相邻元素作同样的工作，从开始第一对到结尾的最后一对。这步做完后，第length-1-i位置上的数字就确定了。
        }
    }

    /**
     * <b>冒泡排序优化</b>
     *
     * <p> 设置标志位flag，如果发生了交换flag设置为true；如果没有交换就设置为false。
     * <p> 这样当一轮比较结束后如果flag仍为false，即：这一轮没有发生交换，说明数据的顺序已经排好，没有必要继续进行下去。
     */
    public static void bubbleOptimize(int[] a){
        int length = a.length;
        // 标志位
        boolean flag;

        for (int i=0; i<length-1; i++){
            // 每次遍历标志位都要先置为true，才能判断后面的元素是否发生了交换
            flag = true;
            for (int j=0; j<length-1-i; j++){
                if (a[j] > a[j+1]){
                    exchange(a, j, j+1);
                    // 发生了交换，flag设为false
                    flag = false;
                }
            }
            if (flag){
                // 标志位flag为true，说明元素没有发生过交换，待排序的序列已经是有序的，排序已经完成。
                break;
            }
        }
    }

    /**
     * <b>用冒泡排序对数组的指定范围进行排序</b>
     *
     * @param a 要排序的数组
     * @param left 要排序的第一个元素（包含）的索引
     * @param right 要排序的最后一个元素（包含）的索引
     */
    public static void bubble(int[] a, int left, int right){
        for (int i=left; i<right; i++){
            for (int j=left; j<right; j++){
                boolean flag = false;
                if (a[j] > a[j+1]){
                    exchange(a, j, j+1);
                    flag = true;
                }
                if (flag){
                    break;
                }
            }
        }
    }


    /**
     * <b>选择排序</b>
     *
     * <p> 非稳定排序
     * <p> 时间复杂度： O（n²）
     * <p> 空间复杂度：O（1）
     * <p> 升序：选择剩余元素中的最小者
     *
     * <p> 首先，找到数组中最小的那个元素，其次将它和数组的第一个元素交换位置（如果第一个元素就是最小元素，那么它和自己交换）。
     * 再次，在剩下的未排序元素中找到最小的元素，将它与数组的第二个元素交换位置。如此往复，直到将整个数组排序，这种方法叫做选择排序，
     * 因为它在不断地选择剩余元素中的最小者。
     *
     * <p> 运行时间和输入无关。为了找出最小的元素而扫描一遍数组并不能为下一遍扫描提供什么信息。
     *
     * <p> 数据移动是最少的。每次交换都会改变两个数组元素的值，因此选择排序用了N次交换——交换次数和数组的大小是线性关系。
     *
     * <p> 该算法将第i小的元素放到a[i]之中。数组的第i个位置的左边是i个最小的元素且它们不会再被访问。
     *
     * <p> 选择排序是给每个位置选择当前元素最小的，比如给第一个位置选择最小的，在剩余元素里面给第二个元素选择第二小的，
     * 依次类推，直到第n-1个元素，第n个元素不用选择了，因为只剩下它一个最大的元素了。
     * 那么，在一趟选择，如果一个元素比当前元素小，而该小的元素又出现在一个和当前元素相等的元素后面，那么交换后稳定性就被破坏了。
     * 举个例子，序列5 8 5 2 9，我们知道第一遍选择第1个元素5会和2交换，那么原序列中两个5的相对前后顺序就被破坏了，
     * 所以选择排序是一个不稳定的排序算法。
     *
     * @param a 要排序的数组
     */
    public static void selection(int[] a){
        final int length = a.length;

        // 总共要经过length-1轮比较（总共length个元素，前面length-1个元素确定了，最后一个元素自然也就确定了）
        for (int i=0; i<length-1; i++){
            // 最小元素索引
            int min = i;
            // 剩余元素中找出最小的元素
            for (int j=i+1; j<length; j++){
                if (a[j] < a[min]){
                    min = j;
                }
            }
            // 交换元素
            if (min != i){
                exchange(a, i, min);
            }
        }
    }

    /**
     * <b>用选择排序对数组的指定范围进行排序</b>
     *
     * @param a 要排序的数组
     * @param left 要排序的第一个元素（包含）的索引
     * @param right 要排序的最后一个元素（包含）的索引
     */
    public static void selection(int[] a, int left, int right){
        rangeCheck(a.length, left, right);

        for (int i=left; i<right; i++){
            int min = i;
            // j要能选择到最右边那个元素，所以是
            for (int j=i+1; j<right+1; j++){
                if (a[j] < a[min]){
                    min = j;
                }
            }
            if (min != i){
                exchange(a, i, min);
            }
        }
    }


    /**
     * <b>插入排序</b>
     *
     * <p> 稳定排序
     * <p> 时间复杂度： O（n²）
     * <p> 空间复杂度：O（1）
     *
     * <p> 通常人们整理桥牌的方法是一张一张的来，<b>将每一张牌插入到其他已经有序的牌中的适当位置</b>。
     * 在计算机的实现中，为了给要插入的元素腾出空间，我们需要将其余所有元素在插入之前都向右移动一位。这种算法叫做插入排序。
     *
     * <p> 与选择排序一样，当前索引左边的所有元素都是有序的，但它们的最终位置还不确定(选择排序是确定的)，为了给更小的元素腾出空间，它们可能会被移动。
     * 但是当索引到达数组的右端时，数组排序就完成了。
     *
     * <p> 插入排序的工作原理是通过构建有序序列，对于未排序数据，在已排序序列中从后向前扫描，找到相应位置并插入。
     * <p> 步骤：
     * <p> 1. 将第一个元素看做一个有序序列，把第二个元素到最后一个元素当成是未排序序列。
     * <p> 2. 从头到尾依次扫描未排序序列，将扫描到的每个元素插入有序序列的适当位置。
     * （如果待插入的元素与有序序列中的某个元素相等，则将待插入元素插入到相等元素的后面）
     *
     * <p> 插入排序对于部分有序的数组十分高效，也很适合小规模数组
     *
     * <p>几种典型的部分有序的数组:
     * <li> 数组中每个元素距离它的最终位置都不远。
     * <li> 一个有序的大数组接一个小数组。
     * <li> 数组中只有几个元素的位置不正确。
     *
     * <p> 对于大规模乱序数组插入排序很慢，因为它只会交换相邻的元素，因此元素只能一点一点地从数组的一端移动到另一端。
     * 一般在输入规模大于1000的场合下不建议使用插入排序。
     *
     * @param a 要排序的数组
     */
    public static void insertion(int[] a){
        final int length = a.length;
        /*
         * 外层循环对除了第一个元素之外的所有元素排序，内层循环对当前元素前面有序序列进行待插入位置查找，并进行移动。
         * 对于1到length-1之间的每一个i，将a[i]与a[0]到a[i-1]中比它小的所有元素依次有序地交换。
         * 在索引i由左向右变化的过程中，它左侧的元素总是有序的，所以当i到达数组的右端时排序就完成了。
         * 从下标为1的元素开始选择合适的位置插入，因为下标为0的只有一个元素，默认是有序的
         */
        for (int i=1; i<length; i++){
            // 从已经有序的序列最右边的开始比较，将a[i]插入到a[i-1]、a[i-2]、a[i-3]...之中
            for (int j=i; j>0&&a[j]<a[j-1]; j--){
                exchange(a, j, j-1);
            }
        }
    }

    /**
     * <b>插入排序优化</b>
     *
     * @param a 要排序的数组
     */
    public static void insertionOptimize(int[] a){
        int length = a.length;

        /*
         * 外层循环对除了第一个元素之外的所有元素，内层循环对当前元素前面有序序列进行待插入位置查找，并进行移动。
         * 从下标为1的元素开始选择合适的位置插入，因为下标为0的只有一个元素，默认是有序的
         */
        for (int i=1; i<length; i++){
            // 记录要插入的数据
            int temp = a[i];

            // 从已经有序的序列的最右边开始比较，找到比其小的数
            int j = i;
            while (j>0 && temp<a[j-1]){
                // 找到比要插入的数大的数a[j-1]，这个数需要往后（右）移一位
                a[j] = a[j-1];
                // 查看下一个
                j--;
            }

            if (j!=i){ // 这个if条件可以去掉，如果j==i就是自己赋值自己（没有往前插入）
                // 存在比其小的数，插入
                // 得到temp的位置，插入
                a[j] = temp;
            }
        }
    }

    /**
     * <b>插入排序优化</b>
     *
     * @param a 要排序的数组
     */
    public static void insertionOptimize2(int[] a){
        insertionOptimize(a, 0, a.length-1, true);
    }

    /**
     * <b> 用插入排序对数组的指定范围进行排序
     * <p> 插入排序优化
     *
     * @param a 要排序的数组
     * @param left 要排序的第一个元素（包含）的索引
     * @param right 要排序的最后一个元素（包含）的索引
     * @param leftmost 指示此部分是否在范围中最左侧（最左侧：left到right这部分元素中最小的元素小于left左边的第一个元素，或者left为0），
     *                 如果不确定就传true，传true永远不会出错。
     */
    public static void insertionOptimize(int[] a, int left, int right, boolean leftmost){
        // 数组下标left和right检查
        rangeCheck(a.length, left, right);

        if (leftmost){
            // 此部分在范围中最左侧：left到right这部分元素中最小的元素小于left左边的第一个元素。

            /*
             * 传统的插入排序，用于最左边的部分。
             * 外层循环对除了第一个元素（最左边的下标为left元素）之外的所有元素，内层循环对当前元素前面有序序列进行待插入位置查找，并进行移动。
             * 从下标为left+1的元素开始选择合适的位置插入，因为下标为left的只有一个元素，默认是有序的。
             */
            for (int i=left+1; i<=right; ++i){
                // 记录要插入的数据
                int temp = a[i];
                int j = i;
                // 从已经有序的序列的最右边开始比较，找到比其小的数
                while (temp < a[j-1]){
                    // 找到比要插入的数大的数a[j-1]，这个数需要往后（右）移一位
                    a[j] = a[j-1];
                    if (j-- == left){
                        // 遍历到最左端结束
                        break;
                    }
                }
                if (j!=i){ // 这个if条件可以去掉，如果j==i就是自己赋值自己（没有往前插入）
                    // 存在比其小的数，插入
                    // 得到temp的位置，插入
                    a[j] = temp;
                }
            }
        }else
        {
            // 此部分不在范围中最左侧，需要满足一下两个条件：
            // 1. left不为0
            // 2. left到right这部分元素中最小的元素大于等于left左边的第一个元素。

            // 跳过最长的升序序列（这里的最长指从最左边开始的升序序列），已经是升序的不用排
            do {
                if (left >= right) {
                    // left大于等于right：已经走到最右边。整个部分都是有序的了
                    return;
                }
                // 升序：右边大于等于左边
            } while (a[++left] >= a[left - 1]);

            /*
             * 相邻部分的每一个元素都扮演着哨兵的角色，因此这允许我们在每次迭代中避免左边界检查。
             * 此外，我们使用了更优化的算法，即所谓的【成对插入排序】，它比传统的插入排序实现更快（在快速排序的上下文中）。
             * 插入排序，提高了插入排序的性能，同时可以插入两个数值。
             */
            // 此时left已经不是该方法传入的left，至少是left+1了，因为在上面的do-while循环中至少执行了一次++left。
            for (int k = left; ++left <= right; k = ++left) {
                // 将要成对插入的数据，第一个值赋值a1，第二个值赋值a2
                int a1 = a[k], a2 = a[left];

                // 然后判断a1与a2的大小，使a1要大于a2
                if (a1 < a2) {
                    a2 = a1; a1 = a[left];
                }
                // 首先是插入大的数值a1，将a1与k之前的数字一一比较，直到数值小于a1为止，把a1插入到合适的位置。
                // 注意：这里的相隔距离为2，因为要预留出a2的位置。
                while (a1 < a[--k]) { // 这个判断就用到了left到right这部分元素中最小的元素大于等于left左边的第一个元素。
                    // 找到比要插入的数a1小的数a[--k]，这个数需要往后（右）移两位
                    a[k + 2] = a[k];
                }
                // 得到a1的位置，插入
                // ...78,3,1,2
                // a1 = 78
                a[++k + 1] = a1;

                // 接下来，插入小的数值a2,将a2与此时k之前的数字一一比较，直到数值小于a2为止，将a2插入到合适的位置。
                // 注意：这里的相隔距离为1。
                while (a2 < a[--k]) {  // 这个判断就用到了left到right这部分元素中最小的元素大于等于left左边的第一个元素。
                    // 找到比要插入的数a2小的数a[--k]，这个数需要往后（右）移一位
                    a[k + 1] = a[k];
                }
                // 得到a2的位置，插入
                a[k + 1] = a2;

                // 成对插入排序的每次先插入较大的数，这样我们就可以确定另一个数肯定在插入位置的左侧
            }
            int last = a[right];
            // 最后循环找到最后一个数
            // 这里两两插入，如果是奇数，最后会剩一个，单独处理
            while (last < a[--right]) {
                a[right + 1] = a[right];
            }
            a[right + 1] = last;
        }
    }

    /**
     * <b>希尔排序(Shell Sort) : 基于插入排序的快速的排序算法，又称缩小增量排序</b>
     *
     * <p> 非稳定排序
     * <p> 空间复杂度：O（1）
     *
     * <p> 希尔排序是基于插入排序的以下两点性质而提出改进方法的：
     * <p> 1. 插入排序在对几乎已经排好序的数据操作时，效率高，即可以达到线性排序的效率。
     * <p> 2. 但插入排序一般来说是低效的，因为插入排序每次只能将数据移动一位。
     *
     * <p> 希尔排序为了加快速度简单地改进了插入排序，交换不相邻的元素以对数组的局部进行排序，并最终用插入排序将局部有序的数组排序。
     *
     * <p> 希尔排序的思想是使数组中任意间隔为h的元素都是有序的。这样的数组被称为h有序数组。 换句话说，一个h有序数组就是h个相互独立的有序数组
     * 编织在一起组成的一个数组。在进行排序时，如果h很大，就能将元素移动到很远的地方，为实现更小的h有序创造方便。用这种方式，对任意以1结尾的h序列，
     * 都能将数组排序。
     *
     * <p> 希尔排序比选择排序和插入排序快的多，并且数组越大，优势越大。
     *
     * <p> 使用递增序列1, 4, 13, 40, 121, 364…的希尔排序所需的比较次数不会超出N的若干倍乘以递增序列的长度。
     *
     * <p> 由于多次插入排序，我们知道一次插入排序是稳定的，不会改变相同元素的相对顺序，
     * 但在不同的插入排序过程中，相同的元素可能在各自的插入排序中移动，最后其稳定性就会被打乱，所以shell排序是不稳定的。
     *
     * @param a 要排序的数组
     */
    public void shell(int[] a){
        final int length = a.length;
        // 增量h
        // 好的增量序列的共同特征：
        // ① 最后一个增量必须为1；
        // ② 应该尽量避免序列中的值(尤其是相邻的值)互为倍数的情况。
        int h = 1;
        while (h < length){
            // 1, 4, 13, 40, 121, 364, 1093, ...
            h = 3*h + 1;
        }

        while (h >= 1){
            // 这个while循环里其实就是一个插入排序

            // 将数组变为h有序
            for (int i=h; i<length; i++){
                // 将a[h]插入到a[i-1*h]、a[i-2*h]、a[i-3*h]...之中
                for (int j=i; j>=h&&a[j]<a[j-h]; j-=h){
                    exchange(a, j, j-h);
                }
            }

            // 递减增量h
            h = h/3;
        }
    }

    /**
     * <b>自顶向下的归并排序</b>
     *
     * <p> 稳定排序
     * <p> 时间复杂度: O(nlogn)
     * <p> 空间复杂度: T(n)
     *
     * <p>分治思想
     * <p> 将两个有序的数组归并成一个更大的有序数组
     *
     * @param a 要排序的数组
     */
    public static void topdownMerge(int[] a){
        final int length = a.length;
        // 一次性分配空间
        int[] aux = new int[length];
        topdownMergeSort(a, aux, 0, length-1);
    }

    /**
     * <b>递归方法实现自顶向下的归并排序</b>
     *
     * <p><b>将数组a[lo..hi]排序</b>
     */
    private static void topdownMergeSort(int[] a, int[] aux, int lo, int hi){
        if (hi <= lo){
            return;
        }

        int mid = lo+(hi-lo)/2;
        // 将左半边排序
        topdownMergeSort(a, aux, lo, mid);
        // 将右半边排序
        topdownMergeSort(a, aux, mid+1, hi);

        // 如果a[mid]小于等于a[mid+1]，就认为数组已经是有序的并跳过merge()方法
        if (a[mid] > a[mid+1]){
            // 归并结果
            merge(a, aux, lo, mid, hi);
        }
    }

    /**
     * <b>自底向上的归并排序</b>
     *
     * <p> 实现归并排序的另一种方法是先归并那些微型数组，然后再成对归并得到的子数组，如此这般，直到我们将整个数组归并在一起。
     *
     * <p> 自底向上的归并排序比较适合用链表组织的数据。
     *
     * @param a 要排序的数组
     */
    public static void bottomUpMerger(int[] a){
        int length = a.length;
        // 一次性分配空间
        int[] aux = new int[length];
        // sz: 子数组大小
        for (int sz=1; sz<length; sz=sz+sz){
            // lo: 子数组索引
            for (int lo=0; lo<length-sz; lo+=sz+sz){
                merge(a, aux, lo, lo+sz-1, Math.min(lo+sz+sz-1, length-1));
            }
        }

        // 自底向上的归并排序会多次遍历整个数组，根据子数组大小进行两两归并。
        // 子数组的大小sz的初始值为1，每次加倍。
        // 最后一个子数组的大小只有在数组大小是sz的偶数倍的时候才会等于sz（否则它会比sz小）
    }

    /**
     * <b>归并排序的归并操作</b>
     *
     * <p> 该方法先将所有元素复制到aux[]中，然后再归并回a[]中
     * <p> 在归并时（第二个for循环）进行了4个条件判断：
     * <p> 左半边用尽（取右半边的元素）
     * <p> 右半边用尽（取左半边的元素）
     * <p> 右半边的当前元素小于左半边的当前元素（取右半边的元素）
     * <p> 以及右半边的当前元素大于等于左半边的当前元素（取左半边的元素）
     */
    private static void merge(int[] a, int[] aux, int lo, int mid, int hi){
        int length = hi - lo +1;
        // 对小规模子数组使用插入排序
        if (length < INSERTION_SORT_THRESHOLD){
            insertionOptimize(a, lo, hi, true);
            return;
        }

        int i=lo, j=mid+1;
        // 将a[lo..hi]复制到aux[lo..hi]
        for (int k=lo; k<=hi; k++){
            aux[k] = a[k];
        }

//        if (hi + 1 - lo >= 0) {
//            System.arraycopy(a, lo, aux, lo, hi + 1 - lo);
//        }

        for (int k=lo; k<=hi; k++){
            if (i>mid)              a[k] = aux[j++];    // 左半边用尽（取右半边的元素）
            else if (j>hi)          a[k] = aux[i++];    // 右半边用尽（取左半边的元素）
            else if (aux[j]<aux[i]) a[k] = aux[j++];    // 右半边的当前元素小于左半边的当前元素（取右半边的元素）
            else                    a[k] = aux[i++];    // 右半边的当前元素大于左半边的当前元素（取左半边的元素）
        }
    }



    /**
     * <b>快速排序</b>
     *
     * <p> 快速排序引实现简单、适用于各种不同的输入数据且在一般应用中比其他排序算法都要快得多
     * <p> 快速排序引人注目的特点包括它是原地排序（只需要一个很小的辅助栈），且将长度为N的数组排序所需的时间和NlgN成正比。
     * <p> 快速排序的内循环比大多数排序算法都要短小，这意味着它无论是在理论上还是在实际中都要更快。
     * <p> 快速排序是一种分治的排序算法。它将一个数组分成两个子数组，将两部分独立地排序。
     * <p> 快速排序和归并排序是互补的：归并排序将数组分成两个子数组分别排序，并将有序的子数组归并以将整个数组排序；
     * 而快速排序将数组排序的方式则是当两个子数组都有序时整个数组也就自然有序了。
     * <p> 在第一种情况中，递归调用发生在处理整个数组之前；在第二种情况中，递归调用发生在处理整个数组之后。
     * 在归并排序中，一个数组被等分为两半；在快速排序中，切分（partition）的位置取决于数组的内容。
     *
     * <p> 该方法的关键在于切分，这个过程使得数组满足下面三个条件:
     * <p> 1. 对于某个j, a[j]已经排定。
     * <p> 2. a[lo]到a[j-1]中的所有元素都不大于a[j]
     * <p> 3. a[j+1]到a[hi]中的所有元素都不小于a[j]
     *
     * <p> 切分过程总是能排定一个元素。
     * <p> 用归纳法不难证明递归能够正确地将数组排序：
     * 如果左子数组和右子数组都是有序的，那么由左子数组（有序且没有任何元素大于切分元素）、
     * 切分元素和右子数组（有序且没有任何元素小于切分元素）组成的结果数组也一定是有序的
     *
     * @param a 要排序的数组
     */
    public static void quick(int[] a){
        int length = a.length;
        quickSort(a, 0, length-1);
    }

    /**
     * <b>快速排序具体实现</b>
     *
     * @param a 要排序的数组
     * @param lo 要排序的第一个元素（包含）的索引
     * @param hi 要排序的最后一个元素（包含）的索引
     */
    private static void quickSort(int[] a, int lo, int hi){
        if (hi<=lo){
            return;
        }
        // 小数组使用插入排序
        if (hi-lo+1 < INSERTION_SORT_THRESHOLD){
            insertionOptimize(a, lo, hi, true);
            return;
        }

        // 切分, 处理整个数组, 切分得到的索引j处的元素已经确定, 每次切分都确定一个元素的位置。
        int j = partition(a, lo, hi);
        // 将左半部分排序a[lo..j-1]
        quickSort(a, lo, j-1);
        // 将右半部分排序a[lo..j+1]
        quickSort(a, j+1, hi);
    }

    /**
     * <b> 快速排序的切分操作 </b>
     *
     * <p> 先随意地取a[lo]作为切分元素，即那个将会被排定的元素，
     * 然后我们从数组的左端开始向右扫描直到找到一个大于等于它的元素，
     * 再从数组的右端开始向左扫描直到找到一个小于等于它的元素。
     * 这两个元素显然是没有排定的，因此我们交换它们的位置。
     * 如此继续，我们就可以保证左指针i的左侧元素都不大于切分元素，右指针j的右侧元素都不小于切分元素。
     * 当两个指针相遇时，我们只需要将切分元素a[lo]和左子数组最右侧的元素（a[j]）交换然后返回j即可。
     *
     * <p> 该切分过程需要使得数组满足下面三个条件:
     * <p> 1. 对于某个j, a[j]已经排定。
     * <p> 2. a[lo]到a[j-1]中的所有元素都不大于a[j]
     * <p> 3. a[j+1]到a[hi]中的所有元素都不小于a[j]
     *
     * <p> 切分过程总是能排定一个元素
     */
    private static int partition(int[] a, int lo, int hi){
        // 左右扫描指针
        int i=lo, j=hi+1;
        // 切分元素
        int v = a[lo];

        while (true){
            // 扫描左右，检查扫描是否结束并交换元素

            // 当左指针元素小于切分元素，继续往右移动，直到找到一个大于等于切分元素的元素
            while (a[++i] < v){}
            // 当右指针元素大于切分元素，继续往左移动，直到找到一个小于等于切分元素的元素
            while (a[--j] > v){}

            if (i >= j){
                // 左右指针相遇时，主动退出循环
                break;
            }
            // 左右指针没有相遇，交换元素
            exchange(a, i, j);
        }
        // 两个指针相遇，需要将切分元素a[lo]和左子数组最右侧的元素（a[j]）交换然后返回j即可
        exchange(a, lo, j);
        return j;

// 这段代码按照a[lo]的值v进行切分。当指针i和j相遇时主循环退出。
// 在循环中，a[i]小于v时我们增大i, a[j]大于v时我们减小j，然后交换a[i]和a[j]来保证i左侧的元素都不大于v, j右侧的元素都不小于v。
// 当指针相遇时交换a[lo]和a[j]，切分结束（这样切分值就留在a[j]中了）

//        while (true){
//            // 扫描左右，检查扫描是否结束并交换元素
//
//            // 当左指针元素小于切分元素，继续往右移动，直到找到一个大于等于切分元素的
//            while (a[++i] < v) {
//                if (i == hi) {
//                    // 移动切分元素位置，其实这个判断是多余的，因为切分元素不可能比自己大
//                    break;
//                }
//            }
//            // 当右指针元素大于切分元素，继续往左移动，直到找到一个小于等于切分元素的
//            while (v < a[--j]) {
//                if (j == lo) {
//                    // 移动切分元素位置，其实这个判断是多余的，因为切分元素不可能比自己小
//                    break;
//                }
//            }
//            if (i >= j) {
//                // 左右指针相遇时，主动退出循环
//                break;
//            }
//            exchange(a, i, j);
//        }
//        exchange(a, lo, j);
//        return j;
    }

    /**
     * <b> 三向切分的快速排序 </b>
     *
     * <p> 将数组切分为三部分，分别对应小于、等于和大于切分元素的数组元素。
     * <p> 从左到右遍历数组一次，维护
     * 一个指针lt使得a[lo..lt-1]中的元素都小于v，
     * 一个指针gt使得a[gt+1..hi]中的元素都大于v，
     * 一个指针i使得a[lt..i-1]中的元素都等于v, a[i..gt]中的元素都还未确定。
     *
     * <p> a[i]进行三向比较来直接处理以下情况：
     * <p> a[i]小于v，将a[lt]和a[i]交换，将lt和i加一；
     * <p> a[i]大于v，将a[gt]和a[i]交换，将gt减一；
     * <p> a[i]等于v，将i加一。
     * @param a 要排序的数组
     * @param lo 要排序的第一个元素（包含）的索引
     * @param hi 要排序的最后一个元素（包含）的索引
     */
    public static void quick3way(int[] a, int lo, int hi){
        if (lo >= hi){
            return;
        }
        if (hi-lo+1 < INSERTION_SORT_THRESHOLD){
            insertionOptimize(a, lo, hi, true);
        }

        int lt = lo, i = lo+1, gt = hi;
        int v = a[lo];
        while (i <= gt){
            if (a[i] < v) {
                exchange(a, lt++, i++);
            } else if (a[i] > v) {
                exchange(a, i, gt--);
            } else {
                i++;
            }
        }
        // 现在a[lo..lt-1] < v = a[lt..gt] < a[gt+1..hi]成立。

        quick3way(a, lo, lt-1);
        quick3way(a, gt+1, hi);
    }

    private static int[] quick3wayPartition(int[] a, int lo, int hi){
        int lt = lo, i = lo+1, gt = hi;
        int v = a[lo];
        while (i <= gt){
            if (a[i] < v) {
                exchange(a, lt++, i++);
            } else if (a[i] > v) {
                exchange(a, i, gt--);
            } else {
                i++;
            }
        }

        // 现在a[lo..lt-1] < v = a[lt..gt] < a[gt+1..hi]成立。
        return new int[]{lt, gt};
    }

    /**
     * 如果要排序的数组长度小于此常量，则优先使用Dual-Pivot快速排序{@link #dualPivotQuicksort(int[], int, int, boolean)}。
     */
    private static final int QUICKSORT_THRESHOLD = 286;
    /**
     * 归并排序中的最大运行次数。
     */
    private static final int MAX_RUN_COUNT = 67;
    /**
     * 合并排序中运行的最大长度。
     */
    private static final int MAX_RUN_LENGTH = 33;
    /**
     * 如果要排序的数组长度小于此常量，则优先使用插入排序而不是快速排序。
     */
    private static final int INSERTION_SORT_THRESHOLD = 47;

    /**
     * 对指定的数组进行排序
     *
     * @param a 要排序的数组
     * @param left 要排序的第一个元素（包含）的索引
     * @param right 要排序的最后一个元素（包含）的索引
     * @param work 工作区数组（切片）
     * @param workBase 工作数组中可用空间的原点
     * @param workLen 工作数组的可用大小
     */
    public static void sort(int[] a, int left, int right,
                             int[] work, int workBase, int workLen) {
        // 对小数组使用Dual-Pivot快速排序
        if (right - left < QUICKSORT_THRESHOLD) {
            dualPivotQuicksort(a, left, right, true);
            return;
        }

        /*
         * Index run[i] is the start of i-th run
         * (ascending or descending sequence).
         */
        int[] run = new int[MAX_RUN_COUNT + 1];
        int count = 0; run[0] = left;

        // Check if the array is nearly sorted
        for (int k = left; k < right; run[count] = k) {
            if (a[k] < a[k + 1]) { // ascending
                while (++k <= right && a[k - 1] <= a[k]);
            } else if (a[k] > a[k + 1]) { // descending
                while (++k <= right && a[k - 1] >= a[k]);
                for (int lo = run[count] - 1, hi = k; ++lo < --hi; ) {
                    int t = a[lo]; a[lo] = a[hi]; a[hi] = t;
                }
            } else { // equal
                for (int m = MAX_RUN_LENGTH; ++k <= right && a[k - 1] == a[k]; ) {
                    if (--m == 0) {
                        dualPivotQuicksort(a, left, right, true);
                        return;
                    }
                }
            }

            /*
             * The array is not highly structured,
             * use Quicksort instead of merge sort.
             */
            if (++count == MAX_RUN_COUNT) {
                dualPivotQuicksort(a, left, right, true);
                return;
            }
        }

        // Check special cases
        // Implementation note: variable "right" is increased by 1.
        if (run[count] == right++) { // The last run contains one element
            run[++count] = right;
        } else if (count == 1) { // The array is already sorted
            return;
        }

        // Determine alternation base for merge
        byte odd = 0;
        for (int n = 1; (n <<= 1) < count; odd ^= 1);

        // Use or create temporary array b for merging
        int[] b;                 // temp array; alternates with a
        int ao, bo;              // array offsets from 'left'
        int blen = right - left; // space needed for b
        if (work == null || workLen < blen || workBase + blen > work.length) {
            work = new int[blen];
            workBase = 0;
        }
        if (odd == 0) {
            System.arraycopy(a, left, work, workBase, blen);
            b = a;
            bo = 0;
            a = work;
            ao = workBase - left;
        } else {
            b = work;
            ao = 0;
            bo = workBase - left;
        }

        // Merging
        for (int last; count > 1; count = last) {
            for (int k = (last = 0) + 2; k <= count; k += 2) {
                int hi = run[k], mi = run[k - 1];
                for (int i = run[k - 2], p = i, q = mi; i < hi; ++i) {
                    if (q >= hi || p < mi && a[p + ao] <= a[q + ao]) {
                        b[i + bo] = a[p++ + ao];
                    } else {
                        b[i + bo] = a[q++ + ao];
                    }
                }
                run[++last] = hi;
            }
            if ((count & 1) != 0) {
                for (int i = right, lo = run[count - 1]; --i >= lo;
                     b[i + bo] = a[i + ao]
                );
                run[++last] = right;
            }
            int[] t = a; a = b; b = t;
            int o = ao; ao = bo; bo = o;
        }
    }

    /**
     * <b> 对于基本类型的数组，Java采用的算法是双枢轴快速排序（Dual-PivotQuicksort） </b>
     *
     * <p> 双枢轴快速排序是对快速排序的优化。
     *
     * @param a 要排序的数组
     * @param left 要排序的第一个元素（包含）的索引
     * @param right 要排序的最后一个元素（包含）的索引
     * @param leftmost 指示此部分是否在范围中最左侧
     */
    private static void dualPivotQuicksort(int[] a, int left, int right, boolean leftmost) {
        rangeCheck(a.length, left, right);
        int length = right - left + 1;

        // 在小数组上使用插入排序
        if (length < INSERTION_SORT_THRESHOLD) {
            insertionOptimize(a, left, right, leftmost);
            return;
        }

        // Inexpensive approximation of length / 7
        int seventh = (length >> 3) + (length >> 6) + 1;

        /*
         * Sort five evenly spaced elements around (and including) the
         * center element in the range. These elements will be used for
         * pivot selection as described below. The choice for spacing
         * these elements was empirically determined to work well on
         * a wide variety of inputs.
         */
        int e3 = (left + right) >>> 1; // The midpoint
        int e2 = e3 - seventh;
        int e1 = e2 - seventh;
        int e4 = e3 + seventh;
        int e5 = e4 + seventh;

        // Sort these elements using insertion sort
        if (a[e2] < a[e1]) { int t = a[e2]; a[e2] = a[e1]; a[e1] = t; }

        if (a[e3] < a[e2]) { int t = a[e3]; a[e3] = a[e2]; a[e2] = t;
            if (t < a[e1]) { a[e2] = a[e1]; a[e1] = t; }
        }
        if (a[e4] < a[e3]) { int t = a[e4]; a[e4] = a[e3]; a[e3] = t;
            if (t < a[e2]) { a[e3] = a[e2]; a[e2] = t;
                if (t < a[e1]) { a[e2] = a[e1]; a[e1] = t; }
            }
        }
        if (a[e5] < a[e4]) { int t = a[e5]; a[e5] = a[e4]; a[e4] = t;
            if (t < a[e3]) { a[e4] = a[e3]; a[e3] = t;
                if (t < a[e2]) { a[e3] = a[e2]; a[e2] = t;
                    if (t < a[e1]) { a[e2] = a[e1]; a[e1] = t; }
                }
            }
        }

        // Pointers
        int less  = left;  // The index of the first element of center part
        int great = right; // The index before the first element of right part

        if (a[e1] != a[e2] && a[e2] != a[e3] && a[e3] != a[e4] && a[e4] != a[e5]) {
            /*
             * Use the second and fourth of the five sorted elements as pivots.
             * These values are inexpensive approximations of the first and
             * second terciles of the array. Note that pivot1 <= pivot2.
             */
            int pivot1 = a[e2];
            int pivot2 = a[e4];

            /*
             * The first and the last elements to be sorted are moved to the
             * locations formerly occupied by the pivots. When partitioning
             * is complete, the pivots are swapped back into their final
             * positions, and excluded from subsequent sorting.
             */
            a[e2] = a[left];
            a[e4] = a[right];

            /*
             * Skip elements, which are less or greater than pivot values.
             */
            while (a[++less] < pivot1);
            while (a[--great] > pivot2);

            /*
             * Partitioning:
             *
             *   left part           center part                   right part
             * +--------------------------------------------------------------+
             * |  < pivot1  |  pivot1 <= && <= pivot2  |    ?    |  > pivot2  |
             * +--------------------------------------------------------------+
             *               ^                          ^       ^
             *               |                          |       |
             *              less                        k     great
             *
             * Invariants:
             *
             *              all in (left, less)   < pivot1
             *    pivot1 <= all in [less, k)     <= pivot2
             *              all in (great, right) > pivot2
             *
             * Pointer k is the first index of ?-part.
             */
            outer:
            for (int k = less - 1; ++k <= great; ) {
                int ak = a[k];
                if (ak < pivot1) { // Move a[k] to left part
                    a[k] = a[less];
                    /*
                     * Here and below we use "a[i] = b; i++;" instead
                     * of "a[i++] = b;" due to performance issue.
                     */
                    a[less] = ak;
                    ++less;
                } else if (ak > pivot2) { // Move a[k] to right part
                    while (a[great] > pivot2) {
                        if (great-- == k) {
                            break outer;
                        }
                    }
                    if (a[great] < pivot1) { // a[great] <= pivot2
                        a[k] = a[less];
                        a[less] = a[great];
                        ++less;
                    } else { // pivot1 <= a[great] <= pivot2
                        a[k] = a[great];
                    }
                    /*
                     * Here and below we use "a[i] = b; i--;" instead
                     * of "a[i--] = b;" due to performance issue.
                     */
                    a[great] = ak;
                    --great;
                }
            }

            // Swap pivots into their final positions
            a[left]  = a[less  - 1]; a[less  - 1] = pivot1;
            a[right] = a[great + 1]; a[great + 1] = pivot2;

            // Sort left and right parts recursively, excluding known pivots
            dualPivotQuicksort(a, left, less - 2, leftmost);
            dualPivotQuicksort(a, great + 2, right, false);

            /*
             * If center part is too large (comprises > 4/7 of the array),
             * swap internal pivot values to ends.
             */
            if (less < e1 && e5 < great) {
                /*
                 * Skip elements, which are equal to pivot values.
                 */
                while (a[less] == pivot1) {
                    ++less;
                }

                while (a[great] == pivot2) {
                    --great;
                }

                /*
                 * Partitioning:
                 *
                 *   left part         center part                  right part
                 * +----------------------------------------------------------+
                 * | == pivot1 |  pivot1 < && < pivot2  |    ?    | == pivot2 |
                 * +----------------------------------------------------------+
                 *              ^                        ^       ^
                 *              |                        |       |
                 *             less                      k     great
                 *
                 * Invariants:
                 *
                 *              all in (*,  less) == pivot1
                 *     pivot1 < all in [less,  k)  < pivot2
                 *              all in (great, *) == pivot2
                 *
                 * Pointer k is the first index of ?-part.
                 */
                outer:
                for (int k = less - 1; ++k <= great; ) {
                    int ak = a[k];
                    if (ak == pivot1) { // Move a[k] to left part
                        a[k] = a[less];
                        a[less] = ak;
                        ++less;
                    } else if (ak == pivot2) { // Move a[k] to right part
                        while (a[great] == pivot2) {
                            if (great-- == k) {
                                break outer;
                            }
                        }
                        if (a[great] == pivot1) { // a[great] < pivot2
                            a[k] = a[less];
                            /*
                             * Even though a[great] equals to pivot1, the
                             * assignment a[less] = pivot1 may be incorrect,
                             * if a[great] and pivot1 are floating-point zeros
                             * of different signs. Therefore in float and
                             * double sorting methods we have to use more
                             * accurate assignment a[less] = a[great].
                             */
                            a[less] = pivot1;
                            ++less;
                        } else { // pivot1 < a[great] < pivot2
                            a[k] = a[great];
                        }
                        a[great] = ak;
                        --great;
                    }
                }
            }

            // Sort center part recursively
            dualPivotQuicksort(a, less, great, false);

        } else { // Partitioning with one pivot
            /*
             * Use the third of the five sorted elements as pivot.
             * This value is inexpensive approximation of the median.
             */
            int pivot = a[e3];

            /*
             * Partitioning degenerates to the traditional 3-way
             * (or "Dutch National Flag") schema:
             *
             *   left part    center part              right part
             * +-------------------------------------------------+
             * |  < pivot  |   == pivot   |     ?    |  > pivot  |
             * +-------------------------------------------------+
             *              ^              ^        ^
             *              |              |        |
             *             less            k      great
             *
             * Invariants:
             *
             *   all in (left, less)   < pivot
             *   all in [less, k)     == pivot
             *   all in (great, right) > pivot
             *
             * Pointer k is the first index of ?-part.
             */
            for (int k = less; k <= great; ++k) {
                if (a[k] == pivot) {
                    continue;
                }
                int ak = a[k];
                if (ak < pivot) { // Move a[k] to left part
                    a[k] = a[less];
                    a[less] = ak;
                    ++less;
                } else { // a[k] > pivot - Move a[k] to right part
                    while (a[great] > pivot) {
                        --great;
                    }
                    if (a[great] < pivot) { // a[great] <= pivot
                        a[k] = a[less];
                        a[less] = a[great];
                        ++less;
                    } else { // a[great] == pivot
                        /*
                         * Even though a[great] equals to pivot, the
                         * assignment a[k] = pivot may be incorrect,
                         * if a[great] and pivot are floating-point
                         * zeros of different signs. Therefore in float
                         * and double sorting methods we have to use
                         * more accurate assignment a[k] = a[great].
                         */
                        a[k] = pivot;
                    }
                    a[great] = ak;
                    --great;
                }
            }

            /*
             * Sort left and right parts recursively.
             * All elements from center part are equal
             * and, therefore, already sorted.
             */
            dualPivotQuicksort(a, left, less - 1, leftmost);
            dualPivotQuicksort(a, great + 1, right, false);
        }
    }

    /**
     * <b> 测试数组元素是否有序 </b>
     *
     * @param a 要被测试的数组
     * @return 有序返回true，无序返回false
     */
    public static boolean isSorted(int[] a){
        if (a==null || a.length==0 || a.length==1){
            return true;
        }
        for (int i=0; i<a.length-1; i++){
            if (a[i] > a[i+1]){
                return false;
            }
        }
        return true;
    }

    /**
     * <b> 堆排序 </b>
     *
     * <p> 堆排序在排序复杂性的研究中有着重要的地位，因为它是我们所知的唯一能够同时最优地利用空间和时间的方法
     * ——在最坏的情况下它也能保证使用～2NlgN次比较和恒定的额外空间。当空间十分紧张的时候（例如在嵌入式系统或低成本的移动设备中）它很流行，
     * 因为它只用几行就能实现（甚至机器码也是）较好的性能。
     * 但现代系统的许多应用很少使用它，因为它无法利用缓存。数组元素很少和相邻的其他元素进行比较，
     * 因此缓存未命中的次数要远远高于大多数比较都在相邻元素间进行的算法，如快速排序、归并排序，甚至是希尔排序。
     *
     * <p> 另一方面，用堆实现的优先队列在现代应用程序中越来越重要，因为它能在插入操作和删除最大元素操作混合的动态场景中保证对数级别的运行时间。
     *
     * <p> 问 ：我还是不明白优先队列是做什么用的。为什么我们不直接把元素排序然后再一个个地引用有序数组中的元素？
     * <p> 答 ：在某些数据处理的例子里，比如TopM和Multiway，总数据量太大，无法排序（甚至无法全部装进内存）。
     * 如果你需要从10亿个元素中选出最大的十个，你真的想把一个10亿规模的数组排序吗？
     * 但有了优先队列，你就只用一个能存储十个元素的队列即可。
     * 在其他的例子中，我们甚至无法同时获取所有的数据，因此只能先从优先队列中取出并处理一部分，然后再根据结果决定是否向优先队列中添加更多的数据。
     *
     * @see PriorityQueue
     * @param a list
     * @param <E> 泛型，参数类型
     */
    public <E extends Comparable<E>> void heapSort(ArrayList<E> a){
        int size = a.size();
        for (int k=size/2; k>=1; k--){
            sink(a, k, size);
        }
        while (size > 1){
            exchange(a, 1, size--);
            sink(a, 1, size);
        }
    }

    private <E extends Comparable<E>> void sink(ArrayList<E> a, int k, int j){
    }

    // 统计数组a[]中不重复元素的个数
    public static int notRepeatedNum(int[] a){
        dualPivotQuicksort(a, 0, a.length-1, true);
        int count = 0;
        for (int i=0; i<a.length-1; i++){
            if (a[i]!=a[i+1]){
                count++;
            }
        }
        return count;
    }

    // 找到一组数中的第k小的元素
    public static int kthSmallest(int[] a, int k){
        int lo = 0, hi = a.length-1;
        while (hi > lo){
            // int j = quick3wayPartition(a, lo, hi)[0];
            int j = partition(a, lo, hi);
            if (j == k) return a[k];
            else if (j > k) hi = j-1;
            else lo = j +1;
        }

        return a[k];
    }

    /**
     * <b> 交换数组a[]中下标为i和j的元素 </b>
     *
     * @param a 数组
     * @param i 下标i
     * @param j 下标j
     */
    private static void exchange(int[] a, int i, int j){
        int temp = a[i];
        a[i] = a[j];
        a[j] = temp;
    }


    private static <E> void exchange(List<E> a, int i, int j){
        E temp = a.get(i);
        a.set(i, a.get(j));
        a.set(j, temp);
    }

    private static <E extends Comparable<E>> boolean less(List<E> a, int left, int right){
        return a.get(left).compareTo(a.get(right)) < 0;
    }

    /**
     * <b> 检查数组下标fromIndex和toIndex是否在范围内，如果不在范围内，则引发异常。 </b>
     *
     * @param arrayLength 数组长度
     * @param fromIndex 开始下标
     * @param toIndex 结束下标
     */
    private static void rangeCheck(int arrayLength, int fromIndex, int toIndex) {
        if (fromIndex > toIndex) {
            throw new IllegalArgumentException(
                    "fromIndex(" + fromIndex + ") > toIndex(" + toIndex + ")");
        }
        if (fromIndex < 0) {
            throw new ArrayIndexOutOfBoundsException(fromIndex);
        }
        if (toIndex > arrayLength) {
            throw new ArrayIndexOutOfBoundsException(toIndex);
        }
    }
}
