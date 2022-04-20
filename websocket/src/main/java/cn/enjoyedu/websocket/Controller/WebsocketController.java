package cn.enjoyedu.websocket.Controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/ws")
public class WebsocketController {

    @RequestMapping(value = "/sendAll", method= RequestMethod.GET)
    public String sendAllMessage(@RequestParam String message){
        return "ok";
    }
}
