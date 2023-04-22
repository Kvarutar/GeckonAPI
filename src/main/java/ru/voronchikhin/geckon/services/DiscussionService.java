package ru.voronchikhin.geckon.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.voronchikhin.geckon.dto.DiscussionDTO;
import ru.voronchikhin.geckon.dto.DiscussionTagsDTO;
import ru.voronchikhin.geckon.models.Discussion;
import ru.voronchikhin.geckon.models.DiscussionTags;
import ru.voronchikhin.geckon.models.Theme;
import ru.voronchikhin.geckon.repositories.DiscussionRepository;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class DiscussionService {
    private final DiscussionRepository discussionRepository;
    private final DiscussionTagsService discussionTagsService;
    private final ThemeService themeService;

    public DiscussionService(DiscussionRepository discussionRepository, DiscussionTagsService discussionTagsService,
                             ThemeService themeService) {
        this.discussionRepository = discussionRepository;
        this.discussionTagsService = discussionTagsService;
        this.themeService = themeService;
    }

    public List<DiscussionDTO> findAll(){
        return discussionRepository.findAll().stream().map(this::convertDiscussionToDiscussionDTO).toList();
    }

    public List<DiscussionDTO> findNew(){
        return discussionRepository.findAllByOrderByDateOfCreation().stream()
                .map(this::convertDiscussionToDiscussionDTO).toList();
    }

    public DiscussionDTO findBySlug(String slug){
        return discussionRepository.findBySlug(slug).map(this::convertDiscussionToDiscussionDTO).orElse(null);
    }

    @Transactional
    public void save(DiscussionDTO discussionDTO){
        Discussion discussionToSave = convertDiscussionDTOToDiscussion(discussionDTO);
        findBySlug(discussionToSave.getSlug());
        discussionRepository.save(discussionToSave);
    }

    @Transactional
    public void delete(String slug){
        discussionRepository.deleteBySlug(slug);
    }

    private DiscussionDTO convertDiscussionToDiscussionDTO(Discussion discussion){
        Set<DiscussionTagsDTO> tagsList = discussion.getDiscussionTags().stream()
                .map(discussionTagsService::convertTagsToTagsDTO).collect(Collectors.toSet());

        return new DiscussionDTO(discussion.getId(), discussion.getName(), discussion.getSlug(),
                discussion.getDescr(), discussion.getImgUrl(), discussion.getDateOfCreation(), tagsList,
                themeService.convertThemeToThemeDTO(discussion.getTheme()));
    }

    private Discussion convertDiscussionDTOToDiscussion(DiscussionDTO discussionDTO){
        Discussion discussion = new Discussion(discussionDTO.getName(), discussionDTO.getSlug(),
                discussionDTO.getDescr(), discussionDTO.getImgUrl(), new Date(System.currentTimeMillis()));

        Theme theme = themeService.convertThemeDTOToTheme(discussionDTO.getTheme());
        Set<DiscussionTags> discussionTags = discussionDTO.getTagsList().stream()
                .map(discussionTagsService::convertTagsDTOToTags).collect(Collectors.toSet());

        discussion.setTheme(theme);
        if (themeService.isPresent(theme.getSlug())){
            theme.getDiscussions().add(discussion);
        }else{
            theme.setDiscussions(new HashSet<>());
            theme.getDiscussions().add(discussion);
        }
        themeService.save(theme);

        discussion.setDiscussionTags(discussionTags);
        discussionTags.forEach((el) -> {
            if (!discussionTagsService.isPresent(el.getSlug())){
                el.setDiscussions(new HashSet<>());
                el.getDiscussions().add(discussion);
                discussionTagsService.save(el);
            }
            el.getDiscussions().add(discussion);
        });

        return discussion;
    }
}
