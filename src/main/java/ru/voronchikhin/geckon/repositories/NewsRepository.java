package ru.voronchikhin.geckon.repositories;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.voronchikhin.geckon.models.News;
import java.util.List;
import java.util.Optional;

@Repository
public interface NewsRepository extends JpaRepository<News, Integer> {
    List<News> findByThemeOrderByDateOfCreation(String theme, PageRequest pageRequest);
    List<News> findAllByOrderByDateOfCreation(PageRequest pageRequest);
    Optional<News> findBySlug(String slug);
    void deleteBySlug(String slug);
}
