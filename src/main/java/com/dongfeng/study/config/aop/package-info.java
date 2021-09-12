/**
 *
 * <b>  5. Aspect Oriented Programming with Spring （基于Spring的面向切面编程）</b>
 *
 * <p> 面向切面编程（Aspect-oriented Programming, AOP）通过另一种思考程序结构的方式，对面向对象编程（Object-oriented Programming, OOP）进行了补充。
 * 在OOP中，模块化的关键单元是类（class），而在AOP中，模块化的单元是切面（aspect）。切面支持跨多个类型和对象的关注点（如事务管理）的模块化。
 *
 * <p> Spring的关键组件之一就是AOP框架。虽然Spring IoC（Inversion of Control控制反转）容器不依赖于AOP，但是AOP补充了Spring IoC，提供了非常强大的中间件解决方案。
 *
 * <p> @AspectJ annotation style
 *
 *
 * <p> AOP 在Spring框架中用于:
 * <pre>
 * 1. 提供声明式企业服务。最重要的此类服务是声明式事务管理（declarative transaction management）。
 * 2. 让用户自定义切面，用AOP补充他们对OOP的使用。
 * </pre>
 *
 * <p><b> 5.1. AOP Concepts（AOP 概念） </b>
 * <p> 1. Aspect（切面）：跨多个关注点的模块化。事务管理是企业Java应用程序中横切关注点的一个很好的例子。
 * 在Spring AOP中，切面是通过正则类（基于模式的方法）或者用@Aspect注解（AspectJ样式风格）来注解的正则类来实现的。
 *
 * <p> 2. Join point（连接点）：程序执行过程中的一个点，如方法的执行或异常的处理。<b>在Spring AOP中连接点总是表示方法的执行。</b>
 *
 * <p> 3. Advice（通知）：切面在特定连接点采取的动作。不同类型的通知包括：around（环绕通知），before（前置通知） 和 after（后置通知）。
 *        许多AOP框架中，包括Spring，将通知建模为一个拦截器并在连接点周围维护一个拦截器链。
 *
 * <p> 4. Pointcut（切点）：匹配连接点（Join point）的谓词/声明（predicate）。通知（Advice）与切点表达式相关联，并在与切点匹配的任何连接点上运行。
 * 由切点表达式匹配的连接点概念是AOP的核心，Spring默认使用AspectJ切入点表达式语句。
 *
 * <p> 5. Introduction（引入）：代表类型声明其他字段或方法。Spring AOP允许向任何通知的对象引入新的接口（以及相应的实现）。
 * 例如，可以使用引入让bean实现IsModified接口，以简化缓存。（AspectJ 社区中的引入称为类型间声明。）
 *
 * <p> 6. Target object（目标对象）：被一个或多个切面通知的对象。也称为“advice object（通知对象）”。
 * 由于Spring AOP是通过运行时代理实现的，所以这个对象总是一个代理对象。
 *
 * <p> 7. AOP proxy（AOP代理）：由AOP框架创建的对象，用于实现切面契约（通知方法执行等等）。在Spring框架中，AOP代理是JDK代理或者CGLIB代理。
 *
 * <p> 8. Weaving（织入）：将切面与其他应用程序类型或者对象链接起来，以创建一个通知对象（advised object）。
 * 这可以在编译时（例如使用AspectJ编译器）、加载时或者运行时完成。Spring AOP与其他纯Java AOP框架一样，在运行时执行织入。
 *
 * <p> Spring AOP 包括以下类型的通知：
 * <pre>
 *     1. Before advice（前置通知）：在连接点之前运行但不能阻止执行流继续到连接点的通知（除非抛出异常）。
 *     2. After return advice（正常返回的后置通知）：连接点正常完成运行后运行的通知（例如，如果方法返回时没有抛出异常）。
 *     4. After throwing advice（抛出异常的后置通知）：方法抛出异常退出，运行此通知。
 *     5. After finally advice：无论连接点以何种方式退出（正常或者异常返回），都要运行的通知。
 *     6. Around advice（环绕通知）：围绕连接点（如方法调用）的通知。这是最强大的通知。Around advice可以在方法调用前后执行自定义行为。
 *        它还负责是继续执行连接点还是通过返回自己的返回值或抛出异常来缩短通知的方法执行。
 * </pre>
 *
 * <p> 环绕通知时最普遍的通知。由于Spring AOP与AspectJ一样，提供了各种各样的通知类型，因此最好使用功能最小的通知来实现所需的行为。例如，如果只需要使用方法的返回值更新缓存，
 * 那么最好使用后置通知而不是环绕通知，尽管环绕通知可以完成相同的任务。使用最具体的通知提供了一个更简单的编程模型，而且出错的可能性更小。例如，不需要在用于环绕通知的连接点
 * 上调用proceed()方法，因此不能失败地调用它。
 *
 * <p> 所有的通知参数都是静态类型的，这样就可以使用适当类型的通知参数（例如，方法执行返回值的类型）而不是Object数组。
 *
 * <p> 与切点匹配的连接点概念是AOP的关键，它将AOP与只提供拦截的旧技术区别开来。切点使通知成为独立于面向对象层次结构的目标。
 * 例如，可以对一组跨多个对象的方法应用环绕通知，提供声明式事务管理。（例如service层中所有的业务操作）。
 *
 * <p><b> 5.2. Spring AOP Capabilities and Goals（Spring AOP的功能和目标） </b>
 *
 * <p> Spring AOP 是用纯Java语言实现的。不需要特殊的编译过程。Spring AOP 不需要控制类加载器（的）层次结构，因此适合在servlet容器或应用服务器使用。
 *
 * <p><b>Spring AOP 目前只支持方法执行连接点（建议在Spring bean上执行方法）</b>。 虽然可以在不破坏核心 Spring AOP APIs 的情况下对字段拦截的支持，但是没有实现字段拦截。
 * 如果需要通知字段和更新连接点，建议使用AspectJ之类的语言。
 *
 * <p> Spring AOP 的方法不同于大多数其他AOP框架。其目的并不是提供最完整的AOP实现（尽管Spring AOP非常有能力）。
 * 相反，其目的是在AOP实现和Spring IoC之间提供紧密的集成。以帮助解决企业应用程序中的常见问题。
 *
 * <p> 因此，Spring框架的AOP功能通常与Spring IoC容器一起使用。切面是通过使用普通bean定义语法配置的（尽管这允许强大的“auto-proxying”功能）。
 * 这是与其他AOP实现的一个关键区别。使用Spring AOP不能轻松或高效地完成某些事情，比如通知非常细粒度的对象（典型的是域对象）。在这种情况下AspectJ是最佳选择。
 * 然而，Spring AOP为企业Java应用程序中的大多数问题提供了一个很好的解决方案，AOP的这些问题都是可以接受的。
 *
 * <p> Spring AOP从不试图与AspectJ竞争以提供全面的AOP解决方案。基于代理框架（如Spring AOP）和成熟的框架（如AspectJ）都是有价值的，它们是互补的而不是竞争的。
 * <p> Spring 将Spring AOP和 Ioc与 AspectJ无缝集成，以便在一致的基于Spring的应用程序体系结构中实现AOP的所有使用。这种集成不会影响Spring API 或 Spring Alliance API.
 * <p> Spring AOP 保持向后兼容。
 *
 * <p> Spring框架的核心原则之一是非侵入性。这就是不应该强迫在业务或域模型中引入特定于框架的类和接口的想法。
 * 然而，在某些地方，Spring框架确实允许选择将Spring框架特定的依赖项引入到您的代码库（codebase）中。
 *
 * <p> 选择是选择哪种AOP框架（以及哪种AOP样式）？ Spring团队倾向于@AspectJ注释样式方法而不是springxml配置样式。
 *
 * <p><b> 5.3. AOP Proxies（AOP 代理） </b>
 *
 * <p> Spring AOP默认使用标准JDK动态代理作为AOP代理。这允许代理任何接口（或一组接口）。
 *
 * <p> Spring AOP也可以使用CGLIB代理，这是代理类而不是接口所需的。默认情况下，如果业务对象没有实现接口，则使用CGLIB。
 * 由于编程到接口而不是类是一种很好的实践，业务类通常实现一个或多个业务接口。强制使用CGLIB是可能的，在那些情况下（希望很少），你需要通知一个没有在接口中声明的方法，
 * 或者需要将代理对象作为具体类型传递给一个方法。
 *
 * <p> 重要的是理解Spring AOP是基于代理的。
 *
 *
 * <p><b> 5.4. @AspectJ support（@AspectJ注解支持） </b>
 *
 * <p> @AspectJ 是一种将切面声明为带有注解的普通Java类的样式。@AspectJ 样式是作为 AspectJ5 版本的一部分由AspectJ项目引入的。
 * Spring使用AspectJ提供的用于切点解析和匹配的库来解释与AspectJ 5 相同的注解。
 * 不过，AOP运行时仍然是纯Spring AOP，并不依赖于AspectJ编译器或者weaver。
 *
 * <p><b> 5.4.1. Enabling @AspectJ Support（启用@AspectJ支持） </b>
 *
 * <p> 要在Spring配置中使用@AspectJ切面，需要启用Spring支持，一边基于@AspectJ切面配置Spring AOP，并且根据这些切面是否建议bean进行自动代理。
 * 通过自动代理，如果Spring确定bean是被一个或多个切面通知的，它会自动为bean生成一个代理来拦截方法调用，并确保根据需要运行通知（advice）。
 *
 * <p> @AspectJ 支持可以通过XML或者Java风格的配置来启用。这两种情况下，还需要确保AspectJ（aspectjweaver.jar）文件库位于应用程序（1.8或更高版本）的类路径上。
 * 该库位于AspectJ发行版的lib目录中，也可以从Maven中心仓库中获取。
 *
 * <p><b> Enabling @AspectJ Support with Java Configuration （通过Java配置启用@AspectJ支持） </b>
 * <p> 要通过@Configuration注解启用@AspectJ支持，请添加{@link org.springframework.context.annotation.EnableAspectJAutoProxy}注解，如以下所示：
 * <pre>
 *     @Configuration
 *     @EnableAspectJAutoProxy
 *     public class AppConfig {
 *     }
 * </pre>
 *
 * <p><b> Enabling @AspectJ Support with XML Configuration （通过XML配置启用@AspectJ支持） </b>
 * 要使用基于XML的配置启用@AspectJ支持，请使用aop:aspectj autoproxy元素，如下例所示：
 * <pre>
 *     <aop:aspectj-autoproxy/>
 * </pre>
 *
 *
 * <p><b> 5.4.2. Declaring an Aspect（声明切面） </b>
 *
 * <p> 启用@AspectJ支持后，Spring将自动检测在应用程序上下文中定义的任何bean，其中包含@AspectJ切面（具有@Aspect注解）的类来配置Spring AOP。
 * 接下来的两个例子展示一个不太有用的切面的最小定义。
 *
 * <p> 第一个示例展示了应用程序上下文中的常规bean定义，该定义指向具有@Aspect注解对的bean类：
 * <pre>
 *     <bean id="myAspect" class="org.xyz.NotVeryUsefulAspect">
 *         <!-- configure properties of the aspect here -->
 *     </bean>
 * </pre>
 *
 * <p> 第二个示例展示了NotVeryUsefulAspect类的定义，该类用了{@link org.aspectj.lang.annotation.Aspect}注解：
 * <pre>
 *     package org.xyz;
 *     import org.aspectj.lang.annotation.Aspect;
 *
 *     @Aspect
 *     public class NotVeryUsefulAspect {
 *     }
 * </pre>
 *
 * <p> 切面（用{@link org.aspectj.lang.annotation.Aspect}注解的类）可以有方法和字段，与任何其他类相同。它们还可以包含切点、通知和引用（类型间）声明。
 *
 * <p> Autodetecting aspects through component scanning （通过组件扫描自动检测切面）
 * <p> 可以在Spring XML配置中将切面类注册为常规bean，或者通过类路径扫描（classpath scanning）自动检测它们-----就像Spring管理的其他bean一样。
 * 但是{@link org.aspectj.lang.annotation.Aspect}注解不能让它们在类路径中自动检测。所以需要另外添加一个单独的{@link org.springframework.stereotype.Component}注解
 * （或者根据Spring组件扫描的规则，添加一个符合条件的自定义原型注解）。
 *
 * <p> 在Spring中，切面本身不能成为其他切面通知的目标，类上的{@link org.aspectj.lang.annotation.Aspect}注解将其标记为切面，因此将其从自动代理中排除。
 *
 * <p><b> 5.4.3. Declaring a Pointcut（声明切点） </b>
 *
 * <p> 切点（Pointcut）确定一些感兴趣的连接点（Join point），从而使我们能控制何时能运行通知（Advice）。Spring AOP只支持Spring bean的方法执行连接点，
 * 因此可以将切点看作与Spring bean上方法的执行相匹配。
 * <p> 切点声明由两部分组成：一部分是由名称和任何参数组成的<b>签名</b>，另一部分是确定我们感兴趣的方法的<b>切点表达式</b>。
 * 在AOP的@AspectJ注解样式中，<b>切点签名</b>由常规方法定义提供，<b>切点表达式</b>由{@link org.aspectj.lang.annotation.Pointcut}注解（用作切点签名的方法返回值必须是void）指示。
 *
 * <p> 下面一个例子可能有助于明确切点签名和切点表达式之间的区别。下面定义了一个名为anyOldTransfer的切点，该切点与名为transfer的任何方法的执行相匹配：
 * <pre>
 *     @Pointcut("execution(* transfer(..))") // the pointcut expression
 *     private void anyOldTransfer() {} // the pointcut signature
 * </pre>
 *
 * <p> Supported Pointcut Designators（支持的切点指示符）
 * <p> Spring AOP支持在切点表达式中使用以下AspectJ切点指示符（AspectJ pointcut designators，PCD）：
 * <pre>
 *     execution：用于匹配方法执行连接点。这是使用Spring AOP时的主要切点指示符。---- execution表达式
 *     within：限制匹配到特定类型的连接点（使用Spring AOP时在匹配类型内声明的方法的执行）。
 *     this：限制匹配到连接点，其中bean引用（Spring AOP proxy）是给定类型的实例。
 *     target：限制匹配到目标对象（被代理的应用程序对象）是给定类型实例的连接点。
 *     args：限制匹配到连接点，其中参数是给定类型的实例
 *     @target ：限制匹配到执行对象的类具有给定类型注解的连接点。
 *     @args ：限制匹配到连接点，其中传递的实际参数的运行时类型具有给定类型的注解。
 *     @within ：限制匹配到具有给你定注解的类型内的连接点。
 *     @annotation ：限制匹配到具有给定注解的连接点。---- 匹配的方法上有给定的注解
 * </pre>
 *
 * <p> Spring AOP 只支持方法执行的连接点（连接点只能是方法），Spring AOP是一个基于代理的系统，它区分代理对象本身（与此绑定）和代理后面的目标对象（与目标绑定）。
 *
 * <p> 由于Spring AOP框架基于代理的特性，根据定义，目标对象的调用不会被拦截。
 * <p> 对于JDK代理，只能拦截代理上的公共接口方法调用。对于CGLIB，可以拦截代理上的公共和受保护方法调用。
 *
 * <p> Combining Pointcut Expressions （组合切点表达式）
 * 可以用 && ，|| ，！ 组合切点表达式。  最好的做法是用较小的命名组件构建更复杂的切点表达式。
 * 比如：
 * <pre>
 *     @Pointcut("execution(public * *(..))")
 *     private void anyPublicOperation() {}    --- 1 ：匹配任何公共方法的执行
 *
 *     @Pointcut("within(com.xyz.myapp.trading..*)")
 *     private void inTrading() {}    ---- 2 ：匹配在trading模块中的方法执行
 *
 *     @Pointcut("anyPublicOperation() && inTrading()")
 *     private void tradingOperation() {}  ---- 3 ：匹配在trading模块中的公共方法的执行
 * </pre>
 *
 * <p> Sharing Common Pointcut Definitions （共享公共的切点定义）
 * <p> 在使用企业应用程序时，开发人员通常希望从几个方面来引用应用程序的模块和特定的操作集。因此，建议定义一个捕获公共切点表达式的 公共切点（CommonPointcuts）。
 * 比如：
 * <pre>
 *     @Aspect
 *     public class CommonPointcuts {
 *
 *          @Pointcut("within(com.xyz.myapp.web..*)")
 *          public void inWebLayer() {}
 *
 *          @Pointcut("within(com.xyz.myapp.service..*)")
 *          public void inServiceLayer() {}
 *
 *          @Pointcut("within(com.xyz.myapp.dao..*)")
 *          public void inDataAccessLayer() {}
 *
 *          @Pointcut("execution(* com.xyz.myapp..service.*.*(..))")
 *          public void businessService() {}
 *
 *          @Pointcut("execution(* com.xyz.myapp.dao.*.*(..))")
 *          public void dataAccessOperation() {}
 *     }
 * </pre>
 *
 * <p> Spring AOP用户最长使用的切点指示符是execution表达式，该表达式的格式如下：
 * <pre>
 *     execution(modifiers-pattern? ret-type-pattern declaring-type-pattern?name-pattern(param-pattern)
 *                 throws-pattern?)
 * </pre>
 * <p> 除了返回类型模式（ret-type-pattern）、名称模式（name-pattern）和参数模式（param-pattern）之外的所有其他部分都是可选的。
 * <p> ret-type-pattern 确定方法的返回类型比如是什么，才能匹配到到连接点。*是最常见的ret-type-pattern，它匹配任何返回类型。完全限定类型名仅在方法返回给定类型时匹配。
 * <p> name-pattern 和方法名称匹配。可以使用*通配符作为name-pattern的全部或者部分。
 * <p> param-pattern 稍微复杂些：()匹配一个不带参数的方法，(..)匹配任意数量（零个或更多）参数的方法，(*)匹配一个参数的方法。
 *                         (*, String)匹配连个参数的方法，其中第一个参数是任意类型，而第二个参数必须是字符串。
 *
 * <p> 一些常见的切点表达式：
 * <pre>
 *     execution(public * *(..))    //匹配所有public方法
 *     execution(* set*(..))    //匹配所有方法名开头为set的方法
 *     execution(* com.xyz.service.AccountService.*(..))    //匹配AccountService下的所有方法
 *     execution(* com.xyz.service.*.*(..))    //匹配service包下的所有方法
 *     execution(* com.xyz.service..*.*(..))    //匹配service包或其子包下的所有方法
 *     within(com.xyz.service.*)    //匹配service包下的所有方法
 *     within(com.xyz.service..*)    //匹配service包或其子包下的所有方法
 *     this(com.xyz.service.AccountService)    //匹配所有实现了AccountService接口的类的代理类的方法（注意是代理类）
 *     target(com.xyz.service.AccountService)    //匹配所有实现了AccountService接口的类的方法（注意是本类）
 *     args(java.io.Serializable)    //匹配只有一个入参，且入参实现了Serializable接口的方法
 *     @target(org.springframework.transaction.annotation.Transactional)    //匹配类上标注了@Transactional注解的类中方法
 *     @within(org.springframework.transaction.annotation.Transactional)    //匹配运行时子类上标注了@Transactional注解的类中方法
 *     @annotation(org.springframework.transaction.annotation.Transactional)    //匹配所有标注了@Transactional注解的方法
 *     @args(com.xyz.security.Classified)    //匹配只有一个入参，且运行时入参有@Classified注解的方法
 *     bean(tradeService)    //匹配命名为tradeService的类的方法
 *     bean(*Service)    //匹配命名后缀为Service的类的方法
 * </pre>
 *
 * <p> Writing Good Pointcuts （写好切点）
 * <p> 在编译期间，AspectJ处理切点以优化匹配性能。检查代码并确定每个连接点是否匹配（静态或动态）给定的切点是一个代价高昂的过程。
 * <pre>
 *     特定的连接点用：execution
 *     某个作用域的连接点用：within
 *     上线文指示符用：this、target和@annotation
 * </pre>
 *
 * <p><b> 5.4.4. Declaring Advice （声明通知） </b>
 *
 * <p> Advice与切点表达式相关联，并且在与切点匹配的方法执行之前（Before advice）、之后（After advice）或周围（Around advice）运行。
 *
 * <p> Before Advice（前置通知）：可以用{@link org.aspectj.lang.annotation.Before}注解在切面中声明前置通知。
 * <pre>
 *     import org.aspectj.lang.annotation.Aspect;
 *     import org.aspectj.lang.annotation.Before;
 *
 *     @Aspect
 *     public class BeforeExample {
 *         //方式一： dataAccessOperation()是上面定义好的切点
 *         @Before("com.xyz.myapp.CommonPointcuts.dataAccessOperation()")
 *         public void doAccessCheck1() {
 *             // ...
 *         }
 *
 *         //方式二： 使用切点表达式
 *         @Before("execution(* com.xyz.myapp.dao.*.*(..))")
 *         public void doAccessCheck2() {
 *            // ...
 *         }
 *     }
 * </pre>
 *
 * <p> After Return Advice（正常返回的后置通知）：当匹配的方法执行正常返回后运行，可以用{@link org.aspectj.lang.annotation.AfterReturning}注解在切面中声明。
 * <pre>
 *     import org.aspectj.lang.annotation.Aspect;
 *     import org.aspectj.lang.annotation.Before;
 *
 *     @Aspect
 *     public class BeforeExample {
 *         @AfterReturning("com.xyz.myapp.CommonPointcuts.dataAccessOperation()")
 *         public void doAccessCheck() {
 *             // ...
 *         }
 *     }
 * </pre>
 *
 * <p> 有时，需要Advice方法中访问返回的实际值，可以使用{@link org.aspectj.lang.annotation.AfterReturning}的形式绑定返回值以获取该访问权限，如下例所示：
 * <pre>
 *     import org.aspectj.lang.annotation.Aspect;
 *     import org.aspectj.lang.annotation.Before;
 *
 *     @Aspect
 *     public class BeforeExample {
 *
 *         // @AfterReturning注解中returning属性的值（此例中是retVal）必须和Advice方法中参数的名称一样。
 *         @AfterReturning(
 *                 pointcut = "com.xyz.myapp.CommonPointcuts.dataAccessOperation()",
 *                 returning = "retVal")
 *         public void doAccessCheck(Object retVal) {
 *             // ...
 *         }
 *     }
 * </pre>
 *
 * <p> After Throwing Advice（抛出异常的后置通知）：当匹配的方法通过抛出异常退出执行时，将运行通知。
 * 可以用{@link org.aspectj.lang.annotation.AfterThrowing}注解在切面中声明。
 * <pre>
 *     import org.aspectj.lang.annotation.Aspect;
 *     import org.aspectj.lang.annotation.Before;
 *
 *     @Aspect
 *     public class BeforeExample {
 *         @AfterThrowing("com.xyz.myapp.CommonPointcuts.dataAccessOperation()")
 *         public void doRecoveryActions() {
 *             // ...
 *         }
 *     }
 * </pre>
 *
 * <p> 通常，如果希望通知仅在抛出给定类型的异常时运行，并且还经常需要访问通知正文中抛出的异常。
 * 可以使用throwing属性来限制匹配，并将抛出的异常绑定到advice参数。
 * <pre>
 *     import org.aspectj.lang.annotation.Aspect;
 *     import org.aspectj.lang.annotation.Before;
 *
 *     @Aspect
 *     public class BeforeExample {
 *
 *         // throwing属性的值必须和Advice方法中参数的名称一样。
 *         // throwing子句还将匹配限制为仅与那些抛出指定类型异常（在本例中为DataAccessException）的方法执行相匹配。
 *         @AfterThrowing(
 *                pointcut = "com.xyz.myapp.CommonPointcuts.dataAccessOperation()",
 *                throwing = "ex")
 *         public void doRecoveryActions(DataAccessException ex) {
 *             // ...
 *         }
 *     }
 * </pre>
 *
 * <p> After Finally Advice：当匹配的方法执行退出时，Advice将运行。类似于try-catch语句中的finally块。
 * <pre>
 *     import org.aspectj.lang.annotation.Aspect;
 *     import org.aspectj.lang.annotation.Before;
 *
 *     @Aspect
 *     public class BeforeExample {
 *         @After("com.xyz.myapp.CommonPointcuts.dataAccessOperation()")
 *         public void doReleaseLock() {
 *             // ...
 *         }
 *     }
 * </pre>
 *
 * <p> Around Advice（环绕通知）：环绕通知“围绕”匹配方法的执行。环绕通知是使用{@link org.aspectj.lang.annotation.Around}注解声明的。
 * Advice方法的第一个参数必须是ProceedingJoinPoint类型。
 * 调用ProceedingJoinPoint的proceed方法会继续下一个通知（Advice）或者目标方法调用。proceed方法还可以传入Object数组（Object[]）参数。
 * <pre>
 *     import org.aspectj.lang.annotation.Aspect;
 *     import org.aspectj.lang.annotation.Around;
 *     import org.aspectj.lang.ProceedingJoinPoint;
 *
 *     @Aspect
 *     public class AroundExample {
 *
 *         @Around("com.xyz.myapp.CommonPointcuts.businessService()")
 *         public Object doBasicProfiling(ProceedingJoinPoint pjp) throws Throwable {
 *             // start stopwatch
 *             Object retVal = pjp.proceed();
 *             // stop stopwatch
 *             return retVal;
 *         }
 *     }
 * </pre>
 *
 * <p> Advice Parameters
 * <p> Spring提供了全类型的advice，这意味着您可以在advice签名中声明所需的参数（正如我们在前面的返回和抛出示例中看到的那样），而不是一直使用Object[]数组。
 *
 * <p> Access to Current JoinPoint （访问当前连接点）
 * <p> 任何Advice（通知）方法都可以声明类型为{@link org.aspectj.lang.JoinPoint}的参数作为其第一个参数
 * （环绕通知需要第一个参数类型为{@link org.aspectj.lang.ProceedingJoinPoint}，它是JoinPoint的子类），
 * <p> JoinPoint接口提供了许多有用的方法：
 * <pre>
 *     getArgs() : 返回方法参数。
 *     getThis() : 返回代理对象。
 *     getTarget() : 返回目标对象。
 *     getSignature() : 返回被通知方法的描述。
 *     toString() : 打印被通知方法的有用描述。
 * </pre>
 *
 * <p> Passing Parameters to Advice （将参数传递给Advice）
 *
 * <pre>
 *     @Before("com.xyz.myapp.CommonPointcuts.dataAccessOperation() && args(account,..)")
 *     public void validateAccount(Account account) {
 *         // ...
 *     }
 * </pre>
 * <p> 上面切点表达式中的args(account,..)部分有两个用途。
 * <p> 第一，它将匹连接点制为至少一个参数的方法，并且参数是Account的实例。
 * <p> 第二，使实际的Account参数Advice可用。
 *
 * <p> 另一种方式是声明一个切点，与它匹配的连接点提供Account对象值，然后在通知（Advice）中引用指定的切点：
 * <pre>
 *     @Pointcut("com.xyz.myapp.CommonPointcuts.dataAccessOperation() && args(account,..)")
 *     private void accountDataAccessOperation(Account account) {}
 *
 *     @Before("accountDataAccessOperation(account)")
 *     public void validateAccount(Account account) {
 *         // ...
 *     }
 * </pre>
 *
 *
 * <p> Advice Parameters and Generics （Advice参数和泛型）
 *
 * <p> Spring AOP 可以处理类声明和方法参数中使用的泛型。
 * <pre>
 *     public interface Sample<T> {
 *         void sampleGenericMethod(T param);
 *         void sampleGenericCollectionMethod(Collection<T> param);
 *     }
 * </pre>
 *
 * <p> 通过将advice参数键入要截取方法的参数类型，可以将方法类型的截取限制为某些参数类型：
 * <pre>
 *     @Before("execution(* ..Sample+.sampleGenericMethod(*)) && args(param)")
 *     public void beforeSampleMethod(MyType param) {
 *         // Advice implementation
 *     }
 * </pre>
 *
 * <p><b> Advice Ordering （通知顺序） </b>
 *
 * <p> 当多个Advice作用于同一个连接点时会发生什么？ Spring AOP遵循与AspectJ相同的优先级规则来确定Advice的执行顺序。优先级最高的Advice在“传入时”
 * 首先运行（两个Before通知，优先级最高的通知第一个运行），在从连接点“退出”时，优先级最高的通知最后运行（两个After通知，优先级最高的通知第二个运行）。
 *
 * <p> 当在不同切面（Aspect）定义的两个通知（Advice）在同一个连接点（Join point）运行时，除非另外指定，否则执行顺序是未定的。
 * 可以通过指定优先级来控制执行顺序。可以通过实现 org.springframework.core.Ordered 接口，或者使用@Order注解来实现。
 *
 *
 * <p><b> 5.4.5. Introductions （引言） </b>
 *
 * <p> Introduction（在AspectJ 中称为类型间声明）使切面（Aspect）声明被通知对象实现给定的接口，并代表这些被通知对象提供该接口的实现。
 *
 * <p> 可以用@DeclareParents注解声明一个引言。此注释用于声明匹配类型有新的父级（即名称）。例如给定一个名为 UsageTracked 的接口以及该接口一个名为
 * DefaultUsageTracked 的实现类，下面这个切面声明service层所有接口的所有实现类也实现UsageTracked接口（例如，通过JMX统计）：
 *
 * <pre>
 *     @Aspect
 *     public class UsageTracking {
 *
 *         @DeclareParents(value="com.xzy.myapp.service.*+", defaultImpl=DefaultUsageTracked.class)
 *         public static UsageTracked mixin;
 *
 *         @Before("com.xyz.myapp.CommonPointcuts.businessService() && this(usageTracked)")
 *         public void recordUsage(UsageTracked usageTracked) {
 *             usageTracked.incrementUseCount();
 *         }
 *     }
 * </pre>
 *
 * <p> 要实现的接口由注解字段的类型决定。@DeclareParents注解的value属性是AspectJ类型模式。任何匹配的bean都实现UsageTracked接口。
 * 注意在上面示例的Before通知中，service层中的bean可以直接用作UserTracked接口的实现。如果以编程方式访问bean，可以像下面这种方式写：
 * <pre>
 *     UsageTracked usageTracked = (UsageTracked) context.getBean("myService");
 * </pre>
 *
 *
 * <p><b> 5.4.6 Aspect Instantiation Models （切面实例化模型） </b>
 *
 * <p> 默认情况下，应用程序上下文中每个切面都有一个实例（单例）。AspectJ称之为单例实例化模型。可以定义具有生命周期的切面。
 * Spring支持AspectJ的perthis 和pertarget 实例化模型。percflow、percflowbelow和pertypewithin 模型目前不支持。
 *
 * <p> 可以通过在{@link org.aspectj.lang.annotation.Aspect}注解中指定perthis子句声明perthis切面。考虑下面示例：
 * <pre>
 *     @Aspect("perthis(com.xyz.myapp.CommonPointcuts.businessService())")
 *     public class MyAspect {
 *
 *         private int someState;
 *
 *         @Before("com.xyz.myapp.CommonPointcuts.businessService()")
 *         public void recordServiceUsage() {
 *             // ...
 *         }
 *     }
 * </pre>
 * <p> 上面的例子中，perthis子句的作用是为执行业务service的每个唯一对象创建一个切面实例。 切面实例是在第一次对service对象调用方法时创建的。
 * 当service对象超出范围时，切面超出范围。在创建切面实例之前，其中的任何通知（Advice）都不会运行。
 * 一旦创建了切面实例，在其中声明的通知就会在匹配的连接点上运行，但仅当服务对象是与此切面关联的对象时。
 *
 *
 * <p><b> 5.8 Proxying Mechanisms （代理机制） </b>
 *
 * <p> Spring AOP使用JDK动态代理或者CGLIB为给定的目标对象创建代理。JDK动态代理内置在Java JDK中，而CGLIB是一个通用的开源类定义库（重新打包到spring-core中）。
 *
 * <p> 如果要代理的目标对象至少实现了一个接口，则使用JDK动态代理。目标类型实现的所有接口都是代理的。如果目标对象没有实现任何接口，则会创建一个CGLIB代理。
 *
 * <p> 如果想要强制使用CGLIB代理（例如，代理为目标对象定义的每个方法，而不仅仅是由其接口实现的方法），可以这样做。但是需要考虑以下问题：
 * <pre>
 *     1. 对于CGLIB，不能通知final方法，因为它们不能在运行时生成的子类中被重写。
 *     2. 从Spring 4.0版本开始，代理对象的构造函数不再被调用两次，因为CGLIB代理实例是通过 Objenesis创建的。
 *        只有当JVM不允许绕过构造函数时，您可能会看到来自Spring的AOP支持的双重调用和相应的调试日志条目。
 * </pre>
 *
 * <p> 要强制使用CGLIB代理，请设置<aop:config>元素中proxy-target-class的值为true：
 * <pre>
 *     <aop:config proxy-target-class="true">
 *         <!-- other beans defined here... -->
 *     </aop:config>
 * </pre>
 *
 * <p> 要在使用@AspectJ自动代理支持时强制CGLIB代理，请设置<aop:aspectj-autoproxy> 的proxy-target-class值为true：
 * <pre>
 *     <aop:aspectj-autoproxy proxy-target-class="true"/>
 * </pre>
 *
 * <p><b> 5.8.1. Understanding AOP Proxies （理解AOP代理） </b>
 *
 * <p> Spring AOP是基于代理的。在写自己的切面或者使用Spring框架提供的任何基于Spring AOP的切面之前，掌握代理是非常重要的。
 *
 * <p> 首先考虑一个场景，有一个普通的没有被代理的，没有什么特别的直接的对象引用，如下代码所示：
 * <pre>
 *     public class SimplePojo implements Pojo {
 *
 *         public void foo() {
 *             // this next method invocation is a direct call on the 'this' reference
 *             this.bar();
 *         }
 *
 *         public void bar() {
 *             // some logic...
 *         }
 *     }
 * </pre>
 *
 *
 *
 *
 *
 *
 * <p> 好文链接: https://blog.csdn.net/q982151756/article/details/80513340
 * @author eastFeng
 * @date 2021-01-27 15:16
 *
 *
 *
 */
