package ru.voronchikhin.geckon.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.voronchikhin.geckon.dto.NewsDTO;
import ru.voronchikhin.geckon.dto.NewsWithContentDTO;
import ru.voronchikhin.geckon.models.News;
import ru.voronchikhin.geckon.services.NewsService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/news")
public class NewsController {
    private final NewsService newsService;

    public NewsController(NewsService newsService) {
        this.newsService = newsService;
    }

    @GetMapping("/all")
    public List<NewsDTO> getNews(){
        return newsService.findAll();
    }

    @GetMapping("/")
    public List<NewsDTO> getPaginationNews(@RequestParam(value = "page", required = true) Integer page,
                                           @RequestParam(value = "news_per_page", required = true) Integer newsPerPage,
                                           @RequestParam(value = "theme") String theme){
        return newsService.findPagination(page, newsPerPage, theme);
    }

    /*@GetMapping("/{id}")
    public NewsWithContentDTO getById(@PathVariable("id") int id){
        return newsService.findById(id);
    }*/

    @GetMapping("/{slug}")
    public NewsWithContentDTO getBySlug(@PathVariable("slug") String slug){
        return newsService.findBySlug(slug);
    }

    //добавить binding response
    @PostMapping("/new")
    public ResponseEntity<HttpStatus> create(@RequestBody NewsWithContentDTO newsWithContentDTO){
        newsService.save(newsWithContentDTO);

        return ResponseEntity.ok(HttpStatus.OK);
    }
}
