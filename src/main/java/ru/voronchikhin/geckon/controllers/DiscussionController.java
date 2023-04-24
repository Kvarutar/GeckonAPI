package ru.voronchikhin.geckon.controllers;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.voronchikhin.geckon.dto.DiscussionDTO;
import ru.voronchikhin.geckon.services.DiscussionService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/discussions")
public class DiscussionController {
    private final DiscussionService discussionService;

    public DiscussionController(DiscussionService discussionService) {
        this.discussionService = discussionService;
    }

    @GetMapping()
    public List<DiscussionDTO> all(@RequestParam(value = "theme", required = false) String theme,
                                   @RequestParam(value = "tag", required = false) String tag){
        if (theme == null && tag == null){
            return discussionService.findAll();
        }else if (theme != null){
            return discussionService.findByTheme(theme);
        }else{
            return discussionService.findByTag(tag);
        }
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
