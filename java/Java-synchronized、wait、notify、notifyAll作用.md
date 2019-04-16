# synchronized,wait与notify

* `Object.wait()`：释放当前对象锁，并进入阻塞队列
* `Object.notify()`：唤醒当前对象阻塞队列里的任一线程（并不保证唤醒哪一个）
* `Object.notifyAll()`：唤醒当前对象阻塞队列里的所有线程

#### `synchronized`是一个非公平的锁，如果竞争激烈的话，可能导致某些线程一直得不到执行。

> 实例，通过synchronized实现有序队列

```java
import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author Lee
 * @Description
 * @Date 2019年04月16日 21:30
 */
public class MyQueue {

    // 集合
    private final LinkedList<Object> list = new LinkedList<>();
    // 计数器
    private AtomicInteger count = new AtomicInteger(0);
    // 上限和下限
    private final int minSize = 0;

    private final int maxSize;
    // 构造函数
    public MyQueue(int size){
        this.maxSize = size;
    }

    private final Object lock = new Object();

    public void put(Object obj){
        synchronized (lock){
            while (count.get() == this.maxSize){
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            // 加入元素
            list.offer(obj);
            System.out.println("新加入元素："+obj);
            // 计数器计数
            count.incrementAndGet();
            // 唤醒线程
            lock.notify();
        }
    }

    public Object take(){
        Object ret = null;
        synchronized (lock){
            while (count.get() == this.minSize){
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            // 移除队列的元素
            ret = list.remove();
            //  计数器减1
            count.decrementAndGet();
            // 唤醒线程
            lock.notify();
        }

        return ret;
    }


    public int getSize(){
        return this.list.size();
    }

    public static void main(String[] args){
        MyQueue mq = new MyQueue(7);
        mq.put("a");
        mq.put("b");
        mq.put("s");
        mq.put("r");
        mq.put("t");
        mq.put("tewf");
        mq.put("twefw");


        System.out.println("当前容器的长度："+mq.getSize());

        Thread t1 = new Thread(new Runnable(){
            @Override
            public void run() {
                mq.put("'as");
                mq.put("sf");
            }
        },"t1");


        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                Object o1 = mq.take();
                System.out.println("移除的元素为："+o1);

                Object o2 = mq.take();
                System.out.println("移除的元素:"+o2);
            }
        },"t2");

        t1.start();

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        t2.start();
    }
}
```
