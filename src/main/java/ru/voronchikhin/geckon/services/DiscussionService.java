package ru.voronchikhin.geckon.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.voronchikhin.geckon.dto.DiscussionDTO;
import ru.voronchikhin.geckon.dto.DiscussionTagsDTO;
import ru.voronchikhin.geckon.models.Discussion;
import ru.voronchikhin.geckon.models.DiscussionTags;
import ru.voronchikhin.geckon.models.Theme;
import ru.voronchikhin.geckon.repositories.DiscussionRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
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
        return discussionRepository.findAll().stream()
                .map(this::convertDiscussionToDiscussionDTO).toList();
    }

    public List<DiscussionDTO> findNew(){
        return discussionRepository.findAllByOrderByDateOfCreation().stream()
                .map(this::convertDiscussionToDiscussionDTO).toList();
    }

    public List<DiscussionDTO> findByTheme(String slug){
        return discussionRepository.findAllByTheme_Slug(slug).stream()
                .map(this::convertDiscussionToDiscussionDTO).toList();
    }

    public DiscussionDTO findBySlug(String slug){
        return discussionRepository.findBySlug(slug).map(this::convertDiscussionToDiscussionDTO).orElse(null);
    }

    public List<DiscussionDTO> findByTag(String tag){
        DiscussionTags tags = discussionTagsService.findTagBySlug(tag);

        return discussionRepository.findAll()
                .stream()
                .filter(el -> el.getDiscussionTags().contains(tags))
                .map(this::convertDiscussionToDiscussionDTO).toList();
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
        Set<DiscussionTagsDTO> tags = discussion.getDiscussionTags().stream()
                .map(discussionTagsService::convertTagsToTagsDTO).collect(Collectors.toSet());

        return new DiscussionDTO(discussion.getId(), discussion.getName(), discussion.getSlug(), discussion.getDescr(),
                discussion.getImgUrl(), discussion.getDateOfCreation(), tags, discussion.getTheme().getSlug());
    }

    private Discussion convertDiscussionDTOToDiscussion(DiscussionDTO discussionDTO){
        Theme theme = themeService.findThemeBySlug(discussionDTO.getThemeSlug());
        Set<DiscussionTags> tags;

        if (discussionDTO.getTagsList() != null){
             tags = discussionDTO.getTagsList().stream()
                    .map(discussionTagsService::convertTagsDTOToTags).collect(Collectors.toSet());
        }else{
            tags = new HashSet<>();
        }


        Discussion discussionToSave = new Discussion(discussionDTO.getName(), discussionDTO.getSlug(),
                discussionDTO.getDescr(), discussionDTO.getImgUrl(), discussionDTO.getDateOfCreation(), tags, theme);

        theme.getDiscussions().add(discussionToSave);

        if (discussionDTO.getTagsList() != null){
            tags.forEach(el -> el.getDiscussions().add(discussionToSave));
        }


        return discussionToSave;
    }

    /*public Discussion convertPureDiscussionsDTOToDiscussions(PureDiscussionsDTO pureDiscussionsDTO){
        return
    }*/
}
