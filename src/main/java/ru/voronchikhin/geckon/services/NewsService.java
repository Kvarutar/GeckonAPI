package ru.voronchikhin.geckon.services;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.voronchikhin.geckon.dto.NewsContentDTO;
import ru.voronchikhin.geckon.dto.NewsDTO;
import ru.voronchikhin.geckon.dto.NewsWithContentDTO;
import ru.voronchikhin.geckon.dto.TagsDTO;
import ru.voronchikhin.geckon.models.News;
import ru.voronchikhin.geckon.models.NewsContent;
import ru.voronchikhin.geckon.models.Tags;
import ru.voronchikhin.geckon.repositories.NewsRepository;
import ru.voronchikhin.geckon.repositories.TagsRepository;
import ru.voronchikhin.geckon.util.NewsAddingException;
import ru.voronchikhin.geckon.util.NewsDeletingException;

import java.util.*;

@Service
@Transactional(readOnly = true)
public class NewsService {
    private final NewsRepository newsRepository;
    private final TagsRepository tagsRepository;

    public NewsService(NewsRepository newsRepository, TagsRepository tagsRepository) {
        this.newsRepository = newsRepository;
        this.tagsRepository = tagsRepository;
    }

    public List<NewsDTO> findAll(int page, int newsPerPage){
        return newsRepository.findAllByOrderByDateOfCreation(PageRequest.of(page, newsPerPage))
                .stream().map(this::convertNewsToNewsDTO).toList();
    }

//    public List<NewsDTO> findAllByNotTags(List<String> tags){
//        return;
//    }

    public List<NewsDTO> findByTagsCount(List<String> tags, int page, int newsPerPage){
        return newsRepository.findByTagsCount(tags, PageRequest.of(page, newsPerPage))
                .stream().map(this::convertNewsToNewsDTO).toList();
    }

    public List<NewsDTO> findWithoutTags(List<String> tags, int page, int newsPerPage){
        return newsRepository.findWithoutTags(tags, PageRequest.of(page, newsPerPage))
                .stream().map(this::convertNewsToNewsDTO).toList();
    }

    public List<NewsDTO> findPagination(int page, int newsPerPage, String theme){
        return newsRepository.findByThemeOrderByDateOfCreation(theme, PageRequest.of(page, newsPerPage))
                .stream().map(this::convertNewsToNewsDTO).toList();
    }

    public NewsWithContentDTO findBySlug(String slug){
        Optional<News> foundedNews = newsRepository.findBySlug(slug);
        return foundedNews.map(this::convertNewsToNewsWithContentDTO).orElse(null);
    }

    @Transactional
    public void save(NewsWithContentDTO newsWithContentDTO) throws NewsAddingException {
        News newNews = convertNewsWithContentDTOToNews(newsWithContentDTO);
        if (newsRepository.findBySlug(newsWithContentDTO.getSlug()).isPresent()){
            throw new NewsAddingException("There is already exist news with this title");
        }else{
            newsRepository.save(newNews);
        }
    }

    @Transactional
    public void delete(String slug) throws NewsDeletingException {
        if (newsRepository.findBySlug(slug).isPresent()){
            newsRepository.deleteBySlug(slug);
        }else{
            throw new NewsDeletingException("No news with this title");
        }
    }

    //
    //converts section
    //

    private NewsWithContentDTO convertNewsToNewsWithContentDTO(News news){
        List<NewsContentDTO> contentDTOList = news.getContentList().stream().map(this::convertContentToContentDTO).toList();
        List<TagsDTO> tagsDTOList = news.getTagsList().stream().map(this::convertTagsToTagsDTO).toList();
        return new NewsWithContentDTO(news.getId(), news.getAuthor(), news.getDateOfCreation(), news.getTheme(),
                news.getDuration(), news.getTitle(), news.getMainUrl(), news.getSlug(), contentDTOList, tagsDTOList);
    }

    private NewsDTO convertNewsToNewsDTO(News news){
        List<TagsDTO> tagsDTOList = news.getTagsList().stream().map(this::convertTagsToTagsDTO).toList();

        return new NewsDTO(news.getId(), news.getAuthor(), news.getDateOfCreation(), news.getTheme(),
                news.getDuration(), news.getTitle(), news.getMainUrl(), news.getSlug(), tagsDTOList);
    }

    private News convertNewsWithContentDTOToNews(NewsWithContentDTO newsWithContentDTO){
        News news = new News(newsWithContentDTO.getAuthor(), new Date(System.currentTimeMillis()),
                newsWithContentDTO.getTitle(), newsWithContentDTO.getDuration(), newsWithContentDTO.getTheme(),
                newsWithContentDTO.getMainUrl(), newsWithContentDTO.getSlug());

        List<NewsContent> newsContents = newsWithContentDTO.getContent()
                .stream().map(el -> convertContentDTOToContent(el, news)).toList();

        Set<Tags> tags = new HashSet<>(newsWithContentDTO.getTags()
                .stream().map(el -> convertTagsDTOToTags(el, news)).toList());

        news.setContentList(newsContents);
        news.setTagsList(tags);

        return news;
    }

    private NewsContentDTO convertContentToContentDTO(NewsContent newsContent){
        return new NewsContentDTO(newsContent.getType(), newsContent.getText(), newsContent.getUrl());
    }

    private NewsContent convertContentDTOToContent(NewsContentDTO newsContentDTO, News news){
        return new NewsContent(newsContentDTO.getType(), newsContentDTO.getText(), newsContentDTO.getUrl(), news);
    }

    private TagsDTO convertTagsToTagsDTO(Tags tags){
        return new TagsDTO(tags.getName(), tags.getSlug());
    }

    private Tags convertTagsDTOToTags(TagsDTO tagsDTO, News news){

        if (tagsRepository.findBySlug(tagsDTO.getSlug()).isEmpty()){
            Tags newTag = new Tags(tagsDTO.getName(), tagsDTO.getSlug());
            newTag.setOwner(new HashSet<>());
            newTag.getOwner().add(news);
            tagsRepository.save(newTag);

            return newTag;
        }else{
            return tagsRepository.findBySlug(tagsDTO.getSlug()).get();
        }
    }
}
