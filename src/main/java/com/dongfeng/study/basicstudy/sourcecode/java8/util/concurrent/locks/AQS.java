package com.dongfeng.study.basicstudy.sourcecode.java8.util.concurrent.locks;

import sun.misc.Unsafe;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

/**
 * 【AbstractQueuedSynchronizer（抽象队列同步器）】
 *
 * <p> AbstractQueuedSynchronizer是Java并发包java.util.concurrent的基础工具类，
 * 是ReentrantLock、CountDownLatch、Semaphore、FutureTask等类的基础。
 *
 * <p> AQS提供一个框架，用于实现【依赖于FIFO队列的】阻塞锁和相关同步器(比如Semaphore,event等)。
 * 对大多数依赖于单个原子的int值(state)表示状态的同步器，这个类AQS被设计为一个有用的基础。
 * 子类必须提供受保护的方法更改此状态(state)，并且定义此状态在被获取或者释放的对象中意味着什么。
 * 给定这些，这个类中的其他方法执行所有的排队(queuing)和阻塞(blocking)机制。
 * 子类可以维护其他状态字段，但是只有使用getState、setState和compareAndSetState原子性的更新int值，才会被跟踪到同步。
 *
 * <p> 子类应该定义非公共(non-public)的内部类，用于实现其外部的同步属性。
 * AQS类不实现任何同步接口。相反，它定义了一些方法，比如acquireInterruptibly，具体的锁和相关的同步器可以在适当的时候调用这些方法来实现它们的公共方法。
 *
 * <p> 这个类支持默认的独占/排他模式(exclusive mode)和共享模式(shared mode)。
 * 当以独占模式获取时，其他线程尝试获取将无法成功。共享模式下多个线程获取可以(但不需要)成功。
 * 这个类并不“理解”这些区别，除了在机械意义，即当一个共享模式获取成功时，下一个等待线程(如果存在的话)也必须确定它是否可以获取。
 * 不同模式下线程共享同样的FIFO队列。
 * 通常实现子类只支持其中一种模式，但这两种模式都是可以发挥作用，例如在读写锁(ReadWriteLock)中。
 * 只支持独占模式或者共享模式的子类不需要定义支持未使用模式的方法。
 *
 * <p> 这个类定义了一个内部类ConditionObject，可以用作Condition接口的实现 子类提供独占模式的方法isHeldExclusively报告是否对当前线程独占同步。
 * release方法和getState方法完全释放该对象，acquire方法给定保存的状态值(state)，最终将这个对象恢复到以前获取的状态。
 * 没有AQS方法会创建这样的条件，因为如果不能满足约束条件，就不要使用它。
 * 当然，ConditionObject的行为取决于其同步器实现的语义。
 *
 * <p> 该类为内部队列提供了检查、检测和监视方法，以及用于条件对象的相似方法。可以根据需要将它们导出到使用AQS实现同步机制的类中。
 *
 * <p> 该类的序列化只存储底层原子Integer值维护状态，因此反序列化的对象具有空线程队列。
 * 需要序列化的子类将定义一个readObject方法，该方法在反序列时将其恢复到已知的初始状态。
 *
 * <p> 使用这个类作为一个同步器的基础，重新定义以下方法:
 * tryAcquire
 * tryRelease
 * tryAcquireShared
 * tryReleaseShared
 * isHeldExclusively
 * 这些方法在默认情况下会抛出UnsupportedOperationException异常。这些方法的实现必须是内部线程安全的，并且应该简短和不阻塞。
 * 定义这些方法是使用该类唯一受支持的方法(方式)。所有其他方法都被声明为final，因为他们不能独立变化。
 *
 * <p> 从AbstractOwnableSynchronizer继承的方法对于跟踪拥有独占同步器的线程非常有用。 这使得监视和诊断工具能够帮助用户确定哪些线程持有锁。
 *
 * <p> 即使这个类是基于内部的FIFO队列，它也不会自动执行FIFO获取策略。独占同步的核心形式是:
 * Acquire:
 *        while (!tryAcquire(arg)) {
 *           enqueue thread if it is not already queued; 如果线程尚未排队，则将其排队
 *           possibly block current thread; 可能阻塞当前线程
 *        }
 *
 *    Release:
 *        if (tryRelease(arg))
 *           unblock the first queued thread; 解锁第一个排队线程的阻塞
 * (共享模式与此类似，但可能涉及级联信号)
 *
 * 因为check in acquire是在排队之前调用的，所以一个新的获取线程可能会比其他被阻塞和排队的线程提前到达。
 * 但是，如果需要，可以通过内部调用一个或者多个定义的tryAcquire 和/或 tryAcquireShared方法来禁用barging，从而提供一个公平的FIFO获取顺序。
 * 特别的，大多数公平同步器可以定义tryAcquire返回false，如果hasQueuedPredecessors(一个专门为公平同步器设计的方法)方法返回true。
 *
 * <p> 默认的barging(也叫贪心greedy、放弃renouncement和convoy-avoidance)策略的吞吐量和可伸缩量通常最高。
 * 虽然不能保证这是公平或无饥饿的，但是允许较早排队的线程在较晚排队的线程之前重新争用，并且每次重新争用都有公平的机会成功的对抗进入的线程。
 * 另外，虽然acquires不像通常意义上的“自旋”，但它们可能在阻塞之前多次调用tryAcquire方法，并穿插其他计算。
 * 当独占同步只是短暂的进行时，这就提供了自旋的大部分好处，而当不是独占同步时，却没有承担大部分责任。
 * 如果需要，可以在调用之前使用“fast-path”检查来增加这一点，可能预先检查hasContended 和/或 hasQueuedThreads方法，以便仅在同步器可能 不竞争时才这样做。
 *
 * <p> 这个类为同步提供了一个有效的、可伸缩的基础，部分是通过将其使用范围专门化到可以依赖于state、acquire和release参数以及内部FIFO等待队列同步器。
 * 当这些还不够，可以使用原子类从更低的级别构建同步器。
 *
 * @author eastFeng
 * @date 2020-11-07 14:58
 */
public class AQS extends AbstractOwnableSynchronizer implements Serializable {

    private static final long serialVersionUID = -269244655096413555L;

    /**
     * 创建一个初始同步状态（initial synchronization state）为0 的新的AQS对象
     */
    protected AQS(){}

    //----------------------------------------------静态内部类Node------------------------------------------------------//
    /**
     * <b> 队列节点类Node </b>
     *
     * <p>Node保存着线程引用和线程状态的容器，每个线程对同步器的访问，都可以看做是队列中的一个节点
     *
     * <p>等待队列是“CLH”(Craig, Landin, and Hagersten)锁队列的变体。
     * CLH锁通常用于自旋锁。相反，我们使用它们来阻塞同步器，但使用相同的基本策略，即在线程的前节点中保存关于线程的一些控制信息。
     * 每个节点的”state“字段跟踪线程是否应该阻塞。当一个节点的前节点被释放时，它会被告知。队列的每个节点充当一个特定通知样式的监视器，其中包含一个等待线程。
     * 状态字段并不控制线程是否被授予锁等等。如果线程是队列中的第一个线程，它可能会尝试获取锁。但是第一个线程并不能保证获取锁成功，
     *
     *
     */
    static final class Node{
        /**
         * 标识节点当前在共享模式下
         * <p>指示节点在共享模式下等待的标记
         */
        static final Node SHARED = new Node();
        /**
         * 指示节点在独占/排他模式下等待的标记
         * <p>标识节点当前在独占模式下
         */
        static final Node EXCLUSIVE = null;

        //--------------下面几个int常量是给waitStatus用的------------------
        /**
         * waitStatus值，表示当前的线程被取消（取消了争抢这个锁）；
         */
        static final int CANCELLED = 1;
        /**
         * waitStatus值，表示当前节点的后继节点包含的线程需要运行，也就是unpark
         */
        static final int SIGNAL = -1;
        /**
         * waitStatus值，表示当前节点在等待condition，也就是在条件队列中
         */
        static final int CONDITION = -2;
        /**
         * waitStatus值，表示当前场景下后续的acquireShared能够得以执行
         */
        static final int PROPAGATE = -3;

        /**
         * <b> 状态字段 </b>
         * <p>SIGNAL: 这个节点的后继被(或即将)阻塞(通过park)，因此当前节点在释放或者取消时必须释放它的后继。
         *            为了避免竞争，acquire方法必须首先声明它们需要一个信号，然后重试原子性的获取，当失败时，阻塞。
         *
         * <p>CANCELLED: 由于超时(timeout)或者中断(interrupt)，该节点被取消。节点不会离开这个状态。特别是，取消节点的线程不会再次阻塞。
         *
         * <p>CONDITION: 此节点当前处于条件队列(condition queue)中。在传输之前，它不会被用作同步队列节点，此时状态将被设为0。
         *            (这里使用这个值与该场的其他用途无关，但简化了机制。)
         *
         * <p>PROPAGATE: 释放的共享应该传播到其他节点。在doReleaseShared方法中设置这个(仅针对头节点)，以确保传播继续，即使其他操作已经干预。
         *
         * <p>0: 以上都不是
         *
         * <p>waitStatus以数字方式排列以简化使用。非负值意味着节点不需要信号。所以，大多数不需要检查特定的值，只需要检查符号(sign)。
         *
         * <p>对于普通同步节点，该字段被初始化为0，对于条件节点，该字段被初始化为CONDITION。使用CAS修改它(或者在可能的情况下，无条件的volatile写操作)。
         *
         */
        volatile int waitStatus;

