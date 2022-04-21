package cn.enjoyedu.websocket.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicInteger;

@ServerEndpoint(value = "/ws/asset")
@Component
public class WebsocketServer {

    private static Logger logger = LoggerFactory.getLogger(WebsocketServer.class);
    private static final AtomicInteger onlineCount = new AtomicInteger(0);
    private static CopyOnWriteArraySet<Session> sessionSet = new CopyOnWriteArraySet<Session>();

    /**
     * 连接建立成功调用此方法
     * @param session
     */
    @OnOpen
    public void onOpen(Session session){
        sessionSet.add(session);
        int cnt = onlineCount.incrementAndGet();//在线数+1
        logger.info("有连接加入，当前连接数为：{}", cnt);
        sendMessage(session, "连接成功");
    }

    /**
     * 连接关闭调用此方法
     * @param session
     */
    @OnClose
    public void onClose(Session session){
        sessionSet.remove(session);
        int cnt = onlineCount.decrementAndGet();
        logger.info("有连接关闭，当前连接数量为：{}", cnt);
    }

    /**
     * 收到客户端消息需要调用的方法
     * @param message
     * @param session
     */
    @OnMessage
    public void onMessage(String message, Session session){
        logger.info("来自客户端的消息：{}", message);
        sendMessage(session, "收到消息内容：" + message);
    }

    @OnError
    public void onError(Session session, Throwable error){
        logger.error("发生错误：{}，Session ID: {}", error.getMessage(), session.getId());
        error.printStackTrace();
    }

    /**
     * 发送消息
     * 每次六拉起刷新，session会发生变化
     * @param session
     * @param message
     */
    private static void sendMessage(Session session, String message) {
        try {
            session.getBasicRemote().sendText(String.format("%s (From Server，Session ID=%s)", message, session.getId()));
        } catch (IOException e) {
            logger.error("发送消磁出错：{}", e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 群发消息
     * @param message
     * @throws IOException
     */
    public static void broadCastInfo(String message) throws IOException{
        for(Session session : sessionSet){
            if(session.isOpen()){
                sendMessage(session, message);
            }
        }
    }

    /**
     * 指定session发送消息
     * @param sessionId
     * @param message
     */
    public static void sendMessage(String sessionId, String message) throws IOException{
        Session session = null;
        for(Session s : sessionSet){
            if(s.getId().equals(sessionId)){
                session = s;
            }
        }
        if(session != null){
            sendMessage(session, message);
        }else{
            logger.warn("没有找到你指定Session ID的会话：{}", sessionId);
        }
    }
}
