package com.dongfeng.study.basicstudy;

import java.util.UUID;

/**
 * @author eastFeng
 * @date 2020/3/16 - 10:03
 */
public class Tings {
    public static String s;
    static {
        System.out.println("父类静态代码块，s="+s);
        s = "staticS";
    }


    /**
     * 物品唯一id
     */
    private Integer id;

    /**
     * 最好将类中的域标记为private，而方法标记为public。
     * 任何声明为private的内容对其他类都是不可见的。这对于子类来说也完全适用，即子类也不能访问超类的私有域。
     * 然而，在有些时候，望超类中的某些方法允许被子类访问，或允许子类的方法访问超类的某个域。为此，需要将这些方法或域声明为protected
     */
    protected String uuid = UUID.randomUUID().toString().replace("-", "").toLowerCase();

    public Tings(){}

    public Tings(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public String getUuid(){
        return uuid;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Tings{" +
                "id=" + id +
                '}';
    }
}
