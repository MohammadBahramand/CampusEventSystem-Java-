package app;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import model.Event;
import model.Organizer;
import model.Registration;
import model.Student;
import persistence.DataConverter;
import persistence.DataFormatException;
import persistence.FileManager;
import persistence.InitResult;
import persistence.ReadResult;
import service.EventSystem;
import ui.ConsoleView;

public class Application {

    private FileManager fileManager;
    private DataConverter dataConverter;
    private ConsoleView consoleView;
    private final EventSystem eventSystem;

    private ReadResult studentsReadResult;
    private ReadResult organizersReadResult;
    private ReadResult eventsReadResult;
    private ReadResult registrationsReadResult;

    private boolean keepRuning = true;

    public Application() {
        fileManager = new FileManager();
        dataConverter = new DataConverter();
        consoleView = new ConsoleView();
        eventSystem = new EventSystem();
    }

    public void init()
    {
        studentsReadResult = null;
        organizersReadResult = null;
        eventsReadResult = null;
        registrationsReadResult = null;

        InitResult result = fileManager.init();
        if (!result.isSuccess()) {
            consoleView.showError("Could not prepare data files: " + result.failedPath()
                    + " [" + result.status() + "]: " + result.cause());
            return;
        }

        ReadResult students = fileManager.readStudentsFile();
        if (!checkReadResult("students.txt", students)) return;

        ReadResult organizers = fileManager.readOrganizersFile();
        if (!checkReadResult("organizers.txt", organizers)) return;

        ReadResult events = fileManager.readEventsFile();
        if (!checkReadResult("events.txt", events)) return;

        ReadResult registrations = fileManager.readRegistrationsFile();
        if (!checkReadResult("registrations.txt", registrations)) return;

        // Keep the complete set of raw text only after every read succeeds.
        studentsReadResult = students;
        organizersReadResult = organizers;
        eventsReadResult = events;
        registrationsReadResult = registrations;

        convertData();
    }

    public void run()
    {
        while(keepRuning)
        {

        }
    }

    private void convertData() {
        String fileName = "students.txt";
        try {
            List<Student> students = dataConverter.parseStudents(studentsReadResult.content());

            fileName = "organizers.txt";
            List<Organizer> organizers = dataConverter.parseOrganizers(organizersReadResult.content());

            fileName = "events.txt";
            List<Event> events = dataConverter.parseEvents(eventsReadResult.content());

            Map<String, Student> studentIndex = new HashMap<>();
            for (Student student : students) {
                studentIndex.put(student.getStudentId(), student);
            }
            Map<Integer, Event> eventIndex = new HashMap<>();
            for (Event event : events) {
                eventIndex.put(event.getEventId(), event);
            }

            fileName = "registrations.txt";
            List<Registration> registrations = dataConverter.parseRegistrations(
                    registrationsReadResult.content(), studentIndex, eventIndex);

            // Publish only the complete conversion after all business relationships are validated.
            eventSystem.replaceData(students, organizers, events, registrations);
        } catch (DataFormatException error) {
            consoleView.showError("Could not parse " + fileName + " at line "
                    + error.getLineNumber() + ": " + error.getMessage());
        } catch (IllegalArgumentException error) {
            consoleView.showError("Could not load data: " + error.getMessage());
        }
    }

    private boolean checkReadResult(String fileName, ReadResult result) {
        if (result.isSuccess()) return true;
        consoleView.showError("Could not read " + fileName
                + " [" + result.status() + "]: " + result.cause());
        return false;
    }
}
