package ru.voronchikhin.geckon.repositories;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.voronchikhin.geckon.models.Event;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface EventRepository extends JpaRepository<Event, Integer> {
    Optional<Event> findBySlug(String slug);
    void deleteBySlug(String slug);
    List<Event> findAllByTimeDateBetweenOrderByTimeDate(Date start, Date end, PageRequest pageRequest);
    List<Event> findAllByOrderByTimeDate(PageRequest pageRequest);
}
