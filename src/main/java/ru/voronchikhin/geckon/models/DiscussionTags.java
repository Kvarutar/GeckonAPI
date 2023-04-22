package ru.voronchikhin.geckon.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "discussion_tags")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class DiscussionTags {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @Column(name = "name")
    String name;

    @Column(name = "slug")
    String slug;

    @Column(name = "count")
    Integer count;

    @ManyToMany
    @ToString.Exclude
    Set<Discussion> discussions;

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
