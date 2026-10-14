package persistence;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import model.Organizer;
import model.Student;

/** Converts between pipe-delimited text and business objects, without file I/O. */
public class DataConverter {
    private static final String STUDENT_HEADER = "studentId|name|email|major";
    private static final String ORGANIZER_HEADER = "organizerId|name|email|organization";
    private static final String REGISTRATION_HEADER = "registrationId|studentId|eventId|registrationDate|status";
    private static final String EVENT_HEADER = "eventId|type|name|description|date|time|location|capacity|organizerId|extraInfo";

    public List<Student> parseStudents(String text) throws DataFormatException {
        return parse(text, STUDENT_HEADER,
                (id, fields) -> new Student(Integer.toString(id), fields[1], fields[2], fields[3]));
    }

    public List<Organizer> parseOrganizers(String text) throws DataFormatException {
        return parse(text, ORGANIZER_HEADER,
                (id, fields) -> new Organizer(Integer.toString(id), fields[1], fields[2], fields[3]));
    }

    /** Produces a header and LF-terminated records in input order, including for an empty list.
     * @throws IllegalArgumentException for null entries, duplicate IDs or unrepresentable fields
     */
    public String serializeStudents(List<Student> students) {
        return serialize(students, STUDENT_HEADER, student -> new String[] {
                student.getStudentId(), student.getName(),
                student.getEmail(), student.getMajor()});
    }

    /** Produces text in the same format accepted by parseOrganizers, without writing a file.
     * @throws IllegalArgumentException for null entries, duplicate IDs or unrepresentable fields
     */
    public String serializeOrganizers(List<Organizer> organizers) {
        return serialize(organizers, ORGANIZER_HEADER, organizer -> new String[] {
                organizer.getOrganizerId(), organizer.getName(),
                organizer.getEmail(), organizer.getOrganization()});
    }

    private <T> String serialize(List<T> objects, String header, FieldsExtractor<T> extractor) {
        if (objects == null) {
            throw new IllegalArgumentException("Objects must not be null.");
        }
        StringBuilder text = new StringBuilder(header).append('\n');
        HashSet<String> ids = new HashSet<>();
        for (T object : objects) {
            if (object == null) {
                throw new IllegalArgumentException("Record must not be null.");
            }
            String[] fields = extractor.extract(object);
            for (String field : fields) {
                if (field == null || field.isBlank() || field.indexOf('|') >= 0
                        || field.indexOf('\r') >= 0 || field.indexOf('\n') >= 0) {
                    throw new IllegalArgumentException("Fields must be nonblank and cannot contain pipes or line breaks.");
                }
            }
            if (!ids.add(fields[0])) {
                throw new IllegalArgumentException("Duplicate ID: " + fields[0]);
            }
            text.append(String.join("|", fields)).append('\n');
        }
        return text.toString();
    }

    private <T> List<T> parse(String text, String header, RecordFactory<T> factory)
            throws DataFormatException {
        if (text == null) {
            throw new IllegalArgumentException("Text must not be null.");
        }
        if (text.startsWith("\uFEFF")) {
            text = text.substring(1);
        }
        String[] lines = text.split("\\r\\n|\\n|\\r", -1);
        ArrayList<T> objects = new ArrayList<>();
        HashSet<Integer> ids = new HashSet<>();
        boolean headerSeen = false;
        for (int index = 0; index < lines.length; index++) {
            String line = lines[index];
            int lineNumber = index + 1;
            if (line.isBlank()) {
                continue;
            }
            if (!headerSeen) {
                if (!line.equals(header)) {
                    throw new DataFormatException(lineNumber, "Expected header: " + header);
                }
                headerSeen = true;
                continue;
            }
            String[] fields = line.split("\\|", -1);
            if (fields.length != 4) {
                throw new DataFormatException(lineNumber, "Expected exactly four fields.");
            }
            try {
                int id = Integer.parseInt(fields[0].trim());
                T object = factory.create(id, fields);
                if (!ids.add(id)) {
                    throw new DataFormatException(lineNumber, "Duplicate ID: " + id);
                }
                objects.add(object);
            } catch (IllegalArgumentException exception) {
                throw new DataFormatException(lineNumber,
                        "Invalid record: " + exception.getMessage(), exception);
            }
        }
        if (!headerSeen) {
            throw new DataFormatException(1, "Missing header: " + header);
        }
        return List.copyOf(objects);
    }

    @FunctionalInterface
    private interface RecordFactory<T> {
        T create(int id, String[] fields);
    }

    @FunctionalInterface
    private interface FieldsExtractor<T> {
        String[] extract(T object);
    }
}
