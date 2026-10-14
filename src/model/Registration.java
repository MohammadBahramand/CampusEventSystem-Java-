import java.time.LocalDate; // OR date could be a string

public class Registration {
    private Student student;
    private Event event;
    private LocalDate date;
    private String status;

    public Registration() {}

    public Registration(Student student, Event event, LocalDate date, String status) {
        this.student = student;
        this.event = event;
        this.date = date;
        this.status = status;
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

    public String getStatus() {
        return status;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setStatus(String status) {
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
