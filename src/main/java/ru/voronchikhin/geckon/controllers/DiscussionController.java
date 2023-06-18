package ru.voronchikhin.geckon.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.voronchikhin.geckon.dto.DiscussionDTO;
import ru.voronchikhin.geckon.dto.DiscussionTagsDTO;
import ru.voronchikhin.geckon.dto.ReachDiscussionDTO;
import ru.voronchikhin.geckon.services.DiscussionService;
import ru.voronchikhin.geckon.services.DiscussionTagsService;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/discussions")
@RequiredArgsConstructor
public class DiscussionController {
    private final DiscussionService discussionService;
    private final DiscussionTagsService discussionTagsService;

    @GetMapping("/")
    public List<DiscussionDTO> all(@RequestParam(value = "page") Integer page,
                                   @RequestParam(value = "discussion_per_page") Integer discussionPerPage,
                                   @RequestParam(value = "theme", required = false) String theme,
                                   @RequestParam(value = "tag", required = false) String tag,
                                   @RequestParam(value = "type", required = false) String type,
                                   @RequestParam(value = "name", required = false) String name){

        if (theme == null && tag == null){
            return discussionService.findAll(page, discussionPerPage, name);
        }else if (theme != null){
            return discussionService.findByTheme(theme, page, discussionPerPage, type, name);
        } else{
            return discussionService.findByTag(tag);
        }
    }

    @GetMapping("/tags")
    public List<DiscussionTagsDTO> getTags(@RequestParam(value = "theme", required = false) String theme){
        if (theme != null){
            return discussionTagsService.findAllByThemeAndDiscussionSize(theme);
        }else{
            return discussionTagsService.findAllByDiscussionSize();
        }

    }

    @GetMapping("/{slug}")
    public ReachDiscussionDTO getMessages(@PathVariable("slug") String slug){
        return discussionService.findWithMessages(slug);
    }

    @GetMapping("/without")
    public List<DiscussionDTO> without(@RequestParam(value = "page") Integer page,
                                       @RequestParam(value = "discussion_per_page") Integer discussionPerPage,
                                       @RequestParam(value = "tags") String[] tags){

        return discussionService.findAllByNotTags(List.of(tags), page, discussionPerPage);
    }

    @PostMapping("/new")
    public ResponseEntity<HttpStatus> create(@RequestBody DiscussionDTO discussionDTO){
        discussionService.save(discussionDTO);

        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PostMapping("/{slug}")
    public ResponseEntity<HttpStatus> delete(@PathVariable("slug") String slug){
        discussionService.delete(slug);

        return ResponseEntity.ok(HttpStatus.OK);
    }
}
