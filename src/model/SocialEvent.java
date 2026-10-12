package model;

// A SocialEvent IS an Event (game night, mixer, celebration, etc.)
public class SocialEvent extends Event
{
    // Theme of the event (for example "Halloween" or "Game Night")
    private String theme;

    // True if food will be served
    private boolean foodProvided;

    // Constructor: 7 shared values for Event + 2 values for SocialEvent
    public SocialEvent(int eventId, String name, String description,
                       String date, String time, String location,
                       int capacity, String theme,
                       boolean foodProvided)
    {
        // Send the shared info up to Event's constructor (must be first)
        super(eventId, name, description, date, time, location, capacity);

        // Don't allow a social event with no theme
        if (theme == null || theme.isBlank())
        {
            throw new IllegalArgumentException("Theme cannot be empty.");
        }

        // Set the info only a SocialEvent has
        this.theme = theme;
        this.foodProvided = foodProvided;
    }

    public String getTheme() { return theme; }

    public boolean isFoodProvided() { return foodProvided; }

    // Required: Event's displayDetails() is abstract
    @Override
    public void displayDetails()
    {
        System.out.println("Social Event: " + getName());
        System.out.println("Description: " + getDescription());
        System.out.println("Date: " + getDate() + " at " + getTime());
        System.out.println("Location: " + getLocation());
        System.out.println("Capacity: " + getCapacity());
        System.out.println("Theme: " + theme);
        System.out.println("Food Provided: " + (foodProvided ? "Yes" : "No"));
    }
}
