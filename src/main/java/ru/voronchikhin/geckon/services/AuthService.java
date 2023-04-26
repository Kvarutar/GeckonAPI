package ru.voronchikhin.geckon.services;

import io.jsonwebtoken.Claims;
import jakarta.security.auth.message.AuthException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
@RequiredArgsConstructor
public class AuthService {

    private final PersonRepository repository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;



    @Transactional
    public AuthenticationResponse register(RegistartionRequest request) throws AuthException {
        if (repository.existsByLogin(request.getLogin())){
            throw new AuthException("Already exist user with this login");
        }else{
            Person person = new Person(request.getLogin(), passwordEncoder.encode(request.getPassword()),
                    request.getName(), ERole.ROLE_USER);
            final String accessToken = jwtService.generateAccessToken(new PersonDetails(person));
            final String refreshToken = jwtService.generateRefreshToken(new PersonDetails(person));
            person.setRefreshToken(refreshToken);
            repository.save(person);

            return new AuthenticationResponse(accessToken, refreshToken);
        }
    }

    @Transactional
    public AuthenticationResponse authenticate(AuthenticationRequest request) throws AuthException {
        final Person person = repository.findByLogin(request.getLogin())
                .orElseThrow(() -> new AuthException("user not found"));

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getLogin(),
                        request.getPassword()
                )
        );

        final String accessToken = jwtService.generateAccessToken(new PersonDetails(person));
        final String refreshToken = jwtService.generateRefreshToken(new PersonDetails(person));
        person.setRefreshToken(refreshToken);
        return new AuthenticationResponse(accessToken, refreshToken);

        /*if (person.getPassword().equals(passwordEncoder.encode(request.getPassword()))){
            final String accessToken = jwtService.generateAccessToken(new PersonDetails(person));
            final String refreshToken = jwtService.generateRefreshToken(new PersonDetails(person));
            person.setRefreshToken(refreshToken);
            return new AuthenticationResponse(accessToken, refreshToken);
        }else{
            throw new AuthException("incorrect password");
        }*/
    }

    public AuthenticationResponse getAccessToken(String refreshToken) throws AuthException {
        if (jwtService.validateRefreshToken(refreshToken)){
            final Claims refreshClaims = jwtService.getRefreshClaims(refreshToken);
            final String login = refreshClaims.getSubject();
            final Person person = repository.findByLogin(login).orElseThrow(() -> new AuthException("user not found"));
            final String savedRefreshToken = person.getRefreshToken();

            if(savedRefreshToken != null && savedRefreshToken.equals(refreshToken)){
                final String accessToken = jwtService.generateAccessToken(new PersonDetails(person));
                return new AuthenticationResponse(accessToken, null);
            }
        }

        return new AuthenticationResponse(null, null);
    }

    @Transactional
    public AuthenticationResponse refresh(String refreshToken) throws AuthException {
        if (jwtService.validateRefreshToken(refreshToken)){
            final Claims refreshClaims = jwtService.getRefreshClaims(refreshToken);
            final String login = refreshClaims.getSubject();
            final Person person = repository.findByLogin(login).orElseThrow(() -> new AuthException("user not found"));
            final String savedRefreshToken = person.getRefreshToken();

            if(savedRefreshToken != null && savedRefreshToken.equals(refreshToken)){
                final String accessToken = jwtService.generateAccessToken(new PersonDetails(person));
                final String newRefreshToken = jwtService.generateRefreshToken(new PersonDetails(person));

                person.setRefreshToken(newRefreshToken);
                return new AuthenticationResponse(accessToken, newRefreshToken);
            }
        }

        throw new AuthException("invalid jwt token");
    }
}