        /**
         * <b>前驱节点的引用</b>
         * <p>链接当前节点/线程的前置节点 当前节点依赖于检查waitStatus。
         * 在排队期间分配，只有在退出队列时才为null(为了GC)。
         * 另外，在取消一个前置节点时，寻找一个未取消的节点时短路，因为头节点从未被取消过，所以这个头节点始终存在: 只有在成功获取后，一个节点才会成为头节点。
         * 被取消的线程永远不会成功获取，并且线程只取消自己，而不取消任何其他节点。
         */
        volatile Node prev;
        /**
         * <b>后继节点的引用</b>
         *
         * <p>链接到当前节点/线程的后继节点(释放的节点)。
         * 在排队期间分配，在绕过取消的前置节点时进行调整，在退出队列时为null(为了GC)。
         * enq方法在附加(attachment)之后才分配前置任务的下一个字段，所以看到空的next字段并不一定意味着节点在队列的末尾。
         */
        volatile Node next;

        /**
         * <b> 入队列时的当前线程 </b>
         * <p>将此节点排入队列的线程。在构造时初始化，在使用后为null。
         */
        volatile Thread thread;

        /**
         * 链接到下一个等待状态的节点，或者特殊值SHARED。
         * 因为条件队列只有在排他模式持有时才会被访问，所以我们只需要一个简单的链接队列在节点等待条件时持有它们。
         * 然后将他们传输到队列中重新获取。因为条件只能是排他的，所以我们通过使用特殊值来表示共享模式来保存字段。
         */
        Node nextWaiter;


        /**
         * 如果节点在共享模式下等待，返回true
         */
        final boolean isShared(){
            return nextWaiter == SHARED;
        }

        /**
         * 返回前一个节点，如果为null则抛出NullPointerException异常。
         */
        final Node predecessor() throws NullPointerException{
            Node p = prev;
            if (p == null){
                throw new NullPointerException();
            }else {
                return p;
            }
        }

        /**
         * 用于创建初始头或者SHARED标记
         */
        Node(){}

        /**
         * Used by addWaiter
         */
        Node(Thread thread, Node mode){
            this.nextWaiter = mode;
            this.thread = thread;
        }

        /**
         * Used by Condition
         */
        Node(Thread thread, int waitStatus){
            this.waitStatus = waitStatus;
            this.thread = thread;
        }
    }


    //---------------------------------------------AQS的成员变量(实例字段)------------------------------------------------//
    // 三个成员变量，锁资源的转移（释放再获取）是从头部开始向后进行
    // 将不需要序列化的属性前添加关键字transient,序列化对象的时候,这个属性就不会被序列化。
    /**
     * 等待（阻塞/同步）队列的头节点，懒加载(lazily initialized)。
     * 除了初始化之外，它只通过setHead方法修改。
     * <p><b>【注意】: 如果head存在，它的waitStatus字段保证不会为CANCELLED</b>
     */
    private transient volatile Node head;

    /**
     * 等待（阻塞/同步）队列的尾部(尾节点)，延迟加载(lazily initialized)。
     * 只能通过enq方法通过添加新的等待节点修改。
     * <p>阻塞的尾节点，每个新的节点进来，都插入到最后，也就形成了一个链表
     */
    private transient volatile Node tail;

    /**
     * <b>这个是最重要的，代表当前锁的状态，0代表没有被占用，大于0代表有线程持有当前锁。</b>
     * <p>这个值可以大于1，是因为锁可以重入，每次重入都加上1。
     */
    private volatile int state;


    /**
     * 当前state值，此操作具有volatile读取的内存语义。
     */
    protected final int getState(){
        return state;
    }

    /**
     * 设置state，此操作具有volatile写操作的内存语义。
     */
    protected final void setState(int newState){
        state = newState;
    }

    /**
     * 如果当前状态值等于预期值，则自动将同步状态设置为给定的更新值。此操作具有volatile读和写的内存语义。
     *
     * @param expect 期望值
     * @param update 新值
     */
    protected final boolean compareAndSetState(int expect, int update){
        return unsafe.compareAndSwapInt(this, stateOffset, expect, update);
    }

    /**
     * 纳秒数(nanoseconds)  提高响应能力的
     */
    static final long spinForTimeoutThreshold = 1000L;

    /**
     * <b>将节点(Node)插入队列，必要时进行初始化</b>
     * <p> 到这个方法只有两种可能：等待队列为空，或者有线程竞争入队
     * <p> 采用自旋方式入队：CAS设置tail的过程中，竞争一次竞争不到，就多次竞争，总会排到的。
     * @param node 要插入的节点
     * @return 插入节点的前驱节点
     */
    private Node enq(final Node node){
        for (;;){
            // 尾节点tail
            // 每次循环重新获取尾节点tail，循环过程中，tail节点有可能被其他线程更换
            Node t = tail;
            if (t == null){
                // 队列为null，初始化head节点（初始化时没有设置任何线程）
                if (compareAndSetHead(new Node())){
                    // CAS初始化head节点成功
                    // 注意：这里只是设置了tail=head，这里可没return
                    // 所以，设置完之后，继续for循环，下次就到了下面的else分支了
                    tail = head;
                }
            }else {
                // 尾节点tail不为null，新加的节点放在尾节点后面
                // 有其他线程竞争排不上（CAS更新失败）的话就重新排队
                node.prev = t;
                if (compareAndSetTail(t, node)){
                    // CAS更新尾节点为node成功
                    t.next = node;
                    return t;
                }

                // CAS失败，重新开始下一次循环，重新更新尾节点
            }
        }
    }

    /**
     * <b>为当前线程和给定模式创建节点，并将该节点插入队列</b>
     *
     * @param mode 独占模式: Node.EXCLUSIVE, 共享模式: Node.SHARED
     * @return 新节点
     */
    private Node addWaiter(Node mode){
        // 创建新节点
        Node node = new Node(Thread.currentThread(), mode);
        Node pred = tail;
        if (pred != null){
            // 尾节点tail不为null，将新节点设为尾节点
            // 将当前尾节点tail设置为新节点的前驱节点
            node.prev = pred;
            if (compareAndSetTail(pred, node)){
                // CAS更新尾节点为node成功
                // 实现和之前的尾节点的双向链接
                pred.next = node;
                return node;
            }
        }
        // 尾节点tail为null（队列是空的）或者CAS更新尾节点为node失败（有其他线程在竞争入队）
        enq(node);
        return node;
    }

    /**
     * 将队列的头设置为节点，从而退出队列。 仅由acquire方法调用。
     * 为了GC和不必要的遍历，将不使用的字段设为null
     * @param node 节点
     */
    private void setHead(Node node){
        head = node;
        node.thread = null;
        node.prev = null;
    }

    /**
     * 唤醒后继节点，参数node是头节点head
     */
    private void unparkSuccessor(Node node){
        int ws = node.waitStatus;
        if (ws < 0){
            // 如果head节点的waitStatus是负数，将其修改为0
            compareAndSetWaitStatus(node, ws, 0);
        }

        // 下面的代码就是唤醒后继节点，但是有可能后继节点取消了等待（waitStatus==1）

        Node next = node.next;
        if (next==null || next.waitStatus>0){
            // head的next节点为null或者next节点取消了等待

            next = null;
            // 从尾节点tail往前找，找到排在最前面的未取消的节点
            for (Node t=tail; t!=null&&t!=node; t=t.prev){ //一直遍历到head节点的前一个节点为止
                if (t.waitStatus <= 0){
                    next = t;
                }
            }
        }

        // 到这里，next就是head节点后面最近的一个需要被唤醒(未取消)的节点了
        if (next != null){
            // 唤醒线程
            // 内部调用的sun.misc.Unsafe类的unpark方法
            LockSupport.unpark(next.thread);
        }
    }

    /**
     * <b>共享模式的释放操作 ---- 向后继节点发送信号并确保传播</b>
     *
     * <p>唤醒所有的等待线程</p>
     */
    private void doReleaseShared(){
        // 确保释放传播，即使有其他正在进行的获取/释放（acquires/releases）。
        // 这是按照通常的方式进行的，即在head需要信号的时候尝试解锁它的继任者。但如果不这样做，则将状态设置为PROPAGATE，以确保在发布时继续传播。
        // 我们必须进行循环，以防在执行此操作时，添加了新节点。
        // 另外，与unparkSuccessor方法的其他使用不同，我们需要知道CAS重置状态是否失败，如果失败，需要重新检查。
        for (;;){
            Node h = head;
            // 1. h==null : 说明阻塞队列为空
            // 2. h==tail : 说明头节点head可能是刚刚初始化的节点，或者是普通线程节点，但是此节点现在既然是头节点了，那么代表已经被唤醒了，阻塞队列没有其他节点了
            // 所以这两种情况不需要进行唤醒后继节点
            if (h!=null && h!=tail){
                // 唤醒后继节点

                int ws = h.waitStatus;
                if (ws == Node.SIGNAL){
                    // CAS方式将 head 的 waitStatus 设为 0
                    if (!compareAndSetWaitStatus(h, Node.SIGNAL, 0)){
                        // CAS失败
                        continue;
                    }
                    // 唤醒 head 的继节点
                    unparkSuccessor(h);
                }else if (ws==0 && !compareAndSetWaitStatus(h, 0, Node.PROPAGATE)){
                    // CAS失败的场景: 执行到这里的时候，刚好有一个节点入队，入队会将这个waitStatus设置为 -1
                    continue;
                }
            }

            // 如果到了这里的时候，前面唤醒的线程已经占领了head，那么继续循环
            // 否则，就是head没有变，那么退出循环
            // 退出循环是不是意味着阻塞队列中的其他节点就不唤醒了？
            // 当然不是，唤醒的线程之后还是会调用这个方法，继续唤醒其他节点，以此类推，直到阻塞队列的所有节点都被唤醒
            if (h == head){
                // head节点没有被其他线程更改
                break;
            }
        }
    }

