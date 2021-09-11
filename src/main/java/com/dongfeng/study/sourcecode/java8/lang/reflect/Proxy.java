package com.dongfeng.study.sourcecode.java8.lang.reflect;

import sun.misc.ProxyGenerator;
import sun.misc.VM;
import sun.reflect.CallerSensitive;
import sun.reflect.Reflection;
import sun.reflect.misc.ReflectUtil;
import sun.security.util.SecurityConstants;

import java.io.Serializable;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.lang.reflect.Modifier;
import java.security.KeyFactory;
import java.security.Permission;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.BiFunction;

/**
 * <b>Proxy提供了创建动态代理类和实例的静态方法，它也是由这些静态方法创建的所有动态代理类的超类/父类（superclass）。</b>
 *
 * <p> 为某接口Foo创建代理：
 * <pre>
 *     InvocationHandler handler = new MyInvocationHandler(...);
 *        Class<?> proxyClass = Proxy.getProxyClass(Foo.class.getClassLoader(), Foo.class);
 *        Foo f = (Foo) proxyClass.getConstructor(InvocationHandler.class).
 *                        newInstance(handler);
 *
 * or more simply:
 *        Foo f = (Foo) Proxy.newProxyInstance(Foo.class.getClassLoader(),
 *                                             new Class<?>[] { Foo.class },
 *                                             handler);
 * </pre>
 *
 * <p> 动态代理类（以下简称代理类）是在创建类时实现运行时指定的接口列表的类，其行为如下所述。
 * 代理接口是由代理类实现的接口。代理实例是代理类的实例。
 * 每个代理实例都有一个关联的调用处理程序（invocation handler）对象，该对象实现了{@link InvocationHandler}接口。
 * 通过代理实例的一个代理接口对代理实例的方法调用将被调度到实例的调用处理程序的invoke方法，
 * 传递的参数为：代理实例、标识被调用方法的{@link java.lang.reflect.Method}对象和包含参数的Object类型数组。
 * 调用处理程序根据需要处理编码的方法调用，它返回的结果将作为代理实例上方法调用的结果返回。
 *
 * <p> 代理类具有以下属性：
 * <pre>
 *     1. 如果所有代理接口都是公共（public）的，那么代理类是public，final ，not abstract。
 *     2. 如果任何一个代理接口是非公共（not public）的，那么代理类是not public，final，not abstract。
 *     3. 未指定代理类的非限定名称。但是应该为代理类保留以字符串 ”$Proxy“ 开头的类名空间。
 *     4. 代理类继承{@link Proxy}类。
 *     5. 代理类完全按照相同的顺序实现创建时指定的接口。
 *     6. 如果代理类实现了非公共（not public）接口，那么它将被定义在与该接口相同的包中。否则代理类的包也未指定。
 *        注意，包封闭不会阻止在运行时在特定包中成功定义代理类，也不会阻止已由同一类加载器和具有特定签名者的同一包定义的类。
 *     7. 由于代理类实现了创建时指定的所有接口，因此在其{@link Class}对象上调用getInterface方法将返回一个包含相同接口列表的数组（按照创建时指定的顺序），
 *        在其{@link Class}对象上调用getMethods方法将返回一个包含这些接口所有方法的Method对象数组，调用getMethod方法将在代理接口中找到特定的方法。
 *     8. {@link #isProxyClass(Class)}方法将返回true，当且仅当传递的类是使用{@link #newProxyInstance(ClassLoader, Class[], InvocationHandler)}方法或者
 *        {@link #getProxyClass(ClassLoader, Class[])}方法动态生成的代理类。否则返回false。
 *     9. 这个java.security.ProtectionDomain代理类的类型与引导类加载程序加载的系统类的类型相同，例如java.lang.Object对象，因为代理类的代码是由受信任的系统代码生成的。
 *        通常会授予此保护域java.security.AllPermission权限.
 *     10. 每个代理类都有一个公共构造函数，它接受一个参数（InvocationHandler接口的实现）来设置代理实例的调用处理程序。
 *         也可以通过调用{@link #newProxyInstance(ClassLoader, Class[], InvocationHandler)}方法。
 * </pre>
 *
 * <p> 代理实例具有以下属性：
 * <pre>
 *     1. 给定一个代理类实例proxy及其代理类实现的一个接口Foo，以下表达式将返回true：
 *        {@code proxy instanceof Foo}                // instanceof 是 Java 的保留关键字。它的作用是测试它左边的对象是否是它右边的类的实例，返回 boolean 的数据类型。
 *       以下将至转换将成功（而不是抛出ClassCastException异常）：
 *        {@code (Foo)proxy}
 *     2. 每个代理实例都有一个关联的调用处理程序，即传递给其构造函数的InvocationHandler接口的实现。
 *        静态方法{@link #getInvocationHandler(Object)}将返回与作为其参数传递的代理实例关联的调用处理程序。
 *     3. 代理实例上的接口方法调用将被编码并分派到调用处理程序的invoke方法，如该方法的文档中所述。
 *     4. 对Object的 hashCode，equals 或者 toString方法和其他方法一样。
 * </pre>
 *
 * <p><b>方法在多个代理接口中重复</b></p>
 *
 * <p> 当代理类的两个或多个接口包含具有相同名称和参数签名的方法时，代理类接口的顺序变得重要。当在代理对象实例上调用这样一个重复方法时，传递给调用处理程序
 * 的{@link java.lang.reflect.Method}参数不一定是其声明类可从调用代理方法的接口的引用类型赋值的对象。
 * 此限制的存在时因为生成的代理类中的相应方法实现无法确定调用它的接口。
 * 因此，当在代理实例上调用重复方法时，在代理类的接口列表中包含方法的最前面接口中的{@link java.lang.reflect.Method}对象将传递给调用处理程序的invoke方法，
 * 不管发生方法调用的引用类型是什么。
 *
 * <p> 如果代理接口包含一个方法与 Object类的hashCode ,equals, toString有相同的名称和参数签名，当在代理实例上调用这样一个方法是，
 * 传递给调用处理程序的{@link java.lang.reflect.Method}参数将具有{@link Object}作为它的声明类。
 * 换句话说，{@link Object}类的public, non-final方法逻辑上在所有代理接口之前，用于确定要传递给调用处理程序的{@link java.lang.reflect.Method}对象。
 *
 * <p>
 *
 * @author eastFeng
 * @date 2021-02-02 18:43
 */
