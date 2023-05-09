package ru.voronchikhin.geckon.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Set;

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
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "slug")
    private String slug;

    @Column(name = "date_of_creation")
    private Date dateOfCreation;

    @OneToMany(mappedBy = "theme", cascade = CascadeType.REMOVE)
    @ToString.Exclude
    private Set<Discussion> discussions;

    public Theme(String name, String slug, Date dateOfCreation) {
        this.name = name;
        this.slug = slug;
        this.dateOfCreation = dateOfCreation;
    }

    public Integer getDiscussionSize(){
        return discussions.size();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Theme theme = (Theme) o;
        return Objects.equals(id, theme.id) && Objects.equals(name, theme.name) && Objects.equals(slug, theme.slug)
                && Objects.equals(dateOfCreation, theme.dateOfCreation);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, slug, dateOfCreation);
    }
}
