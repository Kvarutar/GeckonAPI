package ru.voronchikhin.geckon.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReachThemeDTO {
    private Integer id;

    private String name;

    private String slug;

    private Date dateOfCreation;

    private Set<PureDiscussionsDTO> discussions;
}
