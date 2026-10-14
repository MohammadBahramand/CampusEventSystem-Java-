package model;

import java.time.LocalDate;

public class Registration {
    // Registration status enum
    public enum Status {
        ACTIVE,
        CANCELED,
    }

    private final String registrationId;
    final private Student student;
    final private Event event;
    private LocalDate date;
    private Status status;

    // Constuctor
    public Registration(String registrationId, Student student, Event event, LocalDate date, Status status) {
        this.registrationId = registrationId;
        this.student = student;
        this.event = event;
        this.date = date;
        this.status = status;
    }

    // Getters
    public String getRegistrationId() {
        return registrationId;
    }
    
    public Student getStudent() {
        return student;
    }

    public Event getEvent() {
        return event;
    }

    public LocalDate getDate() {
        return date;
    }

    public Status getStatus() {
        return status;
    }

    // Setters
    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public void display() {
        if (student != null) {
            System.out.print("Student: " + student.getName() + " (" + student.getEmail() + ")\n");
        } else {
            System.out.print("Student: N/A\n");
        }

        if (event != null) {
            System.out.print("Event: " + event.getName() + "\n");
        } else {
            System.out.print("Event: N/A\n");
        }

        System.out.print("Date: " + date + "\n");
        System.out.print("Status: " + status + "\n");
    }

    public String toFileString() {
        String studentId = "N/A";
        String eventId = "N/A";

        if (student != null)
            studentId = student.getStudentId();
        if (event != null)
            eventId = event.getEventId();
        
        return registrationId + "|" + studentId + "|" + eventId + "|" + date + "|" + status;
    }
}
