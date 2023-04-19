package ru.voronchikhin.geckon.dto;

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
    @NotNull
    String name;
    @NotNull
    String slug;
}
