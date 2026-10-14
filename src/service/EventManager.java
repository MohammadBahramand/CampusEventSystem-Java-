package service;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import model.Event;

/** Owns events in insertion order; no file or presentation logic. */
public class EventManager {
    private final List<Event> events = new ArrayList<>();

    public boolean add(Event event) {
        if (event == null) throw new IllegalArgumentException("Event is required.");
        if (findById(event.getEventId()).isPresent()) return false;
        events.add(event);
        return true;
    }


    public void replaceAll(List<Event> objects) {

        if (objects == null)
            throw new IllegalArgumentException("Collection is required.");

        EventManager candidate = new EventManager();

        for (Event object : objects) {
            if (!candidate.add(object)) {
                throw new IllegalArgumentException("Duplicate event ID.");
            }
        }

        events.clear();
        events.addAll(candidate.events);
    }

    public Optional<Event> findById(int eventId) {

        for (Event event : events) {
            if (event.getEventId() == eventId) return Optional.of(event);
        }

        return Optional.empty();
    }

    public List<Event> getAll() {
        return List.copyOf(events);
    }

    /** Case-insensitive substring search; an empty keyword returns all events. */
    public List<Event> searchByName(String keyword) {
        if (keyword == null)
            throw new IllegalArgumentException("Keyword is required.");

        String query = keyword.trim().toLowerCase(Locale.ROOT);
        
        return events.stream()
                .filter(event -> event.getName().toLowerCase(Locale.ROOT).contains(query)).toList();
    }

    public List<Event> findByOrganizerId(String organizerId) {
        return events.stream()
                .filter(event -> event.getOrganizerId().equals(organizerId)).toList();
    }
}
