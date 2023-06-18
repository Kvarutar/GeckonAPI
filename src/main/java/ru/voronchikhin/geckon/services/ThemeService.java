package ru.voronchikhin.geckon.services;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.voronchikhin.geckon.dto.*;
import ru.voronchikhin.geckon.models.Discussion;
import ru.voronchikhin.geckon.models.Event;
import ru.voronchikhin.geckon.models.Theme;
import ru.voronchikhin.geckon.repositories.ThemeRepository;

import java.util.Comparator;
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

    public List<ReachThemeDTO> findAll(int page, int themePerPage){
        return themeRepository
                .findAll(PageRequest.of(page, themePerPage))
                .stream()
                .sorted(Comparator.comparingInt(Theme::getDiscussionSize).reversed())
                .map(this::convertThemeToReachThemeDTO).toList();
    }

    public List<ReachThemeDTO> findAllBySlug(int page, int themePerPage, String slug){
        return themeRepository
                .findAllBySlugContains(slug, PageRequest.of(page, themePerPage))
                .stream()
                .sorted(Comparator.comparingInt(Theme::getDiscussionSize).reversed())
                .map(this::convertThemeToReachThemeDTO).toList();
    }

    public ThemeDTO findBySlug(String slug){
        return themeRepository.findBySlug(slug).map(this::convertThemeToThemeDTO).orElse(null);
    }

    public ReachThemeDTO findReachBySlug(String slug){
        return themeRepository.findBySlug(slug).map(this::convertThemeToReachThemeDTO).orElse(null);
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
        return new ThemeDTO(theme.getId(), theme.getName(), theme.getSlug(), theme.getDateOfCreation(),
                theme.getDiscussions().size());
    }

    public Theme convertThemeDTOToTheme(ThemeDTO themeDTO){
        return new Theme(themeDTO.getName(), themeDTO.getSlug(), new Date(System.currentTimeMillis()));
    }

    private ReachThemeDTO convertThemeToReachThemeDTO(Theme theme){

        List<Discussion> topDiscussions = theme.getDiscussions().stream()
                .sorted(Comparator.comparingInt(Discussion::getMessagesSize)
                        .reversed())
                .toList();

        int endIndex = Math.min(topDiscussions.size(), 3);



        return new ReachThemeDTO(theme.getId(), theme.getName(), theme.getSlug(), theme.getDateOfCreation(),
                topDiscussions.subList(0, endIndex).stream().map(this::convertDiscussionsToDiscussionsDTO)
                        .collect(Collectors.toSet()));
    }

    public DiscussionDTO convertDiscussionsToDiscussionsDTO(Discussion discussion){
        Set<DiscussionTagsDTO> tags = discussion.getDiscussionTags().stream()
                .map(tagsService::convertTagsToTagsDTO).collect(Collectors.toSet());

        return new DiscussionDTO(discussion.getId(), discussion.getName(), discussion.getSlug(),
                discussion.getDescr(), discussion.getImgUrl(), discussion.getDateOfCreation(), tags,
                discussion.getTheme().getSlug(), discussion.getMessages().size());
    }
}
