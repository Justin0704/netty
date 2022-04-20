package cn.enjoyedu.websocket.server;

import org.springframework.stereotype.Component;

import javax.websocket.server.ServerEndpoint;

@ServerEndpoint(value = "/ws/asset")
@Component
public class WebsocketServer {

}
