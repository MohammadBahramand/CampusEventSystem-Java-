package model;

public class ClubEvent extends Event
{
    private String clubName;
    private boolean membersOnly;

    public ClubEvent(int eventId, String name, String description,
                     String date, String time, String location, String organizerId,
                     int capacity, String clubName,
                     boolean membersOnly)
    {
        super(eventId, name, description, date, time, location, organizerId, capacity);

        if (clubName == null || clubName.isBlank())
        {
            throw new IllegalArgumentException("Club name cannot be empty.");
        }

        this.clubName = clubName;
        this.membersOnly = membersOnly;
    }

    public String getClubName() { return clubName; }
    public boolean isMembersOnly() { return membersOnly; }

    @Override
    public void displayDetails()
    {
        System.out.println("Club Event: " + getName());
        System.out.println("Description: " + getDescription());
        System.out.println("Date: " + getDate() + " at " + getTime());
        System.out.println("Location: " + getLocation());
        System.out.println("Organizer: " + getOrganizerId());
        System.out.println("Capacity: " + getCapacity());
        System.out.println("Club: " + clubName);
        System.out.println("Members Only: " + (membersOnly ? "Yes" : "No"));
    }
}
