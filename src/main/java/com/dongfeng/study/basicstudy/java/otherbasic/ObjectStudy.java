package com.dongfeng.study.basicstudy.java.otherbasic;

/**
 * <b> {@link Object} 学习 </b>
 *
 * @author eastFeng
 * @date 2021-04-20 15:11
 */
public class ObjectStudy {
    public static void main(String[] args) {
        /*
         * Object类是Java中所有类的始祖，在Java中每个类都是由它扩展而来的。
         * 如果没有明确地指出超类，Object就被认为是这个类的超类。
         */

        // 可以使用Object类型的变量引用任何类型的对象：
        Object obj = new Tings(31654);

        // 当然，Object类型的变量只能用于作为各种值的通用持有者。
        // 要想对其中的内容进行具体的操作，还需要清楚对象的原始类型，并进行相应的类型转换：
        Tings t = (Tings) obj;
    }


    @Override
    public boolean equals(Object otherObject){
        /*
         * Object类中的equals方法用于检测一个对象是否等于另外一个对象。在Object类中，这个方法将判断两个对象是否具有相同的引用。
         *
         * Java语言规范要求equals方法具有下面的特性：
         * 1）自反性：对于任何非空引用x, x.equals(x)应该返回true。
         * 2）对称性：对于任何引用x和y，当且仅当y.equals(x)返回true, x.equals(y)也应该返回true。
         * 3）传递性：对于任何引用x、y和z，如果x.equals(y)返回true, y.equals(z)返回true, x.equals(z)也应该返回true。
         * 4）一致性：如果x和y引用的对象没有发生变化，反复调用x.equals(y)应该返回同样的结果。
         * 5）对于任意非空引用x, x.equals(null)应该返回false。
         *
         * 下面给出编写一个完美的equals方法的建议：
         * 1）显式参数命名为otherObject，稍后需要将它转换成另一个叫做other的变量。
         * 2）检测this与otherObject是否引用同一个对象：if(this == otherObject) return true;
         * 3）检测otherObject是否为null，如果为null，返回false。这项检测是很必要的。
         *    if (otherObject == null) return false;
         * 4）比较this与otherObject是否属于同一个类。
         *    如果equals的语义在每个子类中有所改变，就使用getClass检测：
         *    if (getClass() != otherObject.getClass()) return false;
         *    如果equals在所有的子类都拥有统一的语义，就使用instanceof检测：
         *    if (!(otherObject instanceof ObjectStudy)) return false
         * 5）将otherObject转换为相应的类类型变量：
         *    ClassName other = (ClassName) otherObject;
         * 6）现在开始对所有需要比较的域进行比较了。使用==比较基本类型域，使用equals比较对象域。如果所有的域都匹配，就返回true；否则返回false。
         */

        // 检测this与otherObject是否引用同一个对象
        if (this == otherObject){
            return true;
        }

        // 检测otherObject是否为null，如果为null，返回false
        if (otherObject == null){
            return false;
        }

        // 比较this与otherObject是否属于同一个类
        // ● 如果子类能够拥有自己的相等概念，则对称性需求将强制采用getClass进行检测。
        // ● 如果由超类决定相等的概念，那么就可以使用instanceof进行检测，这样可以在不同子类的对象之间进行相等的比较。（比如Map的equals方法）
        // 1. 如果equals的语义在每个子类中有所改变，就使用getClass检测
//        if (getClass() != otherObject.getClass()){
//            return false;
//        }
        // 2. 如果equals在所有的子类都拥有统一的语义，就使用instanceof检测
        if (!(otherObject instanceof ObjectStudy)){
            return false;
        }

        ObjectStudy other = (ObjectStudy) otherObject;

        return true;
    }

