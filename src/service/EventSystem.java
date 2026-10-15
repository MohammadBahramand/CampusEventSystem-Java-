//=============EventSystem.java============
//coordinator class, owns the collection managers
//=========================================
package service;

import model.Student;
import java.util.ArrayList;
import java.util.List;
import model.Event;
import model.Organizer;
import model.Registration;
import java.time.LocalDate;
public class EventSystem {

    private StudentManager studentManager = new StudentManager();
    private OrganizerManager organizerManager = new OrganizerManager();
    private EventManager eventManager = new EventManager();
    private RegistrationManager registrationManager = new RegistrationManager();
    //==========addStudent==========
    //Adds a new student only if the ID 
    //is not already used.
    //Returns true if added,false if it was rejected
    //===============================
    public boolean addStudent(String id, String name, String email, String major) {
       
        if (id == null || id.isBlank() || name == null || name.isBlank()) {
            System.out.println("Error: student ID and name cannot be blank.");
            return false;
        }

        //Refuse duplicates before creating anything.
        // findStudent returns null when the ID is not 
        //in the list,
        
        if (findStudent(id) != null) {
            System.out.println("Error: a student with ID " + id + " already exists.");
            return false;   
        }

      
        return studentManager.add(new Student(id, name, email, major));        // student was added
    }

    //==============findStudent================
    // Searches the list one student at a time 
    //
    // Returns the Student, or null if no one has that ID.
    //==========================================
    public Student findStudent(String id) {
        return studentManager.findById(id).orElse(null);
    }

    //============displayStudent============
    //
    //=======================================
    public void displayStudent(String id) {
        Student s = findStudent(id);

        if (s == null) {
            System.out.println("Student not found.");
            return;
        }

        s.display();
    }

    //============displayAllStudents=============
    //
    //===========================================
    public void displayAllStudents() {
        List<Student> students = studentManager.getAll();
		
        //handle the empty list so the user isn't shown a blank screen.
        if (students.isEmpty()) {
            System.out.println("No students in the system.");
            return;
        }

        int count = 1;   
        for (Student s : students) {
            System.out.println("--- Student " + count + " ---");
            s.display();
            count++;
        }
    }

   
    //========getStudents===================
    // Lets other classes read the list when saving.
    //==========================================
    public List<Student> getStudents() {
        return studentManager.getAll();
    }//end of getStudents


    //==============addEvent================
    // Adds a new event only if the event ID
    // is not already used.
    // Returns true if added, false otherwise.
    //======================================
    public boolean addEvent(Event event) {

        if (event == null) {
            System.out.println("Error: event cannot be null.");
            return false;
        }


        if (event.getName() == null || event.getName().isBlank()) {
            System.out.println("Error: event name cannot be blank.");
            return false;
        }

	 // Make sure the organizer exists
        if (findOrganizer(event.getOrganizerId()) == null)  {
  	         System.out.println("Error: organizer does not exist.");
  	         return false;
		}


        // Prevent duplicate event IDs
        if (findEventById(event.getEventId()) != null) {
            System.out.println("Error: an event with ID "+ event.getEventId() + " already exists.");
            return false;
        }

        eventManager.add(event);
        return true;
    }



    //==========getEventsByOrganizer==========
    // Returns all events managed by a specific organizer.
    //========================================
     public ArrayList<Event> getEventsByOrganizer(String organizerId){
        return new ArrayList<>(eventManager.findByOrganizerId(organizerId));
    }


    //==============findEventById================
    // Searches for an event using its unique ID.
    // Returns the Event object if found,
    // otherwise returns null.
    //===========================================
    public Event findEventById(int eventId) {
        return eventManager.findById(eventId).orElse(null);
    }


    //==============searchEventByName================
    // Searches for events whose names contain
    // the given search text.
    // Returns a list of matching events.
    //===============================================
    public ArrayList<Event> searchEventByName(String name) {
        if (name == null || name.isBlank()) return new ArrayList<>();
        return new ArrayList<>(eventManager.searchByName(name));
    }


    //==============displayEvent================
    // Finds and displays one event using its ID.
    //==========================================
    public void displayEvent(int eventId) {

        Event event = findEventById(eventId);

        if (event == null) {
            System.out.println("Event not found.");
            return;
        }

        event.displayDetails();
    }


    //==============displayAllEvents================
    // Displays all events currently stored
    // in the system.
    //==============================================
    public void displayAllEvents() {
        List<Event> events = eventManager.getAll();

        if (events.isEmpty()) {
            System.out.println("No events in the system.");
            return;
        }

        int count = 1;

        for (Event event : events) {
            System.out.println("--- Event " + count + " ---");
            event.displayDetails();
            count++;
        }
    }


    //==============getEvents================
    // Lets other classes read the event list,
    // for example when saving data.
    //=======================================
    public List<Event> getEvents() {
        return eventManager.getAll();
    }


