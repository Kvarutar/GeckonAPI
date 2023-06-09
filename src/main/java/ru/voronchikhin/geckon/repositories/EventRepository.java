package ru.voronchikhin.geckon.repositories;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.voronchikhin.geckon.models.Event;
import ru.voronchikhin.geckon.models.News;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface EventRepository extends JpaRepository<Event, Integer> {
    Optional<Event> findBySlug(String slug);
    void deleteBySlug(String slug);
    List<Event> findAllByTimeDateBetweenOrderByTimeDate(Date start, Date end, PageRequest pageRequest);
    List<Event> findAllByTimeDateBetweenAndSlugContainsOrderByTimeDate(Date start, Date end, String slug,
                                                                       PageRequest pageRequest);
    List<Event> findAllByOrderByTimeDate(PageRequest pageRequest);

    @Query(value = "Select town\n" +
            "from event\n" +
            "group by town",
    nativeQuery = true)
    String[] findAllTowns();

    List<Event> findAllByTownAndTimeDateBetweenOrderByTimeDate(String town, Date start, Date end,
                                                               PageRequest pageRequest);
    List<Event> findAllByTownAndTimeDateBetweenAndSlugContainsOrderByTimeDate(String town, Date start, Date end,
                                                                              String slug, PageRequest pageRequest);
}
