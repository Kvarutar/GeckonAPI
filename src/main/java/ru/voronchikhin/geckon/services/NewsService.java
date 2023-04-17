package ru.voronchikhin.geckon.services;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.voronchikhin.geckon.dto.NewsContentDTO;
import ru.voronchikhin.geckon.dto.NewsDTO;
import ru.voronchikhin.geckon.dto.NewsWithContentDTO;
import ru.voronchikhin.geckon.models.News;
import ru.voronchikhin.geckon.models.NewsContent;
import ru.voronchikhin.geckon.repositories.NewsRepository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class NewsService {
    private final NewsRepository newsRepository;

    public NewsService(NewsRepository newsRepository) {
        this.newsRepository = newsRepository;
    }

    public List<NewsDTO> findAll(){
        return newsRepository.findAll().stream().map(this::convertNewsToNewsDTO).toList();
    }

    public List<NewsDTO> findPagination(int page, int newsPerPage, String theme){
        return newsRepository.findByThemeOrderByDateOfCreation(theme, PageRequest.of(page, newsPerPage))
                .stream().map(this::convertNewsToNewsDTO).toList();
    }

    /*public NewsWithContentDTO findById(int id){
        Optional<News> foundedNews = newsRepository.findById(id);
        return foundedNews.map(this::convertNewsToNewsWithContentDTO).orElse(null);
    }*/

    public NewsWithContentDTO findBySlug(String slug){
        Optional<News> foundedNews = newsRepository.findBySlug(slug);
        return foundedNews.map(this::convertNewsToNewsWithContentDTO).orElse(null);
    }

    @Transactional
    public void save(NewsWithContentDTO NewsWithContentDTO){
        News newNews = convertNewsWithContentDTOToNews(NewsWithContentDTO);
        //contentRepository.saveAll(newNews.getContentList());
        newsRepository.save(newNews);
    }

    private NewsWithContentDTO convertNewsToNewsWithContentDTO(News news){
        List<NewsContentDTO> contentDTOList = news.getContentList().stream().map(this::convertContentToContentDTO).toList();
        return new NewsWithContentDTO(news.getId(), news.getAuthor(), news.getDateOfCreation(), news.getTheme(),
                news.getDuration(), news.getTitle(), news.getMainUrl(), news.getSlug(), contentDTOList);
    }

    private NewsDTO convertNewsToNewsDTO(News news){
        return new NewsDTO(news.getId(), news.getAuthor(), news.getDateOfCreation(), news.getTheme(),
                news.getDuration(), news.getTitle(), news.getMainUrl(), news.getSlug());
    }

    private News convertNewsWithContentDTOToNews(NewsWithContentDTO newsWithContentDTO){
        News news = new News(newsWithContentDTO.getAuthor(), new Date(System.currentTimeMillis()),
                newsWithContentDTO.getTitle(), newsWithContentDTO.getDuration(), newsWithContentDTO.getTheme(),
                newsWithContentDTO.getMainUrl(), newsWithContentDTO.getSlug());

        List<NewsContent> newsContents = newsWithContentDTO.getContentDTOList()
                .stream().map(el -> convertContentDTOToContent(el, news)).toList();

        news.setContentList(newsContents);
        return news;
    }

    private NewsContentDTO convertContentToContentDTO(NewsContent newsContent){
        return new NewsContentDTO(newsContent.getType(), newsContent.getText(), newsContent.getUrl());
    }

    private NewsContent convertContentDTOToContent(NewsContentDTO newsContentDTO, News news){
        return new NewsContent(newsContentDTO.getType(), newsContentDTO.getText(), newsContentDTO.getUrl(), news);
    }
}
