package persistence;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import model.AcademicEvent;
import model.CareerEvent;
import model.ClubEvent;
import model.Event;
import model.Organizer;
import model.Registration;
import model.SocialEvent;
import model.Student;

/** Converts pipe-delimited text and objects without file I/O or business operations. */
public class DataConverter {
    private static final String STUDENT_HEADER = "studentId|name|email|major";
    private static final String ORGANIZER_HEADER = "organizerId|name|email|organization";
    private static final String EVENT_HEADER = "eventId|type|name|description|date|time|location|capacity|organizerId|extraInfo|extraDetail";
    private static final String REGISTRATION_HEADER = "registrationId|studentId|eventId|registrationDate|status";

    public List<Student> parseStudents(String text) throws DataFormatException {
        String[] lines = splitLines(text);
        int header = headerIndex(lines, STUDENT_HEADER);
        List<Student> students = new ArrayList<>();
        Set<String> ids = new HashSet<>();
        for (int i = header + 1; i < lines.length; i++) {
            if (lines[i].isBlank()) continue;
            String[] fields = splitFields(lines[i], 4, i + 1);
            for (int field = 0; field < fields.length; field++) {
                requireText(fields[field], "Student field " + (field + 1), i + 1);
            }
            String id = fields[0];
            checkUnique(ids, id, i + 1);
            students.add(new Student(id, fields[1], fields[2], fields[3]));
        }
        return List.copyOf(students);
    }

    public List<Organizer> parseOrganizers(String text) throws DataFormatException {
        String[] lines = splitLines(text);
        int header = headerIndex(lines, ORGANIZER_HEADER);
        List<Organizer> organizers = new ArrayList<>();
        Set<String> ids = new HashSet<>();
        for (int i = header + 1; i < lines.length; i++) {
            if (lines[i].isBlank()) continue;
            String[] fields = splitFields(lines[i], 4, i + 1);
            for (int field = 0; field < fields.length; field++) {
                requireText(fields[field], "Organizer field " + (field + 1), i + 1);
            }
            String id = fields[0];
            checkUnique(ids, id, i + 1);
            organizers.add(new Organizer(id, fields[1], fields[2], fields[3]));
        }
        return List.copyOf(organizers);
    }

    /** Accepts the eleven-column event format; date and time remain model strings. */
    public List<Event> parseEvents(String text) throws DataFormatException {
        String[] lines = splitLines(text);
        int header = headerIndex(lines, EVENT_HEADER);
        List<Event> events = new ArrayList<>();
        Set<String> ids = new HashSet<>();
        for (int i = header + 1; i < lines.length; i++) {
            if (lines[i].isBlank()) continue;
            int line = i + 1;
            String[] fields = splitFields(lines[i], 11, line);
            int id = parseInteger(fields[0], "eventId", line);
            int capacity = parseInteger(fields[7], "capacity", line);
            for (int field = 1; field < 10; field++) {
                requireText(fields[field], "Event field " + (field + 1), line);
            }
            Event event = switch (fields[1]) {
                case "ACADEMIC" -> new AcademicEvent(id, fields[2], fields[3], fields[4], fields[5],
                        fields[6], fields[8], capacity, fields[9], fields[10]);
                case "CLUB" -> new ClubEvent(id, fields[2], fields[3], fields[4], fields[5],
                        fields[6], fields[8], capacity, fields[9], parseBoolean(fields[10], line));
                case "CAREER" -> new CareerEvent(id, fields[2], fields[3], fields[4], fields[5],
                        fields[6], fields[8], capacity, fields[9], parseBoolean(fields[10], line));
                case "SOCIAL" -> new SocialEvent(id, fields[2], fields[3], fields[4], fields[5],
                        fields[6], fields[8], capacity, fields[9], parseBoolean(fields[10], line));
                default -> throw new DataFormatException(line, "Unknown event type: " + fields[1]);
            };
            checkUnique(ids, Integer.toString(id), line);
            events.add(event);
        }
        return List.copyOf(events);
    }