    /**
     * 设置队列头节点，并检查后续节点是否在共享模式下等待，如果是这样，如果propagate > 0或者 PROPAGATE status已经设置。
     */
    private void setHeadAndPropagate(Node node, int propagate){
        // 记录设置之前的头节点为了下面检查
        Node h = head;
        // 设置头节点
        setHead(node);

        /*
         * 如果1. 传播由调用者指示或者由之前的操作记录(这里对waitStatus做了检查，因为PROPAGATE可能转化为SIGNAL)
         * 并且2. 下一个节点在共享模式下等待，或者我们不知道，因为它是null
         * 则尝试通知下一个排队节点
         */
        if (propagate>0
                || h==null
                || h.waitStatus<0
                || (h=head)==null
                || h.waitStatus<0){
            Node next = node.next;  //此时node节点已经是head节点
            if (next==null || next.isShared()){
                // 下一个节点为null或者在共享模式下等待
                // 唤醒（释放）head（也是就node节点）的后继节点
                doReleaseShared();
            }
        }
    }

    /**
     * 取消正在进行的获取尝试
     */
    private void cancelAcquire(Node node){
        // 如果node为null，则忽略
        if (node == null){
            return;
        }

        node.thread = null;

        // 跳过已经取消的前序节点
        // 找到一个合适的前驱节点。其实就是将它前面的队列中已经取消的节点都 “请出去”
        Node pred = node.prev;
        while (pred.waitStatus > 0){
            node.prev = pred = pred.prev;
        }

        // predNex是要unsplice的节点。如果没有(predNext为null)，下面的CAS操作会失败
        Node predNext = pred.next;

        // node状态设置为已取消
        // 这里可以使用无条件写代替CAS。
        // 在这个原子操作之后，其他的节点可以跳过node。
        // 在这之前不受其他线程干扰。
        node.waitStatus = Node.CANCELLED;

        // 如果node是队列的尾节点(tail)，移除node
        if (node==tail && compareAndSetTail(node, pred)){
            compareAndSetNext(pred, predNext, null);
        }else {
            // 如果后续节点需要SIGNAL，
            int ws;
            if (pred!=head
                    && ((ws=pred.waitStatus)==Node.SIGNAL || (ws<=0 && compareAndSetWaitStatus(pred, ws, Node.SIGNAL)))
                    && pred.thread!=null){
                Node next = node.next;
                if (next!=null && next.waitStatus<=0){
                    compareAndSetNext(pred, predNext, next);
                }
            }else {
                unparkSuccessor(node);
            }

            // 帮助GC
            node.next = node;
        }
    }

    /**
     * 检查和更新未能acquire的节点的状态。如果线程阻塞，返回true。需要pred==node.prev
     *
     * <p><b>这个方法说的是：当前线程没有抢到锁，是否需要挂起当前线程</b></p>
     *
     * @param pred 节点的前驱节点
     * @param node 节点
     * @return 如果线程阻塞，返回true。
     */
    private static boolean shouldParkAfterFailedAcquire(Node pred, Node node){
        int ws = pred.waitStatus;
        if (ws == Node.SIGNAL){
            // 前驱节点的waitStatus == -1，说明前驱节点状态正常，当前线程需要挂起，可以直接返回true
            return true;
        }

        if (ws > 0){
            // 前驱节点是CANCELLED(取消)状态，说明前驱节点取消了排队。
            // 这里需要知道：进入阻塞队列排队的线程会被挂起，而唤醒的操作是由前驱节点完成的。
            // 所以下面这块代码说的是将当前节点的prev指向waitStatus<=0的节点，
            // 简单来说，就是找个好爹，因为还需要依赖它来唤醒呢，如果前驱节点取消了排队
            // 找前驱节点的前驱节点做爹，往前遍历总能找一个好爹的
            do {
                node.prev = pred = pred.prev;
            }while (pred.waitStatus > 0); // 前驱节点pred取消了排队
        }else {
            // 到这里前驱节点pred的waitStatus值一定是0或者Node.PROPAGATE
            // 需要用CAS方式把前驱节点pred的waitStatus值更新为Node.SIGNAL（也就是-1）
            compareAndSetWaitStatus(pred, ws, Node.SIGNAL);
        }

        return false;
    }

    /**
     * 中断当前线程
     */
    static void selfInterrupt(){
        Thread.currentThread().interrupt();
    }

    /**
     * 挂起当前线程通过，并返回当前线程是否被中断
     */
    private final boolean parkAndCheckInterrupt(){
        // LockSupport.park方法来挂起线程，然后就停在这里了，等待被唤醒======
        LockSupport.park(this);

        // 当前线程是否被打断
        return Thread.interrupted();
    }

    /**
     * <b>独占模式（不响应中断）</b>
     * <p>以独占不可中断模式获取正在排队的线程
     * <p>这个方法非常重要，应该说真正的线程挂起，然后唤醒后去获取锁，都在这个方法里面了。
     * <p>正常情况下，该方法应该返回false
     */
    final boolean acquireQueued(final Node node, int arg){
        boolean failed = true;

        try {
            // 线程中断标志，记录是否发生了中断
            boolean interrupted = false;
            for (;;){
                final Node p = node.predecessor();
                if (p==head && tryAcquire(arg)){
                    // p==head 说明当前节点虽然进入到了阻塞队列，但是是阻塞队列的第一个，因为它的前驱节点是head
                    // 注意：阻塞队列不包含head节点，head一般指的是占有锁的线程，head后面的才称为阻塞队列
                    // 所以当前节点node可以去试试抢一下锁（tryAcquire方法）
                    // 为什么可以去试试：
                    // 1.首先node它是队头（阻塞队列的头）
                    // 2.当前的head节点可能刚刚初始化（enq方法里提到，head是延时初始化的，而且初始化时没有设置任何线程，
                    //   也就是说当前head节点不属于任何一个线程，所以作为队头，可以去试一试）
                    // tryAcquire方法如果返回true，说明当前节点node尝试抢锁成功

                    // 设置当前节点node为头节点head
                    setHead(node);
                    // p是之前的head节点, 帮助GC
                    p.next = null;
                    failed = false;
                    return interrupted;
                }

                // 到这里说明上面if分支没有成功：要么当前node不是队头；要么就是tryAcquire方法返回false，没有抢赢别人；

                if (shouldParkAfterFailedAcquire(p,node) && parkAndCheckInterrupt()){
                    // 线程阻塞 并且 线程被中断  ---->  中断标志设为true
                    // 被中断后，只是设置一下中断标志interrupted，并没有返回，继续抢锁。
                    interrupted = true;
                }
            }
        } finally {
            // 只有tryAcquire方法抛异常的时候，failed才会为true
            if (failed){
                cancelAcquire(node);
            }
        }
    }

    /**
     * <b>以独占可中断模式获取</b>
     *
     * @param arg 获取参数
     */
    private void doAcquireInterruptibly(int arg)throws InterruptedException{
        final Node node = addWaiter(Node.EXCLUSIVE);
        boolean failed = true;
        try {
            for (;;){
                final Node p = node.predecessor();
                if (p==head && tryAcquire(arg)){
                    setHead(node);
                    p.next = null;
                    failed = false;
                    return;
                }

                if (shouldParkAfterFailedAcquire(p, node) && parkAndCheckInterrupt()){
                    // 被中断，直接抛出异常
                    throw new InterruptedException();
                }
            }
        } finally {
            if (failed){
                cancelAcquire(node);
            }
        }
    }

    private boolean doAcquireNanos(int arg, long nanosTimeout)throws InterruptedException{


        return true;
    }

    /**
     * <b>共享模式（不响应中断）</b>
     * <p>以共享不间断模式获取锁
     * @param arg 获取参数
     */
    private void doAcquireShared(int arg){
        final Node node = addWaiter(Node.SHARED);
        boolean failed = true;
        boolean interrupted = false;
        try {
            for (;;){
                final Node p = node.predecessor();
                if (p == head){
                    int r = tryAcquireShared(arg);
                    if (r >= 0){
                        setHeadAndPropagate(node, r);
                        // 帮助GC
                        p.next = null;
                        if (interrupted){
                            selfInterrupt();
                        }
                        failed = false;
                        return;
                    }
                }
                if (shouldParkAfterFailedAcquire(p, node) && parkAndCheckInterrupt()){
                    interrupted = true;
                }
            }
        } finally {
            if (failed){
                cancelAcquire(node);
            }
        }
    }

    /**
     * <b>共享可中断模式</b>
     * <p>可中断方式获取共享锁</p>
     *
     * @param arg 获取参数
     */
    private void doAcquireSharedInterruptibly(int arg)throws InterruptedException{
        // 1. 入队
        // 为当前线程创建一个共享模式的节点，并加入阻塞队列
        // node : 当前线程所在的新节点
        final Node node = addWaiter(Node.SHARED);

        boolean failed = true;
        try {
            for (;;){
                final Node p = node.predecessor();
                if (p == head){  // 节点node的前驱节点是head节点
                    // 尝试获取共享锁
                    int r = tryAcquireShared(arg);
                    if (r >= 0){  //获取共享锁成功
                        //设置阻塞队列头节点
                        setHeadAndPropagate(node, r);
                        //p节点已经释放，没用了。帮助GC
                        p.next = null;
                        failed = false;
                        return;
                    }
                }
                if (shouldParkAfterFailedAcquire(p, node) && parkAndCheckInterrupt()){
                    // 当前线程被中断，抛出异常
                    throw new InterruptedException();
                }
            }
        } finally {
            if (failed){  // 只有抛出异常才会走到这里
                cancelAcquire(node);
            }
        }
    }

