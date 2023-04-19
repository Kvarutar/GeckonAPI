package ru.voronchikhin.geckon.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.voronchikhin.geckon.dto.EventDTO;
import ru.voronchikhin.geckon.services.EventService;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/v1/events")
public class EventController {
    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping("/all")
    public List<EventDTO> getAll(){
        return eventService.findAll();
    }

    @GetMapping("/")
    public List<EventDTO> getAllBetween(@RequestParam(value = "page", required = true) Integer page,
                                        @RequestParam(value = "events_per_page", required = true) Integer eventsPerPage,
                                        @RequestParam(value = "start") Date start,
                                        @RequestParam(value = "end") Date end){
        return eventService.findAllBetween(page, eventsPerPage, start, end);
    }

    @PostMapping("/new")
    public ResponseEntity<HttpStatus> create(@RequestBody EventDTO eventDTO){
        eventService.save(eventDTO);

        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PostMapping("/update")
    public ResponseEntity<HttpStatus> update(@RequestBody EventDTO eventDTO){
        eventService.update(eventDTO);

        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PostMapping("/{slug}")
    public ResponseEntity<HttpStatus> delete(@PathVariable("slug") String slug){
        eventService.delete(slug);

        return ResponseEntity.ok(HttpStatus.OK);
    }
}
