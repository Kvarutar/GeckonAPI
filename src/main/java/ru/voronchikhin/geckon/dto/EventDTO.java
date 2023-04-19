package ru.voronchikhin.geckon.dto;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class EventDTO {
    private Integer id;

    private String url;

    private String descr;

    private String title;

    private String imgUrl;

    private Date timeDate;

    private String address;

    private String peopleCount;

    private String town;

    private String metro;

    private String slug;
}
