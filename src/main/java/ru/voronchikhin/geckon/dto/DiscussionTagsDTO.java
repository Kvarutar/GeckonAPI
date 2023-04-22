package ru.voronchikhin.geckon.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DiscussionTagsDTO {
    private Integer id;

    private String name;

    private String slug;

    private Integer count;
}
