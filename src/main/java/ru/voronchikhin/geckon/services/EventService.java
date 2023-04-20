package ru.voronchikhin.geckon.services;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.voronchikhin.geckon.dto.EventDTO;
import ru.voronchikhin.geckon.models.Event;
import ru.voronchikhin.geckon.repositories.EventRepository;
import ru.voronchikhin.geckon.util.EventsAddingException;
import ru.voronchikhin.geckon.util.EventsEditingException;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class EventService {
    private final EventRepository eventRepository;

    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    public List<EventDTO> findAll(){
        List<Event> allEvents = eventRepository.findAllByOrderByTimeDate();
        return allEvents.stream().map(this::convertEventToEventDTO).toList();
    }

    public List<EventDTO> findAllBetween(int page, int eventsPerPage, Date start, Date end){
        List<Event> allEvents = eventRepository.findAllByTimeDateBetween(start, end,
                PageRequest.of(page, eventsPerPage));

        return allEvents.stream().map(this::convertEventToEventDTO).toList();
    }

    @Transactional
    public void save(EventDTO eventDTO) throws EventsAddingException {
        Event newEvent = convertEventDTOToEvent(eventDTO);
        if (eventRepository.findBySlug(eventDTO.getSlug()).isPresent()){
            throw new EventsAddingException("There is already event with this title");
        }else{
            eventRepository.save(newEvent);
        }
    }

    @Transactional
    public void update(EventDTO eventDTO) throws EventsEditingException {
        Optional<Event> eventToUpdate = eventRepository.findBySlug(eventDTO.getSlug());
        if (eventToUpdate.isPresent()){
            Event event = convertEventDTOToEvent(eventDTO);
            event.setId(eventToUpdate.get().getId());
            eventRepository.save(event);
        }else{
            throw new EventsEditingException("There is no event with this title");
        }
    }

    @Transactional
    public void delete(String slug) throws EventsEditingException{
        Optional<Event> eventToDelete = eventRepository.findBySlug(slug);

        if (eventToDelete.isPresent()){
            eventRepository.deleteBySlug(slug);
        }else{
            throw new EventsEditingException("There is no event with this title");
        }
    }

    private EventDTO convertEventToEventDTO(Event event){
        return new EventDTO(event.getId(), event.getUrl(), event.getDescr(), event.getTitle(), event.getImgUrl(),
                event.getTimeDate(), event.getAddress(), event.getPeopleCount(), event.getTown(),
                event.getMetro(), event.getSlug());
    }

    private Event convertEventDTOToEvent(EventDTO eventDTO){
        return new Event(eventDTO.getUrl(), eventDTO.getDescr(), eventDTO.getTitle(), eventDTO.getImgUrl(),
                eventDTO.getTimeDate(), eventDTO.getAddress(), eventDTO.getPeopleCount(), eventDTO.getTown(),
                eventDTO.getMetro(), eventDTO.getSlug());
    }
}
