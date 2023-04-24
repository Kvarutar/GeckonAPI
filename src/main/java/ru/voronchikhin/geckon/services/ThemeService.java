package ru.voronchikhin.geckon.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.voronchikhin.geckon.dto.*;
import ru.voronchikhin.geckon.models.Discussion;
import ru.voronchikhin.geckon.models.Theme;
import ru.voronchikhin.geckon.repositories.ThemeRepository;

import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class ThemeService {
    private final ThemeRepository themeRepository;
    private final DiscussionTagsService tagsService;

    public ThemeService(ThemeRepository themeRepository, DiscussionTagsService tagsService) {
        this.themeRepository = themeRepository;
        this.tagsService = tagsService;
    }

    public List<ReachThemeDTO> findAll(){
        return themeRepository.findAll().stream().map(this::convertThemeToReachThemeDTO).toList();
    }

    public ThemeDTO findBySlug(String slug){
        return themeRepository.findBySlug(slug).map(this::convertThemeToThemeDTO).orElse(null);
    }

    public Theme findThemeBySlug(String slug){
        return themeRepository.findBySlug(slug).orElse(null);
    }

    public boolean isPresent(String slug){
        return themeRepository.findBySlug(slug).isPresent();
    }

    @Transactional
    public void save(ThemeDTO themeDTO){
        findBySlug(themeDTO.getSlug());

        themeRepository.save(convertThemeDTOToTheme(themeDTO));
    }

    @Transactional
    public void delete(String slug){
        themeRepository.deleteBySlug(slug);
    }

    public ThemeDTO convertThemeToThemeDTO(Theme theme){
        return new ThemeDTO(theme.getId(), theme.getName(), theme.getSlug(), theme.getDateOfCreation());
    }

    public Theme convertThemeDTOToTheme(ThemeDTO themeDTO){
        return new Theme(themeDTO.getName(), themeDTO.getSlug(), new Date(System.currentTimeMillis()));
    }

    private ReachThemeDTO convertThemeToReachThemeDTO(Theme theme){
        return new ReachThemeDTO(theme.getId(), theme.getName(), theme.getSlug(), theme.getDateOfCreation(),
                theme.getDiscussions().stream().map(this::convertDiscussionsToPureDiscussionsDTO)
                        .collect(Collectors.toSet()));
    }

    public PureDiscussionsDTO convertDiscussionsToPureDiscussionsDTO(Discussion discussion){
        Set<DiscussionTagsDTO> tags = discussion.getDiscussionTags().stream()
                .map(tagsService::convertTagsToTagsDTO).collect(Collectors.toSet());

        return new PureDiscussionsDTO(discussion.getId(), discussion.getName(), discussion.getSlug(),
                discussion.getDescr(), discussion.getImgUrl(), discussion.getDateOfCreation(), tags);
    }
}
