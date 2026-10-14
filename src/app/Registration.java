import java.time.LocalDate; // OR this could be a string

public class Registration {
    private Student student;
    private Event event;
    private LocalDate registrationDate;
    private String status;

    public Registration(Student student, Event event, LocalDate registrationDate, String status) {
        this.student = student;
        this.event = event;
        this.registrationDate = registrationDate;
        this.status = status;
    }

    public Student getStudent() {
        return student;
    }

    public Event getEvent() {
        return event;
    }

    public LocalDate getRegistration() {
        return registrationDate;
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

    public void setRegistration(LocalDate registrationDate) {
        this.registrationDate = registrationDate;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
