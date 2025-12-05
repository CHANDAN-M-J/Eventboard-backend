package com.eventboard.controller;

import com.eventboard.dto.EventDto;
import com.eventboard.entity.EventNotice;
import com.eventboard.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/events")   // ADMIN APIs
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AdminController {

    private final EventService eventService;

    // Create event by admin
    @PostMapping("/create")
    public ResponseEntity<String> createEvent(
            @RequestBody EventDto dto,
            @RequestParam Long userId) {

        String result = eventService.createEvent(dto, userId);

        if (result.equals("User not found!")) {
            return ResponseEntity.status(404).body(result);
        }

        return ResponseEntity.status(201).body(result);
    }

    // Get all events
    @GetMapping
    public ResponseEntity<List<EventNotice>> getAllEvents() {
        return ResponseEntity.ok(eventService.getAllEvents());
    }

    // Delete event
    @DeleteMapping("/{eventId}")
    public ResponseEntity<String> deleteEvent(@PathVariable Long eventId) {
        String result = eventService.deleteEvent(eventId);

        if (result.equals("Event not found!")) {
            return ResponseEntity.status(404).body(result);
        }

        return ResponseEntity.ok(result);
    }
}