public class Proxy implements Serializable {

    private static final long serialVersionUID = -8052632178764642920L;

    /**
     * 代理类构造函数的参数类型
     */
    private static final Class<?>[] CONSTRUCTOR_PARAMS = { InvocationHandler.class };

    /**
     * 代理类的缓存
     */
    private static final WeakCache<ClassLoader, Class<?>[], Class<?>>
            PROXY_CLASS_CACHE = new WeakCache<>(new KeyFactory(), new ProxyClassFactory());

    /**
     * 此代理实例的调用处理程序
     */
    protected InvocationHandler h;

    /**
     * 禁止实例化
     */
    private Proxy(){}

    /**
     * 构造一个新的Proxy的实例 从具有其调用处理程序的指定值的子类（通常是动态代理类）
     * @param h 此代理实例的调用处理程序
     * @throws NullPointerException 如果参数为null
     */
    protected Proxy(InvocationHandler h){
        Objects.requireNonNull(h);
        this.h = h;
    }

    /**
     * 返回{@link Class}类对象为代理类指定一个类加载器和一个接口数组。
     * 代理类将由指定的类加载器定义，并将实现所有提供的接口。如果任何给定的接口是非公共的（non-public），那么代理类将是非公共的（non-public）。
     * 如果类加载器已经为相同的接口排列定义了代理类，那么将返回现有的代理类；否则将动态生成这些接口的代理类，并由类加载器定义。
     *
     * <p> 传递给该方法的参数有几个限制：
     * <pre>
     *     1. 接口数组中的所有Object对象必须是接口，而不是类或基本类型。
     *     2. 接口数组中的两个元素不能引用相同的Class对象。
     *     3. 所有的接口类型都必须通过制定的类加载器按名称可见。换句话说，对于类加载器cl和每个接口i，一下表达式必须为true：
     *        <pre>
     *            Class.forName(i.getName(), false, cl) == i
     *        </pre>
     *     4. 所有的非公共接口（non-public）接口必须在同一包中；否则，代理类就不能实现所有接口，不管它在哪个包中定义。
     *     5. 对于具有相同签名的指定接口的任何成员方法的集合：
     *        <pre>
     *            5.1 如果任何方法的返回类型是基本类型或void，那么所有方法都必须具有相同的放回类型。
     *            5.2 否则，其中一个方法必须具有可分配给其余方法的所有返回类型的返回类型。
     *        </pre>
     *     6. 生成的代理类不能超过虚拟机对类施加的任何限制。例如，VM可以将类可以实现的接口的数量限制为65535（2^16）；
     *        在这种情况下，接口数组的大小不能超过65535。
     * </pre>
     *
     * <p> 如果违反了这些限制，该方法将抛出IllegalArgumentException异常。如果参数为null，将抛出NullPointerException异常。
     *
     * <p><b>请注意，指定代理接口的顺序很重要：对具有相同接口组合但是顺序不同的代理类的两个请求将导致两个不同的代理类</b></p>
     *
     * @param loader 定义代理类的类加载器
     * @param interfaces 代理类要实现的接口列表
     * @return {@link Class} 在指定的类加载器中定义并实现了指定接口的代理类
     * @throws IllegalArgumentException 如果违反了对可能传递给getProxyClass的参数的任何限制
     * @throws NullPointerException 如果任意参数为null
     * @throws SecurityException 如果安全管理s存在，并且满足以下任何条件：
     * <pre>
     * 1. 给定的类加载器为null，调用方的类加载器不为null，
     *    并且 RuntimePermission("getClassLoader")调用{@link SecurityManager#checkPermission s.checkPermission}会拒绝访问。
     * 2. 对于每个代理接口intf，调用方的类加载器与intf的类加载器不同，或者不是intf的类加载器的祖先，
     *    调用{@link SecurityManager#checkPermission s.checkPermission}会拒绝对intf的访问。
     * </pre>
     */
    @CallerSensitive
    public static Class<?> getProxyClass(ClassLoader loader,
                                         Class<?>... interfaces)
            throws IllegalArgumentException
    {
        Class<?>[] intfs = interfaces.clone();
        // SecurityManager : 安全管理器
        SecurityManager sm = System.getSecurityManager();
        if (sm != null){
            // 检查创建代理类所需的权限
            checkProxyAccess(Reflection.getCallerClass(), loader, intfs);
        }

        // 检查通过


        return null;
    }

