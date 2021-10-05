package com.dongfeng.study.basicstudy.thread;

import cn.hutool.core.util.RandomUtil;
import java.util.concurrent.*;

/**
 * <b> 线程池 </b>
 *
 * @author eastFeng
 * @date 2021-10-02 18:50
 */
public class ThreadPool {

    public static void main(String[] args) {
        // 异步任务
        yiBuRenWu();

        // 线程池基本概念
        basicConcept();

        // 线程池
        UnderstandingThreadPool();

        // 线程池死锁问题
        deadlockOfThreadPool();


        // 线程池使用示例
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(3, // 核心线程数
                5, // 最大线程数
                1L, TimeUnit.SECONDS, // 非核心线程最长空闲时间
                new ArrayBlockingQueue<>(3), // 阻塞队列
                Executors.defaultThreadFactory(), // 线程工厂
                new ThreadPoolExecutor.AbortPolicy() // 拒绝策略
        );
        // 最多可以执行8个任务（5个最大线程+阻塞队列中3个），第9个任务就会执行拒绝策略
        int taskNum = 9;
        for (int i=0; i<taskNum; i++){
            int finalI = i;
            threadPoolExecutor.execute(() -> {
                System.out.println(Thread.currentThread().getName() + "  办理业务--"+ finalI);
            });
        }

        threadPoolExecutor.shutdown();
    }


