package ru.voronchikhin.geckon.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class NewsContentDTO {
    @JsonIgnore
    private String id;
    private String type;
    private String text;
    private String url;
    private String imgId;
    @JsonIgnore
    private String image;
//    @JsonDeserialize(as = MultipartFile.class)
//    private MultipartFile image;

    public NewsContentDTO(String type, String text, String url) {
        this.type = type;
        this.text = text;
        this.url = url;
    }
}
