# Runtime Process exec调用shell

```java
public static void main(String[] args) throws IOException, InterruptedException {
    Runtime runtime = Runtime.getRuntime();
    Process exec = runtime.exec("ipconfig /all");
    InputStream errorStream = exec.getInputStream();
    InputStreamReader isr = new InputStreamReader(errorStream);
    BufferedReader br = new BufferedReader(isr);
    String line;
    while ((line = br.readLine()) != null) {
        System.out.println(line);
        //手動退出
        exec.destroy();
        //            exec.destroyForcibly();
    }

    int exitVal = 0;
    try {
        exec.waitFor();
        exitVal = exec.exitValue();
        System.out.println("Process exitValue: " + exitVal);
        if (exitVal == 0) {
            System.out.println("execute success.");
        } else {
            System.out.println("execute failed.");
        }
    } catch (InterruptedException e) {
        e.printStackTrace();
    }

}
```

