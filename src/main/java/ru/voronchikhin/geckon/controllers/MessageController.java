package ru.voronchikhin.geckon.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import ru.voronchikhin.geckon.dto.MessageDTO;
import ru.voronchikhin.geckon.services.MessageService;

@Controller
@RequiredArgsConstructor
public class MessageController {
    private final MessageService messageService;

    @MessageMapping("/{slug}/send")
    @SendTo("/topic/{slug}")
    public MessageDTO send(@PathVariable("slug") String slug, @RequestBody MessageDTO messageDTO){
        messageService.save(messageDTO, slug);
        return messageDTO;
    }
}
