package ru.voronchikhin.geckon.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "discussion")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Discussion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "slug")
    private String slug;

    @Column(name = "descr")
    private String descr;

    @Column(name = "img_url")
    private String imgUrl;

    @Column(name = "date_of_creation")
    private Date dateOfCreation;

    @ManyToMany
    @JoinTable(
            name = "discussions_tags",
            joinColumns = @JoinColumn(name = "discussion_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    @ToString.Exclude
    private Set<DiscussionTags> discussionTags;

    @ManyToOne
    @JoinColumn(name = "theme_id", referencedColumnName = "id")
    private Theme theme;

    public Discussion(String name, String slug, String descr, String imgUrl, Date dateOfCreation) {
        this.name = name;
        this.slug = slug;
        this.descr = descr;
        this.imgUrl = imgUrl;
        this.dateOfCreation = dateOfCreation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Discussion that = (Discussion) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(slug, that.slug)
                && Objects.equals(descr, that.descr) && Objects.equals(imgUrl, that.imgUrl)
                && Objects.equals(dateOfCreation, that.dateOfCreation);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, slug, descr, imgUrl, dateOfCreation);
    }
}
