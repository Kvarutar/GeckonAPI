package ru.voronchikhin.geckon.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "event")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "url")
    private String url;
    @Column(name = "descr")
    private String descr;
    @Column(name = "title")
    private String title;
    @Column(name = "img_url")
    private String imgUrl;
    @Column(name = "time_date")
    private Date timeDate;
    @Column(name = "address")
    private String address;
    @Column(name = "people_count")
    private String peopleCount;
    @Column(name = "town")
    private String town;
    @Column(name = "metro")
    private String metro;
    @Column(name = "slug")
    private String slug;
    @ManyToMany
    Set<Person> liked;

    //getters and setters, hashcode and equals

    public Event(String url, String descr, String title, String imgUrl, Date timeDate, String address,
                 String peopleCount, String town, String metro, String slug) {
        this.url = url;
        this.descr = descr;
        this.title = title;
        this.imgUrl = imgUrl;
        this.timeDate = timeDate;
        this.address = address;
        this.peopleCount = peopleCount;
        this.town = town;
        this.metro = metro;
        this.slug = slug;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Event event = (Event) o;
        return Objects.equals(id, event.id) && Objects.equals(url, event.url) && Objects.equals(descr, event.descr)
                && Objects.equals(title, event.title) && Objects.equals(imgUrl, event.imgUrl)
                && Objects.equals(timeDate, event.timeDate) && Objects.equals(address, event.address)
                && Objects.equals(peopleCount, event.peopleCount) && Objects.equals(town, event.town)
                && Objects.equals(metro, event.metro);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, url, descr, title, imgUrl, timeDate, address, peopleCount, town, metro);
    }
}
