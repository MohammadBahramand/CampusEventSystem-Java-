package app;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import model.AcademicEvent;
import model.CareerEvent;
import model.ClubEvent;
import model.Event;
import model.Organizer;
import model.Registration;
import model.SocialEvent;
import model.Student;
import persistence.DataConverter;
import persistence.DataFormatException;
import persistence.FileManager;
import persistence.InitResult;
import persistence.ReadResult;
import persistence.WriteResult;
import service.EventSystem;
import ui.ConsoleInput;
import ui.ConsoleView;

public class Application {

    private FileManager fileManager;
    private DataConverter dataConverter;
    private ConsoleView consoleView;
    private ConsoleInput consoleInput;
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
        consoleInput = new ConsoleInput(new Scanner(System.in));
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
            consoleView.showMenu();
            int selection = consoleInput.readInt();
            handleMenuSelection(selection);
        }

        saveData();

    }

    private void handleMenuSelection(int selection) {
        try {
            switch (selection) {
                case 1 -> handleAddStudent();
                case 2 -> eventSystem.displayAllStudents();
                case 3 -> handleCreateEvent();
                case 4 -> eventSystem.displayAllEvents();
                case 5 -> {
                    consoleView.showMessage("Enter event name to search:");
                    List<Event> matches = eventSystem.searchEventByName(consoleInput.readString());
                    if (matches.isEmpty()) {
                        consoleView.showMessage("No matching events.");
                    } else {
                        for (Event event : matches) {
                            eventSystem.displayEvent(event.getEventId());
                        }
                    }
                }
                case 6 -> handleRegisterStudent();
                case 7 -> handleCancelRegistration();
                case 8 -> {
                    consoleView.showMessage("Enter student ID:");
                    eventSystem.displayStudentRegistrations(consoleInput.readString());
                }
                case 9 -> {
                    consoleView.showMessage("Enter event ID:");
                    eventSystem.displayEventAttendees(consoleInput.readInt());
                }
                case 10 -> eventSystem.displayAllOrganizers();
                case 11 -> {
                    eventSystem.reportEventsWithSpace();
                    eventSystem.reportFullEvents();
                    eventSystem.reportRegistrationsPerEvent();
                    eventSystem.reportStudentsInMultipleEvents();
                }
                case 12 -> handleAddOrganizer();
                case 13 -> {
                    consoleView.showMessage("Enter organizer ID:");
                    eventSystem.displayOrganizer(consoleInput.readText());
                }
                case 14 -> handleViewOrganizerEvents();
                case 15 -> {
                    consoleView.showMessage("Enter student ID:");
                    eventSystem.displayStudent(consoleInput.readText());
                }
                case 16 -> {
                    consoleView.showMessage("Enter event ID:");
                    eventSystem.displayEvent(consoleInput.readInt());
                }
                case 0 -> {
                    keepRuning = false;
                }
                default -> consoleView.showError("Invalid selection. Please enter a number from 0 to 16.");
            }
        } catch (IllegalArgumentException error) {
            consoleView.showError("Could not complete operation: " + error.getMessage());
        }
    }

    private void handleAddStudent() {
        String id = readDataField("Enter student ID:");
        String name = readDataField("Enter student name:");
        String email = readDataField("Enter student email:");
        String major = readDataField("Enter student major:");
        if (eventSystem.addStudent(id, name, email, major)) {
            consoleView.showMessage("Student added successfully.");
        }
    }

    private void handleAddOrganizer() {
        String id = readDataField("Enter organizer ID:");
        String name = readDataField("Enter organizer name:");
        String email = readDataField("Enter organizer email:");
        String organization = readDataField("Enter organization name:");
        if (eventSystem.addOrganizer(id, name, email, organization)) {
            consoleView.showMessage("Organizer added successfully.");
        }
    }

    private void handleViewOrganizerEvents() {
        consoleView.showMessage("Enter organizer ID:");
        String organizerId = consoleInput.readText();
        if (eventSystem.findOrganizer(organizerId) == null) {
            consoleView.showMessage("Organizer not found.");
            return;
        }
        List<Event> events = eventSystem.getEventsByOrganizer(organizerId);
        if (events.isEmpty()) {
            consoleView.showMessage("No events managed by this organizer.");
            return;
        }
        for (Event event : events) {
            eventSystem.displayEvent(event.getEventId());
        }
    }


    private void handleCreateEvent() {
        consoleView.showMessage("Select event type: 1. Academic  2. Club  3. Career  4. Social");
        int type = consoleInput.readInt();
        if (type < 1 || type > 4) {
            throw new IllegalArgumentException("Unknown event type. Please choose 1 to 4.");
        }

        consoleView.showMessage("Enter event ID:");
        int id = consoleInput.readInt();
        String name = readDataField("Enter event name:");
        String description = readDataField("Enter event description:");
        String date = readDataField("Enter event date:");
        String time = readDataField("Enter event time:");
        String location = readDataField("Enter event location:");
        String organizerId = readDataField("Enter organizer ID:");
        consoleView.showMessage("Enter event capacity:");
        int capacity = consoleInput.readInt();
        if (capacity < 0) {
            throw new IllegalArgumentException("Capacity must not be negative.");
        }

        Event event = switch (type) {
            case 1 -> new AcademicEvent(id, name, description, date, time, location, organizerId,
                    capacity, readDataField("Enter subject:"), readDataField("Enter speaker (optional):", true));
            case 2 -> {
                String clubName = readDataField("Enter club name:");
                consoleView.showMessage("Members only? Enter true or false:");
                boolean membersOnly = consoleInput.readBoolean();
                yield new ClubEvent(id, name, description, date, time, location, organizerId,
                        capacity, clubName, membersOnly);
            }
            case 3 -> {
                String companyName = readDataField("Enter company name:");
                consoleView.showMessage("Resume required? Enter true or false:");
                boolean resumeRequired = consoleInput.readBoolean();
                yield new CareerEvent(id, name, description, date, time, location, organizerId,
                        capacity, companyName, resumeRequired);
            }
            case 4 -> {
                String theme = readDataField("Enter theme:");
                consoleView.showMessage("Food provided? Enter true or false:");
                boolean foodProvided = consoleInput.readBoolean();
                yield new SocialEvent(id, name, description, date, time, location, organizerId,
                        capacity, theme, foodProvided);
            }
            default -> throw new IllegalArgumentException("Unknown event type.");
        };
        if (eventSystem.addEvent(event)) {
            consoleView.showMessage("Event created successfully.");
        }
    }

    private void handleRegisterStudent() {
        String studentId = readDataField("Enter student ID:");
        consoleView.showMessage("Enter event ID:");
        int eventId = consoleInput.readInt();
        eventSystem.registerStudent(studentId, eventId);
    }

    private void handleCancelRegistration() {
        String studentId = readDataField("Enter student ID:");
        consoleView.showMessage("Enter event ID:");
        int eventId = consoleInput.readInt();
        eventSystem.cancelRegistration(studentId, eventId);
    }

    private String readDataField(String prompt) {
        return readDataField(prompt, false);
    }

    private String readDataField(String prompt, boolean allowBlank) {
        consoleView.showMessage(prompt);
        String value = consoleInput.readText(allowBlank);
        if (value.contains("|") || value.contains("\r") || value.contains("\n")) {
            throw new IllegalArgumentException("Text must not contain pipes or line breaks.");
        }
        return value;
    }


    private void saveData() {
        try {
            String studentsText = dataConverter.serializeStudents(eventSystem.getStudents());
            String organizersText = dataConverter.serializeOrganizers(eventSystem.getOrganizers());
            String eventsText = dataConverter.serializeEvents(eventSystem.getEvents());
            String registrationsText = dataConverter.serializeRegistrations(eventSystem.getRegistrations());

            WriteResult result = fileManager.save(studentsText, organizersText, eventsText, registrationsText);
            if (result.isSuccess()) {
                consoleView.showMessage("Data saved successfully.");
            } else {
                consoleView.showError("Could not save all data [" + result.status() + "]: " + result.cause());
            }
        } catch (IllegalArgumentException error) {
            consoleView.showError("Could not convert data for saving: " + error.getMessage());
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
