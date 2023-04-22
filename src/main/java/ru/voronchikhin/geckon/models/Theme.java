package ru.voronchikhin.geckon.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "theme")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Theme {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @Column(name = "name")
    String name;

    @Column(name = "slug")
    String slug;

    @OneToMany(mappedBy = "theme")
    private List<Discussion> discussions;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Theme theme = (Theme) o;
        return Objects.equals(id, theme.id) && Objects.equals(name, theme.name) && Objects.equals(slug, theme.slug);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, slug);
    }
}
