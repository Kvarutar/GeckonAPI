package ru.voronchikhin.geckon.models;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;

import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "news")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class News {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "author")
    private String author;

    @Column(name = "date_of_creation")
    private Date dateOfCreation;

    @Column(name = "title")
    private String title;

    @Column(name = "duration")
    private String duration;

    //ENUM?
    @Column(name = "theme")
    private String theme;


}
