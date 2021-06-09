# 输出项目访问地址

```java
    private static final Logger log = LoggerFactory.getLogger(StatApplication.class);

    public static void main(String[] args) {
        ConfigurableApplicationContext app = SpringApplication.run(Application.class, args);
        Environment env = app.getEnvironment();
        logApplicationStartup(env);
    }

    private static void logApplicationStartup(Environment env) {
        String protocol = "http";
        if (env.getProperty("server.ssl.key-store") != null) {
            protocol = "https";
        }
        String serverPort = env.getProperty("server.port");
        String contextPath = env.getProperty("server.servlet.context-path");
        if (StringUtils.isBlank(contextPath)) {
            contextPath = "/";
        }
        List<String> ipList = ips();
        StringBuilder sb = new StringBuilder();
        sb.append("\n----------------------------------------------------------\n\t");
        String finalProtocol = protocol;
        String finalContextPath = contextPath;
        Stream.iterate(0, i -> i + 1).limit(ipList.size()).forEach(i -> sb.append(MessageFormat.format("Address{0}:{1}://{2}:{3}{4}/doc.html\n\t",(i+1),finalProtocol, ipList.get(i), serverPort, finalContextPath)));
        sb.append(MessageFormat.format("Profile(s): \t{0}\n----------------------------------------------------------", Arrays.toString(env.getActiveProfiles())));
        log.info(sb.toString());
    }

    private static List<String> ips() {
        List<String> ipList = new ArrayList<>();
        try {
            Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
            NetworkInterface networkInterface;
            Enumeration<InetAddress> inetAddresses;
            InetAddress inetAddress;
            String ip;
            while (networkInterfaces.hasMoreElements()) {
                networkInterface = networkInterfaces.nextElement();
                inetAddresses = networkInterface.getInetAddresses();
                while (inetAddresses.hasMoreElements()) {
                    inetAddress = inetAddresses.nextElement();
                    if (inetAddress instanceof Inet4Address) { // IPV4
                        ip = inetAddress.getHostAddress();
                        ipList.add(ip);
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return ipList;
    }
```

