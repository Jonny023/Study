## 依赖导入

```
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-websocket</artifactId>
</dependency>
```

## @Configuration 配置

```
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;
@Configuration
public class WebSocketConfiguration {
    @Bean  
    public ServerEndpointExporter serverEndpointExporter (){  
        return new ServerEndpointExporter();  
    }  
}
```


## 端点创建

```
import java.io.IOException;
import javax.websocket.CloseReason;
import javax.websocket.EndpointConfig;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
@Component
@ServerEndpoint(value = "/channel/test")
public class TestChannel {
    private static final Logger LOGGER = LoggerFactory.getLogger(TestChannel.class);
    private Session session;
    @OnMessage(maxMessageSize = 10)
    public void onMessage(byte[] message){
        //skip
    }
    @OnOpen
    public void onOpen(Session session, EndpointConfig endpointConfig){
        LOGGER.info("新的连接,id={}",session.getId());
        session.setMaxIdleTimeout(0);
        this.session = session;
    }
    @OnClose
    public void onClose(CloseReason closeReason){
        LOGGER.info("连接断开,id={} reason={}",this.session.getId(),closeReason);
    }
    @OnError
    public void onError(Throwable throwable) throws IOException {
        LOGGER.info("连接异常,id={},throwable={}",this.session.getId(),throwable);
        this.session.close();
        throwable.printStackTrace();
    }
}
```

# 注意:Endpoint 端点实例是由Servlet容器创建的(未由spring管理)，所以在里面使用spring的注入@Autowired会无效
