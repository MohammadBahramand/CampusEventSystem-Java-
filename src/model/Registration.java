package model;

import java.time.LocalDate;

public class Registration {
    // Registration status enum
    public enum Status {
        ACTIVE,
        CANCELED,
    }
    
    final private Student student;
    final private Event event;
    private LocalDate date;
    private Status status;

    // Constuctor
    public Registration(Student student, Event event, LocalDate date, Status status) {
        this.student = student;
        this.event = event;
        this.date = date;
        this.status = status;
    }

    // Getters
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
}
