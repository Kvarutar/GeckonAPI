package ru.voronchikhin.geckon.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PureDiscussionsDTO {
    Integer id;

    private String name;

    private String slug;

    private String descr;

    private String imgUrl;

    private Date dateOfCreation;

    private Set<DiscussionTagsDTO> tagsList;
}
