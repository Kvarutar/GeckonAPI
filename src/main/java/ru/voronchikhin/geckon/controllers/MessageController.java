package ru.voronchikhin.geckon.controllers;

import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.socket.WebSocketSession;
import ru.voronchikhin.geckon.dto.MessageDTO;
import ru.voronchikhin.geckon.services.MessageService;

import java.io.IOException;
import java.util.HashMap;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

@Controller
@RequiredArgsConstructor
public class MessageController {
    private final MessageService messageService;
    private final SimpMessagingTemplate simpMessagingTemplate;
    @MessageMapping("/send")
    //@SendTo("/topic/room")
    public MessageDTO send(@Payload MessageDTO messageDTO){
        messageService.save(messageDTO, messageDTO.getThemeSlug());
        simpMessagingTemplate.convertAndSend("/topic/" + messageDTO.getThemeSlug(), messageDTO);
        return messageDTO;
    }
}

