package ru.voronchikhin.geckon.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.voronchikhin.geckon.dto.DiscussionTagsDTO;
import ru.voronchikhin.geckon.models.Discussion;
import ru.voronchikhin.geckon.models.DiscussionTags;
import ru.voronchikhin.geckon.repositories.DiscussionRepository;
import ru.voronchikhin.geckon.repositories.DiscussionTagsRepository;

import java.util.*;

@Service
@Transactional(readOnly = true)
public class DiscussionTagsService {
    private final DiscussionTagsRepository repository;
    private final DiscussionRepository discussionRepository;

    public DiscussionTagsService(DiscussionTagsRepository repository, DiscussionRepository discussionRepository) {
        this.repository = repository;
        this.discussionRepository = discussionRepository;
    }

    public List<DiscussionTagsDTO> findAllByDiscussionSize(){
        List<DiscussionTags> res = repository.findAll();

        res.sort(Comparator.comparingInt(a -> a.getDiscussions().size()));
        if (res.size() > 10){
            res = res.subList(0, 10);
        }
        return res.stream().map(this::convertTagsToTagsDTO).toList();
    }

    public List<DiscussionTagsDTO> findAllByThemeAndDiscussionSize(String slug){
        List<DiscussionTags> res = repository.findAllByDiscussions_Theme_Slug(slug);

        res.sort(Comparator.comparingInt(a -> a.getDiscussions().size()));
        if (res.size() > 10){
            res = res.subList(0, 10);
        }
        return res.stream().map(this::convertTagsToTagsDTO).toList();
    }

    public DiscussionTagsDTO findBySlug(String slug){
        return repository.findBySlug(slug).map(this::convertTagsToTagsDTO).orElse(null);
    }

    public DiscussionTags findTagBySlug(String slug){
        return repository.findBySlug(slug).get();
    }

    public boolean isPresent(String slug){
        return repository.findBySlug(slug).isPresent();
    }

    @Transactional
    public void save(DiscussionTagsDTO discussionTagsDTO){
        findBySlug(discussionTagsDTO.getSlug());
        repository.save(convertTagsDTOToTags(discussionTagsDTO));
    }

    @Transactional
    public void saveToDiscussion(DiscussionTagsDTO discussionTagsDTO, String newsSlug){
        Optional<DiscussionTags> tag = repository.findBySlug(discussionTagsDTO.getSlug());
        Discussion discussion = discussionRepository.findBySlug(newsSlug).get();

        if (tag.isPresent()){
            Set<Discussion> discussions = tag.get().getDiscussions();
            discussions.add(discussion);
            tag.get().setDiscussions(discussions);
        }else{
            DiscussionTags tagToSave = convertTagsDTOToTags(discussionTagsDTO);
            Set<Discussion> discussions = tagToSave.getDiscussions();
            discussions.add(discussion);
            tagToSave.setDiscussions(discussions);
            repository.save(tagToSave);
        }
    }

    @Transactional
    public void delete(String slug){
        DiscussionTags tags = repository.findBySlug(slug).get();
        repository.deleteBySlug(slug);

        tags.getDiscussions().forEach(el -> el.getDiscussionTags().remove(tags));
    }

    public DiscussionTagsDTO convertTagsToTagsDTO(DiscussionTags discussionTags){
        return new DiscussionTagsDTO(discussionTags.getId(), discussionTags.getName(),
                discussionTags.getSlug(), discussionTags.getDiscussions().size());
    }

    public DiscussionTags convertTagsDTOToTags(DiscussionTagsDTO discussionTagsDTO){
        return new DiscussionTags(discussionTagsDTO.getName(), discussionTagsDTO.getSlug(),
                discussionTagsDTO.getCount(), new HashSet<>());
    }
}