    /**
     * 以共享超时模式获取
     *
     * @param arg 获取参数
     * @param nanosTimeout 最大等待时间（单位：纳秒）
     * @return 如果获取返回true
     * @throws InterruptedException 如果当前线程被打断
     */
    private boolean doAcquireSharedNanos(int arg, long nanosTimeout)throws InterruptedException{
        if (nanosTimeout <= 0){
            return false;
        }
        final long deadline = System.nanoTime() + nanosTimeout;
        final Node node = addWaiter(Node.SHARED);
        boolean failed = true;
        try {
            for (;;){
                final Node p = node.predecessor();
                if (p == head){
                    int r = tryAcquireShared(arg);
                    if (r >= 0){
                        setHeadAndPropagate(node, r);
                        // 帮助GC
                        p.next = null;
                        failed = false;
                        return true;
                    }
                }
                nanosTimeout = deadline - System.nanoTime();
                if (nanosTimeout <= 0){
                    // 已超时
                    return false;
                }
                if (shouldParkAfterFailedAcquire(p, node) && nanosTimeout>spinForTimeoutThreshold){
                    LockSupport.parkNanos(this, nanosTimeout);
                }
                if (Thread.interrupted()){
                    throw new InterruptedException();
                }
            }
        } finally {
            if (failed){
                cancelAcquire(node);
            }
        }
    }


    //---------------------------------------------对外提供的主要方法-----------------------------------------------------/
    /**
     * 【独占模式】
     * <p>在独占模式下尝试获取锁。此方法应该查询对象的状态是否允许以独占模式获取它，如果允许，则获取它。
     * <p>此方法总是由执行acquire操作的线程调用。如果这个方法返回失败，那么acquire方法可能会让线程排队(如果它还没有排队)，
     * 直到其他线程释放它为止。这可以用于实现方法Lock.tryLock().
     * <p>默认实现抛出UnsupportedOperationException异常
     *
     * @param arg 获取参数。这个值始终是传递给获取方法的值，或者是在进入条件等待时保存的值。否则，该值是未解释的，可以表示您喜欢的任何内容。
     * @return 如果成功返回true，一旦成功，就会获得该对象。
     */
    protected boolean tryAcquire(int arg){
        throw new UnsupportedOperationException();
    }

    /**
     * 【独占模式】
     * <p>在独占模式下尝试释放状态（锁）。
     * <p>此方法总是由执行release操作的线程调用。
     * <p>默认实现抛出UnsupportedOperationException异常
     *
     * @param arg 释放的参数。此值始终是传递给release方法的值，或进入条件等待时的当前状态值。否则，该值是未解释的，可以表示您喜欢的任何内容。
     * @return 如果该对象现在处于完全释放状态返回true，那么任何等待的线程都可以尝试获取；否则,则返回false。
     */
    protected boolean tryRelease(int arg){
        throw new UnsupportedOperationException();
    }

    /**
     * 【共享模式】
     * <p>在共享模式下尝试获取。此方法应该查询对象的状态是否允许在共享模式下获取它，如果允许，则获取它。
     * <p>此方法总是由执行acquire操作的线程调用。如果这个方法返回失败，那么acquire方法可能会让线程排队(如果它还没有排队)，直到其他线程释放它为止。
     * <p>默认实现抛出UnsupportedOperationException异常
     *
     * @param arg 获取参数。这个值始终是传递给获取方法的值，或者是在进入条件等待时保存的值。否则，该值是未解释的，可以表示您喜欢的任何内容。
     * @return 失败的时候返回一个负数；
     *         共享模式下获取成功，但是后续共享模式获取不成功，返回0；
     *         共享模式下获取成功，并且后续的共享模式获取也可能成功，则返回正数；在这种情况下，后续的等待线程必须检查可用性；
     *         一旦成功，就会获得该对象。
     */
    protected int tryAcquireShared(int arg){
        throw new UnsupportedOperationException();
    }

    /**
     * 【共享模式】
     * <p>在共享模式下尝试释放状态（锁）。
     * <p>此方法总是由执行release操作的线程调用。
     * <p>默认实现抛出UnsupportedOperationException异常
     *
     * @param arg 释放的参数。此值始终是传递给release方法的值，或进入条件等待时的当前状态值。否则，该值是未解释的，可以表示您喜欢的任何内容。
     * @return 如果共享模式的释放允许等待获取(共享或排他)成功，则为true;否则,则返回false
     */
    protected boolean tryReleaseShared(int arg){
        throw new UnsupportedOperationException();
    }

    /**
     * 持有锁的线程是否是当前线程 :
     * 如果同步只与当前（调用）线程相关，则返回true。
     * 这个方法在每次调用非等待(non-waiting)的AQS.ConditionObject方法时调用。（等待方法改为调用release）
     * <p>默认实现抛出UnsupportedOperationException异常
     *
     * @return 如果同步是独占模式的，则为true；否则返回false
     */
    protected boolean isHeldExclusively(){
        throw new UnsupportedOperationException();
    }

    /**
     * <b>独占模式获取锁</b>
     *
     * <p>在独占模式下获取，忽略中断。
     * 通过至少调用一次tryAcquire方法实现，成功时返回。
     * 否则，线程将排队，可能会反复阻塞和解除阻塞，调用tryAcquire方法直到返回成功；
     * 此方法可以用来实现Lock.lock方法。
     *
     * @param arg 获取参数。这个值被传递给tryAcquire方法
     */
    public final void acquire(int arg){
        // 如果tryAcquire方法返回true(线程获取锁成功)-->该方法(acquire)也就结束了；否则，acquireQueued方法会将当前线程压到阻塞队列中。
        // 首先调用一下tryAcquire方法，如果成功了，也就不需要进阻塞队列排队了
        // tryAcquire方法没有成功，这个时候需要将当前线程挂起，放到阻塞队列中
        if (!tryAcquire(arg) && acquireQueued(addWaiter(Node.EXCLUSIVE), arg)){

            // 进入if里面说明acquireQueued方法返回true, 将调用selfInterrupt方法
            // 所以正常情况下，acquireQueued方法应该返回false
            // 只有线程获取锁的过程中，被中断了acquireQueued方法才会返回true
            selfInterrupt();
        }
    }

    /**
     * <b>独占模式获取锁</b>
     *
     * <p>在独占模式下获取，线程中断时中止。
     * 通过至少调用一次tryAcquire方法实现，成功时返回。
     * 否则，线程将排队，可能会反复阻塞和解除阻塞，调用tryAcquire方法直到返回成功或者线程被中断；
     * 此方法可以用来实现Lock.lockInterruptibly方法。
     *
     * @param arg 获取参数。这个值被传递给tryAcquire方法
     * @throws InterruptedException 如果当前线程被打断
     */
    public final void acquireInterruptibly(int arg)throws InterruptedException{
        if (Thread.interrupted()){
            throw new InterruptedException();
        }
        if (!tryAcquire(arg)){
            doAcquireInterruptibly(arg);
        }
    }

    /**
     * <b>独占模式获取锁</b>
     *
     * <p>在独占模式下尝试获取，线程中断时中止，如果超过给定的超时时间则失败。
     * 首先检查线程中断状态，然后至少调用一次tryAcquire方法，成功后返回。
     * 否则，线程将排队，可能会反复阻塞和解除阻塞，调用tryAcquire，直到成功，或者线程被中断或超时结束。
     * 此方法可以用来实现Lock.tryLock(long, TimeUnit)方法。
     *
     * @param arg 获取参数。这个值被传递给tryAcquire方法
     * @param nanosTimeout 等待的最大纳秒数
     * @return 如果获取返回true，超时返回false
     * @throws InterruptedException 如果当前线程被打断
     */
    public final boolean tryAcquireNanos(int arg, long nanosTimeout)throws InterruptedException{
        if (Thread.interrupted()){
            throw new InterruptedException();
        }
        return (tryAcquire(arg) || doAcquireNanos(arg, nanosTimeout));
    }


    /**
     * <b>独占模式释放锁</b>
     *
     * <p>以独占模式释放（Releases）。如果tryRelease方法返回true，通过释放一个或者多个线程实现。
     * 该方法可以用来实现 Lock.unlock 方法。
     *
     * @param arg 释放参数。这个值被传递给tryRelease方法
     * @return tryRelease方法的返回值
     */
    public final boolean release(int arg){
        if (tryRelease(arg)){
            Node h = head;
            if (h!=null && h.waitStatus!=0){
                // 唤醒head节点之后的一个阻塞节点
                unparkSuccessor(h);
            }
            return true;
        }
        return false;
    }


    /**
     * 【共享模式】
     * <p>以共享模式获取，忽略中断。通过首先至少调用一次tryAcquireShared方法来实现，并在成功时返回。
     * 否则线程将排队，可能会反复阻塞和取消阻塞，调用tryAcquireShared方法直到成功。
     *
     * @param arg 获取参数。这个值被传递给tryAcquireShared方法
     */
    public final void acquireShared(int arg){
        if (tryAcquireShared(arg)<0){
            // 小于0，获取失败，则需要重复调用tryAcquireShared直至成功
            doAcquireShared(arg);
        }
    }


