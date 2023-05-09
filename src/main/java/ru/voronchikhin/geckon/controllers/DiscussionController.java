package ru.voronchikhin.geckon.controllers;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.voronchikhin.geckon.dto.DiscussionDTO;
import ru.voronchikhin.geckon.services.DiscussionService;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/discussions")
public class DiscussionController {
    private final DiscussionService discussionService;

    public DiscussionController(DiscussionService discussionService) {
        this.discussionService = discussionService;
    }

    @GetMapping("/")
    public List<DiscussionDTO> all(@RequestParam(value = "page") Integer page,
                                   @RequestParam(value = "discussion_per_page") Integer discussionPerPage,
                                   @RequestParam(value = "theme", required = false) String theme,
                                   @RequestParam(value = "tag", required = false) String tag,
                                   @RequestParam(value = "type", required = false) String type,
                                   @RequestParam(value = "theme_sort", required = false) String themeSort){
        if (theme == null && tag == null){
            if (type != null && type.equals("hot")){
                return discussionService.findHot(page, discussionPerPage);
            } else if (type != null && type.equals("new")) {
                return discussionService.findNew(page, discussionPerPage);
            }else{
                return discussionService.findAll(page, discussionPerPage);
            }
        }else if (theme != null){
            if (themeSort != null){
                return discussionService.findNewByTheme(theme, page, discussionPerPage);
            }else{
                return discussionService.findByTheme(theme, page, discussionPerPage);
            }
        } else{
            return discussionService.findByTag(tag);
        }
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
