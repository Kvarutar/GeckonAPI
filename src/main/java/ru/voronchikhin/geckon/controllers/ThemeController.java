package ru.voronchikhin.geckon.controllers;

import org.springframework.web.bind.annotation.*;
import ru.voronchikhin.geckon.dto.ReachThemeDTO;
import ru.voronchikhin.geckon.dto.ThemeDTO;
import ru.voronchikhin.geckon.services.ThemeService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/theme")
public class ThemeController {
    private final ThemeService themeService;

    public ThemeController(ThemeService themeService) {
        this.themeService = themeService;
    }

    @GetMapping("/")
    public List<ReachThemeDTO> all(@RequestParam(value = "page") int page,
                                   @RequestParam(value = "theme_per_page") int themePerPage,
                                   @RequestParam(value = "name", required = false) String name){
        if(name == null){
            return themeService.findAll(page, themePerPage);
        }else{
            return themeService.findAllBySlug(page, themePerPage, name);
        }
    }

    @GetMapping("/{slug}")
    public ReachThemeDTO findBySlug(@PathVariable("slug") String slug){
        return themeService.findReachBySlug(slug);
    }

    @PostMapping("/new")
    public void save(@RequestBody ThemeDTO themeDTO){
        themeService.save(themeDTO);
    }
}
