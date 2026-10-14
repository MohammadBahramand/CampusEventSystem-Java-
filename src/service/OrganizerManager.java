package service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import model.Organizer;

/** Owns organizers in insertion order; ID queries use linear traversal. */
public class OrganizerManager {
    private final List<Organizer> organizers = new ArrayList<>();

    /** Returns false for a duplicate ID without replacing the existing organizer. */
    public boolean add(Organizer organizer) {
        if (organizer == null)
            throw new IllegalArgumentException("Organizer must not be null.");

        if (findById(organizer.getOrganizerId()).isPresent())
            return false;

        organizers.add(organizer);
        return true;
    }

    /** Replaces the collection after complete validation; preserves references and input order.
     * Accepts any List implementation, including ArrayList and Vector.
     * @throws IllegalArgumentException for a null list, null entry, or duplicate ID
     */
    public void replaceAll(List<Organizer> objects) {
        if (objects == null) 
            throw new IllegalArgumentException("Collection is required.");
        OrganizerManager candidate = new OrganizerManager();
        for (Organizer object : objects) {
            if (!candidate.add(object)) {
                throw new IllegalArgumentException("Duplicate organizer ID.");
            }
        }
        organizers.clear();
        organizers.addAll(candidate.organizers);
    }

    public Optional<Organizer> findById(String organizerId) {
        for (Organizer organizer : organizers) {
            if (organizer.getOrganizerId().equals(organizerId)) return Optional.of(organizer);
        }
        return Optional.empty();
    }

    /** Returns an immutable snapshot in insertion order. */
    public List<Organizer> getAll() {
        return List.copyOf(organizers);
    }
}