    /**
     * 以共享模式获取，如果中断则中止。首先检查中断状态，然后调用至少一次tryAcquireShared方法，然后在成功时返回。
     * 否则，线程将排队，可能会反复阻塞和取消阻塞，调用tryAcquireShared方法直到成功或线程被中断。
     *
     * @param arg 获取参数。这个值被传递给tryAcquireShared方法
     * @throws InterruptedException 如果当前线程被中断
     */
    public final void acquireSharedInterruptibly(int arg)throws InterruptedException{
        // 因为该方法是响应中断的，首先判断当前线程有没有被打断
        if (Thread.interrupted()){
            throw new InterruptedException();
        }

        // 先调用一次 tryAcquireShared 方法，尝试获取一下锁
        if (tryAcquireShared(arg) < 0){
            // 第一次尝试获取锁失败
            doAcquireSharedInterruptibly(arg);  // 该方法内部会循环调用 tryAcquireShared 方法
        }
    }


    /**
     * 尝试以共享模式获取，如果中断则中止，如果超过给定的超时，则失败。
     * 首先检查中断状态，然后调用至少一次tryAcquireShared，然后在成功时返回。
     * 否则，线程将排队，可能会反复阻塞和取消阻塞，调用tryAcquireShared，直到成功、线程中断或超时时间过去。
     *
     * @param arg 获取参数。这个值被传递给tryAcquireShared方法
     * @param nanosTimeout 等待的最大纳秒数
     * @return 获取时为true；超时时为false
     * @throws InterruptedException  如果当前线程被中断
     */
    public final boolean tryAcquireSharedNanos(int arg, long nanosTimeout)throws InterruptedException{
        if (Thread.interrupted()){
            throw new InterruptedException();
        }
        return tryAcquireShared(arg)>=0 || doAcquireSharedNanos(arg, nanosTimeout);
    }


    /**
     * 以共享模式释放。如果tryReleaseShared方法返回true，则通过取消阻止一个或多个线程来实现。
     *
     * @param arg 释放参数。这个值被传递给tryRelease方法
     * @return tryRelease方法的返回值
     */
    public final boolean releaseShared(int arg){
        if (tryReleaseShared(arg)){
            // 返回true，唤醒阻塞队列的线程
            doReleaseShared();
            return true;
        }
        return false;
    }


    //------------------------------------------------队列检验方法-------------------------------------------------------/
    /**
     * 查询是否有线程正在排队。请注意，由于中断和超时导致的取消可能会在任何时候发生，返回true并不保证任何其他线程都会获得。
     * <p>在这个实现中，此操作以恒定时间返回。
     *
     * @return 如果可能有其他线程在排队，返回true
     */
    public final boolean hasQueuedThreads(){
        // 头节点等于尾节点，没有线程等待
        // 头节点不等于尾节点，有线程等待
        return head != tail;
    }

    /**
     * 查询是否有任何线程曾争用此同步器；也就是说，如果某个acquire方法曾经阻塞过。
     * <p>在这个实现中，此操作以恒定时间返回。
     *
     * @return 如果曾经有过，返回true
     */
    public final boolean hasContended(){
        return head != null;
    }

    /**
     * 返回队列中的第一个（等待时间最长）线程，如果当前没有线程排队，则返回null。
     * <p>在这个实现中，此操作通常以固定的时间返回，但如果其他线程同时修改队列，则可能在争用时迭代。
     *
     * @return 返回队列中的第一个（等待时间最长）线程，如果当前没有线程排队，则返回null。
     */
    public final Thread getFirstQueuedThread(){
        // head==tail : 队列为空
        return (head == tail) ? null : Thread.currentThread();
    }


    /**
     * 离头节点head最近的一个节点的thread
     */
    private Thread fullGetFirstQueuedThread(){
        /*
         * 【第一个节点通常是head.next（而不是头节点head）】。
         * 尝试获取它的thread字段，确保读取的一致性：
         * 如果thread字段为null或者s.prev不再是head，那么其他线程在一些读取之间并发执行了setHead。
         * 在使用遍历之前，尝试获取两次。
         */
        Node h, s;
        Thread st;
        if (
                //第一次获取
                ((h=head)!=null && (s=h.next)!=null && s.prev==head && (st=s.thread)!=null) ||
                //第二次获取
                ((h=head)!=null && (s=h.next)!=null && s.prev==head && (st=s.thread)!=null)){
            return st;
        }

        /*
         * head.next节点可能尚未设置，或者可能在setHead之后未设置。
         * 所以我们必须检查tail是否是第一个节点。如果不是，我们继续，安全地从尾部到头部找到第一个，保证终止
         */
        Node t = tail;
        Thread firstThread = null;
        // 第一次循环，tail是否是head
        while (t!=null && t!=head){
            Thread tt = t.thread;
            if (tt != null){
                firstThread = tt;
            }
            // 从尾节点往前遍历
            t = t.prev;
        }
        // 离头节点head最近的一个节点的thread
        return firstThread;
    }


    /**
     * 如果给定的线程在队列中排队，则返回true。
     * <p>此实现遍历队列以确定给定线程的存在。
     *
     * @param thread 要判断的线程
     * @return 如果给定线程在队列中，则为true
     */
    public final boolean isQueued(Thread thread){
        if (thread == null){
            throw new NullPointerException();
        }

        // 从尾节点开始遍历队列
        for (Node p=tail; p!=null; p=p.prev){
            if (p.thread == thread){
                return true;
            }
        }
        return false;
    }

    /**
     * 如果第一个排队的线程（如果存在）正在以独占模式等待，则返回true。
     *
     */
    final boolean apparentlyFirstQueuedIsExclusive(){
        Node h,s;

        // head节点不为null
        return (h=head)!=null
                // head节点的下一个节点也不为null
                && (s=h.next)!=null
                // 下一个节点是独占模式
                && !s.isShared()
                // 下一个节点的线程不为null
                && s.thread!=null;
    }


    /**
     * 查询是否有任何线程等待获取锁的时间比当前线程长。
     * <p>调用此方法相当于调用： getFirstQueuedThread() != Thread.currentThread() && hasQueuedThreads()。但是更有效
     * <p>请注意，由于中断和超时可能会在任何时候取消，所以返回true并不能保证其他线程会在当前线程之前获取锁。
     * 同样，由于队列为空，返回false，其他线程也可能赢得排队竞争。
     *
     * <p>该方法被设计用于公平同于器，以避免碰撞。
     * 如果该方法返回true，这种同步器的tryAcquire方法应该返回false，并且tryAcquireShared方法应该返回一个负数。
     * （除非这是一个可重入的获取，可重入锁）
     * 例如，一个公平、可重入、独占模式的同步器的tryAcquire方法可能如下所示：
     * <pre>
     * protected boolean tryAcquire(int arg) {
     *    if (isHeldExclusively()) {
     *      // A reentrant acquire; increment hold count
     *      return true;
     *    } else if (hasQueuedPredecessors()) {
     *      return false;
     *    } else {
     *      // try to acquire normally
     *    }
     *  }</pre>
     *
     * @return true:有
     */
    public final boolean hasQueuedPredecessors(){
        Node t = tail;
        Node h = head;
        Node s;
        //队列不为空
        return h!=t
                //头节点head的下一个节点为null或者下一个节点的thread不等于当前线程
                && ((s=h.next)==null || s.thread!=Thread.currentThread());
    }


    //-------------------------------------Instrumentation and monitoring methods--------------------------------------/
    /**
     * 队列中排队的线程数目的估计值。这个值只是一个估计值，因为当此方法遍历队列的时候，线程数目可能会动态的变化。
     * 这个方法是为监视系统状态而设计的，而不是用于同步控制。
     *
     * @return 估计值
     */
    public final int getQueueLength(){
        int n = 0;
        for (Node p=tail; p!=null; p=p.prev){
            if (p.thread!=null){
                ++n;
            }
        }
        return n;
    }

    /**
     * 队列中正在排队的线程的集合。
     * 也是一个估计值。并且集合的元素没有特定的顺序。
     *
     * @return 线程的集合
     */
    public final Collection<Thread> getQueuedThreads(){
        ArrayList<Thread> list = new ArrayList<>();
        for (Node p=tail; p!=null; p=p.prev){
            Thread t = p.thread;
            if (t != null){
                list.add(t);
            }
        }
        return list;
    }


    /**
     * 在独占模式下排队的线程的集合。
     */
    public final Collection<Thread> getExclusiveQueuedThreads(){
        ArrayList<Thread> list = new ArrayList<>();
        for (Node p=tail; p!=null; p=p.prev){
            if (!p.isShared()){
                //节点p是独占模式
                Thread t = p.thread;
                if (t != null){
                    list.add(t);
                }
            }
        }
        return list;
    }


    /**
     * 在共享模式下排队的线程的集合。
     */
    public final Collection<Thread> getSharedQueuedThreads(){
        ArrayList<Thread> list = new ArrayList<>();
        for (Node p=tail; p!=null; p=p.prev){
            if (p.isShared()){
                Thread t = p.thread;
                if (t!=null){
                    list.add(t);
                }
            }
        }
        return list;
    }


    @Override
    public String toString(){
        int s = getState();
        String q = hasQueuedThreads() ? "non" : "";
        return super.toString() + "[State = " +s+ ", " +q+ "empty queue]";
    }


