package ru.voronchikhin.geckon.services;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.voronchikhin.geckon.models.ERole;
import ru.voronchikhin.geckon.models.Person;
import ru.voronchikhin.geckon.models.Role;
import ru.voronchikhin.geckon.repositories.PersonRepository;
import ru.voronchikhin.geckon.repositories.RoleRepository;
import ru.voronchikhin.geckon.security.PersonDetails;
import ru.voronchikhin.geckon.util.AuthenticationRequest;
import ru.voronchikhin.geckon.util.AuthenticationResponse;
import ru.voronchikhin.geckon.util.RegistartionRequest;

import java.util.Collections;
import java.util.HashSet;

@Service
public class AuthService {

    private final PersonRepository repository;
    private final JwtService jwtService;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;
    private final RoleRepository roleRepository;

    public AuthService(PersonRepository repository, JwtService jwtService, PasswordEncoder passwordEncoder,
                       AuthenticationManager authenticationManager, RoleRepository roleRepository) {
        this.repository = repository;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.roleRepository = roleRepository;
    }

    public AuthenticationResponse register(RegistartionRequest request) {
        if (repository.existsByLogin(request.getLogin())){
            throw new RuntimeException();
        }else{
            //Role role = new Role(ERole.ROLE_USER);
            //roleRepository.save(role);
            Person person = new Person(request.getLogin(), passwordEncoder.encode(request.getPassword()), request.getName(),
                    ERole.ROLE_USER);

            repository.save(person);
            String jwt = jwtService.generateToken(new PersonDetails(person));
            return new AuthenticationResponse(jwt);
        }
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getLogin(),
                        request.getPassword()
                )
        );

        Person person = repository.findByLogin(request.getLogin()).orElseThrow();
        String jwtToken = jwtService.generateToken(new PersonDetails(person));
        return new AuthenticationResponse(jwtToken);
    }
}
