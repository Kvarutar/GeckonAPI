package ru.voronchikhin.geckon.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.voronchikhin.geckon.models.Message;

import java.util.Date;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReachDiscussionDTO {
    private String name;

    private String descr;

    private Date dateOfCreation;

    private Set<DiscussionTagsDTO> tagsList;

    private Set<MessageDTO> messages;
}
