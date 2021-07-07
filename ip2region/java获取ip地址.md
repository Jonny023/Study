# ip地址：

## 获取公网ip

```java
package cn.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

/**
 * @description: 获取客户端ip
 * @author: jonny
 * @date: 2021-03-12
 */
public final class IpUtil {

    private static final Logger log = LoggerFactory.getLogger(IpUtil.class);

    /**
     * 请求通过反向代理之后，可能包含请求客户端真实IP的HTTP HEADER
     * 如果后续扩展，有其他可能包含IP的HTTP HEADER，加到这里即可
     */
    private static final String[] POSSIBLE_HEADERS = new String[] {
            "X-Forwarded-For", "X-Real-IP", "Proxy-Client-IP",
            "WL-Proxy-Client-IP", "HTTP_CLIENT_IP", "HTTP_X_FORWARDED_FOR"
    };

    private IpUtil() {}

    /**
     * 获取请求客户端的真实IP地址
     * @param request javax.servlet.http.HttpServletRequest
     * @return 客户端端真实IP地址
     */
    public static String getRequestClientRealIP(HttpServletRequest request) {
        String ip;
        // 先检查代理：逐个HTTP HEADER检查过去，看看是否存在客户端真实IP
        for (String header : POSSIBLE_HEADERS) {
            ip = request.getHeader(header);
            if (isNotBlank(ip) && !"unknown".equalsIgnoreCase(ip)) {
                // 请求经过多次反向代理后可能会有多个IP值（以英文逗号分隔），第一个IP才是客户端真实IP
                return ip.contains(",") ? ip.split(",")[0] : ip;
            }
        }
        // 从所有可能的HTTP HEADER中都没有找到客户端真实IP，采用request.getRemoteAddr()来兜底
        ip = request.getRemoteAddr();
        if ("0:0:0:0:0:0:0:1".equals(ip) || "127.0.0.1".equals(ip)) {
            // 说明是从本机发出的请求，直接获取并返回本机IP地址
            return getLocalRealIP();
        }
        return ip;
    }

    /**
     * 获取本机IP地址
     * @return 若配置了外网IP则优先返回外网IP；否则返回本地IP地址。如果本机没有被分配局域网IP地址（例如本机没有连接任何网络），则默认返回127.0.0.1
     */
    public static String getLocalRealIP() {
        String localIP = "127.0.0.1"; // 本地IP
        String netIP = null; // 外网IP

        Enumeration<NetworkInterface> netInterfaces;
        try {
            netInterfaces = NetworkInterface.getNetworkInterfaces();
        } catch (SocketException e) {
            log.error("SocketException {}", e);
            // 发生异常则返回null
            return null;
        }
        InetAddress ip;
        boolean netIPFound = false; // 是否找到外网IP
        while (netInterfaces.hasMoreElements() && !netIPFound) {
            Enumeration<InetAddress> address = netInterfaces.nextElement().getInetAddresses();
            while (address.hasMoreElements()) {
                ip = address.nextElement();
                if (!ip.isSiteLocalAddress() && !ip.isLoopbackAddress() && !ip.getHostAddress().contains(":")) {
                    // 外网IP
                    netIP = ip.getHostAddress();
                    netIPFound = true;
                    break;
                } else if (ip.isSiteLocalAddress() && !ip.isLoopbackAddress() && !ip.getHostAddress().contains(":")) {
                    // 内网IP
                    localIP = ip.getHostAddress();
                }
            }
        }

        if (isNotBlank(netIP)) {
            // 如果配置了外网IP则优先返回外网IP地址
            return netIP;
        }
        return localIP;
    }

    /**
     * <pre>
     * isBlank(null)      = true
     * isBlank("")        = true
     * isBlank(" ")       = true
     * isBlank("bob")     = false
     * isBlank("  bob  ") = false
     * </pre>
     * @param cs 输入参数
     * @return
     */
    private static boolean isBlank(final CharSequence cs) {
        int strLen;
        if (cs == null || (strLen = cs.length()) == 0) {
            return true;
        }
        for (int i = 0; i < strLen; i++) {
            if (!Character.isWhitespace(cs.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * <pre>
     * isNotBlank(null)      = false
     * isNotBlank("")        = false
     * isNotBlank(" ")       = false
     * isNotBlank("bob")     = true
     * isNotBlank("  bob  ") = true
     * </pre>
     * @param cs 输入参数
     * @return
     */
    private static boolean isNotBlank(final CharSequence cs) {
        return !isBlank(cs);
    }

    /**
     * ip地址转成long型数字
     * 将IP地址转化成整数的方法如下：
     * 1、通过String的split方法按.分隔得到4个长度的数组
     * 2、通过左移位操作（<<）给每一段的数字加权，第一段的权为2的24次方，第二段的权为2的16次方，第三段的权为2的8次方，最后一段的权为1
     * @param strIp
     * @return
     */
    public static long ipToLong(String strIp) {
        String[]ip = strIp.split("\\.");
        return (Long.parseLong(ip[0]) << 24) + (Long.parseLong(ip[1]) << 16) + (Long.parseLong(ip[2]) << 8) + Long.parseLong(ip[3]);
    }

    /**
     * 将十进制整数形式转换成127.0.0.1形式的ip地址
     * 将整数形式的IP地址转化成字符串的方法如下：
     * 1、将整数值进行右移位操作（>>>），右移24位，右移时高位补0，得到的数字即为第一段IP。
     * 2、通过与操作符（&）将整数值的高8位设为0，再右移16位，得到的数字即为第二段IP。
     * 3、通过与操作符吧整数值的高16位设为0，再右移8位，得到的数字即为第三段IP。
     * 4、通过与操作符吧整数值的高24位设为0，得到的数字即为第四段IP。
     * @param longIp
     * @return
     */
    public static String longToIP(long longIp) {
        StringBuffer sb = new StringBuffer("");
        // 直接右移24位
        sb.append(String.valueOf((longIp >>> 24)));
        sb.append(".");
        // 将高8位置0，然后右移16位
        sb.append(String.valueOf((longIp & 0x00FFFFFF) >>> 16));
        sb.append(".");
        // 将高16位置0，然后右移8位
        sb.append(String.valueOf((longIp & 0x0000FFFF) >>> 8));
        sb.append(".");
        // 将高24位置0
        sb.append(String.valueOf((longIp & 0x000000FF)));
        return sb.toString();
    }
}
```

## 内网ip

```java
private static final Logger log = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) {
        ConfigurableApplicationContext app = SpringApplication.run(Application.class, args);
        Environment env = app.getEnvironment();
        loadApplicationStartup(env);
    }

    private static void loadApplicationStartup(Environment env) {
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
            log.error("{}", e);
        }
        return ipList;
    }
```

