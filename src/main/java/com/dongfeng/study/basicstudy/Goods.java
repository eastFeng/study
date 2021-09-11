package com.dongfeng.study.basicstudy;

import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.Date;

/**
 * <p> 关键字extends表示继承。一个类最多只能有一个父类；
 *
 * <p> 尽管在Goods类中没有显式地定义getId方法，但属于Goods类的对象却可以使用它们，这是因为Goods类自动地继承了超类Tings中的方法。
 * <p> 同样，从超类中还继承了id域。
 *
 * <p> 在通过扩展超类定义子类的时候，仅需要指出子类与超类的不同之处。
 * 因此在设计类的时候，应该将通用的方法放在超类中，而将具有特殊用途的方法放在子类中，
 * 这种将通用的功能放到超类的做法，在面向对象程序设计中十分普遍。
 *
 * <p> 在子类中可以增加域、增加方法或覆盖超类的方法，然而绝对不能删除继承的任何域和方法。
 *
 * <p> Cloneable接口指示一个类提供了一个安全的clone方法。
 * Cloneable接口是Java提供的一组标记接口（tagging interface）之一。
 * 标记接口不包含任何方法；它唯一的作用就是允许在类型查询中使用instanceof。
 *
 * @author eastFeng
 * @date 2020/3/15 - 14:52
 */
public class Goods extends Tings implements Serializable,Cloneable {
    private static final long serialVersionUID = 3931346938947413149L;

    private String type;

    private String name;

    private Integer number;

    private double price;

    private Boolean testB = false;

    private Date date;

    public Goods(){
        super();
    }

    public Goods(Integer id) {
        super(id);
    }

    public Goods(Integer id, String type, String name, Integer number, double price) {
        /*
         * 由于Goods类的构造器不能访问父类Tings的私有域，所以必须利用Tings类的构造器对这部分私有域进行初始化，
         * 我们可以通过super实现对超类构造器的调用。使用super调用构造器的语句必须是子类构造器的第一条语句。
         *
         * 如果子类的构造器没有显式地调用超类的构造器，则将自动地调用超类默认（没有参数）的构造器。
         * 如果超类没有不带参数的构造器，并且在子类的构造器中又没有显式地调用超类的其他构造器，则Java编译器将报告错误。
         */
        // 构造参数（id,type,name,number,price）既可以传递给本类（this）的其他构造器，也可以传递给超类（super）的构造器。
        super(id);
        this.type = type;
        this.name = name;
        this.number = number;
        this.price = price;
    }

    /**
     * <p> Object类的clone方法是浅拷贝：没有克隆对象中引用的其他对象。
     * <p> 如果对象中的所有数据域都是数值或其他基本类型，拷贝这些域没有任何问题。
     * 但是如果对象包含子对象的引用，拷贝域就会得到相同子对象的另一个引用，这样一来，原对象和克隆的对象仍然会共享一些信息。
     *
     * <p> 如果原对象和浅克隆对象共享的子对象是不可变的，那么这种共享（浅拷贝）就是安全的。
     * <p> 不过，通常子对象都是可变的，必须重新定义clone方法来建立一个深拷贝，同时克隆所有子对象。
     */
    @Override
    public Object clone() throws CloneNotSupportedException {
        Goods clone = (Goods) super.clone();

        return clone;
    }


    public String myUuid(Object obj){
        String myUuid = this.uuid;
        if (obj == this){
            return myUuid;
        }
        if (obj instanceof Goods){
            String anotherUuid = ((Goods) obj).uuid;
            return myUuid + "_" + anotherUuid;
        }
        if (obj instanceof Good2){
            String uuid = ((Good2) obj).uuid;

        }
        return StringUtils.EMPTY;
    }

    public String getNameId(){
        // return name + "_" + id     ---> 会报错
        /*
         * 这是因为Goods类的getNameId方法不能够直接地访问超类Tings的私有域。
         * 也就是说，尽管每个Goods对象都拥有一个名为id的域，但在Goods类的getNameId方法中并不能够直接地访问salary域。
         * 只有Tings类的方法才能够访问私有部分。
         * 如果Goods类的方法一定要访问超类私有域，就必须借助于公有的接口，Tings类中的公有方法getId()正是这样一个接口。
         */
        return name + "_" + super.getId();
        /*
         * 关键字this有两个用途：一是引用隐式参数，二是调用该类其他的构造器。
         * 同样，super关键字也有两个用途：一是调用超类的方法，二是调用超类的构造器。
         */
    }

    public Boolean getTestB() {
        return testB;
    }

    public void setTestB(Boolean testB) {
        this.testB = testB;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "Goods{" +
                "type='" + type + '\'' +
                ", name='" + name + '\'' +
                ", number=" + number +
                ", price=" + price +
                '}';
    }
}
