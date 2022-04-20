package cn.enjoyedu.stomp.web;

import cn.enjoyedu.stomp.domain.ChatRoomRequest;
import cn.enjoyedu.stomp.domain.ChatRoomResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class StompController {

    /**
     * spring实现的一个发送模板类
     */
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    /**
     * 消息群发，接受发送至自massRequest的请求
     *
     * SendTo 发送至Broker下的指定订阅路径mass
     * Broker再根据getResponse发送消息到订阅/mass/getResponse的用户那里
     */
    @MessageMapping("/massRequest")
    @SendTo("/mass/getResponse")
    public ChatRoomResponse mass(ChatRoomRequest chatRoomRequest){
        System.out.println("name = " + chatRoomRequest.getName()
                + " chatValue = " + chatRoomRequest.getChatValue());
        ChatRoomResponse response = new ChatRoomResponse();
        response.setName(chatRoomRequest.getName());
        response.setChatValue(chatRoomRequest.getChatValue());
        //this.messagingTemplate.convertAndSend();
        return response;
    }

    /**
     * 单独聊天，接受发送至自aloneRequest的请求
     */
    @MessageMapping("/aloneRequest")
    //@SendToUser
    public ChatRoomResponse alone(ChatRoomRequest request){
        System.out.println("SendToUser = " + request.getUserId()
        + " FromName = " + request.getName()
        + " ChatValue = " + request.getChatValue());
        ChatRoomResponse response = new ChatRoomResponse();
        response.setName(request.getName());
        response.setChatValue(request.getChatValue());
        //发送到订阅了/user/{用户的Id}/alone的用户那里
        this.messagingTemplate.convertAndSendToUser(request.getUserId(), "/alone", response);
        return response;
    }


}
