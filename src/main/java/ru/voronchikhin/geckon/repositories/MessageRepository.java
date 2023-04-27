package ru.voronchikhin.geckon.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.voronchikhin.geckon.models.Message;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Integer> {
    List<Message> findAllByDiscussion_SlugOrderByDate(String slug);
}