    //==========addOrganizer==========
    //Adds a new organizer only if the ID
    //is not already used.
    //Returns true if added, false otherwise.
    //================================
    public boolean addOrganizer(String id, String name, String email, String organization) {
        //an empty ID or name makes no sense
        if (id == null || id.isBlank() || name == null || name.isBlank()) {
            System.out.println("Error: organizer ID and name cannot be blank.");
            return false;
        }

        //same duplicate rule as students: search first, create second
        if (findOrganizer(id) != null) {
            System.out.println("Error: an organizer with ID " + id + " already exists.");
            return false;
        }

        //the list holds a REFERENCE to this one object
        organizerManager.add(new Organizer(id, name, email, organization));
        return true;
    }

    //==========findOrganizer==========
    //Linear search by ID.
    //Returns the Organizer, or null if no match.
    //=================================
    public Organizer findOrganizer(String id) {
        return organizerManager.findById(id).orElse(null);
    }

    //==========displayOrganizer==========
    //Prints one organizer, or a message if
    //the ID does not exist.
    //====================================
    public void displayOrganizer(String id) {
        Organizer o = findOrganizer(id);

        //always check for null before using the reference
        if (o == null) {
            System.out.println("Organizer not found.");
            return;
        }

        o.display();
    }

    //==========displayAllOrganizers==========
    //Prints every organizer, numbered.
    //========================================
    public void displayAllOrganizers() {
        List<Organizer> organizers = organizerManager.getAll();
        if (organizers.isEmpty()) {
            System.out.println("No organizers in the system.");
            return;
        }

        int count = 1;
        for (Organizer o : organizers) {
            System.out.println("--- Organizer " + count + " ---");
            o.display();
            count++;
        }
    }

    //==========getOrganizers==========
    //Read only view for saving to a file.
    //the immutable snapshot lets callers look but
    //not add or remove, which protects the
    //duplicate-ID rule.
    //=================================
    public List<Organizer> getOrganizers() {
        return organizerManager.getAll();
    }
    
    //==========registerStudent==========
    //Registers a student for an event after checking that the student
    //exists, the event exists, the student is not already registered,
    //and the event is not full.
    //Returns true only if the registration was made.
    //===================================
    public boolean registerStudent(String studentId, int eventId, LocalDate date) {
        Student student = findStudent(studentId);
        if (student == null) {
            System.out.println("Error: student not found.");
            return false;
        }

        Event event = findEventById(eventId);
        if (event == null) {
            System.out.println("Error: event not found.");
            return false;
        }

        if (date == null) {
            System.out.println("Error: a registration date is required.");
            return false;
        }

        //an earlier registration for the same student and event, if any
        Registration existing = findRegistration(studentId, eventId);
        if (existing != null && existing.getStatus() == Registration.Status.ACTIVE) {
            System.out.println("Error: this student is already registered for this event.");
            return false;
        }

        if (getRegisteredCount(eventId) >= event.getCapacity()) {
            System.out.println("Error: this event is full.");
            return false;
        }

        if (!registrationManager.register(student, event, date)) return false;

        System.out.println("Registration successful!");
        System.out.println("Student: " + student.getName());
        System.out.println("Event: " + event.getName());
        return true;
    }

    //same as above, using today's date
    public boolean registerStudent(String studentId, int eventId) {
        return registerStudent(studentId, eventId, LocalDate.now());
    }

    //==========cancelRegistration==========
    //Cancels an active registration.
    //Returns false if there is nothing to cancel.
    //======================================
    public boolean cancelRegistration(String studentId, int eventId) {
        if (!registrationManager.cancel(studentId, eventId)) {
            System.out.println("Error: no active registration found for that student and event.");
            return false;
        }

        System.out.println("Registration canceled.");
        return true;
    }

    //==========findRegistration==========
    //Helper: finds the registration (active or canceled) for one
    //student and one event, or null.
    //====================================
    private Registration findRegistration(String studentId, int eventId) {
        return registrationManager.findByStudentAndEvent(studentId, eventId).orElse(null);
    }

    //==========getRegisteredCount==========
    //Number of ACTIVE registrations for an event (for capacity checks
    //and reports).
    //======================================
    public int getRegisteredCount(int eventId) {
        return registrationManager.getActiveCount(eventId);
    }

    //==========displayStudentRegistrations==========
    //Lists the events a student is registered for.
    //===============================================
    public void displayStudentRegistrations(String studentId) {
        Student student = findStudent(studentId);
        if (student == null) {
            System.out.println("Student not found.");
            return;
        }

        System.out.println("Student: " + student.getName());
        int count = 0;
        for (Registration r : registrationManager.findActiveByStudent(studentId)) {
            if (count == 0) {
                System.out.println("Registered Events:");
            }
            count++;
            System.out.println(count + ". " + r.getEvent().getName());
        }

        if (count == 0) {
            System.out.println("No registered events.");
        }
    }