    public static void yiBuRenWu(){
        /*
         * 任务执行服务涉及的基本接口：
         * Runnable和Callable：表示要执行的异步任务。
         * Executor[ɪɡˈzekjətə(r)]和ExecutorService：表示执行服务。
         * Future：表示异步任务的结果。
         *
         * Runnable没有返回结果，而Callable有，Runnable不会抛出异常，而Callable会。
         */

        Executor executor = null;
        ExecutorService executorService = null;
        /*
         * Executor表示最简单的执行服务，其定义为：
         * public interface Executor {
         *     void execute (Runnable command);
         * }
         *
         * 就是可以执行一个Runnable，没有返回结果。
         * 接口没有限定任务如何执行，可能是创建一个新线程，可能是复用线程池中的某个线程，也可能是在调用者线程中执行。
         *
         * ExecutorService扩展了Executor，定义了更多服务，基本方法有：
         * public interface ExecutorService extends Executor {
         *     // 有两个关闭方法：shutdown和shutdownNow。
         *     // 区别是，shutdown表示不再接受新任务，但已提交的任务会继续执行，即使任务还未开始执行
         *     // shutdownNow不仅不接受新任务，而且会终止已提交但尚未执行的任务，
         *     // 对于正在执行的任务，一般会调用线程的interrupt方法尝试中断，不过，线程可能不响应中断，
         *     // shutdownNow会返回已提交但尚未执行的任务列表。
         *     void shutdown ();
         *     List<Runnable> shutdownNow ();
         *
         *     boolean isShutdown ();
         *     boolean isTerminated ();
         *     boolean awaitTermination ( long timeout, TimeUnit unit) throws InterruptedException;
         *
         *     // 这三个submit都表示提交一个任务，返回值类型都是Future，返回后，只是表示任务已提交，不代表已执行，
         *     // 通过Future可以查询异步任务的状态、获取最终结果、取消任务等。
         *     <T > Future < T > submit(Callable < T > task);
         *     // 对于Callable，任务最终有个返回值，而对于Runnable是没有返回值的
         *     // 所以提交Runnable的方法可以同时提供一个结果，在异步任务结束时返回
         *     <T > Future < T > submit(Runnable task, T result);
         *     Future<?> submit (Runnable task);
         *
         *     // 两组批量提交任务的方法：invokeAll和invokeAny，它们都有两个版本，其中一个限定等待时间。
         *     // invokeAll等待所有任务完成，返回的Future列表中，每个Future的isDone方法都返回true，
         *     // 不过isDone为true不代表任务就执行成功了，可能是被取消了。invokeAll可以指定等待时间，如果超时后有的任务没完成，就会被取消。
         *     // invokeAny，只要有一个任务在限时内成功返回了，它就会返回该任务的结果，其他任务会被取消；
         *     // 如果没有任务能在限时内成功返回，抛出TimeoutException；如果限时内所有任务都结束了，但都发生了异常，抛出ExecutionException。
         *     <T > List < Future < T >> invokeAll(Collection < ? extends Callable<T>>tasks)
         *         throws InterruptedException;
         *     <T > List < Future < T >> invokeAll(Collection < ? extends Callable<T>>tasks, long timeout, TimeUnit unit)
         *         throws InterruptedException;
         *     <T > T invokeAny(Collection < ? extends Callable<T>>tasks)
         *         throws InterruptedException, ExecutionException;
         *     <T > T invokeAny(Collection < ? extends Callable<T>>tasks, long timeout, TimeUnit unit)
         *         throws InterruptedException, ExecutionException, TimeoutException;
         * }
         */

        Future future = null;
        /*
         * Future表示异步任务的结果。
         * Future接口的定义：
         * public interface Future<V> {
         *
         *     // cancel用于取消异步任务，如果任务已完成、或已经取消、或由于某种原因不能取消，cancel返回false，否则返回true。
         *     // 如果任务还未开始，则不再运行。但如果任务已经在运行，则不一定能取消，
         *     // 参数mayInterruptIfRunning表示，如果任务正在执行，是否调用interrupt方法中断线程，如果为false就不会，
         *     // 如果为true，就会尝试中断线程，但中断不一定能取消线程
         *     boolean cancel ( boolean mayInterruptIfRunning);
         *
         *     // sDone和isCancelled用于查询任务状态。
         *     // isCancelled表示任务是否被取消，只要cancel方法返回了true，随后的isCancelled方法都会返回true，即使执行任务的线程还未真正结束。
         *     boolean isCancelled ();
         *     // isDone表示任务是否结束，不管什么原因都算，可能是任务正常结束，可能是任务抛出了异常，也可能是任务被取消。
         *     boolean isDone ();
         *
         *     // get用于返回异步任务最终的结果，如果任务还未执行完成，会阻塞等待
         *     // 另一个get方法可以限定阻塞等待的时间，如果超时任务还未结束，会抛出TimeoutException。
         *     V get () throws InterruptedException, ExecutionException;
         *     V get ( long timeout, TimeUnit unit)
         *         throws InterruptedException, ExecutionException, TimeoutException;
         * }
         *
         * 我们再来看下get方法，任务最终大概有三种结果：
         * 1）正常完成，get方法会返回其执行结果，如果任务是Runnable且没有提供结果，返回null。
         * 2）任务执行抛出了异常，get方法会将异常包装为ExecutionException重新抛出，通过异常的getCause方法可以获取原异常。
         * 3）任务被取消了，get方法会抛出异常CancellationException。如果调用get方法的线程被中断了，get方法会抛出InterruptedException。
         *
         * Future是一个重要的概念，是实现“任务的提交”与“任务的执行”相分离的关键，是其中的“纽带”，
         * 任务提交者和任务执行服务通过它隔离各自的关注点，同时进行协作。
         */

        /*
         * 基本实现原理
         * ExecutorService的主要实现类是ThreadPoolExecutor，它是基于线程池实现的。
         * ExecutorService有一个抽象实现类AbstractExecutorService。
         * Future的主要实现类是FutureTask。
         */

    }

    public static class MyTask implements Callable<Integer> {

        @Override
        public Integer call() throws Exception {
            int sleepSecond = RandomUtil.randomInt(1, 5);
            TimeUnit.SECONDS.sleep(sleepSecond);
            return sleepSecond;
        }
    }


    public static void basicConcept(){
        /*
         * 线程池是并发程序中一个非常重要的概念和技术。
         *
         * 线程池，顾名思义，就是一个线程的池子，里面有若干线程，
         * 它们的目的就是执行提交给线程池的任务，执行完一个任务后不会退出，而是继续等待或执行新任务。
         *
         * 线程池主要由两个概念组成：一个是任务队列；另一个是工作者线程。
         * 工作者线程主体就是一个循环，循环从队列中接受任务并执行，任务队列保存待执行的任务。
         *
         * 线程池的优点是显而易见的：
         * 它可以重用线程，避免线程创建的开销。
         * 任务过多时，通过排队避免创建过多线程，减少系统资源消耗和竞争，确保任务有序完成。
         *
         * Java并发包中线程池的实现类是ThreadPoolExecutor，它继承自AbstractExecutorService，实现了ExecutorService。
         */
    }

