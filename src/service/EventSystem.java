//=============EventSystem.java============
//coordinator class, owns the collections of students
//=========================================
package service;

import model.Student;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import model.Event;
import model.Organizer;
public class EventSystem {

    //a resizable list that keeps students in the order
    //they were added
    //the list can ONLY hold Students.
    private ArrayList<Student> students = new ArrayList<>();

	// A resizable list that stores all events in the system
	private ArrayList<Event> events = new ArrayList<>();

    //A resizable list that stores all organizers in the system
    private ArrayList<Organizer> organizers = new ArrayList<>();

    //==========addStudent==========
    //Adds a new student only if the ID 
    //is not already used.
    //Returns true if added,false if it was rejected
    //===============================
    public boolean addStudent(String id, String name, String email, String major) {
       
        if (id == null || id.trim().isEmpty() || name == null || name.trim().isEmpty()) {
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

      
        students.add(new Student(id, name, email, major));
        return true;        // student was added
    }

    //==============findStudent================
    // Searches the list one student at a time 
    //
    // Returns the Student, or null if no one has that ID.
    //==========================================
    public Student findStudent(String id) {
       
        for (Student s : students) {
			
            //while .equals() checks whether the text is the same.
            if (s.getStudentId().equals(id)) {
                return s;   // found: return the reference
            }
        }
        return null;        // checked everyone, no match
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
        return Collections.unmodifiableList(students);
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

        events.add(event);
        return true;
    }



    //==========getEventsByOrganizer==========
    // Returns all events managed by a specific organizer.
    //========================================
     public ArrayList<Event> getEventsByOrganizer(String organizerId){

    ArrayList<Event> matches = new ArrayList<>();

    for (Event event : events)
    {
        if (event.getOrganizerId().equals(organizerId))
        {
            matches.add(event);
        }
    }

    return matches;}


    //==============findEventById================
    // Searches for an event using its unique ID.
    // Returns the Event object if found,
    // otherwise returns null.
    //===========================================
    public Event findEventById(int eventId) {

        for (Event event : events) {
            if (event.getEventId() == eventId) {
                return event;
            }
        }

        return null;
    }


    //==============searchEventByName================
    // Searches for events whose names contain
    // the given search text.
    // Returns a list of matching events.
    //===============================================
    public ArrayList<Event> searchEventByName(String name) {

        ArrayList<Event> matches = new ArrayList<>();

        if (name == null || name.trim().isEmpty()) {
            return matches;
        }

        for (Event event : events) {
            if (event.getName().toLowerCase()
				.contains(name.toLowerCase())) {
                matches.add(event);
            }
        }

        return matches;
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
        return Collections.unmodifiableList(events);
    }


    //==========addOrganizer==========
    //Adds a new organizer only if the ID
    //is not already used.
    //Returns true if added, false otherwise.
    //================================
    public boolean addOrganizer(String id, String name, String email, String organization) {
        //an empty ID or name makes no sense
        if (id == null || id.trim().isEmpty() || name == null || name.trim().isEmpty()) {
            System.out.println("Error: organizer ID and name cannot be blank.");
            return false;
        }

        //same duplicate rule as students: search first, create second
        if (findOrganizer(id) != null) {
            System.out.println("Error: an organizer with ID " + id + " already exists.");
            return false;
        }

        //the list holds a REFERENCE to this one object
        organizers.add(new Organizer(id, name, email, organization));
        return true;
    }

    //==========findOrganizer==========
    //Linear search by ID.
    //Returns the Organizer, or null if no match.
    //=================================
    public Organizer findOrganizer(String id) {
        for (Organizer o : organizers) {
            //strings are compared with .equals(), never ==
            if (o.getOrganizerId().equals(id)) {
                return o;
            }
        }
        return null;
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
    //unmodifiableList lets callers look but
    //not add or remove, which protects the
    //duplicate-ID rule.
    //=================================
    public List<Organizer> getOrganizers() {
        return Collections.unmodifiableList(organizers);
    }
}