    /** Restores references from caller-owned indexes; does not enforce registration business rules. */
    public List<Registration> parseRegistrations(String text,
            Map<String, Student> students, Map<Integer, Event> events) throws DataFormatException {
        if (students == null || events == null) {
            throw new IllegalArgumentException("Student and event indexes are required.");
        }
        String[] lines = splitLines(text);
        int header = headerIndex(lines, REGISTRATION_HEADER);
        List<Registration> registrations = new ArrayList<>();
        Set<String> ids = new HashSet<>();
        for (int i = header + 1; i < lines.length; i++) {
            if (lines[i].isBlank()) continue;
            int line = i + 1;
            String[] fields = splitFields(lines[i], 5, line);
            requireText(fields[0], "registrationId", line);
            requireText(fields[1], "studentId", line);
            int eventId = parseInteger(fields[2], "eventId", line);
            Student student = students.get(fields[1]);
            Event event = events.get(eventId);
            if (student == null) {
                throw new DataFormatException(line, "Student does not exist: " + fields[1]);
            }
            if (event == null) {
                throw new DataFormatException(line, "Event does not exist: " + fields[2]);
            }
            LocalDate date = parseDate(fields[3], line);
            Registration.Status status = parseStatus(fields[4], line);
            checkUnique(ids, fields[0], line);
            registrations.add(new Registration(fields[0], student, event, date, status));
        }
        return List.copyOf(registrations);
    }


    public String serializeStudents(List<Student> students) {
        if (students == null) throw new IllegalArgumentException("Students are required.");
        StringBuilder text = new StringBuilder(STUDENT_HEADER).append('\n');
        Set<String> ids = new HashSet<>();
        for (Student student : students) {
            if (student == null) throw new IllegalArgumentException("Student must not be null.");
            appendRow(text, ids, false, student.getStudentId(), student.getName(),
                    student.getEmail(), student.getMajor());
        }
        return text.toString();
    }


    public String serializeOrganizers(List<Organizer> organizers) {
        if (organizers == null) throw new IllegalArgumentException("Organizers are required.");
        StringBuilder text = new StringBuilder(ORGANIZER_HEADER).append('\n');
        Set<String> ids = new HashSet<>();
        for (Organizer organizer : organizers) {
            if (organizer == null) throw new IllegalArgumentException("Organizer must not be null.");
            appendRow(text, ids, false, organizer.getOrganizerId(), organizer.getName(),
                    organizer.getEmail(), organizer.getOrganization());
        }
        return text.toString();
    }


    public String serializeEvents(List<Event> events) {
        if (events == null) throw new IllegalArgumentException("Events are required.");
        StringBuilder text = new StringBuilder(EVENT_HEADER).append('\n');
        Set<String> ids = new HashSet<>();
        for (Event event : events) {
            if (event == null) throw new IllegalArgumentException("Event must not be null.");
            String type;
            String extraInfo;
            String extraDetail;
            boolean allowEmptySpeaker = false;
            if (event instanceof AcademicEvent academic) {
                type = "ACADEMIC";
                extraInfo = academic.getSubject();
                extraDetail = academic.getSpeaker();
                allowEmptySpeaker = true;
            } else if (event instanceof ClubEvent club) {
                type = "CLUB";
                extraInfo = club.getClubName();
                extraDetail = Boolean.toString(club.isMembersOnly());
            } else if (event instanceof CareerEvent career) {
                type = "CAREER";
                extraInfo = career.getCompanyName();
                extraDetail = Boolean.toString(career.isResumeRequired());
            } else if (event instanceof SocialEvent social) {
                type = "SOCIAL";
                extraInfo = social.getTheme();
                extraDetail = Boolean.toString(social.isFoodProvided());
            } else {
                throw new IllegalArgumentException("Unsupported event type: " + event.getClass().getName());
            }
            appendRow(text, ids, allowEmptySpeaker, Integer.toString(event.getEventId()), type,
                    event.getName(), event.getDescription(), event.getDate(), event.getTime(),
                    event.getLocation(), Integer.toString(event.getCapacity()), event.getOrganizerId(),
                    extraInfo, extraDetail);
        }
        return text.toString();
    }


