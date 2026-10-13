import java.time.LocalDate; // OR this could be a string

public class Registration {
    private Student student;
    private Event event;
    private LocalDate registrationDate
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

    public Registration getRegistration() {
        return Registration;
    }

    public String getStatus() {
        return status;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public void getEvent(Event event) {
        this.event = event;
    }

    public void getRegistration(LocalDate registrationDate) {
        this.registrationDate = registrationDate;
    }

    public void getStatus(String status) {
        this.status = status
    }
}
