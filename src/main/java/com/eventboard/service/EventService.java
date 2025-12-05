package com.eventboard.service;

import com.eventboard.dto.EventDto;
import com.eventboard.entity.EventNotice;
import com.eventboard.entity.User;
import com.eventboard.repository.EventNoticeRepository;
import com.eventboard.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EventService {

    private final EventNoticeRepository eventNoticeRepository;
    private final UserRepository userRepository;

    // -----------------------------------------------------------
    // 🔍 CHECK DUPLICATE TITLE
    // -----------------------------------------------------------
    public boolean isTitleDuplicate(String title) {
        return eventNoticeRepository.existsByTitleIgnoreCase(title);
    }

    // -----------------------------------------------------------
    // ➕ CREATE EVENT
    // -----------------------------------------------------------
    public String createEvent(EventDto dto, Long userId) {

        // 1️⃣ Check duplicate title
        if (eventNoticeRepository.existsByTitleIgnoreCase(dto.getTitle())) {
            return "Duplicate event title! Please use a different title.";
        }

        // 2️⃣ Validate user
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()) {
            return "User not found!";
        }

        // 3️⃣ Create new Event
        EventNotice event = new EventNotice();
        event.setTitle(dto.getTitle());
        event.setDescription(dto.getDescription());
        event.setLocation(dto.getLocation());
        event.setCategory(dto.getCategory());
        event.setDateTime(dto.getDateTime());
        event.setCreatedBy(user.get());

        event.setCreatedAt(LocalDateTime.now());
        event.setUpdatedAt(LocalDateTime.now());

        eventNoticeRepository.save(event);

        return "Event created successfully!";
    }

    // -----------------------------------------------------------
    // 📌 GET ALL EVENTS
    // -----------------------------------------------------------
    public List<EventNotice> getAllEvents() {
        return eventNoticeRepository.findAll();
    }

    // -----------------------------------------------------------
    // 📌 GET EVENT BY ID
    // -----------------------------------------------------------
    public EventNotice getEventById(Long eventId) {
        return eventNoticeRepository.findById(eventId).orElse(null);
    }

    // -----------------------------------------------------------
    // ✏ UPDATE EVENT
    // -----------------------------------------------------------
    public String updateEvent(Long eventId, EventDto dto) {

        Optional<EventNotice> optionalEvent = eventNoticeRepository.findById(eventId);

        if (optionalEvent.isEmpty()) {
            return "Event not found!";
        }

        EventNotice existingEvent = optionalEvent.get();

        // Prevent duplicate title (exclude current event)
        if (!existingEvent.getTitle().equalsIgnoreCase(dto.getTitle())
                && eventNoticeRepository.existsByTitleIgnoreCase(dto.getTitle())) {
            return "Duplicate title! Choose another title.";
        }

        existingEvent.setTitle(dto.getTitle());
        existingEvent.setDescription(dto.getDescription());
        existingEvent.setLocation(dto.getLocation());
        existingEvent.setCategory(dto.getCategory());
        existingEvent.setDateTime(dto.getDateTime());

        existingEvent.setUpdatedAt(LocalDateTime.now());

        eventNoticeRepository.save(existingEvent);

        return "Event updated successfully!";
    }

    // -----------------------------------------------------------
    // ❌ DELETE EVENT
    // -----------------------------------------------------------
    public String deleteEvent(Long eventId) {

        if (!eventNoticeRepository.existsById(eventId)) {
            return "Event not found!";
        }

        eventNoticeRepository.deleteById(eventId);
        return "Event deleted!";
        }
        public List<EventNotice> searchEvents (String title, String category, String location, Long userId){
            return eventNoticeRepository.searchEvents(title, category, location, userId);
        }

}
