package ru.voronchikhin.geckon.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ThemeDTO {
    private Integer id;

    private String name;

    private String slug;

    private Date dateOfCreation;
}
