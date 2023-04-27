package ru.voronchikhin.geckon.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "message")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "text")
    private String text;

    @Column(name = "date")
    private Date date;

    @ManyToOne
    @JoinColumn(name = "sender_id", referencedColumnName = "id")
    private Person sender;

    @ManyToOne
    @JoinColumn(name = "discussion_id", referencedColumnName = "id")
    private Discussion discussion;

    public Message(String text, Date date, Person sender, Discussion discussion) {
        this.text = text;
        this.date = date;
        this.sender = sender;
        this.discussion = discussion;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Message message = (Message) o;
        return Objects.equals(id, message.id) && Objects.equals(text, message.text)
                && Objects.equals(date, message.date) && Objects.equals(sender, message.sender)
                && Objects.equals(discussion, message.discussion);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, text, date, sender, discussion);
    }
}