    public String serializeRegistrations(List<Registration> registrations) {
        if (registrations == null) throw new IllegalArgumentException("Registrations are required.");
        StringBuilder text = new StringBuilder(REGISTRATION_HEADER).append('\n');
        Set<String> ids = new HashSet<>();
        for (Registration registration : registrations) {
            if (registration == null || registration.getStudent() == null || registration.getEvent() == null
                    || registration.getDate() == null || registration.getStatus() == null) {
                throw new IllegalArgumentException("Registration, references, date and status are required.");
            }
            String status = switch (registration.getStatus()) {
                case ACTIVE -> "ACTIVE";
                case CANCELED -> "CANCELLED";
            };
            appendRow(text, ids, false, registration.getRegistrationId(),
                    registration.getStudent().getStudentId(), Integer.toString(registration.getEvent().getEventId()),
                    registration.getDate().toString(), status);
        }
        return text.toString();
    }

    private static String[] splitLines(String text) {
        if (text == null) throw new IllegalArgumentException("Text must not be null.");
        if (text.startsWith("\uFEFF")) text = text.substring(1);
        return text.split("\\r\\n|\\n|\\r", -1);
    }

    private static int headerIndex(String[] lines, String expected) throws DataFormatException {
        for (int i = 0; i < lines.length; i++) {
            if (lines[i].isBlank()) continue;
            if (!lines[i].equals(expected)) {
                throw new DataFormatException(i + 1, "Expected header: " + expected);
            }
            return i;
        }
        throw new DataFormatException(1, "Missing header: " + expected);
    }

    private static String[] splitFields(String line, int count, int lineNumber) throws DataFormatException {
        String[] fields = line.split("\\|", -1);
        if (fields.length != count) {
            throw new DataFormatException(lineNumber, "Expected exactly " + count + " fields.");
        }
        return fields;
    }

    private static int parseInteger(String value, String field, int line) throws DataFormatException {
        try {
            return Integer.parseInt(value.trim());
        } catch (NumberFormatException error) {
            throw new DataFormatException(line, "Invalid integer for " + field + ": " + value, error);
        }
    }

    private static void requireText(String value, String field, int line) throws DataFormatException {
        if (value.isBlank()) throw new DataFormatException(line, field + " must not be blank.");
    }

    private static boolean parseBoolean(String value, int line) throws DataFormatException {
        if (value.equals("true")) return true;
        if (value.equals("false")) return false;
        throw new DataFormatException(line, "Expected true or false: " + value);
    }

    private static LocalDate parseDate(String value, int line) throws DataFormatException {
        try {
            return LocalDate.parse(value);
        } catch (DateTimeParseException error) {
            throw new DataFormatException(line, "Invalid registration date: " + value, error);
        }
    }

    private static Registration.Status parseStatus(String value, int line) throws DataFormatException {
        return switch (value) {
            case "ACTIVE" -> Registration.Status.ACTIVE;
            case "CANCELED", "CANCELLED" -> Registration.Status.CANCELED;
            default -> throw new DataFormatException(line, "Unknown registration status: " + value);
        };
    }

    private static void checkUnique(Set<String> ids, String id, int line) throws DataFormatException {
        if (!ids.add(id)) throw new DataFormatException(line, "Duplicate ID: " + id);
    }

    private static void appendRow(StringBuilder text, Set<String> ids,
            boolean allowEmptyLastField, String... fields) {
        for (int i = 0; i < fields.length; i++) {
            String field = fields[i];
            boolean allowEmpty = allowEmptyLastField && i == fields.length - 1;
            if (field == null || (!allowEmpty && field.isBlank()) || field.indexOf('|') >= 0
                    || field.indexOf('\r') >= 0 || field.indexOf('\n') >= 0) {
                throw new IllegalArgumentException("Invalid text field at column " + (i + 1));
            }
        }
        if (!ids.add(fields[0])) throw new IllegalArgumentException("Duplicate ID: " + fields[0]);
        text.append(String.join("|", fields)).append('\n');
    }
}
