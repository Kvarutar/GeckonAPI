package ru.voronchikhin.geckon.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.voronchikhin.geckon.models.Tags;

import java.util.Optional;

@Repository
public interface TagsRepository extends JpaRepository<Tags, Integer> {
    Optional<Tags> findByName(String name);
}
