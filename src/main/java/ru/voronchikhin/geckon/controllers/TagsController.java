package ru.voronchikhin.geckon.controllers;

import org.springframework.web.bind.annotation.*;
import ru.voronchikhin.geckon.dto.DiscussionTagsDTO;
import ru.voronchikhin.geckon.services.DiscussionTagsService;

@RestController
@RequestMapping("/api/v1/tags")
public class TagsController {
    private final DiscussionTagsService tagsService;

    public TagsController(DiscussionTagsService tagsService) {
        this.tagsService = tagsService;
    }

    @PostMapping("/{slug}")
    public void save(@RequestBody DiscussionTagsDTO discussionTagsDTO, @PathVariable("slug") String slug){
        tagsService.saveToDiscussion(discussionTagsDTO, slug);
    }
}
