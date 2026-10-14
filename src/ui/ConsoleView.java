package ui;

import java.util.List;
import model.Event;
import model.Organizer;
import model.Student;

/** Console output only. Input and business operations belong to the caller. */
public class ConsoleView {
    public void showMenu() {
        System.out.println("====================================");
        System.out.println("    CAMPUS EVENT MANAGEMENT SYSTEM");
        System.out.println("====================================");
        System.out.println("1. Add Student");
        System.out.println("2. View Students");
        System.out.println("3. Create Event");
        System.out.println("4. View Events");
        System.out.println("5. Search Events");
        System.out.println("6. Register Student for Event");
        System.out.println("7. Cancel Registration");
        System.out.println("8. View Student Registrations");
        System.out.println("9. View Event Attendees");
        System.out.println("10. View Organizers");
        System.out.println("11. System Reports");
        System.out.println("0. Exit");
        System.out.print("Enter selection: ");
    }

    public void displayStudent(Student student) {
        if (student == null) {
            showMessage("Student not found.");
            return;
        }
        System.out.println("------------------------------------");
        System.out.println("Student ID: " + student.getStudentId());
        System.out.println("Name: " + student.getName());
        System.out.println("Email: " + student.getEmail());
        System.out.println("Major: " + student.getMajor());
    }

    public void displayStudents(List<Student> students) {
        boolean displayed = false;
        if (students != null) {
            for (Student student : students) {
                if (student != null) {
                    displayStudent(student);
                    displayed = true;
                }
            }
        }
        if (!displayed) showMessage("No students available.");
    }

    public void displayOrganizer(Organizer organizer) {
        if (organizer == null) {
            showMessage("Organizer not found.");
            return;
        }
        System.out.println("------------------------------------");
        System.out.println("Organizer ID: " + organizer.getOrganizerId());
        System.out.println("Name: " + organizer.getName());
        System.out.println("Email: " + organizer.getEmail());
        System.out.println("Organization: " + organizer.getOrganization());
    }

    public void displayOrganizers(List<Organizer> organizers) {
        boolean displayed = false;
        if (organizers != null) {
            for (Organizer organizer : organizers) {
                if (organizer != null) {
                    displayOrganizer(organizer);
                    displayed = true;
                }
            }
        }
        if (!displayed) showMessage("No organizers available.");
    }

    public void displayEvent(Event event) {
        if (event == null) {
            showMessage("Event not found.");
            return;
        }
        System.out.println("------------------------------------");
        System.out.println("Event ID: " + event.getEventId());
        System.out.println("Name: " + event.getName());
        System.out.println("Description: " + event.getDescription());
        System.out.println("Date: " + event.getDate());
        System.out.println("Time: " + event.getTime());
        System.out.println("Location: " + event.getLocation());
        System.out.println("Organizer ID: " + event.getOrganizerId());
        System.out.println("Capacity: " + event.getCapacity());
    }

    public void displayEvents(List<Event> events) {
        boolean displayed = false;
        if (events != null) {
            for (Event event : events) {
                if (event != null) {
                    displayEvent(event);
                    displayed = true;
                }
            }
        }
        if (!displayed) showMessage("No events available.");
    }

    public void showMessage(String message) {
        System.out.println(message);
    }

    public void showError(String message) {
        System.err.println(message);
    }
}
