垃圾收集器则是内存回收的具体实现

- 回收新生代的收集器包括Serial、PraNew、Parallel Scavenge
- 回收老年代的收集器包括Serial Old、Parallel Old、CMS
- 回收整个Java堆的G1收集器



## **JDK1.8默认的垃圾回收器**

默认：UseParallelGC
UseParallelGC 即 Parallel Scavenge + Parallel Old



### GC算法

常见的GC(垃圾回收)算法：标记-清除、标记-复制和标记-整理。



## 总结

> 新生代垃圾回收器一般采用的是复制算法，复制算法的优点是效率高，缺点是内存利用率低；老年代回收器一般采用的是标记-整理的算法进行垃圾回收。

## 分代垃圾回收器如何工作？

> 分代回收器有两个分区：老生代和新生代，新生代默认的空间占比总空间的 1/3，老生代的默认占比是 2/3。新生代使用的是复制算法，新生代里有 3 个分区：Eden、To Survivor、From Survivor，它们的默认占比是 8:1:1，它的执行流程如下：

1. 把 Eden + From Survivor 存活的对象放入 To Survivor 区；
2. 清空 Eden 和 From Survivor 分区；
3. From Survivor 和 To Survivor 分区交换，From Survivor 变 To Survivor，To Survivor 变 From Survivor。



> 每次在 From Survivor 到 To Survivor 移动时都存活的对象，年龄就 +1，当年龄到达 15（默认配置是 15）时，升级为老生代。大对象也会直接进入老生代。

* 老生代当空间占用到达某个值之后就会触发全局垃圾收回，一般使用标记
  整理的执行算法。以上这些循环往复就构成了整个分代垃圾回收的整体执
  行流程。
