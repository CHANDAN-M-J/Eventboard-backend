package com.eventboard.repository;

import com.eventboard.entity.EventNotice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EventNoticeRepository extends JpaRepository<EventNotice, Long> {

    // Check duplicate title (case-insensitive)
    boolean existsByTitleIgnoreCase(String title);

    // ---------------------- ADD THIS QUERY BELOW ----------------------

    @Query("SELECT e FROM EventNotice e WHERE "
            + "(:title IS NULL OR LOWER(e.title) LIKE LOWER(CONCAT('%', :title, '%'))) AND "
            + "(:category IS NULL OR e.category = :category) AND "
            + "(:location IS NULL OR LOWER(e.location) LIKE LOWER(CONCAT('%', :location, '%'))) AND "
            + "(:userId IS NULL OR e.createdBy.id = :userId)")
    List<EventNotice> searchEvents(
            @Param("title") String title,
            @Param("category") String category,
            @Param("location") String location,
            @Param("userId") Long userId
    );

}

