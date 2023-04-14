package ru.voronchikhin.geckon.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.voronchikhin.geckon.models.NewsContent;

public interface NewsContentRepository extends JpaRepository<NewsContent, Integer> {
}
