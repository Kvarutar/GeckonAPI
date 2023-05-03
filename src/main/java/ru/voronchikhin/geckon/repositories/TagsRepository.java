package ru.voronchikhin.geckon.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.voronchikhin.geckon.models.DiscussionTags;
import ru.voronchikhin.geckon.models.Tags;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;

@Repository
public interface TagsRepository extends JpaRepository<Tags, Integer> {
    Optional<Tags> findBySlug(String slug);
}
