package ru.voronchikhin.geckon.dto;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class NewsContentDTO {
    private String type;
    private String text;
    private String url;
}
