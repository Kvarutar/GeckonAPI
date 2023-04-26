package ru.voronchikhin.geckon.services;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.voronchikhin.geckon.models.Person;
import ru.voronchikhin.geckon.repositories.PersonRepository;
import ru.voronchikhin.geckon.security.PersonDetails;

import java.util.Optional;

@Service
public class PersonDetailsService implements UserDetailsService {

    private final PersonRepository personRepository;

    public PersonDetailsService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Person> person = personRepository.findByLogin(username);

        if (person.isEmpty()){
            throw new UsernameNotFoundException("user not found");
        }

        return new PersonDetails(person.get());
    }
}
