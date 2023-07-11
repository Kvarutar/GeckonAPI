package ru.voronchikhin.geckon.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import ru.voronchikhin.geckon.dto.LikedDTO;
import ru.voronchikhin.geckon.dto.PersonDTO;
import ru.voronchikhin.geckon.security.PersonDetails;
import ru.voronchikhin.geckon.services.PersonDetailsService;

@RestController
@RequestMapping("/api/v1/person")
@RequiredArgsConstructor
public class PersonController {
    private final PersonDetailsService personDetailsService;

    @GetMapping("/")
    public PersonDTO getOne(){
        PersonDetails personDetails = (PersonDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return personDetailsService.getByUsername(personDetails.getUsername());
    }

    @PostMapping("/like")
    public ResponseEntity<HttpStatus> like(@RequestBody LikedDTO likedDTO){
        PersonDetails personDetails = (PersonDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        personDetailsService.like(personDetails.getUsername(), likedDTO.getTags());
        return ResponseEntity.ok(HttpStatus.OK);
    }
}
