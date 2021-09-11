package com.dongfeng.study.sourcecode.java8.util.concurrent.locks;

import java.util.concurrent.TimeUnit;

/**
 * Lock 接口的实现类提供了比 synchronized方法和语句（代码块）更广泛的锁操作。
 * 它们允许更灵活的结构，可能具有完全不同的属性，并且可能支持多个关联的 Condition 对象。
 *
 * <p> 锁是一种工具，用于控制多个线程对共享资源的访问。
 * 通常锁提供对共享资源的独占访问：一次只有一个线程可以获取锁，对共享资源所有的访问都要求首先获取锁。 但是有些锁允许并发访问共享资源，例如ReadWriteLock的读锁。
 *
 * <p> 使用 synchronized方法或者语句（代码块）提供了访问与每个对象关联的隐式监视器锁（implicit monitor lock），
 * 但是强制所有锁的获取和释放都以块结构（block-structured）方式进行：当获取多个锁时，它们必须以相反的顺序释放，并且所有的锁都必须在获取它们的同一作用域内释放。
 *
 * <p> 虽然synchronized方法和语句（代码块）的作用域机制（scoping mechanism）使使用监视器锁编程更加变得更加容易，并且有助于避免许多涉及锁的常见编程错误，
 * 但有时需要以灵活的方式使用锁。例如，一些用于遍历并发访问的数据结构的算法需要使用 “hand-over-hand” 或者 “chain locking” :
 * 首先获取节点A的锁，然后获取节点B的锁，然后释放节点A的锁并获取节点C的锁，然后释放B的锁并获取D的锁，以此类推。Lock接口的实现类通过允许在不同的作用域中获取和释放锁，
 * 并允许以任何顺序获取和释放多个锁，从而允许使用这些技术。
 *
 * <p> 灵活性的提高带来了额外的责任，如果没有块结构锁，那么同步方法和语句就会自动释放锁。在大多数情况下，应该使用以下习惯：
 * <pre> {@code
 * Lock l = ...;
 *  l.lock();
 *  try {
 *    // access the resource protected by this lock
 *  } finally {
 *    l.unlock();
 *  }}</pre>
 *
 * 当加锁和解锁发生在不同的作用域时，必须要注意确保在持有锁时执行的所有代码都受到try-finally 或者 try-catch的保护，以确保在必要时释放锁。
 *
 * <p> Lock接口的实现类 通过提供非阻塞方式获取锁（tryLock），获取可以中断锁的尝试（lockInterruptibly）和获取可以超时的锁的尝试（tryLock(long time, TimeUnit unit)）
 * 提供了比synchronized方法和语句（代码块）更多的功能。
 *
 * <p> Lock接口的实现类还可以提供与隐式监视器锁完全不同的语义和行为，例如保证顺序，不可重入锁或者死锁检测。
 * 如果一个实现类提供了这种专门的语义那么这个实现类必须记录这些语义（写在文档中）。
 *
 * <p> 请注意，Lock实例只是普通对象，它们本身可以用作synchronized语句中的目标（锁对象）。获取Lock实例的监视器锁与调用该实例的任何有关锁的方法没有任何关系。
 * 建议不要以这种方式使用Lock实例，除非它们在自己的实现中。
 *
 * <p> 除非另有说明，否则为任何参数传递null值都将抛出NullPointerException 异常。
 *
 * <h3><b> 内存同步 (Memory Synchronization) </h3>
 * <p> 所有Lock的实现类都必须执行与内置监视器锁提供的相同的内存同步语义，如Java语言规范的内存模型（memory model）中所述：
 * <ul>
 * <li> 成功的 lock 操作和成功的 Lock 操作具有相同的内存同步效果
 * <li> 成功的 unlock 操作和成功的 Unlock 具有相同的内存同步效果
 * </ul>
 *
 * <p> 不成功的locking和unlocking， 以及可重入的locking/unlocking 不需要任何内存同步效果
 *
 * <h3><b> 实现注意事项 (Implementation Considerations) </h3>
 * <p> 锁获取的三种形式（可中断、不可中断和定时）在性能特征、顺序保证或者其他实现质量/特征 方面可能有所不同。
 * 此外，中断正在进行的锁获取的能力在给定的Lock类中可能不可用。
 * 因此，一个Lock实现类不需要为所有三种形式的锁获取定义完全相同的保证或语义，也不需要支持正在进行的锁获取的中断。
 * 一个实现类需要清楚的说明每个锁方法（lock方法）提供的保证和语义。
 * 它还必须遵守此接口中定义的中断语义，以支持中断锁获取：要么完全中断，要么仅在方法入口（method entry）中断。
 * <p> 由于中断通常意味着取消，并且对中断的检查通常不经常发生。所以更倾向于响应中断，而不是正常的方法返回。
 * 即使可以显示中断发生在另一个操作解除线程阻塞之后，这也是正确的。实现应该记录这种行为。
 *
 * @author eastFeng
 * @date 2021-01-09 17:10
 */
public interface Lock {

    /**
     * <b>获取锁</b>
     *
     * <p> 如果锁获取不到，则当前线程将被禁止用于线程调度(thread scheduling)，并处于休眠状态，直到获取到锁位置。
     * <p><b> 实现注意事项 (Implementation Considerations) </b>
     * <p> 锁的实现类可能能够检测到锁的错误使用，例如导致死锁的调用，并且在这种情况下可能抛出（未检查）异常。
     * 锁的实现类必须说明这些情况和异常类型。
     */
    void lock();

