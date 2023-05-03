package ru.voronchikhin.geckon.dto;

import jakarta.persistence.Column;
import lombok.*;

import java.util.Date;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DiscussionDTO {
    Integer id;

    private String name;

    private String slug;

    private String descr;

    private String imgUrl;

    private Date dateOfCreation;

    private Set<DiscussionTagsDTO> tagsList;

    private String themeSlug;

    private Integer messagesCount;
}
