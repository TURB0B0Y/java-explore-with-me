package ru.practicum.specification;

import org.springframework.data.jpa.domain.Specification;
import ru.practicum.enums.StatusRequest;
import ru.practicum.filter.AdminEventFilter;
import ru.practicum.filter.EventFilter;
import ru.practicum.model.Event;
import ru.practicum.model.Request;

import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class EventSpecification {

    public static Specification<Event> isOnlyTheFilter(AdminEventFilter filter) {
        return (root, query, builder) -> {
            List<Predicate> predicates = new LinkedList<>();
            if (Objects.nonNull(filter.getUsers()) && !filter.getUsers().isEmpty())
                predicates.add(root.get("initiator").get("id").in(filter.getUsers()));
            if (Objects.nonNull(filter.getStates()) && !filter.getStates().isEmpty())
                predicates.add(root.get("state").in(filter.getStates()));
            if (Objects.nonNull(filter.getCategories()) && !filter.getCategories().isEmpty())
                predicates.add(root.get("category").get("id").in(filter.getCategories()));
            if (Objects.nonNull(filter.getRangeStart()))
                predicates.add(builder.greaterThanOrEqualTo(root.get("eventDate"), filter.getRangeStart()));
            if (Objects.nonNull(filter.getRangeEnd()))
                predicates.add(builder.lessThanOrEqualTo(root.get("eventDate"), filter.getRangeEnd()));
            return builder.and(predicates.toArray(Predicate[]::new));
        };
    }

    public static Specification<Event> isOnlyTheFilter(EventFilter filter) {
        return (root, query, builder) -> {
            List<Predicate> predicates = new LinkedList<>();
            if (Objects.nonNull(filter.getText()) && !filter.getText().trim().isEmpty())
                predicates.add(builder.or(
                        builder.like(root.get("annotation"), '%' + filter.getText() + '%'),
                        builder.like(root.get("description"), '%' + filter.getText() + '%')
                ));
            if (Objects.nonNull(filter.getCategories()) && !filter.getCategories().isEmpty())
                predicates.add(root.get("category").get("id").in(filter.getCategories()));
            if (Objects.nonNull(filter.getPaid()))
                predicates.add(builder.equal(root.get("paid"), filter.getPaid()));
            if (Objects.nonNull(filter.getRangeStart()))
                predicates.add(builder.greaterThanOrEqualTo(root.get("eventDate"), filter.getRangeStart()));
            if (Objects.nonNull(filter.getRangeEnd()))
                predicates.add(builder.lessThanOrEqualTo(root.get("eventDate"), filter.getRangeEnd()));
            if (Objects.nonNull(filter.getOnlyAvailable()) && filter.getOnlyAvailable()) {
                Subquery<Long> sub = query.subquery(Long.class);
                Root<Request> requestRoot = sub.from(Request.class);
                sub.select(builder.count(requestRoot)).where(builder.and(
                        builder.equal(requestRoot.get("event").get("id"), root.get("id")),
                        builder.equal(requestRoot.get("status"), StatusRequest.CONFIRMED.name())
                ));
                predicates.add(builder.greaterThan(root.get("participantLimit"), sub));
            }
            return builder.and(predicates.toArray(Predicate[]::new));
        };
    }

}
