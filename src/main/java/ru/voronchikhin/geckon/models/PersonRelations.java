package ru.voronchikhin.geckon.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;

@Entity
@Table(name = "person_relations")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class PersonRelations {
    @EmbeddedId
    private PersonRelationsKey id;

    @ManyToOne
    @MapsId("personId")
    @JoinColumn(name = "person_id")
    private Person person;

    @ManyToOne
    @MapsId("friendId")
    @JoinColumn(name = "friend_id")
    private Person friend;

    @Column(name = "status")
    private String status;

    public PersonRelations(PersonRelationsKey id, String status) {
        this.id = id;
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PersonRelations that = (PersonRelations) o;
        return Objects.equals(id, that.id) && Objects.equals(person, that.person) && Objects.equals(friend, that.friend)
                && Objects.equals(status, that.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, person, friend, status);
    }
}
