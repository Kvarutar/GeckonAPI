package ru.voronchikhin.geckon.services;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.voronchikhin.geckon.dto.NewsDTO;
import ru.voronchikhin.geckon.models.News;
import ru.voronchikhin.geckon.repositories.NewsRepository;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class NewsService {
    private final NewsRepository newsRepository;

    public NewsService(NewsRepository newsRepository) {
        this.newsRepository = newsRepository;
    }

    public List<News> findAll(){
        return newsRepository.findAll();
    }

    public List<News> findPagination(int page, int newsPerPage){
        return newsRepository.findAll(PageRequest.of(page, newsPerPage, Sort.by("dateOfCreation")))
                .getContent();
    }

    public News findById(int id){
        Optional<News> foundedNews = newsRepository.findById(id);
        return foundedNews.orElse(null);
    }

    private NewsDTO convertNewsToNewsDTO(News news){
        NewsDTO newsDTO = new NewsDTO();
        return new NewsDTO(news.getId(), news.getAuthor(), news.getDateOfCreation(), news.getTheme(),
                news.getDuration(), news.getTitle());
    }
}