    //==========displayEventAttendees==========
    //Lists the students registered for an event.
    //=========================================
    public void displayEventAttendees(int eventId) {
        Event event = findEventById(eventId);
        if (event == null) {
            System.out.println("Event not found.");
            return;
        }

        System.out.println("Event: " + event.getName());
        int count = 0;
        for (Registration r : registrationManager.findActiveByEvent(eventId)) {
            if (count == 0) {
                System.out.println("Registered Students:");
            }
            count++;
            System.out.println(count + ". " + r.getStudent().getName());
        }

        if (count == 0) {
            System.out.println("No registered students.");
        }
    }

    //==========getRegistrations==========
    //Read-only view for reports and saving.
    //====================================
    public List<Registration> getRegistrations() {
        return registrationManager.getAll();
    }
    
    //==========reportEventsWithSpace==========
    //Prints the events that still have open spots
    //(capacity minus active registrations is above 0).
    //=========================================
    public void reportEventsWithSpace() {
        List<Event> events = eventManager.getAll();
        System.out.println("=== Events With Available Capacity ===");
        boolean foundAny = false;

        for (Event event : events) {
            int registered = getRegisteredCount(event.getEventId());
            int remaining = event.getCapacity() - registered;

            if (remaining > 0) {
                foundAny = true;
                System.out.println(event.getName());
                System.out.println("  Capacity: " + event.getCapacity());
                System.out.println("  Registered: " + registered);
                System.out.println("  Spaces Remaining: " + remaining);
            }
        }

        //tell the user when nothing matched instead of printing nothing
        if (!foundAny) {
            System.out.println("No events have available capacity.");
        }
    }

    //==========reportFullEvents==========
    //Prints the events where the active registrations have
    //reached the capacity.
    //====================================
    public void reportFullEvents() {
        List<Event> events = eventManager.getAll();
        System.out.println("=== Full Events ===");
        boolean foundAny = false;

        for (Event event : events) {
            int registered = getRegisteredCount(event.getEventId());

            if (registered >= event.getCapacity()) {
                foundAny = true;
                System.out.println(event.getName() + " (" + registered + "/"
                        + event.getCapacity() + ")");
            }
        }

        if (!foundAny) {
            System.out.println("No events are full.");
        }
    }

    //==========reportRegistrationsPerEvent==========
    //Prints one line per event with its number of active
    //registrations.
    //===============================================
    public void reportRegistrationsPerEvent() {
        List<Event> events = eventManager.getAll();
        System.out.println("=== Registrations Per Event ===");

        if (events.isEmpty()) {
            System.out.println("No events in the system.");
            return;
        }

        for (Event event : events) {
            System.out.println(event.getName() + ": "
                    + getRegisteredCount(event.getEventId()) + " registered");
        }
    }

    //==========reportStudentsInMultipleEvents==========
    //For every student, collects the names of the events they are
    //actively registered for. Prints the student when there are two
    //or more.
    //==================================================
    public void reportStudentsInMultipleEvents() {
        List<Student> students = studentManager.getAll();
        System.out.println("=== Students Registered for Multiple Events ===");
        boolean foundAny = false;

        for (Student student : students) {
            ArrayList<String> eventNames = new ArrayList<>();

            for (Registration r : registrationManager.findActiveByStudent(student.getStudentId())) {
                eventNames.add(r.getEvent().getName());
            }

            if (eventNames.size() >= 2) {
                foundAny = true;
                System.out.println(student.getName() + " (" + eventNames.size() + " events)");
                for (String name : eventNames) {
                    System.out.println("  - " + name);
                }
            }
        }

        if (!foundAny) {
            System.out.println("No students are registered for multiple events.");
        }
    }

    public void replaceData(List<Student> students, List<Organizer> organizers,
            List<Event> events, List<Registration> registrations) {
        StudentManager candidateStudents = new StudentManager();
        OrganizerManager candidateOrganizers = new OrganizerManager();
        EventManager candidateEvents = new EventManager();
        RegistrationManager candidateRegistrations = new RegistrationManager();

        candidateStudents.replaceAll(students);
        candidateOrganizers.replaceAll(organizers);
        candidateEvents.replaceAll(events);
        for (Event event : candidateEvents.getAll()) {
            if (candidateOrganizers.findById(event.getOrganizerId()).isEmpty()) {
                throw new IllegalArgumentException("Event organizer does not exist.");
            }
        }
        candidateRegistrations.replaceAll(registrations);
        for (Registration registration : candidateRegistrations.getAll()) {
            if (candidateStudents.findById(registration.getStudent().getStudentId()).orElse(null)
                    != registration.getStudent()) {
                throw new IllegalArgumentException("Registration must reference the candidate student instance.");
            }
            if (candidateEvents.findById(registration.getEvent().getEventId()).orElse(null)
                    != registration.getEvent()) {
                throw new IllegalArgumentException("Registration must reference the candidate event instance.");
            }
        }
        for (Event event : candidateEvents.getAll()) {
            if (candidateRegistrations.getActiveCount(event.getEventId()) > event.getCapacity()) {
                throw new IllegalArgumentException("Event capacity exceeded.");
            }
        }

        studentManager = candidateStudents;
        organizerManager = candidateOrganizers;
        eventManager = candidateEvents;
        registrationManager = candidateRegistrations;
    }
}
