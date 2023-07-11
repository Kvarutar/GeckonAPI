package ru.voronchikhin.geckon.services;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.voronchikhin.geckon.dto.PersonDTO;
import ru.voronchikhin.geckon.models.Person;
import ru.voronchikhin.geckon.models.Tags;
import ru.voronchikhin.geckon.repositories.PersonRepository;
import ru.voronchikhin.geckon.repositories.TagsRepository;
import ru.voronchikhin.geckon.security.PersonDetails;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PersonDetailsService implements UserDetailsService {

    private final PersonRepository personRepository;
    private final TagsRepository tagsRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Person> person = personRepository.findByLogin(username);

        if (person.isEmpty()){
            throw new UsernameNotFoundException("user not found");
        }

        return new PersonDetails(person.get());
    }

    @Transactional
    public void like(String username, Set<String> tags) throws UsernameNotFoundException {
        Optional<Person> person = personRepository.findByLogin(username);

        if (person.isEmpty()){
            throw new UsernameNotFoundException("user not found");
        }
        Person thisPerson = person.get();

        Set<Tags> likedTags = tags.stream().map((el) -> tagsRepository.findBySlug(el).get()).collect(Collectors.toSet());
        thisPerson.setLikedTags(likedTags);
    }

    public PersonDTO getByUsername(String username){
        Optional<Person> person = personRepository.findByLogin(username);

        return personToPersonDTO(person.get());
    }

    private PersonDTO personToPersonDTO(Person person){
        return new PersonDTO(person.getId(), person.getName(), person.getPhotoUrl(), person.getRole(),
                person.getFollows(), person.getFollowers(), person.getLikedEvents(), person.getLikedTags());
    }
}
