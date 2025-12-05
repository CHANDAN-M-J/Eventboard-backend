package com.eventboard.controller;

import com.eventboard.dto.EventDto;
import com.eventboard.entity.EventNotice;
import com.eventboard.service.EventService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/events")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class EventController {

    private final EventService eventService;

    // ----------------------------------------------------
    // CREATE EVENT (with duplicate title check + VALIDATION)
    // ----------------------------------------------------
    @PostMapping("/create")
    public ResponseEntity<?> createEvent(
            @Valid @RequestBody EventDto dto,
            @RequestParam Long userId) {

        // Check duplicate title
        if (eventService.isTitleDuplicate(dto.getTitle())) {
            return ResponseEntity.status(409).body("Duplicate title! Choose another.");
        }

        String result = eventService.createEvent(dto, userId);

        if (result.equals("User not found!")) {
            return ResponseEntity.status(404).body(result);
        }

        return ResponseEntity.status(201).body(result);
    }

    // ----------------------------------------------------
    // GET ALL EVENTS
    // ----------------------------------------------------
    @GetMapping
    public ResponseEntity<List<EventNotice>> getAllEvents() {
        return ResponseEntity.ok(eventService.getAllEvents());
    }

    // ----------------------------------------------------
    // GET EVENT BY ID
    // ----------------------------------------------------
    @GetMapping("/{eventId}")
    public ResponseEntity<?> getEventById(@PathVariable Long eventId) {

        EventNotice event = eventService.getEventById(eventId);

        if (event == null) {
            return ResponseEntity.status(404).body("Event not found!");
        }

        return ResponseEntity.ok(event);
    }

    // ----------------------------------------------------
    // CHECK TITLE DUPLICATE
    // ----------------------------------------------------
    @GetMapping("/check-title")
    public ResponseEntity<?> checkTitleDuplicate(@RequestParam String title) {

        boolean isDuplicate = eventService.isTitleDuplicate(title);

        return ResponseEntity.ok(isDuplicate);
    }

    // ----------------------------------------------------
    // SEARCH API (Title + Category + Location + UserId)
    // ----------------------------------------------------
    @GetMapping("/search")
    public ResponseEntity<List<EventNotice>> searchEvents(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) Long userId) {

        List<EventNotice> events = eventService.searchEvents(title, category, location, userId);

        return ResponseEntity.ok(events);
    }

    // ----------------------------------------------------
    // UPDATE EVENT (with duplicate title check + VALIDATION)
    // ----------------------------------------------------
    @PutMapping("/{eventId}")
    public ResponseEntity<?> updateEvent(
            @PathVariable Long eventId,
            @Valid @RequestBody EventDto dto) {

        String result = eventService.updateEvent(eventId, dto);

        if (result.equals("Event not found!")) {
            return ResponseEntity.status(404).body(result);
        }

        if (result.equals("Duplicate title! Choose another title.")) {
            return ResponseEntity.status(409).body(result);
        }

        return ResponseEntity.ok(result);
    }

    // ----------------------------------------------------
    // DELETE EVENT
    // ----------------------------------------------------
    @DeleteMapping("/{eventId}")
    public ResponseEntity<?> deleteEvent(@PathVariable Long eventId) {

        String result = eventService.deleteEvent(eventId);

        if (result.equals("Event not found!")) {
            return ResponseEntity.status(404).body(result);
        }

        return ResponseEntity.ok(result);
    }
}
