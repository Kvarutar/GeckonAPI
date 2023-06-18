package ru.voronchikhin.geckon.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.voronchikhin.geckon.models.DiscussionTags;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface DiscussionTagsRepository extends JpaRepository<DiscussionTags, Integer> {
    Optional<DiscussionTags> findBySlug(String slug);
    List<DiscussionTags> findAllByDiscussions_Theme_Slug(String slug);
    void deleteBySlug(String slug);
}
