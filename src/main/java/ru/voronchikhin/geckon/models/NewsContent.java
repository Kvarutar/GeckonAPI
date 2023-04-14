package ru.voronchikhin.geckon.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "news_content")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class NewsContent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    //сделать ENUM?
    @Column(name = "type")
    private String type;

    @Column(name = "text")
    private String text;

    @Column(name = "url")
    private String url;
}
