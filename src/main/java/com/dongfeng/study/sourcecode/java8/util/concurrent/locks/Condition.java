package com.dongfeng.study.sourcecode.java8.util.concurrent.locks;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Condition 是在Java 1.5 中新增的，用来代替传统的 Object 的wait，notify<b>实现线程间的协作</b>，相比于 Object 的wait，notify使用 Condition
 * 的await，signal方式实现线程间协作更加安全和高效。
 * Condition 中提供了一系列类似于Object中的监视器方法。与Lock配合可以完成等待通知模式。
 * Condition依赖于Lock接口，生成一个Condition的基本代码是 lock.newCondition
 * 调用Condition 的await和signal方法都必须要先获取锁（Lock.lock）。
 *
 * <p>Condition将Object对象的监视器方法（wait、notify和notifyAll）分解为不同的objects，通过将它们与任意Lock实现类的使用相结合，为每个对象提供多个等待集合的效果。
 * 如果Lock取代了synchronized方法和语句的使用，则Condition取代了Object监视器方法的使用。
 * </p>
 *
 * <p>Condition(也称为条件队列或者条件变量)为一个线程提供了一种暂停执行（to "wait"）直到一些状态为true被另一个线程通知（唤醒）。
 * 由于对共享状态信息的访问发生在不同的线程中，因此必须对其进行保护，因为某种形式的锁与条件相关联。
 * Condition的await提供的关键属性是，它以原子属性释放关联的锁并挂起当前线程，就行Object.wait方法一样。
 * </p>
 *
 * <p>Condition实例是绑定到Lock实例上的。要获取特定Lock实例上的Condition实例，请使用Lock.newCondition方法</p>
 *
 * <p>例如，假设我们有一个支持put和take方法的有界缓冲区。如果尝试在空缓冲区上执行take，则线程将阻塞，直到有元素变为可用；
 * 如果尝试在满缓冲区上执行put，则线程将阻塞，直到某个空间变为可用。
 * 我们希望在不同的等待集中保持等待put线程和take线程，这样我们就可以在缓冲区中的元素或空间变为可用时只通知单个线程。
 * 这可以通过使用两个Condition实例来实现。</p>
 * <pre>
 * class BoundedBuffer {
 *      final Lock lock = new ReentrantLock();
 *      final Condition notFull  = lock.newCondition();
 *      final Condition notEmpty = lock.newCondition();
 *
 *      final Object[] items = new Object[100];
 *      int putptr, takeptr, count;
 *
 *      public void put(Object x) throws InterruptedException {
 *        lock.lock();
 *        try {
 *          while (count == items.length)
 *            notFull.await();
 *          items[putptr] = x;
 *          if (++putptr == items.length) putptr = 0;
 *          ++count;
 *          notEmpty.signal();
 *        } finally {
 *          lock.unlock();
 *        }
 *      }
 *
 *      public Object take() throws InterruptedException {
 *        lock.lock();
 *        try {
 *          while (count == 0)
 *            notEmpty.await();
 *          Object x = items[takeptr];
 *          if (++takeptr == items.length) takeptr = 0;
 *          --count;
 *          notFull.signal();
 *          return x;
 *        } finally {
 *          lock.unlock();
 *        }
 *      }
 *    }
 * </pre>
 * (java.util.concurrent.ArrayBlockingQueue提供此功能，因此没有必要实现此示例用法类。)
 *
 * <p>Condition实现类可以提供不同于Object监视器方法的行为和语义，例如保证通知的顺序，或者在执行通知时不需要持有锁。
 * 如果一个实现类提供了这种专门的语义，则必须说明这些语义</p>
 *
 * <p>请注意，Condition实例只是普通的对象，它们本身可以用作synchronized的目标，并且可以调用它们自己的监视器方法（wait和notify）。
 * 获取Condition实例的监视器锁或者调用它们的监视器方法与该Condition关联的Lock或使用其await和signal方法没有特殊的关系。
 * 建议不要以这种方式使用Condition实例，除非在它们自己的实现中</p>
 *
 * <p>除非另有说明，否则为任何参数传递null值都将抛出NullPointerException异常。</p>
 *
 * <h3><b> 实现注意事项 (Implementation Considerations) </h3>
 *
 * <p>在等待某个Condition时，通常允许发生“<b>虚假唤醒</b>”，作为对底层平台语句的让步。
 * 这对大多数应用程序几乎没有实际影响，因为Condition应该始终在循环中等待，测试正在等待的状态。
 * Condition实现类可以自由地消除虚假唤醒的可能性，但是建议程序员应该始终假设它们可以发生，因此<b>始终在循环中等待(而不是if)</b>。
 * </p>
 *
 * <p>条件等待的三种形式（可中断、不可中断和定时）在某些平台上的易实现性和性能特征可能有所不同。
 * 尤其是，可能很难提供这些特性并维护特定的语义，例如保证顺序。
 * 此外，中断线程实际挂起的能力可能并不总是能够在所有平台上实现。</p>
 *
 * <p>因此，实现不需要为所有三种形式的等待定义完全相同的保证或语义，也不需要支持中断线程的实际挂起。</p>
 *
 * <p>一个实现需要清楚地说明每个等待方法提供的语义和保证，当一个实现确实支持线程挂起的中断时，它必须遵守这个接口中定义的中断语义。</p>
 *
 * <p>由于中断通常意味着取消，并且对中断的检查通常不经常发生，所以实现可能会倾向于响应中断而不是正常的方法返回。
 * 即使可以显示中断发生在另一个可能已解除线程阻塞的操作之后，也是如此。实现应该记录这种行为。</p>
 *
 * @author eastFeng
 * @date 2020-12-25 10:02
 */
