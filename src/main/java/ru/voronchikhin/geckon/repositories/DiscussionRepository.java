package ru.voronchikhin.geckon.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.voronchikhin.geckon.models.Discussion;

import java.util.List;
import java.util.Optional;

@Repository
public interface DiscussionRepository extends JpaRepository<Discussion, Integer> {
    Optional<Discussion> findBySlug(String slug);
    List<Discussion> findAllByOrderByDateOfCreation();
    List<Discussion> findAllByTheme_Slug(String slug);

    List<Discussion> findAll();

    void deleteBySlug(String slug);
}
