package ru.voronchikhin.geckon.models;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Table;
import lombok.*;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class PersonRelationsKey implements Serializable {
    @Column(name = "person_id")
    private Integer personId;

    @Column(name = "friend_id")
    private Integer friendId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PersonRelationsKey that = (PersonRelationsKey) o;
        return Objects.equals(personId, that.personId) && Objects.equals(friendId, that.friendId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(personId, friendId);
    }
}
