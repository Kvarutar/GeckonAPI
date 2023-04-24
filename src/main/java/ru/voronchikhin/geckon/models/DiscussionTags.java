package ru.voronchikhin.geckon.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "discussiontags")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class DiscussionTags {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "slug")
    private String slug;

    @Column(name = "count")
    private Integer count;

    @ManyToMany
    @ToString.Exclude
    private Set<Discussion> discussions;

    public DiscussionTags(String name, String slug, Integer count, Set<Discussion> discussions) {
        this.name = name;
        this.slug = slug;
        this.count = count;
        this.discussions = discussions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DiscussionTags discussionTags = (DiscussionTags) o;
        return Objects.equals(id, discussionTags.id) && Objects.equals(name, discussionTags.name)
                && Objects.equals(slug, discussionTags.slug) && Objects.equals(count, discussionTags.count);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, slug, count);
    }
}
