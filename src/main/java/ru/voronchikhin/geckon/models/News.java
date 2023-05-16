package ru.voronchikhin.geckon.models;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Set;

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
    @Column(name = "theme")
    private String theme;
    @Column(name = "main_url")
    private String mainUrl;
    @Column(name = "slug")
    private String slug;

    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<NewsContent> contentList;
    @ManyToMany
    @JoinTable(
            name = "news_tags",
            joinColumns = @JoinColumn(name = "news_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    @ToString.Exclude
    private Set<Tags> tagsList;

    //getters and setters, hashcode and equals

    public News(String author, Date dateOfCreation, String title, String duration, String theme,
                String mainUrl, String slug) {
        this.author = author;
        this.dateOfCreation = dateOfCreation;
        this.title = title;
        this.duration = duration;
        this.theme = theme;
        this.mainUrl = mainUrl;
        this.slug = slug;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        News news = (News) o;
        return Objects.equals(id, news.id) && Objects.equals(author, news.author)
                && Objects.equals(dateOfCreation, news.dateOfCreation) && Objects.equals(title, news.title)
                && Objects.equals(duration, news.duration) && Objects.equals(theme, news.theme)
                && Objects.equals(mainUrl, news.mainUrl) && Objects.equals(slug, news.slug);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, author, dateOfCreation, title, duration, theme, mainUrl, slug);
    }
}
