package ru.voronchikhin.geckon.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.voronchikhin.geckon.dto.EventDTO;
import ru.voronchikhin.geckon.services.EventService;
import ru.voronchikhin.geckon.util.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
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
    public List<EventDTO> getAllBetween(@RequestParam Integer page,
                                        @RequestParam Integer eventsPerPage,
                                        @RequestParam String start,
                                        @RequestParam String end) throws ParseException {

        Date startDate = new SimpleDateFormat("yyyy-MM-dd").parse(start);
        Date endDate = new SimpleDateFormat("yyyy-MM-dd").parse(end);

        return eventService.findAllBetween(page, eventsPerPage, startDate, endDate);
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

    @ExceptionHandler({EventsAddingException.class, EventsEditingException.class})
    public ResponseEntity<ErrorResponse> handleException(RuntimeException e){
        ErrorResponse response = new ErrorResponse(
                e.getMessage(),
                System.currentTimeMillis()
        );

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
