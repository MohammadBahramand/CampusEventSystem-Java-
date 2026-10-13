//=============EventSystem.java============
//coordinator class, owns the collections of students
//=========================================
package service;

import model.Student;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import model.Event;

public class EventSystem {

    //a resizable list that keeps students in the order
    //they were added
    //the list can ONLY hold Students.
    private ArrayList<Student> students = new ArrayList<>();

// A resizable list that stores all events in the system
private ArrayList<Event> events = new ArrayList<>();

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
            //while .equals() checks whether the text 
            //is the same.
            if (s.getStudentId().equals(id)) {
                return s;   // found: hand back the reference
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
        //handle the empty list so the user isn't 
        //shown a blank screen.
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

        // Prevent duplicate event IDs
        if (findEventById(event.getEventId()) != null) {
            System.out.println("Error: an event with ID "
                    + event.getEventId() + " already exists.");
            return false;
        }

        events.add(event);
        return true;
    }


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

}
