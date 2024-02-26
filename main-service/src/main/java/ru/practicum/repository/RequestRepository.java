package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.enums.StatusRequest;
import ru.practicum.model.Event;
import ru.practicum.model.Request;
import ru.practicum.model.User;

import java.util.List;

@Repository
public interface RequestRepository extends JpaRepository<Request, Integer> {

    List<Request> findAllByRequester(User requester);

    List<Request> findAllByEvent(Event event);

    @Query("select count(1) from Request r where r.event.id = :eventId and r.status = :status")
    long getEventRequestCountByStatus(int eventId, StatusRequest status);

    long countByEventAndRequesterAndStatusIn(Event event, User requester, List<StatusRequest> pending);

}
