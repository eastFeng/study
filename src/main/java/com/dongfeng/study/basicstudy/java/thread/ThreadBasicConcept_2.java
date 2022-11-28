package com.dongfeng.study.basicstudy.java.thread;

import java.util.concurrent.TimeUnit;

/**
 * <b> 线程的基本概念 </b>
 * @author eastFeng
 * @date 2021-05-06 11:51
 */
public class ThreadBasicConcept_2 {
    public static void main(String[] args) {
        /*
         * 书《Java编程的逻辑》
         */

        // 创建线程
//        creatThread();
        // 属性和方法
//        propertiesAndMethod();
        // 共享内存
        sharedMemory();

        /*
         * 线程的优点及成本
         *
         * 优点：
         * 1）充分利用多CPU的计算能力，单线程只能利用一个CPU，使用多线程可以利用多CPU的计算能力。
         * 2）充分利用硬件资源，CPU和硬盘、网络是可以同时工作的，一个线程在等待网络IO的同时，
         *   另一个线程完全可以利用CPU，对于多个独立的网络请求，完全可以使用多个线程同时请求。
         * 3）在用户界面（GUI）应用程序中，保持程序的响应性，界面和后台任务通常是不同的线程，
         *   否则，如果所有事情都是一个线程来执行，当执行一个很慢的任务时，整个界面将停止响应，也无法取消该任务。
         * 4）简化建模及IO处理，比如，在服务器应用程序中，对每个用户请求使用一个单独的线程进行处理，
         *   相比使用一个线程，处理来自各种用户的各种请求，以及各种网络和文件IO事件，建模和编写程序要容易得多。
         *
         * 关于线程，它是有成本的。
         * 创建线程需要消耗操作系统的资源，操作系统会为每个线程创建必要的数据结构、栈、程序计数器等，创建也需要一定的时间。
         * 线程调度和切换也是有成本的，当有大量可运行线程的时候，一个线程被切换出去后，操作系统需要保存它的当前上下文状态到内存，
         * 上下文状态包括当前CPU寄存器的值、程序计数器的值等，而一个线程被切换回来后，操作系统需要恢复它原来的上下文状态，
         * 整个过程称为上下文切换，这个切换不仅耗时，而且使CPU中的很多缓存失效。
         *
         * 当然，这些成本是相对而言的，如果线程中实际执行的事情比较多，这些成本是可以接受的；但如果只是执行本节示例中的counter++，那相对成本就太高了。
         * 另外，如果执行的任务都是CPU密集型的，即主要消耗的都是CPU，那创建超过CPU数量的线程就是没有必要的，并不会加快程序的执行。
         */
    }

    /**
     * 创建线程
     */
    public static void creatThread(){
        /*
         * 【线程表示一条单独的执行流】，它有自己的程序执行计数器，有自己的栈。
         * 在Java中创建线程有两种方式：一种是继承Thread；另外一种是实现Runnable接口。
         */

        /*
         * 通过匿名内部类方式创建线程对象t，并重写了run方法。
         * run方法的方法签名是固定的， public，没有参数，没有返回值，不能抛出受检异常。
         * run方法类似于单线程程序中的main方法，线程从run方法的第一条语句开始执行直到结束。
         */
        Thread t = new Thread() {
            @Override
            public void run(){
                System.out.println("Thread t");
                Thread currentThread = Thread.currentThread();
                System.out.println(currentThread.getId());
                System.out.println(currentThread.getName());
            }
        };
        // 线程需要被启动，启动调用Thread的start方法
        t.start();
        /*
         * 【start表示启动该线程，使其成为一条单独地执行流】，
         * 操作系统会分配线程相关的资源，每个线程会有单独的程序执行计数器和栈，
         * 操作系统会把这个线程作为一个独立的个体进行调度，分配时间片让它执行，执行的起点就是run方法。
         *
         * 不调用start，而直接调用run方法呢？
         * 屏幕的输出并不会发生变化，但并不会启动一条单独地执行流，
         * run方法的代码依然是在main线程中执行的，run方法只是main方法调用的一个普通方法。
         *
         * 调用start后，就有了两条执行流，新的一条执行run方法，旧的一条继续执行main方法，
         * 两条执行流并发执行，操作系统负责调度。
         */

        // Thread有一个静态方法currentThread，返回当前执行的线程对象：
        Thread currentThread = Thread.currentThread();
        // 每个Thread都有一个id和name：这样就可以判断代码是在哪个线程执行的
        long id = currentThread.getId();
        String name = currentThread.getName();

        /*
         * 通过继承Thread来实现线程虽然比较简单，但Java中只支持单继承，每个类最多只能有一个父类，如果类已经有父类了，就不能再继承Thread，
         * 这时，可以通过实现java.lang.Runnable接口来实现线程。Runnable接口的定义很简单，只有一个run方法。
         */
        // 函数式编程，lambda方式创建Runnable对象
        Runnable runnable = ()->{
            System.out.println("by implement Runnable");
        };
        // 仅仅实现Runnable是不够的，要启动线程，还是要创建一个Thread对象，但传递一个Runnable对象
        Thread thread = new Thread(runnable);
        // 无论是通过继承Thead还是实现Runnable接口来创建线程，启动线程都是调用start方法。
        thread.start();
    }


