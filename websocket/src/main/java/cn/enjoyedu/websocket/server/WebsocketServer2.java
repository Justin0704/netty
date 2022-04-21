package cn.enjoyedu.websocket.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicInteger;

@ServerEndpoint(value = "/ws/asset2")
@Component
public class WebsocketServer2 {
    private static Logger logger = LoggerFactory.getLogger(WebsocketServer.class);
    private static final AtomicInteger onlineCount = new AtomicInteger(0);
    //线程安全set，用来存放每个客户端对应的session对象
    private static CopyOnWriteArraySet<Session> sessionSet = new CopyOnWriteArraySet<>();
    //线程安全map，用来存放每个客户端sessionid和用户名对应的关系
    private static ConcurrentHashMap<String, String> sessionMap = new ConcurrentHashMap<>();

    /**
     * 连接建立成功调用此方法
     * @param session
     */
    @OnOpen
    public void onOpen(Session session){
        sessionSet.add(session);
        Map<String, List<String>> pathParameters = session.getRequestParameterMap();
        String userId = pathParameters.get("toUserId").get(0);
        sessionMap.put(session.getId(), userId);
        int cnt = onlineCount.incrementAndGet();//在线数+1
        logger.info("有连接加入，当前连接数为：{}，sessionIdd = {}", cnt, session.getId());
        try {
            broadCastInfo("系统消息@^用户【"+userId+"】加入群聊");
        } catch (IOException e) {
            e.printStackTrace();
        }
        //sendMessage(session, "连接成功");
    }
    /**
     * 连接关闭调用此方法
     * @param session
     */
    @OnClose
    public void onClose(Session session){
        //将用户session，session和用户名对应关系从本地缓存移除
        sessionSet.remove(session);
        Map<String, List<String>> pathParameters = session.getRequestParameterMap();
        String userId = sessionMap.get(session.getId());
        sessionMap.remove(session.getId());
        int cnt = onlineCount.decrementAndGet();
        logger.info("有连接关闭，当前连接数量为：{}, sessionId = {}", cnt, session.getId());
        try {
            broadCastInfo("系统消息@^用户【"+userId+"】推出群聊");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 收到客户端消息需要调用的方法
     * @param message
     * @param session
     */
    @OnMessage
    public void onMessage(String message, Session session){
        logger.info("来自客户端的消息：{}", message);
        if(message.startsWith("ToUser:")){
            //这里实现一对一的聊天 sendMessageAlone()
        }else{
            //实现群聊
            String msger = sessionMap.get(session.getId());
            try {
                broadCastInfo(msger + "@^" + message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
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
    private static void basicSendMessage(Session session, String message) {
        try {
            session.getBasicRemote().sendText(message);
            logger.info("发送消息方法，message = {}， sessionId = {}", message, session.getId());
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
    public static void broadCastInfo(String message) throws IOException {
        for(Session session : sessionSet){
            if(session.isOpen()){
                basicSendMessage(session, message);
            }
        }
    }

    /**
     * 指定session发送消息
     * @param sessionId
     * @param message
     */
    public static void sendMessageAlone(String sessionId, String message) throws IOException{
        Session session = null;
        for(Session s : sessionSet){
            if(s.getId().equals(sessionId)){
                session = s;
                break;
            }
        }
        if(session != null){
            basicSendMessage(session, message);
        }else{
            logger.warn("没有找到你指定Session ID的会话：{}", sessionId);
        }
    }
}
