package model;

// A ClubEvent IS an Event (club meeting, club social, etc.)
public class ClubEvent extends Event
{
    // Name of the club hosting the event
    private String clubName;

    // True if only club members can attend
    private boolean membersOnly;

    // Constructor: 7 shared values for Event + 2 values for ClubEvent
    public ClubEvent(int eventId, String name, String description,
                     String date, String time, String location,
                     int capacity, String clubName,
                     boolean membersOnly)
    {
        // Send the shared info up to Event's constructor (must be first)
        super(eventId, name, description, date, time, location, capacity);

        // Don't allow a club event with no club
        if (clubName == null || clubName.isBlank())
        {
            throw new IllegalArgumentException("Club name cannot be empty.");
        }

        // Set the info only a ClubEvent has
        this.clubName = clubName;
        this.membersOnly = membersOnly;
    }

    public String getClubName() { return clubName; }

    public boolean isMembersOnly() { return membersOnly; }

    // Required: Event's displayDetails() is abstract
    @Override
    public void displayDetails()
    {
        System.out.println("Club Event: " + getName());
        System.out.println("Description: " + getDescription());
        System.out.println("Date: " + getDate() + " at " + getTime());
        System.out.println("Location: " + getLocation());
        System.out.println("Capacity: " + getCapacity());
        System.out.println("Club: " + clubName);
        System.out.println("Members Only: " + (membersOnly ? "Yes" : "No"));
    }
}