    /**
     * 属性和方法
     */
    public static void propertiesAndMethod(){
        Thread t1 = new Thread(() -> {
            System.out.println("lambda thread");
        });

        // 线程有一些基本属性和方法，包括id、name、优先级、状态、是否daemon线程、
        // sleep方法、yield方法、join方法、过时方法等


        // 1. 每个线程都有一个id和name。
        // id是一个递增的整数，每创建一个线程就加一。
        // name的默认值是Thread-后跟一个编号，name可以在Thread的构造方法中进行指定，也可以通过setName方法进行设置，
        // 给Thread设置一个友好的名字，可以方便调试。
        t1.setName("t1");

        // 2. 线程有一个优先级的概念，在Java中，优先级从1到10，默认为5。
        // 这个优先级会被映射到操作系统中线程的优先级，不过，因为操作系统各不相同，不一定都是10个优先级，
        // Java中不同的优先级可能会被映射到操作系统中相同的优先级。
        // 另外，优先级对操作系统而言主要是一种建议和提示，而非强制。
        // 简单地说，在编程中，不要过于依赖优先级。
        // 获取线程优先级
        int priority = t1.getPriority();
        // 设置线程优先级
        t1.setPriority(10);

        // 3. 线程有一个状态的概念，Thread有一个方法用于获取线程的状态：返回值类型为Thread.State，它是一个枚举类型
        Thread.State state = t1.getState();
        /*
         * NEW：创建后尚未启动，没有调用start的线程状态为NEW。
         * TERMINATED：线程运行结束后状态为TERMINATED。
         * RUNNABLE：调用start后线程在执行run方法且没有阻塞时状态为RUNNABLE，
         *          不过，RUNNABLE不代表CPU一定在执行该线程的代码，可能正在执行也可能在等待操作系统分配时间片，只是它没有在等待其他条件。
         *          线程在运行或具备运行条件只是在等待操作系统调度。
         * BLOCKED：没有获取锁。
         *         当前线程不能获得锁的时候，它会加入等待队列等待，线程的状态会变为BLOCKED。
         *         线程在等待锁，试图进入同步块。
         * WAITING、TIMED_WAITING：线程进入了条件等待队列等待。当调用了sleep方法，wait方法
         */

        // 4. 是否daemon（守护）线程
        // 当整个程序中剩下的都是daemon线程的时候，程序就会退出。
        // daemon线程有什么用呢？它一般是其他线程的辅助线程，在它辅助的主线程退出的时候，它就没有存在的意义了。
        boolean daemon = t1.isDaemon();
        t1.setDaemon(false);

        // 5. sleep方法：Thread有一个静态的sleep方法，调用该方法会让当前线程睡眠指定的时间，单位是毫秒。
        // sleep方法不会释放对象锁，也就是说当前线程持有某个对象的锁，即使调用sleep方法，其他线程也无法获取这个对象的锁。
        try {
            // 睡眠期间，该线程会让出CPU
            // 睡眠期间，线程可以被中断，如果被中断，sleep会抛出InterruptedException
            Thread.sleep(1000);
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // 6. yield方法：Thread另外一个让出CPU的静态方法。
        // 调用该方法，是告诉操作系统的调度器：我现在不着急占用CPU，你可以先让其他线程运行。
        // 不过，这对调度器也仅仅是建议，调度器如何处理是不一定的，它可能完全忽略该调用。
        // 同样不会释放锁。
        Thread.yield();

        // 7. join方法：线程等待，Thread有一个join方法，可以让调用join的线程等待该线程结束
        try {
            // 哪个线程调用了线程t1的join方法，哪个线程就要等待线程t1结束
            // 在等待线程结束的过程中，这个等待可能被中断，如果被中断，会抛出Interrupted-Exception。
            t1.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 共享内存
     */
    public static void sharedMemory(){
        /*
         * 共享内存：
         * 每个线程表示一条单独的执行流，有自己的程序计数器，有自己的栈，
         * 但线程之间可以共享内存，它们可以访问和操作相同的对象。
         *
         * 共享内存有两个重要问题，一个是竞态条件，另一个是内存可见性。
         */

        /*
         * 1. 竞态条件
         * 所谓竞态条件（race condition）是指，
         * 【当多个线程访问和操作同一个对象时，最终执行结果与执行时序有关，可能正确也可能不正确】。
         * 期望的结果是100万，但实际执行，发现每次输出的结果都不一样，一般都不是100万，经常是99万多。
         * 为什么会这样呢？因为counter++这个操作不是原子操作，它分为三个步骤：
         * 1）取counter的当前值；
         * 2）在当前值基础上加1；
         * 3）将新值重新赋值给counter。
         * 两个线程可能同时执行第一步，取到了相同的counter值，比如都取到了100，
         * 第一个线程执行完后counter变为101，而第二个线程执行完后还是101，最终的结果就与期望不符。
         *
         * CPU能保证的原子操作是CPU指令级别的，而不是高级语言的操作符。
         *
         * 怎么解决这个问题呢？有多种方法：1. 使用synchronized关键字；2. 使用显式锁；3. 使用原子变量。
         */
        int num = 1000;
        Thread[] threads = new Thread[num];
        for (int i=0; i<num; i++){
            // 创建每个线程并启动
            threads[i] = new RaceConditionDemo();
            threads[i].start();
        }
        // 主线程等待每个线程运行完
        for (int i=0; i<num; i++){
            try {
                threads[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println(count);


        /*
         * 2. 内存可见性
         * 多个线程可以共享访问和操作相同的变量，但一个线程对一个共享变量的修改，另一个线程不一定马上就能看到，甚至永远也看不到。
         *
         * 在计算机系统中，除了内存，数据还会被缓存在CPU的寄存器以及各级缓存中，
         * 当访问一个变量时，可能直接从寄存器或CPU缓存中获取，而不一定到内存中去取，当修改一个变量时，也可能是先写到缓存中，稍后才会同步更新到内存中。
         * 在多线程的程序中，尤其是在有多CPU的情况下，会发生严重的问题。
         * 一个线程对内存的修改，另一个线程看不到：一是修改没有及时同步到内存，二是另一个线程根本就没从内存读。
         *
         * 怎么解决这个问题呢？有多种方法：1. 使用volatile关键字。 2. 使用synchronized关键字或显式锁同步。
         */
        new VisibilityDemo().start();
        try {
            // 主线程休眠1秒
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        flag = false;
        System.out.println("exit main, flag=" + flag);
    }


    // 内存可见性
    private static boolean flag = true;
    private static class VisibilityDemo extends Thread{
        @Override
        public void run(){
            for (int i=0; i<100000; i++){
                if (flag){
                    System.out.println("flag is true "+i);
                }
            }
        }
    }

    // 竞态条件
    private static int count = 0;
    private static class RaceConditionDemo extends Thread{
        @Override
        public void run(){
            for (int i=0; i<1000; i++){
                count++;
            }
        }
    }




}






















































































