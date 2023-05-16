package ru.voronchikhin.geckon.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.Column;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.voronchikhin.geckon.dto.DiscussionDTO;
import ru.voronchikhin.geckon.dto.NewsDTO;
import ru.voronchikhin.geckon.dto.NewsWithContentDTO;
import ru.voronchikhin.geckon.services.NewsService;
import ru.voronchikhin.geckon.util.ErrorResponse;
import ru.voronchikhin.geckon.util.NewsAddingException;
import ru.voronchikhin.geckon.util.NewsDeletingException;

import java.util.*;

@RestController
@RequestMapping("/api/v1/news")
public class NewsController {
    private final NewsService newsService;
    public NewsController(NewsService newsService) {
        this.newsService = newsService;
    }
    @GetMapping("/")
    public List<NewsDTO> getPaginationNews(@RequestParam(value = "page") Integer page,
                                           @RequestParam(value = "news_per_page") Integer newsPerPage,
                                           @RequestParam(value = "theme", required = false) String theme){

        if (theme == null){
            return newsService.findAll(page, newsPerPage);
        }else{
            return newsService.findPagination(page, newsPerPage, theme);
        }

    }
    @GetMapping("/with")
    public List<NewsDTO> with(@RequestParam(value = "page") Integer page,
                              @RequestParam(value = "news_per_page") Integer newsPerPage,
                              @RequestParam(value = "tags") String[] tags){

        return newsService.findByTagsCount(List.of(tags), page, newsPerPage);
    }
    @GetMapping("/without")
    public List<NewsDTO> without(@RequestParam(value = "page") Integer page,
                              @RequestParam(value = "news_per_page") Integer newsPerPage,
                              @RequestParam(value = "tags") String[] tags){

        return newsService.findWithoutTags(List.of(tags), page, newsPerPage);
    }
    @GetMapping("/{slug}")
    public NewsWithContentDTO getBySlug(@PathVariable("slug") String slug){
        return newsService.findBySlug(slug);
    }

    @PostMapping("/new")
    public ResponseEntity<HttpStatus> create(@RequestParam("model")  String model,
                                             @RequestParam("files") MultipartFile[] files) throws JsonProcessingException {
        NewsWithContentDTO newsWithContentDTO = new ObjectMapper().readValue(model, NewsWithContentDTO.class);
        newsService.save(newsWithContentDTO, files);

        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PostMapping("/{slug}")
    public ResponseEntity<HttpStatus> delete(@PathVariable("slug") String slug) {
        newsService.delete(slug);

        return ResponseEntity.ok(HttpStatus.OK);
    }

    @ExceptionHandler({NewsDeletingException.class, NewsAddingException.class})
    public ResponseEntity<ErrorResponse> handleException(RuntimeException e){
        ErrorResponse response = new ErrorResponse(
                e.getMessage(),
                System.currentTimeMillis()
        );

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
