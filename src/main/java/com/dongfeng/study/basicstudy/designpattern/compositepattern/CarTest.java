package com.dongfeng.study.basicstudy.designpattern.compositepattern;

/**
 * @author eastFeng
 * @date 2021-01-27 14:22
 */
public class CarTest {
    public static void main(String[] args) {

        BlackColor blackColor = new BlackColor();
        RedColor redColor = new RedColor();

        // blackColor 是  Color类型的变量

        ElectricCar electricCar = new ElectricCar();
        electricCar.setCarColor(blackColor);

        System.out.println("电动车颜色："+ electricCar.getCarColor());


        // MethodUtil 测试
//        Class<ElectricCar> aClass = ElectricCar.class;
//        Method[] methods = aClass.getMethods();
//        for (Method method : methods) {
//            System.out.println(MethodUtil.resolveMethodName(method));
//        }


    }
}