public interface Condition {

    /**
     * <b>使当前线程等待直到被唤醒或中断 (响应中断)</b>
     *
     * <p>与此Condition相关联的锁以原子方式释放，当前线程出于线程调度目的被禁用，并出于休眠状态，直到发生以下四种情况之一当前线程将苏醒：</p>
     * <ul>
     * <li>另一个线程调用了此Condition的signal方法，而当前线程恰好被选为要唤醒的线程。</li>
     * <li>另一个线程调用了此Condition的signalAll方法。</li>
     * <li>其他线程中断当前线程，支持中断线程挂起。</li>
     * <li>出现“虚假唤醒”。</li>
     * </ul>
     *
     * <p>在所有情况下，在该方法返回之前，当前线程必须重新获取与该条件相关联的锁。当线程返回时，保证持有锁。</p>
     *
     * <p>如果当前线程：</p>
     * <ul>
     * <li>在进入此方法时设置其中断状态。</li>
     * <li>等待时中断，支持线程挂起中断。</li>
     * </ul>
     * 那么将抛出InterruptedException并清除当前线程的中断状态。在第一种情况下，没有规定是否在释放锁之前进行中断测试。
     *
     * <p><b>实现注意事项 (Implementation Considerations) </b></p>
     * <p>调用此方法时，假定当前线程持有与此条件关联的锁。如果当前线程没有持有锁，会抛出异常（例如IllegalMonitorStateException），实现必须记录该情况。</p>
     * <p>与响应信号的正常方法返回相比，实现更倾向于响应中断。在这种情况下，实现必须确保信号被重定向到另一个等待线程（如果有）。</p>
     *
     * @throws InterruptedException 如果当前线程被中断（并且支持中断线程挂起）
     */
    void await() throws InterruptedException;

    /**
     * <b>使当前线程等待，直到被唤醒 (不响应中断)</b>
     *
     * <p>与此Condition相关联的锁以原子方式释放，当前线程出于线程调度目的被禁用，并处于休眠状态，直到发生以下三种情况之一：</p>
     * <ul>
     * <li>另一个线程调用了此Condition的signal方法，而当前线程恰好被选为要唤醒的线程。</li>
     * <li>另一个线程调用了此Condition的signalAll方法。</li>
     * <li>出现“虚假唤醒”。</li>
     * </ul>
     *
     * <p>在所有情况下，在该方法返回之前，当前线程必须重新获取与该条件相关联的锁。当线程返回时，保证持有锁</p>
     *
     * <p>如果当前线程的中断状态是在它进入这个方法时设置的，或者它在等待时被中断，它将继续等待直到发出信号。当它最终从此方法返回时，它的中断状态仍将被设置。</p>
     *
     * <p><b>实现注意事项 (Implementation Considerations) </b></p>
     * <p>调用此方法时，假定当前线程持有与此条件关联的锁。如果当前线程没有持有锁，会抛出异常（例如IllegalMonitorStateException），实现必须记录该情况。</p>
     */
    void awaitUninterruptibly();

