package model;

public class AcademicEvent extends Event
{
    private String subject;
    private String speaker;

    public AcademicEvent(int eventId, String name, String description,
                         String date, String time, String location, String organizerId,
                         int capacity, String subject, String speaker)
    {
        super(eventId, name, description, date, time, location, organizerId, capacity);

        if (subject == null || subject.isBlank())
        {
            throw new IllegalArgumentException("Subject cannot be empty.");
        }

        this.subject = subject;
        this.speaker = speaker;
    }

    public String getSubject() { return subject; }
    public String getSpeaker() { return speaker; }

    @Override
    public void displayDetails()
    {
        System.out.println("Academic Event: " + getName());
        System.out.println("Description: " + getDescription());
        System.out.println("Date: " + getDate() + " at " + getTime());
        System.out.println("Location: " + getLocation());
        System.out.println("Organizer: " + getOrganizerId());
        System.out.println("Capacity: " + getCapacity());
        System.out.println("Subject: " + subject);
        System.out.println("Speaker: " + speaker);
    }
}
