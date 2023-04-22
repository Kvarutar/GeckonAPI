package ru.voronchikhin.geckon.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.voronchikhin.geckon.models.DiscussionTags;

import java.util.List;
import java.util.Optional;

@Repository
public interface DiscussionTagsRepository extends JpaRepository<DiscussionTags, Integer> {
    Optional<DiscussionTags> findBySlug(String slug);
    List<DiscussionTags> findAllByOrderByCount();

    void deleteBySlug(String slug);
}
