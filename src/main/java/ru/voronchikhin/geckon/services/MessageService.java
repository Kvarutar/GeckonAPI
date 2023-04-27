package ru.voronchikhin.geckon.services;

import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.descriptor.web.ContextHandler;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.voronchikhin.geckon.dto.MessageDTO;
import ru.voronchikhin.geckon.models.Discussion;
import ru.voronchikhin.geckon.models.Message;
import ru.voronchikhin.geckon.models.Person;
import ru.voronchikhin.geckon.repositories.MessageRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MessageService {
    private final MessageRepository messageRepository;
    private final DiscussionService discussionService;

    public List<MessageDTO> findAllByDiscussion(String slug){
        return messageRepository.findAllByDiscussion_SlugOrderByDate(slug).stream()
                .map(this::convertMessageToMessageDTO).toList();
    }

    @Transactional
    public void save(MessageDTO messageDTO, String slug){
        Person person = (Person) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Discussion discussion = discussionService.findDiscussionBySlug(slug);
        Message messageToSave = new Message(messageDTO.getText(), messageDTO.getDate(), person, discussion);

        messageRepository.save(messageToSave);
    }

    private MessageDTO convertMessageToMessageDTO(Message message){
        return new MessageDTO(message.getId(), message.getText(), message.getDate(), message.getSender().getId(),
                message.getSender().getName());
    }
}