package com.dongfeng.study.config.aop;

/*
 *
 * <b>  5. Aspect Oriented Programming with Spring （基于Spring的面向切面编程）</b>
 *
 * <p> 面向切面编程（Aspect-oriented Programming, AOP）通过另一种思考程序结构的方式，对面向对象编程（Object-oriented Programming, OOP）进行了补充。
 * 在OOP中，模块化的关键单元是类（class），而在AOP中，模块化的单元是切面（aspect）。切面支持跨多个类型和对象的关注点（如事务管理）的模块化。
 *
 * <p> Spring的关键组件之一就是AOP框架。虽然Spring IoC（Inversion of Control控制反转）容器不依赖于AOP，但是AOP补充了Spring IoC，提供了非常强大的中间件解决方案。
 *
 * <p> @AspectJ annotation style
 *
 *
 * <p> AOP 在Spring框架中用于:
 * <pre>
 * 1. 提供声明式企业服务。最重要的此类服务是声明式事务管理（declarative transaction management）。
 * 2. 让用户自定义切面，用AOP补充他们对OOP的使用。
 * </pre>
 *
 * <p><b> 5.1. AOP Concepts（AOP 概念） </b>
 * <p> 1. Aspect（切面）：跨多个关注点的模块化。事务管理是企业Java应用程序中横切关注点的一个很好的例子。
 * 在Spring AOP中，切面是通过正则类（基于模式的方法）或者用@Aspect注解（AspectJ样式风格）来注解的正则类来实现的。
 *
 * <p> 2. Join point（连接点）：程序执行过程中的一个点，如方法的执行或异常的处理。<b>在Spring AOP中连接点总是表示方法的执行。</b>
 *
 * <p> 3. Advice（通知）：切面在特定连接点采取的动作。不同类型的通知包括：around（环绕通知），before（前置通知） 和 after（后置通知）。
 *        许多AOP框架中，包括Spring，将通知建模为一个拦截器并在连接点周围维护一个拦截器链。
 *
 * <p> 4. Pointcut（切点）：匹配连接点（Join point）的谓词/声明（predicate）。通知（Advice）与切点表达式相关联，并在与切点匹配的任何连接点上运行。
 * 由切点表达式匹配的连接点概念是AOP的核心，Spring默认使用AspectJ切入点表达式语句。
 *
 * <p> 5. Introduction（引入）：代表类型声明其他字段或方法。Spring AOP允许向任何通知的对象引入新的接口（以及相应的实现）。
 * 例如，可以使用引入让bean实现IsModified接口，以简化缓存。（AspectJ 社区中的引入称为类型间声明。）
 *
 * <p> 6. Target object（目标对象）：被一个或多个切面通知的对象。也称为“advice object（通知对象）”。
 * 由于Spring AOP是通过运行时代理实现的，所以这个对象总是一个代理对象。
 *
 * <p> 7. AOP proxy（AOP代理）：由AOP框架创建的对象，用于实现切面契约（通知方法执行等等）。在Spring框架中，AOP代理是JDK代理或者CGLIB代理。
 *
 * <p> 8. Weaving（织入）：将切面与其他应用程序类型或者对象链接起来，以创建一个通知对象（advised object）。
 * 这可以在编译时（例如使用AspectJ编译器）、加载时或者运行时完成。Spring AOP与其他纯Java AOP框架一样，在运行时执行织入。
 *
 * <p> Spring AOP 包括以下类型的通知：
 * <pre>
 *     1. Before advice（前置通知）：在连接点之前运行但不能阻止执行流继续到连接点的通知（除非抛出异常）。
 *     2. After return advice（正常返回的后置通知）：连接点正常完成运行后运行的通知（例如，如果方法返回时没有抛出异常）。
 *     4. After throwing advice（抛出异常的后置通知）：方法抛出异常退出，运行此通知。
 *     5. After finally advice：无论连接点以何种方式退出（正常或者异常返回），都要运行的通知。
 *     6. Around advice（环绕通知）：围绕连接点（如方法调用）的通知。这是最强大的通知。Around advice可以在方法调用前后执行自定义行为。
 *        它还负责是继续执行连接点还是通过返回自己的返回值或抛出异常来缩短通知的方法执行。
 * </pre>
 *
 * <p> 环绕通知时最普遍的通知。由于Spring AOP与AspectJ一样，提供了各种各样的通知类型，因此最好使用功能最小的通知来实现所需的行为。例如，如果只需要使用方法的返回值更新缓存，
 * 那么最好使用后置通知而不是环绕通知，尽管环绕通知可以完成相同的任务。使用最具体的通知提供了一个更简单的编程模型，而且出错的可能性更小。例如，不需要在用于环绕通知的连接点
 * 上调用proceed()方法，因此不能失败地调用它。
 *
 * <p> 所有的通知参数都是静态类型的，这样就可以使用适当类型的通知参数（例如，方法执行返回值的类型）而不是Object数组。
 *
 * <p> 与切点匹配的连接点概念是AOP的关键，它将AOP与只提供拦截的旧技术区别开来。切点使通知成为独立于面向对象层次结构的目标。
 * 例如，可以对一组跨多个对象的方法应用环绕通知，提供声明式事务管理。（例如service层中所有的业务操作）。
 *
 * <p><b> 5.2. Spring AOP Capabilities and Goals（Spring AOP的功能和目标） </b>
 *
 * <p> Spring AOP 是用纯Java语言实现的。不需要特殊的编译过程。Spring AOP 不需要控制类加载器（的）层次结构，因此适合在servlet容器或应用服务器使用。
 *
 * <p><b>Spring AOP 目前只支持方法执行连接点（建议在Spring bean上执行方法）</b>。 虽然可以在不破坏核心 Spring AOP APIs 的情况下对字段拦截的支持，但是没有实现字段拦截。
 * 如果需要通知字段和更新连接点，建议使用AspectJ之类的语言。
 *
 * <p> Spring AOP 的方法不同于大多数其他AOP框架。其目的并不是提供最完整的AOP实现（尽管Spring AOP非常有能力）。
 * 相反，其目的是在AOP实现和Spring IoC之间提供紧密的集成。以帮助解决企业应用程序中的常见问题。
 *
 * <p> 因此，Spring框架的AOP功能通常与Spring IoC容器一起使用。切面是通过使用普通bean定义语法配置的（尽管这允许强大的“auto-proxying”功能）。
 * 这是与其他AOP实现的一个关键区别。使用Spring AOP不能轻松或高效地完成某些事情，比如通知非常细粒度的对象（典型的是域对象）。在这种情况下AspectJ是最佳选择。
 * 然而，Spring AOP为企业Java应用程序中的大多数问题提供了一个很好的解决方案，AOP的这些问题都是可以接受的。
 *
 * <p> Spring AOP从不试图与AspectJ竞争以提供全面的AOP解决方案。基于代理框架（如Spring AOP）和成熟的框架（如AspectJ）都是有价值的，它们是互补的而不是竞争的。
 * <p> Spring 将Spring AOP和 Ioc与 AspectJ无缝集成，以便在一致的基于Spring的应用程序体系结构中实现AOP的所有使用。这种集成不会影响Spring API 或 Spring Alliance API.
 * <p> Spring AOP 保持向后兼容。
 *
 * <p> Spring框架的核心原则之一是非侵入性。这就是不应该强迫在业务或域模型中引入特定于框架的类和接口的想法。
 * 然而，在某些地方，Spring框架确实允许选择将Spring框架特定的依赖项引入到您的代码库（codebase）中。
 *
 * <p> 选择是选择哪种AOP框架（以及哪种AOP样式）？ Spring团队倾向于@AspectJ注释样式方法而不是springxml配置样式。
 *
 * <p><b> 5.3. AOP Proxies（AOP 代理） </b>
 *
 * <p> Spring AOP默认使用标准JDK动态代理作为AOP代理。这允许代理任何接口（或一组接口）。
 *
 * <p> Spring AOP也可以使用CGLIB代理，这是代理类而不是接口所需的。默认情况下，如果业务对象没有实现接口，则使用CGLIB。
 * 由于编程到接口而不是类是一种很好的实践，业务类通常实现一个或多个业务接口。强制使用CGLIB是可能的，在那些情况下（希望很少），你需要通知一个没有在接口中声明的方法，
 * 或者需要将代理对象作为具体类型传递给一个方法。
 *
 * <p> 重要的是理解Spring AOP是基于代理的。
 *
 *
 * <p><b> 5.4. @AspectJ support（@AspectJ注解支持） </b>
 *
 * <p> @AspectJ 是一种将切面声明为带有注解的普通Java类的样式。@AspectJ 样式是作为 AspectJ5 版本的一部分由AspectJ项目引入的。
 * Spring使用AspectJ提供的用于切点解析和匹配的库来解释与AspectJ 5 相同的注解。
 * 不过，AOP运行时仍然是纯Spring AOP，并不依赖于AspectJ编译器或者weaver。
 *
 * <p><b> 5.4.1. Enabling @AspectJ Support（启用@AspectJ支持） </b>
 *
 * <p> 要在Spring配置中使用@AspectJ切面，需要启用Spring支持，一边基于@AspectJ切面配置Spring AOP，并且根据这些切面是否建议bean进行自动代理。
 * 通过自动代理，如果Spring确定bean是被一个或多个切面通知的，它会自动为bean生成一个代理来拦截方法调用，并确保根据需要运行通知（advice）。
 *
 * <p> @AspectJ 支持可以通过XML或者Java风格的配置来启用。这两种情况下，还需要确保AspectJ（aspectjweaver.jar）文件库位于应用程序（1.8或更高版本）的类路径上。
 * 该库位于AspectJ发行版的lib目录中，也可以从Maven中心仓库中获取。
 *
 * <p><b> Enabling @AspectJ Support with Java Configuration （通过Java配置启用@AspectJ支持） </b>
 * <p> 要通过@Configuration注解启用@AspectJ支持，请添加{@link org.springframework.context.annotation.EnableAspectJAutoProxy}注解，如以下所示：
 * <pre>
 *     @Configuration
 *     @EnableAspectJAutoProxy
 *     public class AppConfig {
 *
 *     }
 * </pre>
 *
 * <p><b> Enabling @AspectJ Support with XML Configuration （通过XML配置启用@AspectJ支持） </b>
 * 要使用基于XML的配置启用@AspectJ支持，请使用aop:aspectj autoproxy元素，如下例所示：
 * <pre>
 *     <aop:aspectj-autoproxy/>
 * </pre>
 *
 *
 * <p><b> 5.4.2. Declaring an Aspect（声明切面） </b>
 *
 * <p> 启用@AspectJ支持后，Spring将自动检测在应用程序上下文中定义的任何bean，其中包含@AspectJ切面（具有@Aspect注解）的类来配置Spring AOP。
 * 接下来的两个例子展示一个不太有用的切面的最小定义。
 *
 * <p> 第一个示例展示了应用程序上下文中的常规bean定义，该定义指向具有@Aspect注解对的bean类：
 * <pre>
 *     <bean id="myAspect" class="org.xyz.NotVeryUsefulAspect">
 *         <!-- configure properties of the aspect here -->
 *     </bean>
 * </pre>
 *
 * <p> 第二个示例展示了NotVeryUsefulAspect类的定义，该类用了{@link org.aspectj.lang.annotation.Aspect}注解：
 * <pre>
 *     package org.xyz;
 *     import org.aspectj.lang.annotation.Aspect;
 *
 *     @Aspect
 *     public class NotVeryUsefulAspect {
 *     }
 * </pre>
 *
 * <p> 切面（用{@link org.aspectj.lang.annotation.Aspect}注解的类）可以有方法和字段，与任何其他类相同。它们还可以包含切点、通知和引用（类型间）声明。
 *
 * <p> Auto detecting aspects through component scanning （通过组件扫描自动检测切面）
 * <p> 可以在Spring XML配置中将切面类注册为常规bean，或者通过类路径扫描（classpath scanning）自动检测它们-----就像Spring管理的其他bean一样。
 * 但是{@link org.aspectj.lang.annotation.Aspect}注解不能让它们在类路径中自动检测。所以需要另外添加一个单独的{@link org.springframework.stereotype.Component}注解
 * （或者根据Spring组件扫描的规则，添加一个符合条件的自定义原型注解）。
 *
 * <p> 在Spring中，切面本身不能成为其他切面通知的目标，类上的{@link org.aspectj.lang.annotation.Aspect}注解将其标记为切面，因此将其从自动代理中排除。
 *
 * <p><b> 5.4.3. Declaring a Pointcut（声明切点） </b>
 *
 * <p> 切点（Pointcut）确定一些感兴趣的连接点（Join point），从而使我们能控制何时能运行通知（Advice）。Spring AOP只支持Spring bean的方法执行连接点，
 * 因此可以将切点看作与Spring bean上方法的执行相匹配。
 * <p> 切点声明由两部分组成：一部分是由名称和任何参数组成的<b>签名</b>，另一部分是确定我们感兴趣的方法的<b>切点表达式</b>。
 * 在AOP的@AspectJ注解样式中，<b>切点签名</b>由常规方法定义提供，<b>切点表达式</b>由{@link org.aspectj.lang.annotation.Pointcut}注解（用作切点签名的方法返回值必须是void）指示。
 *
 * <p> 下面一个例子可能有助于明确切点签名和切点表达式之间的区别。下面定义了一个名为anyOldTransfer的切点，该切点与名为transfer的任何方法的执行相匹配：
 * <pre>
 *     @Pointcut("execution(* transfer(..))") // the pointcut expression
 *     private void anyOldTransfer() {} // the pointcut signature
 * </pre>
 *
 * <p> Supported Pointcut Designators（支持的切点指示符）
 * <p> Spring AOP支持在切点表达式中使用以下AspectJ切点指示符（AspectJ pointcut designators，PCD）：
 * <pre>
 *     execution：用于匹配方法执行连接点。这是使用Spring AOP时的主要切点指示符。---- execution表达式
 *     within：限制匹配到特定类型的连接点（使用Spring AOP时在匹配类型内声明的方法的执行）。
 *     this：限制匹配到连接点，其中bean引用（Spring AOP proxy）是给定类型的实例。
 *     target：限制匹配到目标对象（被代理的应用程序对象）是给定类型实例的连接点。
 *     args：限制匹配到连接点，其中参数是给定类型的实例
 *     @target ：限制匹配到执行对象的类具有给定类型注解的连接点。
 *     @args ：限制匹配到连接点，其中传递的实际参数的运行时类型具有给定类型的注解。
 *     @within ：限制匹配到具有给你定注解的类型内的连接点。
 *     @annotation ：限制匹配到具有给定注解的连接点。---- 匹配的方法上有给定的注解
 * </pre>
 *
 * <p> Spring AOP 只支持方法执行的连接点（连接点只能是方法），Spring AOP是一个基于代理的系统，它区分代理对象本身（与此绑定）和代理后面的目标对象（与目标绑定）。
 *
 * <p> 由于Spring AOP框架基于代理的特性，根据定义，目标对象的调用不会被拦截。
 * <p> 对于JDK代理，只能拦截代理上的公共接口方法调用。对于CGLIB，可以拦截代理上的公共和受保护方法调用。
 *
 * <p> Combining Pointcut Expressions （组合切点表达式）
 * 可以用 && ，|| ，！ 组合切点表达式。  最好的做法是用较小的命名组件构建更复杂的切点表达式。
 * 比如：
 * <pre>
 *     @Pointcut("execution(public * *(..))")
 *     private void anyPublicOperation() {}    --- 1 ：匹配任何公共方法的执行
 *
 *     @Pointcut("within(com.xyz.myapp.trading..*)")
 *     private void inTrading() {}    ---- 2 ：匹配在trading模块中的方法执行
 *
 *     @Pointcut("anyPublicOperation() && inTrading()")
 *     private void tradingOperation() {}  ---- 3 ：匹配在trading模块中的公共方法的执行
 * </pre>
 *
 * <p> Sharing Common Pointcut Definitions （共享公共的切点定义）
 * <p> 在使用企业应用程序时，开发人员通常希望从几个方面来引用应用程序的模块和特定的操作集。因此，建议定义一个捕获公共切点表达式的 公共切点（CommonPointcuts）。
 * 比如：
 * <pre>
 *     @Aspect
 *     public class CommonPointcuts {
 *
 *          @Pointcut("within(com.xyz.myapp.web..*)")
 *          public void inWebLayer() {}
 *
 *          @Pointcut("within(com.xyz.myapp.service..*)")
 *          public void inServiceLayer() {}
 *
 *          @Pointcut("within(com.xyz.myapp.dao..*)")
 *          public void inDataAccessLayer() {}
 *
 *          @Pointcut("execution(* com.xyz.myapp..service.*.*(..))")
 *          public void businessService() {}
 *
 *          @Pointcut("execution(* com.xyz.myapp.dao.*.*(..))")
 *          public void dataAccessOperation() {}
 *     }
 * </pre>
 *
 * <p> Spring AOP用户最长使用的切点指示符是execution表达式，该表达式的格式如下：
 * <pre>
 *     execution(modifiers-pattern? ret-type-pattern declaring-type-pattern?name-pattern(param-pattern)
 *                 throws-pattern?)
 * </pre>
 * <p> 除了返回类型模式（ret-type-pattern）、名称模式（name-pattern）和参数模式（param-pattern）之外的所有其他部分都是可选的。
 * <p> ret-type-pattern 确定方法的返回类型比如是什么，才能匹配到到连接点。*是最常见的ret-type-pattern，它匹配任何返回类型。完全限定类型名仅在方法返回给定类型时匹配。
 * <p> name-pattern 和方法名称匹配。可以使用*通配符作为name-pattern的全部或者部分。
 * <p> param-pattern 稍微复杂些：()匹配一个不带参数的方法，(..)匹配任意数量（零个或更多）参数的方法，(*)匹配一个参数的方法。
 *                         (*, String)匹配连个参数的方法，其中第一个参数是任意类型，而第二个参数必须是字符串。
 *
 * <p> 一些常见的切点表达式：
 * <pre>
 *     execution(public * *(..))    //匹配所有public方法
 *     execution(* set*(..))    //匹配所有方法名开头为set的方法
 *     execution(* com.xyz.service.AccountService.*(..))    //匹配AccountService下的所有方法
 *     execution(* com.xyz.service.*.*(..))    //匹配service包下的所有方法
 *     execution(* com.xyz.service..*.*(..))    //匹配service包或其子包下的所有方法
 *     within(com.xyz.service.*)    //匹配service包下的所有方法
 *     within(com.xyz.service..*)    //匹配service包或其子包下的所有方法
 *     this(com.xyz.service.AccountService)    //匹配所有实现了AccountService接口的类的代理类的方法（注意是代理类）
 *     target(com.xyz.service.AccountService)    //匹配所有实现了AccountService接口的类的方法（注意是本类）
 *     args(java.io.Serializable)    //匹配只有一个入参，且入参实现了Serializable接口的方法
 *     @target(org.springframework.transaction.annotation.Transactional)    //匹配类上标注了@Transactional注解的类中方法
 *     @within(org.springframework.transaction.annotation.Transactional)    //匹配运行时子类上标注了@Transactional注解的类中方法
 *     @annotation(org.springframework.transaction.annotation.Transactional)    //匹配所有标注了@Transactional注解的方法
 *     @args(com.xyz.security.Classified)    //匹配只有一个入参，且运行时入参有@Classified注解的方法
 *     bean(tradeService)    //匹配命名为tradeService的类的方法
 *     bean(*Service)    //匹配命名后缀为Service的类的方法
 * </pre>
 *
 * <p> Writing Good Pointcuts （写好切点）
 * <p> 在编译期间，AspectJ处理切点以优化匹配性能。检查代码并确定每个连接点是否匹配（静态或动态）给定的切点是一个代价高昂的过程。
 * <pre>
 *     特定的连接点用：execution
 *     某个作用域的连接点用：within
 *     上线文指示符用：this、target和@annotation
 * </pre>
 *
 * <p><b> 5.4.4. Declaring Advice （声明通知） </b>
 *
 * <p> Advice与切点表达式相关联，并且在与切点匹配的方法执行之前（Before advice）、之后（After advice）或周围（Around advice）运行。
 *
 * <p> Before Advice（前置通知）：可以用{@link org.aspectj.lang.annotation.Before}注解在切面中声明前置通知。
 * <pre>
 *     import org.aspectj.lang.annotation.Aspect;
 *     import org.aspectj.lang.annotation.Before;
 *
 *     @Aspect
 *     public class BeforeExample {
 *         //方式一： dataAccessOperation()是上面定义好的切点
 *         @Before("com.xyz.myapp.CommonPointcuts.dataAccessOperation()")
 *         public void doAccessCheck1() {
 *             // ...
 *         }
 *
 *         //方式二： 使用切点表达式
 *         @Before("execution(* com.xyz.myapp.dao.*.*(..))")
 *         public void doAccessCheck2() {
 *            // ...
 *         }
 *     }
 * </pre>
 *
 * <p> After Return Advice（正常返回的后置通知）：当匹配的方法执行正常返回后运行，可以用{@link org.aspectj.lang.annotation.AfterReturning}注解在切面中声明。
 * <pre>
 *     import org.aspectj.lang.annotation.Aspect;
 *     import org.aspectj.lang.annotation.Before;
 *
 *     @Aspect
 *     public class BeforeExample {
 *         @AfterReturning("com.xyz.myapp.CommonPointcuts.dataAccessOperation()")
 *         public void doAccessCheck() {
 *             // ...
 *         }
 *     }
 * </pre>
 *
 * <p> 有时，需要Advice方法中访问返回的实际值，可以使用{@link org.aspectj.lang.annotation.AfterReturning}的形式绑定返回值以获取该访问权限，如下例所示：
 * <pre>
 *     import org.aspectj.lang.annotation.Aspect;
 *     import org.aspectj.lang.annotation.Before;
 *
 *     @Aspect
 *     public class BeforeExample {
 *
 *         // @AfterReturning注解中returning属性的值（此例中是retVal）必须和Advice方法中参数的名称一样。
 *         @AfterReturning(
 *                 pointcut = "com.xyz.myapp.CommonPointcuts.dataAccessOperation()",
 *                 returning = "retVal")
 *         public void doAccessCheck(Object retVal) {
 *             // ...
 *         }
 *     }
 * </pre>
 *
 * <p> After Throwing Advice（抛出异常的后置通知）：当匹配的方法通过抛出异常退出执行时，将运行通知。
 * 可以用{@link org.aspectj.lang.annotation.AfterThrowing}注解在切面中声明。
 * <pre>
 *     import org.aspectj.lang.annotation.Aspect;
 *     import org.aspectj.lang.annotation.Before;
 *
 *     @Aspect
 *     public class BeforeExample {
 *         @AfterThrowing("com.xyz.myapp.CommonPointcuts.dataAccessOperation()")
 *         public void doRecoveryActions() {
 *             // ...
 *         }
 *     }
 * </pre>
 *
 * <p> 通常，如果希望通知仅在抛出给定类型的异常时运行，并且还经常需要访问通知正文中抛出的异常。
 * 可以使用throwing属性来限制匹配，并将抛出的异常绑定到advice参数。
 * <pre>
 *     import org.aspectj.lang.annotation.Aspect;
 *     import org.aspectj.lang.annotation.Before;
 *
 *     @Aspect
 *     public class BeforeExample {
 *
 *         // throwing属性的值必须和Advice方法中参数的名称一样。
 *         // throwing子句还将匹配限制为仅与那些抛出指定类型异常（在本例中为DataAccessException）的方法执行相匹配。
 *         @AfterThrowing(
 *                pointcut = "com.xyz.myapp.CommonPointcuts.dataAccessOperation()",
 *                throwing = "ex")
 *         public void doRecoveryActions(DataAccessException ex) {
 *             // ...
 *         }
 *     }
 * </pre>
 *
 * <p> After Finally Advice：当匹配的方法执行退出时，Advice将运行。类似于try-catch语句中的finally块。
 * <pre>
 *     import org.aspectj.lang.annotation.Aspect;
 *     import org.aspectj.lang.annotation.Before;
 *
 *     @Aspect
 *     public class BeforeExample {
 *         @After("com.xyz.myapp.CommonPointcuts.dataAccessOperation()")
 *         public void doReleaseLock() {
 *             // ...
 *         }
 *     }
 * </pre>
 *
 * <p> Around Advice（环绕通知）：环绕通知“围绕”匹配方法的执行。环绕通知是使用{@link org.aspectj.lang.annotation.Around}注解声明的。
 * Advice方法的第一个参数必须是ProceedingJoinPoint类型。
 * 调用ProceedingJoinPoint的proceed方法会继续下一个通知（Advice）或者目标方法调用。proceed方法还可以传入Object数组（Object[]）参数。
 * <pre>
 *     import org.aspectj.lang.annotation.Aspect;
 *     import org.aspectj.lang.annotation.Around;
 *     import org.aspectj.lang.ProceedingJoinPoint;
 *
 *     @Aspect
 *     public class AroundExample {
 *
 *         @Around("com.xyz.myapp.CommonPointcuts.businessService()")
 *         public Object doBasicProfiling(ProceedingJoinPoint pjp) throws Throwable {
 *             // start stopwatch
 *             Object retVal = pjp.proceed();
 *             // stop stopwatch
 *             return retVal;
 *         }
 *     }
 * </pre>
 *
 * <p> Advice Parameters
 * <p> Spring提供了全类型的advice，这意味着您可以在advice签名中声明所需的参数（正如我们在前面的返回和抛出示例中看到的那样），而不是一直使用Object[]数组。
 *
 * <p> Access to Current JoinPoint （访问当前连接点）
 * <p> 任何Advice（通知）方法都可以声明类型为{@link org.aspectj.lang.JoinPoint}的参数作为其第一个参数
 * （环绕通知需要第一个参数类型为{@link org.aspectj.lang.ProceedingJoinPoint}，它是JoinPoint的子类），
 * <p> JoinPoint接口提供了许多有用的方法：
 * <pre>
 *     getArgs() : 返回方法参数。
 *     getThis() : 返回代理对象。
 *     getTarget() : 返回目标对象。
 *     getSignature() : 返回被通知方法的描述。
 *     toString() : 打印被通知方法的有用描述。
 * </pre>
 *
 * <p> Passing Parameters to Advice （将参数传递给Advice）
 *
 * <pre>
 *     @Before("com.xyz.myapp.CommonPointcuts.dataAccessOperation() && args(account,..)")
 *     public void validateAccount(Account account) {
 *         // ...
 *     }
 * </pre>
 * <p> 上面切点表达式中的args(account,..)部分有两个用途。
 * <p> 第一，它将匹连接点制为至少一个参数的方法，并且参数是Account的实例。
 * <p> 第二，使实际的Account参数Advice可用。
 *
 * <p> 另一种方式是声明一个切点，与它匹配的连接点提供Account对象值，然后在通知（Advice）中引用指定的切点：
 * <pre>
 *     @Pointcut("com.xyz.myapp.CommonPointcuts.dataAccessOperation() && args(account,..)")
 *     private void accountDataAccessOperation(Account account) {}
 *
 *     @Before("accountDataAccessOperation(account)")
 *     public void validateAccount(Account account) {
 *         // ...
 *     }
 * </pre>
 *
 *
 * <p> Advice Parameters and Generics （Advice参数和泛型）
 *
 * <p> Spring AOP 可以处理类声明和方法参数中使用的泛型。
 * <pre>
 *     public interface Sample<T> {
 *         void sampleGenericMethod(T param);
 *         void sampleGenericCollectionMethod(Collection<T> param);
 *     }
 * </pre>
 *
 * <p> 通过将advice参数键入要截取方法的参数类型，可以将方法类型的截取限制为某些参数类型：
 * <pre>
 *     @Before("execution(* ..Sample+.sampleGenericMethod(*)) && args(param)")
 *     public void beforeSampleMethod(MyType param) {
 *         // Advice implementation
 *     }
 * </pre>
 *
 * <p><b> Advice Ordering （通知顺序） </b>
 *
 * <p> 当多个Advice作用于同一个连接点时会发生什么？ Spring AOP遵循与AspectJ相同的优先级规则来确定Advice的执行顺序。优先级最高的Advice在“传入时”
 * 首先运行（两个Before通知，优先级最高的通知第一个运行），在从连接点“退出”时，优先级最高的通知最后运行（两个After通知，优先级最高的通知第二个运行）。
 *
 * <p> 当在不同切面（Aspect）定义的两个通知（Advice）在同一个连接点（Join point）运行时，除非另外指定，否则执行顺序是未定的。
 * 可以通过指定优先级来控制执行顺序。可以通过实现 org.springframework.core.Ordered 接口，或者使用@Order注解来实现。
 *
 *
 * <p><b> 5.4.5. Introductions （引言） </b>
 *
 * <p> Introduction（在AspectJ 中称为类型间声明）使切面（Aspect）声明被通知对象实现给定的接口，并代表这些被通知对象提供该接口的实现。
 *
 * <p> 可以用@DeclareParents注解声明一个引言。此注释用于声明匹配类型有新的父级（即名称）。例如给定一个名为 UsageTracked 的接口以及该接口一个名为
 * DefaultUsageTracked 的实现类，下面这个切面声明service层所有接口的所有实现类也实现UsageTracked接口（例如，通过JMX统计）：
 *
 * <pre>
 *     @Aspect
 *     public class UsageTracking {
 *
 *         @DeclareParents(value="com.xzy.myapp.service.*+", defaultImpl=DefaultUsageTracked.class)
 *         public static UsageTracked mixin;
 *
 *         @Before("com.xyz.myapp.CommonPointcuts.businessService() && this(usageTracked)")
 *         public void recordUsage(UsageTracked usageTracked) {
 *             usageTracked.incrementUseCount();
 *         }
 *     }
 * </pre>
 *
 * <p> 要实现的接口由注解字段的类型决定。@DeclareParents注解的value属性是AspectJ类型模式。任何匹配的bean都实现UsageTracked接口。
 * 注意在上面示例的Before通知中，service层中的bean可以直接用作UserTracked接口的实现。如果以编程方式访问bean，可以像下面这种方式写：
 * <pre>
 *     UsageTracked usageTracked = (UsageTracked) context.getBean("myService");
 * </pre>
 *
 *
 * <p><b> 5.4.6 Aspect Instantiation Models （切面实例化模型） </b>
 *
 * <p> 默认情况下，应用程序上下文中每个切面都有一个实例（单例）。AspectJ称之为单例实例化模型。可以定义具有生命周期的切面。
 * Spring支持AspectJ的perthis 和pertarget 实例化模型。percflow、percflowbelow和pertypewithin 模型目前不支持。
 *
 * <p> 可以通过在{@link org.aspectj.lang.annotation.Aspect}注解中指定perthis子句声明perthis切面。考虑下面示例：
 * <pre>
 *     @Aspect("perthis(com.xyz.myapp.CommonPointcuts.businessService())")
 *     public class MyAspect {
 *
 *         private int someState;
 *
 *         @Before("com.xyz.myapp.CommonPointcuts.businessService()")
 *         public void recordServiceUsage() {
 *             // ...
 *         }
 *     }
 * </pre>
 * <p> 上面的例子中，perthis子句的作用是为执行业务service的每个唯一对象创建一个切面实例。 切面实例是在第一次对service对象调用方法时创建的。
 * 当service对象超出范围时，切面超出范围。在创建切面实例之前，其中的任何通知（Advice）都不会运行。
 * 一旦创建了切面实例，在其中声明的通知就会在匹配的连接点上运行，但仅当服务对象是与此切面关联的对象时。
 *
 *
 * <p><b> 5.8 Proxying Mechanisms （代理机制） </b>
 *
 * <p> Spring AOP使用JDK动态代理或者CGLIB为给定的目标对象创建代理。JDK动态代理内置在Java JDK中，而CGLIB是一个通用的开源类定义库（重新打包到spring-core中）。
 *
 * <p> 如果要代理的目标对象至少实现了一个接口，则使用JDK动态代理。目标类型实现的所有接口都是代理的。如果目标对象没有实现任何接口，则会创建一个CGLIB代理。
 *
 * <p> 如果想要强制使用CGLIB代理（例如，代理为目标对象定义的每个方法，而不仅仅是由其接口实现的方法），可以这样做。但是需要考虑以下问题：
 * <pre>
 *     1. 对于CGLIB，不能通知final方法，因为它们不能在运行时生成的子类中被重写。
 *     2. 从Spring 4.0版本开始，代理对象的构造函数不再被调用两次，因为CGLIB代理实例是通过 Objenesis创建的。
 *        只有当JVM不允许绕过构造函数时，您可能会看到来自Spring的AOP支持的双重调用和相应的调试日志条目。
 * </pre>
 *
 * <p> 要强制使用CGLIB代理，请设置<aop:config>元素中proxy-target-class的值为true：
 * <pre>
 *     <aop:config proxy-target-class="true">
 *         <!-- other beans defined here... -->
 *     </aop:config>
 * </pre>
 *
 * <p> 要在使用@AspectJ自动代理支持时强制CGLIB代理，请设置<aop:aspectj-autoproxy> 的proxy-target-class值为true：
 * <pre>
 *     <aop:aspectj-autoproxy proxy-target-class="true"/>
 * </pre>
 *
 * <p><b> 5.8.1. Understanding AOP Proxies （理解AOP代理） </b>
 *
 * <p> Spring AOP是基于代理的。在写自己的切面或者使用Spring框架提供的任何基于Spring AOP的切面之前，掌握代理是非常重要的。
 *
 * <p> 首先考虑一个场景，有一个普通的没有被代理的，没有什么特别的直接的对象引用，如下代码所示：
 * <pre>
 *     public class SimplePojo implements Pojo {
 *
 *         public void foo() {
 *             // this next method invocation is a direct call on the 'this' reference
 *             this.bar();
 *         }
 *
 *         public void bar() {
 *             // some logic...
 *         }
 *     }
 * </pre>
 *
 *
 *
 *
 *
 *
 * <p> 好文链接: https://blog.csdn.net/q982151756/article/details/80513340
 *
 */