    /**
     * <b> 散列码（hash code）是由对象导出的一个整型值。 </b>
     *
     * <p> hashCode返回对象的哈希值。哈希值可以是任意的整数，包括正数或负数。两个相等的对象要求返回相等的散列码。
     * <p> 字符串的哈希值是由内容导出的。在StringBuffer类中没有定义hashCode方法，它的散列码是由Object类的默认hashCode方法导出的对象存储地址。
     * <p> 如果重新定义equals方法，就必须重新定义hashCode方法，以便用户可以将对象插入到散列表中。
     * <p> equals与hashCode的定义必须一致：如果x.equals(y)返回true，那么x.hashCode( )就必须与y.hashCode( )具有相同的值。
     *
     * @see String#hashCode()
     * @return hashCode
     */
    @Override
    public int hashCode(){

        /*
         * hashCode和equals方法联系密切，【对两个对象，如果equals方法返回true，则hashCode也必须一样。反之不要求】，
         * equal方法返回false时，hashCode可以一样，也可以不一样，但应该尽量不一样。
         *
         * 1）hashCode在哈希表中起作用，如java.util.HashMap。
         * 2）如果对象在equals中使用的信息都没有改变，那么hashCode值始终不变。
         * 3）如果两个对象使用equals方法返回true，则hashCode必须相等。
         * 4）如果两个对象使用equals方法返回false，则不要求hashCode也必须不相等；
         *    但是不相等的对象产生不相同的hashCode可以提高哈希表的性能。
         *
         * hashCode的作用：总的来说，hashCode在哈希表中起作用，如HashSet、HashMap等。
         * 当我们向哈希表(如HashSet、HashMap等)中添加对象时，首先调用hashCode方法计算对象的哈希值，
         * 通过哈希值可以直接定位对象在哈希表中的位置(一般是哈希值对哈希表大小取余)。
         * 如果该位置没有对象，可以直接将该对象插入该位置；
         * 如果该位置有对象(可能有多个，通过链表实现)，则调用equals方法比较这些对象与object是否相等，
         * 如果相等，则不需要保存object；如果不相等，则将该对象加入到链表中。
         * 这也就解释了为什么equals相等，则hashCode必须相等。
         * 如果两个对象equals相等，则它们在哈希表(如HashSet、HashMap等)中只应该出现一次；
         * 但是如果hashCode不相等，那么它们会被散列到哈希表的不同位置，会在哈希表中出现不止一次。
         */

        // 下面是String类的hashCode方法：
        // public int hashCode() {
        //        // String类将hashCode()的结果缓存为hash值，提高性能。hash是一个实例域。
        //        int h = hash;
        //        if (h == 0 && value.length > 0) {
        //            char val[] = value;
        //
        //            for (int i = 0; i < value.length; i++) {
        //                h = 31 * h + val[i];
        //                // 关于String类的hashCode()计算过程中，为什么使用了数字31，主要有以下原因：
        //                // 1）使用质数计算哈希值，由于质数的特性，它与其他数字相乘之后，计算结果唯一的概率更大，哈希冲突的概率更小。
        //                // 2）使用的质数越大，哈希冲突的概率越小，但是计算的速度也越慢；31是哈希冲突和性能的折中，实际上是实验观测的结果。
        //                // 3）JVM会自动对31进行优化：31 * i == (i << 5) – i
        //            }
        //            hash = h;
        //        }
        //        return h;
        //    }

        /*
         * 重写hashCode需要遵守以下原则：
         * 1）如果重写了equals方法，检查条件“两个对象使用equals方法判断为相等，则hashCode方法也应该相等”是否成立，
         *    如果不成立，则重写hashCode方法。
         * 2）hashCode方法不能太过简单，否则哈希冲突过多。
         * 3）hashCode方法不能太过复杂，否则计算复杂度过高，影响性能。
         */
        return super.hashCode();
    }

    /**
     * 它用于返回表示对象值的字符串。
     * <p> 返回对象的字符串表示形式。通常，toString方法返回一个字符串，该字符串“以文本形式表示”这个对象。
     * 结果应该是一个简洁，但信息丰富的代表，是一个人容易阅读。建议所有子类重写此方法。
     *
     * <p> 绝大多数（但不是全部）的toString方法都遵循这样的格式：类的名字，随后是一对方括号括起来的域值。
     * 实际上，还可以设计得更好一些。最好通过调用getClass( ).getName( )获得类名的字符串，而不要将类名硬加到toString方法中。
     *
     * @return 对象的字符串表示形式。
     */
    @Override
    public String toString(){
        return super.toString();
    }

    /*
     * Object类中的getClass方法是final的，不能重写的：public final native Class<?> getClass();
     * 返回包含对象信息的类对象。
     */


}
