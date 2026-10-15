package service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import model.Event;
import model.Registration;
import model.Student;

/** Owns registrations in insertion order; no file, presentation or manager dependencies. */
public class RegistrationManager {
    private final List<Registration> registrations = new ArrayList<>();

    /** Registers or reactivates one student/event pair without replacing existing objects. */
    public boolean register(Student student, Event event, LocalDate date) {
        if (student == null || student.getStudentId() == null || student.getStudentId().isBlank()
                || event == null || date == null) {
            throw new IllegalArgumentException("Student, student ID, event and registration date are required.");
        }
        Registration existing = findByStudentAndEvent(student.getStudentId(), event.getEventId()).orElse(null);
        if (existing != null && existing.getStatus() == Registration.Status.ACTIVE) return false;
        if (getActiveCount(event.getEventId()) >= event.getCapacity()) return false;

        if (existing != null) {
            //canceled before: reactivate instead of adding a second record
            existing.setStatus(Registration.Status.ACTIVE);
            existing.setDate(date);
        } else {
            int number = registrations.size() + 1;
            while (findById("R" + number).isPresent()) number++;
            registrations.add(new Registration("R" + number, student, event, date, Registration.Status.ACTIVE));
        }
        return true;
    }

    /** Cancels an active registration; repeated cancellation returns false. */
    public boolean cancel(String studentId, int eventId) {
        Registration registration = findByStudentAndEvent(studentId, eventId).orElse(null);
        if (registration == null || registration.getStatus() != Registration.Status.ACTIVE) return false;
        registration.setStatus(Registration.Status.CANCELED);
        return true;
    }

    public Optional<Registration> findById(String registrationId) {
        for (Registration registration : registrations) {
            if (registration.getRegistrationId().equals(registrationId)) return Optional.of(registration);
        }
        return Optional.empty();
    }

    public Optional<Registration> findByStudentAndEvent(String studentId, int eventId) {
        for (Registration registration : registrations) {
            if (registration.getStudent().getStudentId().equals(studentId)
                    && registration.getEvent().getEventId() == eventId) {
                return Optional.of(registration);
            }
        }
        return Optional.empty();
    }

    public List<Registration> findActiveByStudent(String studentId) {
        return registrations.stream()
                .filter(registration -> registration.getStatus() == Registration.Status.ACTIVE
                        && registration.getStudent().getStudentId().equals(studentId)).toList();
    }

    public List<Registration> findActiveByEvent(int eventId) {
        return registrations.stream()
                .filter(registration -> registration.getStatus() == Registration.Status.ACTIVE
                        && registration.getEvent().getEventId() == eventId).toList();
    }

    public int getActiveCount(int eventId) {
        int count = 0;
        for (Registration registration : registrations) {
            if (registration.getStatus() == Registration.Status.ACTIVE
                    && registration.getEvent().getEventId() == eventId) count++;
        }
        return count;
    }

    /** Returns an immutable snapshot, retaining the original registration objects. */
    public List<Registration> getAll() {
        return List.copyOf(registrations);
    }

    public void replaceAll(List<Registration> objects) {

        if (objects == null)
            throw new IllegalArgumentException("Collection is required.");
        RegistrationManager candidate = new RegistrationManager();

        for (Registration registration : objects) {
            if (registration == null || registration.getRegistrationId() == null
                    || registration.getRegistrationId().isBlank() || registration.getStudent() == null
                    || registration.getStudent().getStudentId() == null
                    || registration.getStudent().getStudentId().isBlank() || registration.getEvent() == null
                    || registration.getDate() == null || registration.getStatus() == null) {
                throw new IllegalArgumentException("Registration ID, references, date and status are required.");
            }

            if (candidate.findById(registration.getRegistrationId()).isPresent()) {
                throw new IllegalArgumentException("Duplicate registration ID.");
            }
            
            if (candidate.findByStudentAndEvent(registration.getStudent().getStudentId(),
                    registration.getEvent().getEventId()).isPresent()) {
                throw new IllegalArgumentException("Duplicate student/event pair.");
            }
            candidate.registrations.add(registration);
        }
        for (Registration registration : candidate.registrations) {
            if (candidate.getActiveCount(registration.getEvent().getEventId()) > registration.getEvent().getCapacity()) {
                throw new IllegalArgumentException("Event capacity exceeded.");
            }
        }
        registrations.clear();
        registrations.addAll(candidate.registrations);
    }
}
