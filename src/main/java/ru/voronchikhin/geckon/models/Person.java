package ru.voronchikhin.geckon.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;

@Entity
@Table(name = "person")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @Column(name = "login")
    String login;

    @Column(name = "password")
    String password;

    @Column(name = "name")
    String name;

    @Column(name = "photourl")
    String photoUrl;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return Objects.equals(id, person.id) && Objects.equals(login, person.login) && Objects.equals(password, person.password) && Objects.equals(name, person.name) && Objects.equals(photoUrl, person.photoUrl);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, login, password, name, photoUrl);
    }
}