    //----------------------------------------内部对Conditions支持的方法--------------------------------------------------/
    /**
     * 如果一个节点（始终是最初放在条件队列中的节点）现在正在队列中重新获取，则返回true。
     * 该方法用于判断节点是否已经转移到阻塞队列了
     *
     * @param node 节点
     * @return 如果正在重新获取，返回true。
     */
    final boolean isOnSyncQueue(Node node){
        //转移到阻塞队列中的时候，node的 waitStatus 会置为0
        //如果 waitStatus 还是Node.CONDITION(也就是-2)，那肯定就是还在条件队列中
        //如果 node 的前驱节点prev 指向还是null,说明node肯定没有在阻塞队列（prev是阻塞队列链表中使用的）
        if (node.waitStatus==Node.CONDITION || node.prev==null){
            // 还在条件队列中
            return false;
        }

        //如果node已经有后继节点next的时候，那肯定是在阻塞队列了
        if (node.next != null){
            return true;
        }

        //findNodeFromTail: 从阻塞队列的队尾开始往前遍历，如果找到相等的，说明在阻塞队列中，否在就不在阻塞队列中
        //可以通过node.prev!=null 得出node在阻塞队列中吗？ 答案是：不能
        //node的前驱节点可以不为null，但是节点还没有在阻塞队列中
        //在AQS入队的过程中，首先设置 node.prev 指向tail, 然后CAS操作将自己设置为新的tail，可是这次CAS是可能失败的。（enq方法）
        return findNodeFromTail(node);
    }


    /**
     * 节点在阻塞队列中，则返回true。
     * <p> 从阻塞队列的队尾往前遍历，如果找到返回true。
     * <p> 仅由isOnSyncQueue方法调用。
     *
     * @return 存在，返回true
     */
    private boolean findNodeFromTail(Node node){
        Node t = tail;
        for (;;){
            if (t == node){
                return true;
            }
            if (t==null){
                return false;
            }
            t = t.prev;
        }
    }


    /**
     * 将节点从条件队列传输到同步（阻塞）队列。如果成功，则返回true。
     */
    final boolean transferForSignal(Node node){
        // If cannot change waitStatus, the node has been cancelled.
        // 如果无法改变该节点的waitStatus值，说明此node的waitStatus已不是Node.CONDITION，则该节点已被取消
        // 既然已经取消，也就不需要转移了，方法返回
        if (!compareAndSetWaitStatus(node, Node.CONDITION, 0)){
            //CAS失败：返回false
            return false;
        }

        //CAS成功，节点node的waitStatus设置为0成功

        //节点node插入阻塞队列，返回的节点p是node的前置(前驱)节点
        Node p = enq(node);

        int ws = p.waitStatus;
        //ws>0 : 说明node在阻塞队列中的前驱节点取消了等待锁，直接唤醒node对应的线程。
        //如果ws<=0 : 那么compareAndSetWaitStatus将会被调用
        if (ws>0 || !compareAndSetWaitStatus(p, ws, Node.SIGNAL)){
            //如果前驱节点取消或者CAS失败，会进到这里唤醒node对应的线程
            LockSupport.unpark(node.thread);
        }

        return true;
    }


    /**
     * 只有线程处于中断状态，才会调用此方法
     * <p> 判断线程是否在 signal之前发生中断
     * <p> 如果必要，将这个已经取消等待的节点转移到阻塞队列。
     * <p> 如果线程在 signal（唤醒）之前发生中断，则返回true。 否则返回false
     */
    final boolean transferAfterCancelledWait(Node node){
        // CAS方式将节点状态设为 0
        // 如果成功，说明是signal方法之前发生的中断，因为signal中会将waitStatus设置为0
        if (compareAndSetWaitStatus(node, Node.CONDITION, 0)){
            // 该节点node放入阻塞队列
            // 这里可以看到，即使中断了，node依然会转移到阻塞队列
            enq(node);
            return true;
        }

        // 走到这里，说明上面的CAS失败，肯定是因为signal方法已经将waitStatus设置为了0
        // 还要判断node是否已经转移到阻塞队列，signal方法会先将node的waitStatus设置为0，
        // 然后调用enq(node)将node转移到阻塞队列，enq中会自旋（循环）方式将node插入阻塞队列
        // 所有这里要循环等待node节点成功插入阻塞队列
        while (!isOnSyncQueue(node)){ // node没有转移到阻塞队列
            Thread.yield();
        }

        // node已经转移到阻塞队列
        return false;
    }

    /**
     * 【完全释放独占锁】
     * <p> 使用当前state值调用release方法，返回state值。
     * <p> release方法失败时：节点的waitStatus设为Node.CANCELLED，并抛出异常。
     * <p> 如果一个线程在没有持有锁的时候就去调用await()方法，它能进入条件队列，
     * 但是释放锁肯定会失败，release(savedState)方法会抛异常，进入到异常分支，
     * 然后进入finally块。
     *
     * @param node 等待的条件节点
     * @return 之前的同步状态（state）
     */
    final int fullyRelease(Node node){
        boolean failed = true;
        try {
            //savedState : release之前的state值。
            int savedState = getState();
            //使用了当前的state值作为release方法的参数，也就是完全释放掉锁，将state置为0
            if (release(savedState)){  //完全释放锁的具体实现
                failed = false;
                return savedState;
            }else {
                //释放锁失败，抛出异常
                throw new IllegalMonitorStateException();
            }
        } finally {
            if (failed){
                //释放锁失败，将当前节点状态设为 “取消” 状态
                node.waitStatus = Node.CANCELLED;
            }
        }
    }

    //transient : 不参与序列化

    /**
     * ConditionObject可以控制线程的 “等待” 和 “唤醒”。
     * 为并发编程中的同步提供了等待通知的实现方式，可以在不满足某个条件的时候挂起线程等待。直到满足某个条件的时候在唤醒线程。
     * <ol>
     * <li> 条件队列的节点和阻塞（等待）队列的节点都是Node的实例，因为条件队列的节点需要转移到阻塞队列中去。
     * <li> ReentrantLock实例可以通过多次调用newCondition方法来产生多个Condition实例。
     *       注意：ConditionObject只有两个属性firstWaiter和lastWaiter;
     * <li> 每个Condition有一个关联的条件队列，如果线程1调用condition1.await()方法即可将当前线程1包装成Node后加入到条件队列中，
     *       然后阻塞在这里，不继续往下执行，条件队列是一个单向链表。
     * <li> 调用condition1.signal()方法触发一次唤醒，此时唤醒的是队头，会将condition1对应的条件队列的firstWaiter(队头)移到阻塞队列的队尾，
     *       等待获取锁，获取锁后await方法才能返回，继续往下执行。
     * </ol>
     */
    public class ConditionObject implements Condition, java.io.Serializable{

        private static final long serialVersionUID = -8086410572002256969L;

        /**
         * 【条件队列】的第一个节点
         */
        private transient Node firstWaiter;
        /**
         * 【条件队列】的最后一个节点
         */
        private transient Node lastWaiter;

        public ConditionObject(){}

        //-------------------private方法---------------------
        /**
         * 条件队列添加新的节点
         * 将当前线程对应的节点放入条件队列队尾。
         *
         * @return 新的条件节点
         */
        private Node addConditionWaiter(){
            Node t = lastWaiter;
            //如果条件队列的最后一个节点取消了，将其清除出去
            //为什么 waitStatus 不等于 Node.CONDITION 就判定为该节点取消了排队？
            if (t!=null && t.waitStatus!=Node.CONDITION){
                //遍历整个条件队列，然后会将已取消的所有节点清除出队列
                unlinkCancelledWaiters();
                t = lastWaiter;
            }

            //当前线程封装成Node，指定 waitStatus 为 Node.CONDITION
            Node node = new Node(Thread.currentThread(), Node.CONDITION);
            //t此时是lastWaiter，条件队列队尾
            if (t == null){
                //条件队列为空
                firstWaiter = node;
            }else {
                //条件队列不为空，当前节点为队尾的下一个节点
                t.nextWaiter = node;
            }
            //当前节点为队尾
            lastWaiter = node;
            return node;
        }

        /**
         * 从条件队列队头往后遍历，找出第一个需要转移的 node 并转移到阻塞队列, 因为有些线程会取消排队，但是可能还在队列中
         * 移除并传输节点，直到命中一个非取消状态的节点或者为null的节点。
         *
         * @param first 条件队列中的第一个节点（非null）
         */
        private void doSignal(Node first){
            do {
                //每次循环 firstWaiter=first.nextWaiter 都会执行。从新赋值
                //如果将first移除后，后面没有节点在等待了，那么需要将lastWaiter置为null
                if ((firstWaiter = first.nextWaiter) == null){
                    //first节点后面没有节点了，将lastWaiter置为null
                    lastWaiter = null;
                }
                //因为first马上要被转移到阻塞队列了，和条件队列的链接关系在这里断掉
                first.nextWaiter = null;

                //while里面: first=firstWaiter（first.nextWaiter） -->向first后面第一个节点转移
            }while (!transferForSignal(first) && (first=firstWaiter)!=null); //如果first转移不成功，那么选择first后面的第一个节点进行转移，以此类推
        }


        /**
         * 唤醒所有节点
         */
        private void doSignalAll(Node first){
            lastWaiter = firstWaiter = null;
            do {
                Node next = first.nextWaiter;
                first.nextWaiter = null;

                transferForSignal(first);
                first = next;
            }while (first != null);
        }

