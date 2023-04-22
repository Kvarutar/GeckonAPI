package ru.voronchikhin.geckon.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.voronchikhin.geckon.dto.DiscussionTagsDTO;
import ru.voronchikhin.geckon.models.Discussion;
import ru.voronchikhin.geckon.models.DiscussionTags;
import ru.voronchikhin.geckon.models.News;
import ru.voronchikhin.geckon.repositories.DiscussionRepository;
import ru.voronchikhin.geckon.repositories.DiscussionTagsRepository;

import java.util.HashSet;

@Service
@Transactional(readOnly = true)
public class DiscussionTagsService {
    private final DiscussionTagsRepository repository;
    private final DiscussionRepository discussionRepository;

    public DiscussionTagsService(DiscussionTagsRepository repository, DiscussionRepository discussionRepository) {
        this.repository = repository;
        this.discussionRepository = discussionRepository;
    }

    public DiscussionTagsDTO findBySlug(String slug){
        return repository.findBySlug(slug).map(this::convertTagsToTagsDTO).orElse(null);
    }

    public boolean isPresent(String slug){
        return repository.findBySlug(slug).isPresent();
    }

    @Transactional
    public void save(DiscussionTags discussionTags){
        findBySlug(discussionTags.getSlug());
        repository.save(discussionTags);
    }

    @Transactional
    public void saveToNews(DiscussionTagsDTO discussionTagsDTO, String newsSlug){
        DiscussionTags tagToSave = convertTagsDTOToTags(discussionTagsDTO);
        Discussion discussion = discussionRepository.findBySlug(newsSlug).get();

        discussion.getDiscussionTags().add(tagToSave);
        tagToSave.setDiscussions(new HashSet<>());
        tagToSave.getDiscussions().add(discussion);

        repository.save(tagToSave);
    }

    @Transactional
    public void delete(String slug){
        DiscussionTags tags = repository.findBySlug(slug).get();
        repository.deleteBySlug(slug);

        tags.getDiscussions().forEach(el -> el.getDiscussionTags().remove(tags));
    }

    public DiscussionTagsDTO convertTagsToTagsDTO(DiscussionTags discussionTags){
        return new DiscussionTagsDTO(discussionTags.getId(), discussionTags.getName(),
                discussionTags.getSlug(), discussionTags.getCount());
    }

    public DiscussionTags convertTagsDTOToTags(DiscussionTagsDTO discussionTagsDTO){
        return new DiscussionTags(discussionTagsDTO.getName(), discussionTagsDTO.getSlug(),
                discussionTagsDTO.getCount());
    }
}
