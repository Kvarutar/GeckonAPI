package ru.voronchikhin.geckon.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;

@Entity
@Table(name = "tags")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Tags {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "slug")
    private String slug;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tags tags = (Tags) o;
        return Objects.equals(id, tags.id) && Objects.equals(name, tags.name) && Objects.equals(slug, tags.slug);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, slug);
    }
}
