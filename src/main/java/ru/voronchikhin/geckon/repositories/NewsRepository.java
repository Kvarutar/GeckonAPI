package ru.voronchikhin.geckon.repositories;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.voronchikhin.geckon.models.Discussion;
import ru.voronchikhin.geckon.models.News;
import java.util.List;
import java.util.Optional;

@Repository
public interface NewsRepository extends JpaRepository<News, Integer> {
    List<News> findByTheme(String theme, PageRequest pageRequest);
    List<News> findAllByOrderByDateOfCreation(PageRequest pageRequest);
    Optional<News> findBySlug(String slug);

    List<News> findBySlugContains(String slug, PageRequest pageRequest);

    List<News> findAllByThemeAndSlugContains(String theme, String slug, PageRequest pageRequest);

    @Query(value = "Select *\n" +
            "from news d\n" +
            "where d.id in (select news_id\n" +
            "               from news_tags q\n" +
            "               where q.tag_id in (select id\n" +
            "                                  from tags z\n" +
            "                                  where z.slug in :tags)\n" +
            "               group by news_id\n" +
            "               order by count(*) DESC)",
            nativeQuery = true)
    List<News> findByTagsCount(@Param(value = "tags") List<String> tags, PageRequest pageRequest);

    @Query(value = "Select *\n" +
            "from news d\n" +
            "where d.id not in (select news_id\n" +
            "               from news_tags q\n" +
            "               where q.tag_id in (select id\n" +
            "                                  from tags z\n" +
            "                                  where z.slug in :tags)\n)",
            nativeQuery = true)
    List<News> findWithoutTags(@Param(value = "tags") List<String> tags, PageRequest pageRequest);
    void deleteBySlug(String slug);
}