        /**
         * 用于清除条件队列中已经取消等待的节点
         *
         * 条件队列是一个单向链表，遍历链表将已经取消等待的节点清除出去
         */
        private void unlinkCancelledWaiters(){
            Node t = firstWaiter;
            //trail : 离lastWaiter节点最近的一个没有被取消的节点
            Node trail = null;
            while (t != null){
                Node next = t.nextWaiter;
                //如果节点的状态(waitStatus)不是 Node.CONDITION 的话，这个节点就是被取消的
                if (t.waitStatus != Node.CONDITION){
                    //节点t是被取消的
                    //清除节点t，方便GC
                    t.nextWaiter = null;
                    if (trail == null){
                        //t和t之前的节点都是被取消的
                        firstWaiter = next;
                    }else {
                        //trail和t之间被取消的节点给过滤掉
                        trail.nextWaiter = next;
                    }
                    if (next == null){
                        //t是最后一个节点（遍历到最后一个节点lastWaiter），并且lastWaiter是被取消的，需要重新设置lastWaiter
                        //trail : 离lastWaiter最近的一个没有被取消的节点
                        lastWaiter = trail;
                    }
                }else {
                    //节点t不是被取消的
                    trail = t;
                }

                //判断下一个节点
                t = next;
            }
        }

        // -------------------public方法-----------------------------
        // 调用await 和 signal之前肯定是获取到了独占锁 （没有获取锁也可以调用，但是会失败；正常经常下肯定要先获取独占锁，再调用这俩方法）
        // await : 使当前线程等待
        // signal : 唤醒等待的线程
        // 【signal中会将要唤醒的节点从条件队列转移到阻塞队列的队尾】 , signal会调用transferForSignal方法, transferForSignal方法会调用【enq方法】
        // 【await中也有可能将节点从条件队列转移到阻塞队列的队尾】, await可能会调用checkInterruptWhileWaiting方法,
        // checkInterruptWhileWaiting方法可能会调用transferAfterCancelledWait方法, transferAfterCancelledWait方法【enq方法】

        //// 【所有的await方法都会先完全释放独占锁，并且后面会重新获取独占锁，
        // 释放锁到重新获取锁中间这段时间（这段时间就是等待啦 ps:个人理解）, 其他线程可以获取锁执行自己的任务。】////

        /**
         * 【响应中断的等待】
         *
         * <p>这个方法会阻塞，直到调用signal方法（指 signal()和signalAll()俩方法，下同），或被中断。
         *
         * <ol>
         * <li> 如果当前线程被中断，抛出InterruptedException异常。
         * <li> 保存getState方法返回的锁的状态。
         * <li> 以保存的state作为参数调用release方法，如果失败，抛出IllegalMonitorStateException异常。
         * <li> 阻塞直到有信号或者被中断。
         * <li> 通过使用保存的state作为参数调用acquire方法来重新获取。
         * <li> 如果在步骤4阻塞时被打断，则抛出InterruptedException异常。
         * </ol>
         * @throws InterruptedException 中断异常
         */
        @Override
        public void await() throws InterruptedException {
            // 既然该方法要响应中断，那么在最开始就判断中断状态
            if (Thread.interrupted()){
                // 当前线程中断，抛出InterruptedException异常
                throw new InterruptedException();
            }

            // 添加新的等待节点---->当前线程添加到condition的条件队列中
            Node node = addConditionWaiter();

            // 在调用await方法之前当前线程已经获取了独占锁，所以这里可以释放独占锁
            // 返回值是释放锁之前的state值，调用await()方法之前，当前线程是必须持有锁的，这里肯定要释放掉
            //************** 【完全释放独占锁】 **************** //
            int savedState = fullyRelease(node);

            int interruptMode = 0;
            // 4. 释放掉锁之后，如果自己还没有到【阻塞队列】，那么挂起，等待被转移到【阻塞队列】。
            // 当前线程没有重新获取，循环阻塞
            // isOnSyncQueue(node) : 该方法用于检测节点node是否已经转移到了阻塞队列
            // 退出循环有两种情况：
            // 1）. isOnSyncQueue(node)返回true，即当前node已经转移到阻塞队列中了。---------------> node转移到了阻塞队列
            // 2）. checkInterruptWhileWaiting(node)!=0，执行break，然后退出循环，代表的是线程中断。--------> 转移过程中线程发生了中断
            while (!isOnSyncQueue(node)){  //当前node（线程）没有转移到阻塞队列
                // 挂起线程
                LockSupport.park(this);

                // 检查线程挂起期间是否被中断，并对打断模式重新赋值  (0 : 没有发生中断)
                // checkInterruptWhileWaiting(node): 如果该方法 返回值不等于0，说明线程中断，
                // 【但是在该方法内部，如果线程中断，就会将node转移到阻塞队列】
                if ((interruptMode = checkInterruptWhileWaiting(node)) != 0){  //当前线程被中断
                    // 被中断，阻塞取消
                    break;
                }
            }

            // 到这里，当前线程所在的节点node已经转移到阻塞队列
            // 被唤醒后，将进入阻塞队列，等待获取锁。

            //THROW_IE :  代表await返回的时候，需要抛出 InterruptedException 异常
            //acquireQueued : 获取锁
            //************** 【重新获取锁】 **************** //
            if (acquireQueued(node, savedState) && interruptMode!=THROW_IE){
                //acquireQueued 返回true说明被中断了，而且interruptMode!=THROW_IE，说明在signal之前就发生中断了，这里将interruptMode设为REITERRUPT
                //REITERRUPT : 代表await返回的时候，需要重新设置中断状态
                interruptMode = REINTERRUPT;
            }

            //【到这里已经获取到了锁, 在完全释放独占锁到上一步骤重新获取锁中间这段时间（这段时间就是等待啦 ps:个人理解）, 其他线程可以获取锁执行自己的任务。】

            if (node.nextWaiter != null){
                unlinkCancelledWaiters();
            }
            // 判断步骤4是否被打断
            // 0 : 说明在await期间，没有发生中断
            if (interruptMode != 0){
                //被中断，根据中断模式做不同动作
                reportInterruptAfterWait(interruptMode);
            }
        }

        /**
         * 【不响应中断的等待】
         * <p>不抛出InterruptedException的await
         * <ol>
         *     <li>保存getState方法返回的锁的状态state。</li>
         *     <li>以savedState作为参数调用release方法，如果失败，则抛出IllegalMonitorStateException。</li>
         *     <li>阻塞直到有信号</li>
         *     <li>通过使用saveState作为参数调用acquire的专用版本来重新获取。</li>
         * </ol>
         */
        @Override
        public void awaitUninterruptibly() {
            //添加新的等待节点
            Node node = addConditionWaiter();
            //************** 【完全释放独占锁】 **************** //
            int savedState = fullyRelease(node);
            //中断标志
            boolean interrupted = false;
            //阻塞直到有信号
            while (!isOnSyncQueue(node)){
                LockSupport.park(this);
                if (Thread.interrupted()){
                    //当前线程被中断，中断标志设为true
                    interrupted = true;
                }
            }
            //************** 【重新获取锁】 **************** //
            if (acquireQueued(node, savedState) || interrupted){
                //获取锁成功或者发生了中断  ---->  自我中断
                selfInterrupt();
            }

            //【到这里当前线程已经获取到了锁, 方法可以结束 ,, 执行业务逻辑 唤醒其他线程等等
            //在完全释放独占锁到上一步骤重新获取锁中间这段时间（这段时间就是等待啦 ps:个人理解）, 其他线程可以获取锁执行自己的任务。】
        }

        /**
         * 【带超时时间并且响应中断的等待】
         * @param nanosTimeout 等待的时间,单位是纳秒
         * @throws InterruptedException 中断异常
         */
        @Override
        public long awaitNanos(long nanosTimeout) throws InterruptedException {
            if (Thread.interrupted()){
                //1. 当前线程中断，抛出InterruptedException异常
                throw new InterruptedException();
            }
            //新节点加入条件队列
            Node node = addConditionWaiter();
            //************** 【完全释放独占锁】 **************** //
            int savedState = fullyRelease(node);
            //当前时间 + 等待时间 = 过期时间
            final long deadline = System.nanoTime() + nanosTimeout;
            int interruptMode = 0;

            //isOnSyncQueue : 返回true表示node成功转移到了阻塞队列
            while (!isOnSyncQueue(node)){
                //判断是否超时
                if (nanosTimeout <= 0){
                    transferAfterCancelledWait(node);
                    break;
                }
                //判断等待时间是否大于等于一毫秒
                if (nanosTimeout >= spinForTimeoutThreshold){
                    LockSupport.parkNanos(this, nanosTimeout);
                }
                //判断是否发生中断
                if ((interruptMode = checkInterruptWhileWaiting(node)) != 0){
                    //发生中断
                    break;
                }
                nanosTimeout = deadline - System.nanoTime();
            }

            //************** 【重新获取锁】 **************** //
            if (acquireQueued(node, savedState) && interruptMode!=THROW_IE){
                //node成功获取锁，并且interruptMode!=THROW_IE
                interruptMode = REINTERRUPT;
            }

            //【到这里当前线程已经获取到了锁, 方法可以结束 ,, 执行业务逻辑 唤醒其他线程等等
            //在完全释放独占锁到上一步骤重新获取锁中间这段时间（这段时间就是等待啦 ps:个人理解）, 其他线程可以获取锁执行自己的任务。】

            if (node.nextWaiter != null){
                unlinkCancelledWaiters();
            }
            //是否发生中断
            if (interruptMode != 0){
                //发生中断，响应不同中断模式
                reportInterruptAfterWait(interruptMode);
            }
            return deadline-System.nanoTime();
        }

