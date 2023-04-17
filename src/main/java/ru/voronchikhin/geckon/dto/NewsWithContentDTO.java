package ru.voronchikhin.geckon.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class NewsWithContentDTO {
    private int id;
    @NotBlank
    private String author;
    private Date dateOfCreation;
    @NotBlank
    private String theme;
    @NotBlank
    private String duration;
    @NotBlank
    private String title;
    @NotBlank
    private String mainUrl;
    @NotBlank
    private String slug;
    private List<NewsContentDTO> contentDTOList;
}