    /**
     * <b>检查创建代理类所需的权限</b>
     *
     *
     * @param caller
     * @param loader 类加载器
     * @param interfaces 接口列表
     */
    private static void checkProxyAccess(Class<?> caller,
                                         ClassLoader loader,
                                         Class<?>... interfaces)
    {
        // SecurityManager : 安全管理器
        SecurityManager sm = System.getSecurityManager();
        if (sm != null) {
            ClassLoader ccl = caller.getClassLoader();
            if (VM.isSystemDomainLoader(loader) && !VM.isSystemDomainLoader(ccl)) {
                sm.checkPermission(SecurityConstants.GET_CLASSLOADER_PERMISSION);
            }
            ReflectUtil.checkProxyPackageAccess(ccl, interfaces);
        }
    }

    private static Class<?> getProxyClass0(ClassLoader loader,
                                           Class<?>... interfaces) {
        // 判断接口个数是否超过2^16个
        if (interfaces.length > 65535) {
            throw new IllegalArgumentException("interface limit exceeded");
        }

        // 如果根据提供的类加载器和接口数组能在缓存中找到代理类就直接返回该代理类，
        // 否则会调用ProxyClassFactory工厂创建代理类。
        return PROXY_CLASS_CACHE.get(loader, interfaces);
    }

    // 代理类在{@link WeakCache}对应的的sub-key

    /**
     * 具有0个实现接口的代理类在{@link WeakCache}对应的的sub-key
     */
    private static final Object key0 = new Object();

    /**
     * 具有1个实现接口的代理类在{@link WeakCache}对应的的sub-key
     */
    private static final class Key1 extends WeakReference<Class<?>> {
        private final int hash;

        public Key1(Class<?> referent) {
            super(referent);
            this.hash = referent.hashCode();
        }

        @Override
        public int hashCode(){
            return hash;
        }

        @Override
        public boolean equals(Object obj){
            Class<?> intf;
            return this == obj ||
                    obj != null &&
                            obj.getClass() == Key1.class &&
                            (intf = get()) != null &&
                            intf == ((Key1) obj).get();
        }
    }

    /**
     * 具有2个实现接口的代理类在{@link WeakCache}对应的的sub-key
     */
    private static final class Key2 extends WeakReference<Class<?>> {
        private final int hash;
        private final WeakReference<Class<?>> ref2;

        public Key2(Class<?> referent1, Class<?> referent2) {
            super(referent1);
            this.hash = 31 * referent1.hashCode() + referent2.hashCode();
            ref2 = new WeakReference<>(referent2);
        }

        @Override
        public int hashCode(){
            return hash;
        }

        @Override
        public boolean equals(Object obj) {
            Class<?> intf1, intf2;
            return this == obj ||
                    obj != null &&
                            obj.getClass() == Key2.class &&
                            (intf1 = get()) != null &&
                            intf1 == ((Key2) obj).get() &&
                            (intf2 = ref2.get()) != null &&
                            intf2 == ((Key2) obj).ref2.get();
        }
    }

