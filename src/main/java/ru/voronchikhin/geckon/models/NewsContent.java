package ru.voronchikhin.geckon.models;

        import jakarta.persistence.*;
        import lombok.*;

@Entity
@Table(name = "news_content")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class NewsContent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    //сделать ENUM?
    @Column(name = "type")
    private String type;

    @Column(name = "text", length = 1024)
    private String text;

    @Column(name = "url", length = 1024)
    private String url;

    @ManyToOne
    @JoinColumn(name = "news_id", referencedColumnName = "id")
    private News owner;

    public NewsContent(String type, String text, String url, News owner){
        this.type = type;
        this.text = text;
        this.url = url;
        this.owner = owner;
    }
}
