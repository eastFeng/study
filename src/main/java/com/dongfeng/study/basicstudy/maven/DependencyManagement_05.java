package com.dongfeng.study.basicstudy.maven;

/**
 * 依赖管理：依赖配置 依赖传递 依赖冲突 排除依赖
 *
 * @author eastFeng
 * @date 2022-11-30 22:17
 */
public class DependencyManagement_05 {

    /**
     * 一.依赖配置
     * <p> 依赖指当前项目运行所需的jar，一个项目可以设置多个依赖。
     *    项目所需依赖在pom.xml文件中进行配置。
     *
     * <p> 二.依赖传递
     * <p> 1.依赖具有传递性
     * <ul>
     *     <li>直接依赖：在当前项目中通过依赖配置建立的依赖关系</li>
     *     <li>间接依赖：被资源的资源如果依赖其他资源，当前项目间接依赖其他资源</li>
     * </ul>
     * 比如：在项目中导入了依赖A，而A内部依赖了B，则A自动出发B也被导入项目。
     * <p> 2.传递性的好处和坏处
     * <ul>
     *     <li>好处：没必要导入所有依赖在项目中，传递性会自动导入一些依赖。简化依赖导入管理</li>
     *     <li>坏处：依赖冲突（jar冲突）</li>
     * </ul>
     *
     * 三.依赖冲突
     * <p> 1.什么是依赖冲突？
     * <p> 依赖冲突是指项目依赖的某一个jar包，有多个不同的版本，因而造成包版本冲突。
     *
     * <p> 2.依赖冲突的原因？
     * <p> 依赖冲突经常是jar包之间的间接依赖引起的。
     *    每个显式声明的类包都会依赖于一些其它的隐式jar包，这些隐式的jar包会被maven间接引入进来，从而造成jar包冲突。
     * 比如：
     * <ul>
     *     <li>依赖链路1：A ——依赖——> B ——依赖——> X（版本1.1）</li>
     *     <li>依赖链路2：A ——依赖——> D ——依赖——> X（版本1.2）</li>
     * </ul>
     * 此时项目A中就产生了依赖冲突（X）。
     *
     * <p> 3.如何解决依赖冲突？
     * <p> Maven依赖有两个原则（也是解决依赖冲突的两个方法）：
     * <p> 1）路径优先：当依赖中出现相同的资源时，层级越深，优先级越低，层级越浅，优先级越高。
     * 比如：
     * <ul>
     *     <li>依赖链路1：A ——依赖——> B ——依赖——> C——依赖——> X（版本1.1）</li>
     *     <li>依赖链路2：A ——依赖——> D ——依赖——> X（版本1.2）</li>
     * </ul>
     * 该例中，项目A依赖的X（版本1.1）的路径长度为3，而X（版本1.2）的路径长度为2，因此X（版本1.2）会被解析使用。
     * <p> 2）声明优先：当资源在相同层级被依赖时，在POM中依赖声明的顺序决定了谁会被解析使用，
     *               声明/配置顺序靠前的覆盖声明/配置顺序靠后的。
     * 比如：
     * <ul>
     *     <li>依赖链路1：A ——依赖——> B ——依赖——> Y（版本1.0）</li>
     *     <li>依赖链路2：A ——依赖——> D ——依赖——> Y（版本2.0）</li>
     * </ul>
     * Y(1.0)和Y(2.0)的依赖路径长度是一样的，都为2。如果B的依赖声明在D之前，那么Y（版本1.0）就会被解析使用。
     * <p>【注意】子pom内声明的依赖优先于父pom中的依赖。
     *
     * <p> 4.默认的两个选择原则明显是不能复合实际需求的，所以有其他设置可以改默认原则：</p>
     * <p> 【排除依赖 exclusion】
     * <p> 在pom.xml文件中，exclusions可以包含一个或者多exclusion子元素，因此可以排除一个或者多个传递性依赖。
     * <p> 查看冲突：IDEA通过Maven Helper插件来检查依赖冲突。
     * Maven Helper插件安装成功之后，点开pom.xml文件，会在窗口下方看到Dependency Analyzer标志。
     * 关于Maven Helper插件的使用百度就行，很方便。
     *
     */

    public static void main(String[] args) {
    }
}
