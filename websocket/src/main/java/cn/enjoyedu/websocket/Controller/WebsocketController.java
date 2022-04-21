package cn.enjoyedu.websocket.Controller;

import cn.enjoyedu.websocket.server.WebsocketServer;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/api/ws")
public class WebsocketController {
    /**
     * 群发消息
     * @param message
     * @return
     */
    @RequestMapping(value = "/sendAll", method= RequestMethod.GET)
    public String sendAllMessage(@RequestParam String message){
        try {
            WebsocketServer.broadCastInfo(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "ok";
    }

    @RequestMapping(value = "/sendOne", method = RequestMethod.GET)
    public String sendOneMessage(@RequestParam String message, @RequestParam String id){
        try {
            WebsocketServer.sendMessage(id, message);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "ok";
    }
}
