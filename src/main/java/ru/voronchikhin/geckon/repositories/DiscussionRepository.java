package ru.voronchikhin.geckon.repositories;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.voronchikhin.geckon.models.Discussion;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface DiscussionRepository extends JpaRepository<Discussion, Integer> {
    Optional<Discussion> findBySlug(String slug);
    List<Discussion> findAllByOrderByDateOfCreation(PageRequest pageRequest);
    List<Discussion> findAllByTheme_Slug(String slug, PageRequest pageRequest);
    List<Discussion> findAllByTheme_SlugOrderByDateOfCreation(String slug, PageRequest pageRequest);
    List<Discussion> findAllByOrderByMessagesAsc(PageRequest pageRequest);
    @Query(value = "Select *\n" +
            "from discussion d\n" +
            "where d.id not in (select discussions_id\n" +
            "               from discussiontags_discussions q\n" +
            "               where q.discussion_tags_id in (select id\n" +
            "                                              from discussiontags z\n" +
            "                                              where z.slug in :tags))",
    nativeQuery = true)
    List<Discussion> findWithoutTagList(@Param(value = "tags") List<String> tags, PageRequest pageRequest);
    void deleteBySlug(String slug);
}
