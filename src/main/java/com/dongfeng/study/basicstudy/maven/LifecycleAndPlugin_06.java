package com.dongfeng.study.basicstudy.maven;

/**
 *
 * Maven生命周期（LifeCycle） 和 Maven插件（plugin）
 *
 * @author eastFeng
 * @date 2022-12-01 1:30
 */
public class LifecycleAndPlugin_06 {

    /**
     * 一.生命周期（LifeCycle）
     * <p> 1.生命周期是指项目构建的生命周期，一个项目构建要经过的各个阶段（phase）。
     *
     * <p> Maven的生命周期，IDEA中（IDEA界面左侧栏）会显示（很少，也是很重要）一部分阶段：
     * clean validate compile test package verify install site deploy
     *
     * <p> 早期项目构建，方式各不相同，Maven抽取了大量经验，总结了一套规范的项目构建过程，即项目构建的各个阶段。
     *
     * <p> 2.Maven生命周期分为3个/套/部分：clean周期，default周期，site周期
     * <ul>
     *     <li>clean周期：负责清理项目（清理上次编译的文件）</li>
     *     <li>default周期：主体周期，负责完成项目构建的主体过程</li>
     *     <li>site周期：建立站点（生成站点文章，发送站点到服务器）</li>
     * </ul>
     *
     * <p> 3.每个生命周期都分为（包含）一系列阶段（phase）
     * <ul>
     *     <li>clean周期：pre-clean、clean、post-clean三个阶段</li>
     *     <li>default周期：validate、initialize、generate-source、...
     *     compile、...package、...install、site、deploy多个阶段</li>
     *     <li>site周期：pre-site、site、post-site、site-deploy四个阶段。
     *     site负责生成站点、文档，site-deploy负责发送站点到服务器。</li>
     * </ul>
     *
     * <p> 4.执行方式：mvn phase名
     *
     * <p> 5.执行原则：周期独立，阶段顺序依赖
     *
     * <p> 二. 插件（plugin）
     * <p> 1.Maven中的生命周期都是抽象的，真正的工作都由插件来实现。
     * <p> 我们在输入mvn命令的时候，比如mvn clean，clean对应的就是Clean生命周期中的clean阶段。
     *     但是clean的具体操作是由maven-clean-plugin来实现的。
     *     还比如：compile阶段由maven-compile-plugin插件来实现，package阶段由maven-jar-plugin插件来实现。
     * <p> 2.Maven生命周期和Maven插件的关系类似于接口与实现类的关系，这样就提供了很好的扩展性。
     *     这些phase就相当于Maven提供的统一的接口，然后这些phase的实现由Maven的插件来完成。
     *     在有限的生命周期上可以定制无限的插件实现。
     * <p> 3.Maven为大多数的阶段，绑定了默认插件。比如compile阶段、package阶段等等。
     * <p> 生命周期和插件密不可分，密切配合。
     * <p> 4.每个插件，都有很多目标（goal），插件和目标的关系有一个对应关系：goal of plugin ==> phase of lifecycle
     * <p> 5.maven调用plugin的两种方式
     * <ul>
     *     <li>
     *         在命令行中直接指定插件（plugin）和目标（goal）：mvn [plugin-name]:[goal-name]。
     *         注：这种带冒号的调用方式和生命周期无关。
     *     </li>
     *     <li>
     *         将插件的某一个或者多个目标（goal）和阶段（phase）进行绑定，这样，
     *         在maven执行该phase时就会执行该plugin的该goal。
     *     </li>
     * </ul>
     * <p> 新增一个Maven插件是在pom.xml中使用build元素中的plugins元素中配置的。
     *     将插件的某一个或者多个目标（goal）和阶段（phase）进行绑定也是在plugins元素中配置的。
     * <p> 可以将插件中的多个目标（goal）绑定到某个阶段（phase），当执行某个阶段（phase）时，绑定到阶段（phase）
     *     的所有目标都会执行工作。
     */
    public static void main(String[] args) {
    }
}