    /**
     * <b>使当前线程等待，直到被唤醒或中断，或者经过指定的等待时间 (响应中断)</b>
     *
     * <p>与此Condition相关联的锁以原子方式释放，当前线程出于线程调度目的被禁用，并出于休眠状态，直到发生以下五种情况之一：</p>
     * <ul>
     * <li>另一个线程调用了此Condition的signal方法，而当前线程恰好被选为要唤醒的线程。</li>
     * <li>另一个线程调用了此Condition的signalAll方法。</li>
     * <li>其他线程中断当前线程，支持中断线程挂起。</li>
     * <li>经过指定的等待时间。</li>
     * <li>出现“虚假唤醒”。</li>
     * </ul>
     *
     * <p>在所有情况下，在该方法返回之前，当前线程必须重新获取与该条件相关联的锁。当线程返回时，保证持有锁</p>
     *
     * <p>如果当前线程：</p>
     * <ul>
     * <li>在进入此方法时设置其中断状态。</li>
     * <li>等待时中断，支持线程挂起中断。</li>
     * </ul>
     * 那么将抛出InterruptedException并清除当前线程的中断状态。在第一种情况下，没有规定是否在释放锁之前进行中断测试。
     *
     * <p>该方法返回提供的nanosTimeout值的剩余等待纳秒数的估计值，如果超时，则返回小于或等于零的值。
     * 在等待返回但等待的条件仍不成立的情况下，此值可用于确定是否重新等待以及等待多长时间。此方法的典型用途如下：</p>
     * <pre>
     * boolean aMethod(long timeout, TimeUnit unit) {
     *    long nanos = unit.toNanos(timeout);
     *    lock.lock();
     *    try {
     *      while (!conditionBeingWaitedFor()) {
     *        if (nanos <= 0L)
     *          return false;
     *        nanos = theCondition.awaitNanos(nanos);
     *      }
     *      // ...
     *    } finally {
     *      lock.unlock();
     *    }
     *  }
     * </pre>
     *
     * <p>设计说明：此方法需要纳秒参数，以避免在报告剩余时间时出现截断错误。这样的精度损失将使程序员很难确保在重新等待发生时，总等待时间不会系统地短于指定的时间。</p>
     *
     * <p><b>实现注意事项 (Implementation Considerations) </b></p>
     * <p>调用此方法时，假定当前线程持有与此条件关联的锁。如果当前线程没有持有锁，会抛出异常（例如IllegalMonitorStateException），实现必须记录该情况。</p>
     * <p>与响应信号的正常方法返回相比，实现更倾向于响应中断，或者经过指定的等待时间。在这两种情况下，实现必须确保信号被重定向到另一个等待线程（如果有）。</p>
     *
     * @param nanosTimeout 等待的最长时间，以纳秒为单位
     * @return 超时值的估计值减去等待此方法返回所花费的时间。正值可以用作此方法的后续调用的参数，以完成所需时间的等待。小于或等于零的值表示没有剩余时间。
     * @throws InterruptedException 如果当前线程被中断（并且支持中断线程挂起
     */
    long awaitNanos(long nanosTimeout) throws InterruptedException;

    /**
     * 使当前线程进入等待状态，直到被唤醒、中断或者超时
     *
     *
     * @param time 最长等待时间
     * @param unit time参数的时间单位
     * @return 如果在方法返回之前检测到等待时间已过，则为false，否则为true
     * @throws InterruptedException 如果当前线程被中断（并且支持中断线程挂起
     */
    boolean await(long time, TimeUnit unit) throws InterruptedException;

    /**
     * <b>使当前线程等待，直到被唤醒或被中断，或者指定的截止时间已过 (响应中断)</b>
     *
     * <p>返回值表示截止日期是否已过，可按如下方式使用：</p>
     * <pre>
     * boolean aMethod(Date deadline) {
     *    boolean stillWaiting = true;
     *    lock.lock();
     *    try {
     *      while (!conditionBeingWaitedFor()) {
     *        if (!stillWaiting)
     *          return false;
     *        stillWaiting = theCondition.awaitUntil(deadline);
     *      }
     *      // ...
     *    } finally {
     *      lock.unlock();
     *    }
     *  }
     * </pre>
     *
     * @param deadline
     * @return
     * @throws InterruptedException
     */
    boolean awaitUntil(Date deadline) throws InterruptedException;

    /**
     * <b>唤醒一个正在等待的线程。(条件队列)</b>
     *
     * <p>如果有任何线程正在等待此条件，则会选择一个线程进行唤醒。然后，该线程必须在从await方法返回之前重新获取锁。</p>
     */
    void signal();

    /**
     * <b>唤醒所有等待的线程。(条件队列)</b>
     *
     * <p>如果有线程正在等待此条件，那么它们都会被唤醒。每个线程必须重新获取锁然后才能从await方法返回。</p>
     */
    void signalAll();
}
