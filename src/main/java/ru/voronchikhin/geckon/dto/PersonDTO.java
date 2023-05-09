package ru.voronchikhin.geckon.dto;

import jakarta.persistence.*;
import lombok.*;
import ru.voronchikhin.geckon.models.*;

import java.util.Objects;
import java.util.Set;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PersonDTO {
    private Integer id;
    private String name;
    private String photoUrl;
    private ERole role;
    private Set<PersonRelations> follows;
    private Set<PersonRelations> followers;
    private Set<Event> likedEvents;
    private Set<Tags> likedTags;
}