    public static void UnderstandingThreadPool(){
        ThreadPoolExecutor threadPoolExecutor = null;
        /*
         * 主要构造方法
         * public ThreadPoolExecutor(int corePoolSize,    // 核心线程个数
         *                           int maximumPoolSize, // 最大线程个数
         *                           long keepAliveTime,  // 空闲线程存活时间
         *                           TimeUnit unit,       // 时间单位
         *                           BlockingQueue<Runnable> workQueue, // 阻塞队列
         *                           ThreadFactory threadFactory,       // 线程工厂
         *                           RejectedExecutionHandler handler   // 任务拒绝策略
         *                           )
         *
         */

        /*
         * 1．线程池大小
         *
         * 线程池的大小主要与4个参数有关：
         * corePoolSize：核心线程个数。
         * maximumPoolSize：最大线程个数。
         * keepAliveTime和unit：空闲线程存活时间。
         *
         * maximumPoolSize表示线程池中的最多线程数，线程的个数会动态变化，但这是最大值，不管有多少任务，都不会创建比这个值大的线程个数。
         * corePoolSize表示线程池中的核心线程个数，不过，并不是一开始就创建这么多线程，刚创建一个线程池后，实际上并不会创建任何线程。
         *
         * 一般情况下，有新任务到来的时候，如果当前线程个数小于corePoolSiz，就会创建一个新线程来执行该任务，
         * 需要说明的是，即使其他线程现在也是空闲的，也会创建新线程。
         * 不过，如果线程个数大于等于corePoolSiz，那就不会立即创建新线程了，它会先尝试排队，
         * 需要强调的是，它是“尝试”排队，而不是“阻塞等待”入队，如果队列满了或其他原因不能立即入队，它就不会排队，
         * 而是检查线程个数是否达到了maximumPoolSize，如果没有，就会继续创建线程，直到线程数达到maximumPoolSize。
         *
         * keepAliveTime的目的是为了释放多余的线程资源，
         * 它表示，当线程池中的线程个数大于corePoolSize时额外空闲线程的存活时间。
         * 也就是说，一个非核心线程，在空闲等待新任务时，会有一个最长等待时间，即keepAliveTime，如果到了时间还是没有新任务，就会被终止。
         * 如果该值为0，则表示所有线程都不会超时终止。
         *
         * 这几个参数除了可以在构造方法中进行指定外，还可以通过getter/setter方法进行查看和修改。
         */

        /*
         * 2．队列
         *
         * ThreadPoolExecutor要求的队列类型是阻塞队列BlockingQueue，
         * 有多种BlockingQueue，它们都可以用作线程池的队列，比如：
         * LinkedBlockingQueue：基于链表的阻塞队列，可以指定最大长度，但默认是无界的。
         * ArrayBlockingQueue：基于数组的有界阻塞队列。
         * PriorityBlockingQueue：基于堆的无界阻塞优先级队列。
         * SynchronousQueue：没有实际存储空间的同步阻塞队列。
         *
         * 如果用的是无界队列，需要强调的是，线程个数最多只能达到corePoolSize，
         * 到达core-PoolSize后，新的任务总会排队，参数maximumPoolSize也就没有意义了。
         *
         * 对于SynchronousQueue，我们知道，它没有实际存储元素的空间，当尝试排队时，只有正好有空闲线程在等待接受任务时，才会入队成功，
         * 否则，总是会创建新线程，直到达到maximumPoolSize。
         */

        ThreadPoolExecutor.AbortPolicy abortPolicy = null;
        /*
         * 3．任务拒绝策略
         *
         * 如果队列有界，且maximumPoolSize有限，则当队列排满，线程个数也达到了maximumPoolSize，这时，新任务来了，如何处理呢？
         * 此时，会触发线程池的任务拒绝策略。
         *
         * 默认情况下，提交任务的方法（如execute/submit/invokeAll等）会抛出异常，类型为RejectedExecutionException。
         *
         * 不过，拒绝策略是可以自定义的，ThreadPoolExecutor实现了4种处理方式。
         * 1）ThreadPoolExecutor.AbortPolicy：这就是默认的方式，抛出异常。
         * 2）ThreadPoolExecutor.DiscardPolicy：静默处理，忽略新任务，不抛出异常，也不执行。
         * 3）ThreadPoolExecutor.DiscardOldestPolicy：将等待时间最长的任务扔掉，然后自己排队。
         * 4）ThreadPoolExecutor.CallerRunsPolicy：在任务提交者线程中执行任务，而不是交给线程池中的线程执行。
         *
         * 它们都是ThreadPoolExecutor的public静态内部类，都实现了RejectedExecutionHandler接口，这个接口的定义为：
         * public interface RejectedExecutionHandler {
         *     void rejectedExecution (Runnable r, ThreadPoolExecutor executor);
         * }
         * 当线程池不能接受任务时，调用其拒绝策略的rejectedExecution方法。
         *
         * 拒绝策略只有在队列有界，且maximumPoolSize有限的情况下才会触发。
         * 如果队列无界，服务不了的任务总是会排队，但这不一定是期望的结果，因为请求处理队列可能会消耗非常大的内存，甚至引发内存不够的异常。
         * 如果队列有界但maximumPoolSize无限，可能会创建过多的线程，占满CPU和内存，使得任何任务都难以完成。
         * 所以，在任务量非常大的场景中，让拒绝策略有机会执行是保证系统稳定运行很重要的方面。
         */

        ThreadFactory threadFactory = null;
        /*
         * 4．线程工厂
         *
         * 线程池还可以接受一个参数：ThreadFactory。
         * 它是一个接口，定义为：
         * public interface ThreadFactory {
         *     Thread newThread (Runnable r);
         * }
         *
         * 这个接口根据Runnable创建一个Thread, ThreadPoolExecutor的默认实现是Executors类中的静态内部类DefaultThreadFactory，
         * 主要就是创建一个线程，给线程设置一个名称，设置daemon属性为false，设置线程优先级为标准默认优先级，
         * 线程名称的格式为：pool-<线程池编号>-thread-<线程编号>。
         * 如果需要自定义一些线程的属性，比如名称，可以实现自定义的ThreadFactory。
         */

        /*
         * 5．关于核心线程的特殊配置
         * 线程个数小于等于corePoolSize时，我们称这些线程为核心线程，默认情况下：
         * 1）核心线程不会预先创建，只有当有任务时才会创建。
         * 2）核心线程不会因为空闲而被终止，keepAliveTime参数不适用于它。
         *
         * 不过，ThreadPoolExecutor有如下方法，可以改变这个默认行为。
         *
         * // 预先创建一个核心线程，如果所有核心线程都已经创建，则返回false
         * public boolean prestartCoreThread()
         */
    }

    public static void deadlockOfThreadPool(){
        /*
         * 线程池的死锁
         *
         * 关于提交给线程池的任务，我们需要注意一种情况，就是任务之间有依赖，这种情况可能会出现死锁。
         * 比如任务A，在它的执行过程中，它给同样的任务执行服务提交了一个任务B，但需要等待任务B结束。
         * 如果任务A是提交给了一个单线程线程池，一定会出现死锁，A在等待B的结果，而B在队列中等待被调度。
         *
         * 如果是提交给了一个限定线程个数的线程池，也有可能因线程数限制出现死锁。
         *
         * 怎么解决这种问题呢？
         * 可以使用newCachedThreadPool创建线程池，让线程数不受限制。
         * 另一个解决方法是使用SynchronousQueue，它可以避免死锁，怎么做到的呢？
         * 对于普通队列，入队只是把任务放到了队列中，而对于SynchronousQueue来说，入队成功就意味着已有线程接受处理，
         * 如果入队失败，可以创建更多线程直到maximumPoolSize，如果达到了maximumPoolSize，会触发拒绝机制，不管怎么样，都不会死锁。
         */
    }
}
