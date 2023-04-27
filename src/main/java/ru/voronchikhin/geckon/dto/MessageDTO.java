package ru.voronchikhin.geckon.dto;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.voronchikhin.geckon.models.Person;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class MessageDTO {
    private Integer id;

    private String text;

    private Date date;

    private Integer personId;

    private String personName;
}
