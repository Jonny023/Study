



> synchronized:同步锁，悲观锁，互斥锁，重量级锁
>
> cas:无锁，自旋锁，轻量级锁，乐观锁（原子问题，ABA问题）

synchronized:

* monitor，waitset，monitorenter monitorexit 
* 如果是静态方法锁的是Class对象
* 同步代码块，锁的是括号里的对象
* 非静态方法，锁的是当前类的实例对象

cas：

* 原子性 lock cmpxchgq 缓存行锁/总线锁
* ABA问题 可以通过加版本号解决，AtomicStampedReference

偏向锁（锁优化技术）：只有一个线程，缓存线程id，之后判断缓存中如果存在对应的线程id，直接执行

轻量级锁：多线程竞争锁，cas自旋不成功，重度竞争，线程数太多，升级为重量级锁

重量级锁：synchronized

优化：LongAdder(分段cas)

