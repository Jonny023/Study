# 队列方式遍历所有目录下的文件

```java
File file = new File("D:\\zipOut");
Queue<File> queue = new LinkedList<>();
queue.offer(file);

while (!queue.isEmpty()) {
    File file1 = queue.poll();
    if (file1.isDirectory()) {
        File[] files = file1.listFiles();
        for (int i = 0, len = files.length; i < len; i++) {
            queue.offer(files[i]);
        }
    } else {
        System.out.println("文件：" + file1.getName());
    }
}
```
