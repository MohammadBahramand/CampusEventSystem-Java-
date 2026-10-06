package model;

// An AcademicEvent IS an Event (guest lecture, study session, etc.)
public class AcademicEvent extends Event
{
    // Academic subject the event covers
    private String subject;

    // Person giving the talk or leading the session
    private String speaker;

    // Constructor: 7 shared values for Event + 2 values for AcademicEvent
    public AcademicEvent(int eventId, String name, String description,
                         String date, String time, String location,
                         int capacity, String subject, String speaker)
    {
        // Send the shared info up to Event's constructor (must be first)
        super(eventId, name, description, date, time, location, capacity);

        // Don't allow an academic event with no subject
        if (subject == null || subject.isBlank())
        {
            throw new IllegalArgumentException("Subject cannot be empty.");
        }

        // Set the info only an AcademicEvent has
        this.subject = subject;
        this.speaker = speaker;
    }

    public String getSubject() { return subject; }

    public String getSpeaker() { return speaker; }

    // Required: Event's displayDetails() is abstract
    @Override
    public void displayDetails()
    {
        System.out.println("Academic Event: " + getName());
        System.out.println("Description: " + getDescription());
        System.out.println("Date: " + getDate() + " at " + getTime());
        System.out.println("Location: " + getLocation());
        System.out.println("Capacity: " + getCapacity());
        System.out.println("Subject: " + subject);
        System.out.println("Speaker: " + speaker);
    }
}
