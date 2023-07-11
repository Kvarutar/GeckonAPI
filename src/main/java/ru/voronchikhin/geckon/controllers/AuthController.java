package ru.voronchikhin.geckon.controllers;

import jakarta.security.auth.message.AuthException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import ru.voronchikhin.geckon.services.AuthService;
import ru.voronchikhin.geckon.util.*;

import javax.naming.AuthenticationException;

@RestController
@RequestMapping(value = "/api/v1/auth", consumes = {"application/json"},
        produces = {"application/json"})
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/signup")
    public ResponseEntity<AuthenticationResponse> signup(@RequestBody RegistartionRequest request)
            throws AuthException {

        return ResponseEntity.ok(authService.register(request));
    }

    @PostMapping("/signin")
    public ResponseEntity<AuthenticationResponse> signin(@RequestBody AuthenticationRequest request)
            throws AuthException {
        return ResponseEntity.ok(authService.authenticate(request));
    }

    @PostMapping("/token")
    public ResponseEntity<AuthenticationResponse> getNewAccessToken(@RequestBody RefreshJwtRequest request)
            throws AuthException {
        return ResponseEntity.ok(authService.getAccessToken(request.getRefreshToken()));
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthenticationResponse> getNewRefreshToken(@RequestBody RefreshJwtRequest request)
            throws AuthException {
        return ResponseEntity.ok(authService.refresh(request.getRefreshToken()));
    }

    @ExceptionHandler({AuthenticationException.class})
    public ResponseEntity<ErrorResponse> handleException(RuntimeException e){
        ErrorResponse response = new ErrorResponse(
                e.getMessage(),
                System.currentTimeMillis()
        );

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
