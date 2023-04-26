package ru.voronchikhin.geckon.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;
import java.util.Set;

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
    private Integer id;

    @Column(name = "login")
    private String login;

    @Column(name = "password")
    private String password;

    @Column(name = "name")
    private String name;

    @Column(name = "photourl")
    private String photoUrl;

    @Column(name = "refresh_token")
    private String refreshToken;

    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private ERole role;

    @OneToMany(mappedBy = "person")
    @ToString.Exclude
    private Set<PersonRelations> follows;

    @OneToMany(mappedBy = "friend")
    @ToString.Exclude
    private Set<PersonRelations> followers;

    /*@ManyToMany
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles;*/

    @ManyToMany
    @JoinTable(
            name = "person_events",
            joinColumns = @JoinColumn(name = "person_id"),
            inverseJoinColumns = @JoinColumn(name = "event_id")
    )
    @ToString.Exclude
    private Set<Event> likedEvents;

    @ManyToMany
    @JoinTable(
            name = "person_tags",
            joinColumns = @JoinColumn(name = "person_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    @ToString.Exclude
    private Set<Tags> likedTags;

    public Person(String login, String password, String name, ERole roles) {
        this.login = login;
        this.password = password;
        this.name = name;
        this.role = roles;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return Objects.equals(id, person.id) && Objects.equals(login, person.login)
                && Objects.equals(password, person.password) && Objects.equals(name, person.name)
                && Objects.equals(photoUrl, person.photoUrl);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, login, password, name, photoUrl);
    }
}