    /**
     * 生成key的工厂。
     * 将接口数组映射到一个最优键，其中表示接口的Object对象被弱引用。
     */
    private static final class KeyFactory
            implements BiFunction<ClassLoader, Class<?>[], Object>
    {
        @Override
        public Object apply(ClassLoader classLoader, Class<?>[] interfaces) {
//            switch (interfaces.length) {
//                case 1: return Key1(interfaces[0]); // the most frequent
//                case 2: return Key2(interfaces[0], interfaces[1]);
//                case 0: return key0;
//                default: return new java.lang.reflect.Proxy.KeyX(interfaces);
//            }
            return null;
        }
    }

    /**
     * 在给定类加载器和接口数组的情况下生成、定义和返回代理类的工厂。
     */
    private static final class ProxyClassFactory
            implements BiFunction<ClassLoader, Class<?>[], Class<?>>
    {
        // prefix for all proxy class names
        private static final String proxyClassNamePrefix = "$Proxy";

        // next number to use for generation of unique proxy class names
        private static final AtomicLong nextUniqueNumber = new AtomicLong();

        @Override
        public Class<?> apply(ClassLoader loader, Class<?>[] interfaces) {

            Map<Class<?>, Boolean> interfaceSet = new IdentityHashMap<>(interfaces.length);
            for (Class<?> intf : interfaces) {
                /*
                 * Verify that the class loader resolves the name of this
                 * interface to the same Class object.
                 */
                Class<?> interfaceClass = null;
                try {
                    interfaceClass = Class.forName(intf.getName(), false, loader);
                } catch (ClassNotFoundException e) {
                }
                if (interfaceClass != intf) {
                    throw new IllegalArgumentException(
                            intf + " is not visible from class loader");
                }
                /*
                 * Verify that the Class object actually represents an
                 * interface.
                 */
                if (!interfaceClass.isInterface()) {
                    throw new IllegalArgumentException(
                            interfaceClass.getName() + " is not an interface");
                }
                /*
                 * Verify that this interface is not a duplicate.
                 */
                if (interfaceSet.put(interfaceClass, Boolean.TRUE) != null) {
                    throw new IllegalArgumentException(
                            "repeated interface: " + interfaceClass.getName());
                }
            }

            String proxyPkg = null;     // package to define proxy class in
            int accessFlags = Modifier.PUBLIC | Modifier.FINAL;

            /*
             * Record the package of a non-public proxy interface so that the
             * proxy class will be defined in the same package.  Verify that
             * all non-public proxy interfaces are in the same package.
             */
            for (Class<?> intf : interfaces) {
                int flags = intf.getModifiers();
                if (!Modifier.isPublic(flags)) {
                    accessFlags = Modifier.FINAL;
                    String name = intf.getName();
                    int n = name.lastIndexOf('.');
                    String pkg = ((n == -1) ? "" : name.substring(0, n + 1));
                    if (proxyPkg == null) {
                        proxyPkg = pkg;
                    } else if (!pkg.equals(proxyPkg)) {
                        throw new IllegalArgumentException(
                                "non-public interfaces from different packages");
                    }
                }
            }

            if (proxyPkg == null) {
                // if no non-public proxy interfaces, use com.sun.proxy package
                proxyPkg = ReflectUtil.PROXY_PACKAGE + ".";
            }

            /*
             * Choose a name for the proxy class to generate.
             */
            long num = nextUniqueNumber.getAndIncrement();
            String proxyName = proxyPkg + proxyClassNamePrefix + num;

            /*
             * Generate the specified proxy class.
             */
            byte[] proxyClassFile = ProxyGenerator.generateProxyClass(
                    proxyName, interfaces, accessFlags);
            try {
//                return defineClass0(loader, proxyName,
//                        proxyClassFile, 0, proxyClassFile.length);
                return null;
            } catch (ClassFormatError e) {
                /*
                 * A ClassFormatError here means that (barring bugs in the
                 * proxy class generation code) there was some other
                 * invalid aspect of the arguments supplied to the proxy
                 * class creation (such as virtual machine limitations
                 * exceeded).
                 */
                throw new IllegalArgumentException(e.toString());
            }
        }
    }


    public static Object newProxyInstance(ClassLoader loader,
                                          Class<?>[] interfaces,
                                          InvocationHandler h)
            throws IllegalArgumentException
    {
        return null;
    }

    public static boolean isProxyClass(Class<?> cl) {
//        return Proxy.class.isAssignableFrom(cl) && proxyClassCache.containsValue(cl);
        return true;
    }

    @CallerSensitive
    public static InvocationHandler getInvocationHandler(Object proxy)
            throws IllegalArgumentException
    {
        return null;
    }
}