        /**
         * 【带超时实际并且响应中断的等待】
         * @param time 等待时间
         * @param unit 时间单位
         * @throws InterruptedException 中断异常
         */
        @Override
        public boolean await(long time, TimeUnit unit) throws InterruptedException {
            //等待的时间换算成纳秒数
            long nanosTimeout = unit.toNanos(time);

            //首先判断当前线程有没有被中断
            if (Thread.interrupted()){
                throw new InterruptedException();
            }

            //加入新节点到条件队列
            Node node = addConditionWaiter();

            //************** 【完全释放独占锁】 **************** //
            int savedState = fullyRelease(node);

            //当前时间 + 等待时间 = 过期时间
            final long deadline = System.nanoTime() + nanosTimeout;
            boolean timedout = false;
            //中断模式: 0(没发生中断), -1, 1
            int interruptMode = 0;

            //isOnSyncQueue: 返回true表示node转移到了阻塞队列
            while (!isOnSyncQueue(node)){
                //判断时间有没有到
                if (nanosTimeout <= 0L){
                    //时间到了，取消等待
                    //取消等待的话一定要调用 transferAfterCancelledWait(node) 这个方法,
                    //如果这个方法返回true，在这个方法内，将节点成功转移到了阻塞队列；
                    //如果返回false，说明signal已经发生，signal方法将节点转移了。也就是没有超时。
                    timedout = transferAfterCancelledWait(node);
                    break;
                }
                //spinForTimeoutThreshold 的值 是1000 纳秒，也就是一毫秒
                //如果等待时间不到一毫秒，就不要选择parkNanos了，自旋的性能反而更好
                if (nanosTimeout >= spinForTimeoutThreshold){
                    //等待时间超过了一毫秒，选择parkNanos
                    LockSupport.parkNanos(this, nanosTimeout);
                }
                if ((interruptMode = checkInterruptWhileWaiting(node)) != 0){
                    //发生了中断
                    break;
                }
                //计算剩余等待时间
                nanosTimeout = deadline - System.nanoTime();
            }

            //************** 【重新获取锁】 **************** //
            if (acquireQueued(node, savedState) && interruptMode!=THROW_IE){
                //当前线程获取了独占锁，并且interruptMode!=THROW_IE（signal之前就发生了中断）
                interruptMode = REINTERRUPT;
            }

            //【到这里当前线程已经获取到了锁, 方法可以结束 ,, 执行业务逻辑 唤醒其他线程等等
            //在完全释放独占锁到上一步骤重新获取锁中间这段时间（这段时间就是等待啦 ps:个人理解）, 其他线程可以获取锁执行自己的任务。】

            if (node.nextWaiter != null){
                unlinkCancelledWaiters();
            }

            if (interruptMode != 0){
                //发生了中断，根据中断模式做出不同响应
                reportInterruptAfterWait(interruptMode);
            }

            return !timedout;
        }

        /**
         * 【定时功能的等待】
         * @param deadline 定时时间
         */
        @Override
        public boolean awaitUntil(Date deadline) throws InterruptedException {
            // 毫秒时间戳
            long abstime = deadline.getTime();
            if (Thread.interrupted()){
                throw new InterruptedException();
            }
            Node node = addConditionWaiter();

            //************** 【完全释放独占锁】 **************** //
            int savedState = fullyRelease(node);

            boolean timedout = false;
            int interruptMode = 0;
            while (!isOnSyncQueue(node)) {
                if (System.currentTimeMillis() > abstime) {
                    timedout = transferAfterCancelledWait(node);
                    break;
                }
                LockSupport.parkUntil(this, abstime);
                if ((interruptMode = checkInterruptWhileWaiting(node)) != 0){
                    break;
                }
            }

            //************** 【重新获取锁】 **************** //
            if (acquireQueued(node, savedState) && interruptMode != THROW_IE){
                interruptMode = REINTERRUPT;
            }

            //【到这里当前线程已经获取到了锁, 方法可以结束 ,, 执行业务逻辑 唤醒其他线程等等
            //在完全释放独占锁到上一步骤重新获取锁中间这段时间（这段时间就是等待啦 ps:个人理解）, 其他线程可以获取锁执行自己的任务。】

            if (node.nextWaiter != null){
                unlinkCancelledWaiters();
            }
            if (interruptMode != 0){
                reportInterruptAfterWait(interruptMode);
            }
            return !timedout;
        }

        /**
         * 唤醒等待了最久的线程:
         * 将最长等待线程（如果存在）对应的node从条件队列中移动到阻塞队列中去。
         *
         * @throws IllegalMonitorStateException 如果isHeldExclusively方法返回false
         */
        @Override
        public void signal() {
            // 调用signal方法的线程必须持有当前的独占锁
            if (!isHeldExclusively()){
                // 当前线程不是持有锁的线程
                throw new IllegalMonitorStateException();
            }

            Node first = this.firstWaiter;
            if (first != null){
                // 唤醒一个
                doSignal(first);
            }
        }

        /**
         * 将所有等待线程从此条件的等待队列中 移动到 所属锁的等待队列中。
         *
         * @throws IllegalMonitorStateException 如果isHeldExclusively方法返回false
         */
        @Override
        public void signalAll() {
            if (!isHeldExclusively()){
                // 当前线程不是持有锁的线程
                throw new IllegalMonitorStateException();
            }

            Node first = firstWaiter;
            if (first!=null){
                // 唤醒所有
                doSignalAll(first);
            }
        }


        // 响应中断（InterruptedException异常）的 await 方法用到 :
        // 退出 await 时重新设置中断状态
        private static final int REINTERRUPT = 1;
        // 退出 await 时抛出InterruptedException异常
        private static final int THROW_IE = -1;

        /**
         * await方法调用，检查当前线程是否有中断:
         * <ol>
         * <li>如果线程在 signal 之前已经中断则返回 THROW_IE
         * <li>如果线程在 signal 之后中断则返回 REINTERRUPT
         * <li>如果没有中断，则返回0。
         * </ol>
         */
        private int checkInterruptWhileWaiting(Node node){
            return Thread.interrupted() ? (transferAfterCancelledWait(node)?THROW_IE: REINTERRUPT) : 0;
        }

        /**
         * 根据模式抛出InterruptedException异常、重新中断当前线程或者不执行任何操作
         */
        private void reportInterruptAfterWait(int interruptMode)throws InterruptedException{
            if (interruptMode == THROW_IE){
                throw new InterruptedException();
            }else if (interruptMode == REINTERRUPT){
                selfInterrupt();
            }
        }
    }


    //-------------------------------------------------CAS操作----------------------------------------------------------/
    /**
     * Unsafe可以帮我们直接去操作硬件资源，如直接访问系统内存资源、自主管理内存资源等，这些方法在提升Java运行效率、增强Java语言底层资源操作能力方面起到了很大的作用。
     * 但由于Unsafe类使Java语言拥有了类似C语言指针一样操作内存空间的能力，这无疑也增加了程序发生相关指针问题的风险。不建议使用。
     * 当且仅当调用getUnsafe方法的类为引导类加载器所加载时才合法，否则抛出SecurityException异常
     */
    private static final Unsafe unsafe = Unsafe.getUnsafe();
    private static  long stateOffset;
    private static  long headOffset;
    private static  long tailOffset;
    private static  long waitStatusOffset;
    private static  long nextOffset;

    static {
        try {
            stateOffset = unsafe.objectFieldOffset
                    (AQS.class.getDeclaredField("state"));
            headOffset = unsafe.objectFieldOffset
                    (AQS.class.getDeclaredField("head"));
            tailOffset = unsafe.objectFieldOffset
                    (AQS.class.getDeclaredField("tail"));
            waitStatusOffset = unsafe.objectFieldOffset
                    (Node.class.getDeclaredField("waitStatus"));
            nextOffset = unsafe.objectFieldOffset
                    (Node.class.getDeclaredField("next"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Unsafe中的CAS方法：
    //o: 包含要修改field的对象
    //offset: 对象中某field的偏移量
    //expected: 期望值
    //update: 更新值


    /**
     * CAS方式更新AQS的队列的头节点(head)，只被enq方法调用
     *
     * @param update head更新值
     * @return true成功，false失败
     */
    private final boolean compareAndSetHead(Node update){
        return unsafe.compareAndSwapObject(this, headOffset, null, update);
    }

    /**
     * CAS方式更新AQS的队列的尾节点(tail)，只被enq方法调用
     *
     * @param update tail更新值
     * @return true成功，false失败
     */
    private final boolean compareAndSetTail(Node expect, Node update){
        return unsafe.compareAndSwapObject(this, tailOffset, expect, update);
    }

    /**
     * CAS方式更新节点node的waitStatus值
     *
     * @param node 需要更新的node
     * @param expect waitStatus期望值
     * @param update waitStatus更新值
     * @return true成功，false失败
     */
    private static final boolean compareAndSetWaitStatus(Node node, int expect, int update){
        return unsafe.compareAndSwapInt(node, waitStatusOffset, expect, update);
    }

    /**
     * CAS方式更新节点node的next值
     *
     * @param node 需要更新的node
     * @param expect next的期望值
     * @param update next的更新值
     * @return true成功，false失败
     */
    private static final boolean compareAndSetNext(Node node, Node expect, Node update){
        return unsafe.compareAndSwapObject(node, nextOffset, expect, update);
    }
}
