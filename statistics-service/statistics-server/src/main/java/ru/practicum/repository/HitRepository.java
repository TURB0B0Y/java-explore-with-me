package ru.practicum.repository;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.model.Hit;
import ru.practicum.model.StatisticView;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface HitRepository extends JpaRepository<Hit, Integer> {

    @Query("SELECT h.app as app, h.uri as uri, COUNT(DISTINCT h.ip) as hits " +
            "FROM Hit h " +
            "WHERE h.created BETWEEN :start AND :end AND h.uri IN (:uris) " +
            "GROUP BY h.app, h.uri")
    List<StatisticView> getStatsUnique(LocalDateTime start, LocalDateTime end, String[] uris, Sort sort);

    @Query("SELECT h.app as app, h.uri as uri, COUNT(DISTINCT h.ip) as hits " +
            "FROM Hit h " +
            "WHERE h.created BETWEEN :start AND :end " +
            "GROUP BY h.app, h.uri")
    List<StatisticView> getStatsUnique(LocalDateTime start, LocalDateTime end, Sort sort);

    @Query("SELECT h.app as app, h.uri as uri, COUNT(h.id) as hits " +
            "FROM Hit h " +
            "WHERE h.created BETWEEN :start AND :end AND h.uri IN (:uris) " +
            "GROUP BY h.app, h.uri")
    List<StatisticView> getAll(LocalDateTime start, LocalDateTime end, String[] uris, Sort sort);

    @Query("SELECT h.app as app, h.uri as uri, COUNT(h.id) as hits " +
            "FROM Hit h " +
            "WHERE h.created BETWEEN :start AND :end " +
            "GROUP BY h.app, h.uri")
    List<StatisticView> getAll(LocalDateTime start, LocalDateTime end, Sort sort);

}
