package ru.voronchikhin.geckon.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TagsDTO {
    @JsonIgnore
    String inputValue;
    @NotNull
    String name;
    @NotNull
    String slug;

    public TagsDTO(String name, String slug) {
        this.name = name;
        this.slug = slug;
    }
}