    /**
     * <b>获取锁，除非当前线程被中断 (响应中断)</b>
     * <p>获取锁（如果可用）并立即返回</p>
     * <p>如果锁不可用，则出于线程调度目的，当前线程将被禁用，并处于休眠状态，直到发生以下两种情况之一：
     * <ul>
     * <li> 当前线程获取锁。
     * <li> 其他线程会中断当前线程，并且支持中断锁获取。
     * </ul>
     *
     * <p>如果当前线程：</p>
     * <ul>
     * <li> 在进入此方法时设置其中断状态
     * <li> 在获取锁时被中断，支持中断获取锁
     * </ul>
     * 那么将抛出InterruptedException并清除当前线程的中断状态。
     *
     * <p><b> 实现注意事项 (Implementation Considerations) </b>
     * <p>在一些实现类中，中断锁获取(interrupt a lock acquisition)的能力可能是不可能的，如果可能的话，可能是一个昂贵的操作。
     * 程序员应该意识到可能有这种情况。在这种情况下，应该说明下。</p>
     * <p>与正常的方法返回相比，实现更有利于响应中断。</p>
     * <p>锁的实现类可能能够检测到锁的错误使用，例如导致死锁的调用，并且在这种情况下可能抛出（未检查）异常。
     * 锁的实现类必须说明这些情况和异常类型。</p>
     *
     * @throws InterruptedException 如果当前线程在获取锁时被中断（并且支持中断锁获取）
     */
    void lockInterruptibly() throws InterruptedException;

    /**
     * <b>仅当在调用时锁是空闲的时才获取锁。(只试一次)</b>
     *
     * <p>获取锁（如果可用），并立即返回值true。如果锁不可用，则此方法将立即返回值false。</p>
     *
     * <p>此方法的典型用法是：
     * <pre> {@code
     * Lock lock = ...;
     *  if (lock.tryLock()) {
     *    try {
     *      // manipulate protected state
     *    } finally {
     *      lock.unlock();
     *    }
     *  } else {
     *    // perform alternative actions
     *  }}</pre>
     * 此用法确保在获取锁时将其解锁，而在未获取锁时不会尝试解锁。
     *
     * @return 如果获取了锁，则为true，否则为false
     */
    boolean tryLock();

    /**
     * <b>如果锁在给定的等待时间内空闲并且当前线程没有中断，则获取该锁。</b>
     *
     * <p>如果锁可用，此方法立即返回值true。如果锁不可用，则出于线程调度目的，当前线程将被禁用，并处于休眠状态，直到发生以下三种情况之一：</p>
     * <ul>
     * <li>锁由当前线程获取</li>
     * <li>其他线程会中断当前线程，并且支持中断锁获取</li>
     * <li>超过指定的等待时间</li>
     * </ul>
     *
     * <p>如果获取了锁，则返回值true。</p>
     *
     * <p>如果当前线程：</p>
     * <ul>
     * <li>在进入此方法时设置其中断状态</li>
     * <li>在获取锁时被中断，支持中断获取锁</li>
     * </ul>
     * 那么将抛出InterruptedException并清除当前线程的中断状态。
     *
     * <p>如果超过指定的等待时间，则返回值false。如果时间小于或等于零，则该方法根本不会等待。</p>
     *
     * <p><b> 实现注意事项 (Implementation Considerations) </b>
     * <p>在一些实现类中，中断锁获取(interrupt a lock acquisition)的能力可能是不可能的，如果可能的话，可能是一个昂贵的操作。
     * 程序员应该意识到可能有这种情况。在这种情况下，应该说明下</p>
     * <p>与正常的方法返回相比，实现更倾向于响应中断，或者报告超时。</p>
     * <P>锁的实现类可能能够检测到锁的错误使用，例如导致死锁的调用，并且在这种情况下可能抛出（未检查）异常。 锁的实现类必须说明这些情况和异常类型</P>
     *
     * @param time 等待锁定的最长时间
     * @param unit time参数的时间单位
     * @return 如果获取了锁，则为true；如果在获取锁之前经过了等待时间，则为false
     * @throws InterruptedException 如果当前线程在获取锁时被中断（并且支持中断锁获取）
     */
    boolean tryLock(long time, TimeUnit unit) throws InterruptedException;

    /**
     * <b>释放锁</b>
     *
     * <p><b>实现注意事项 (Implementation Considerations)</b></p>
     * <p>锁实现通常会对哪个线程可以释放锁施加限制（通常只有锁的持有者可以释放锁），
     * 如果违反了限制，则可能抛出（未检查）异常。锁实现必须记录任何限制和异常类型。</p>
     */
    void unlock();

    /**
     * <b>返回绑定到此Lock实例的新Condition实例</b>
     *
     * <p>在等待条件之前，锁必须由当前线程持有。调用Condition.await()方法会自动释放锁，并在等待返回前（await方法结束前）重新获取锁。</p>
     *
     * <b>实现注意事项 (Implementation Considerations)</b>
     * <p>Condition实例的确切操作取决于Lock实现类，并且必须由该实现说明。</p>
     *
     * @return 关联到此Lock实例的Condition实例
     * @throws UnsupportedOperationException 如果此Lock实现类不支持条件
     */
    Condition newCondition();
}
