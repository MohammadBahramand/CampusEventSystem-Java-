package model;

public class SocialEvent extends Event
{
    private String theme;
    private boolean foodProvided;

    public SocialEvent(int eventId, String name, String description,
                       String date, String time, String location, String organizerId,
                       int capacity, String theme,
                       boolean foodProvided)
    {
        super(eventId, name, description, date, time, location, organizerId, capacity);

        if (theme == null || theme.isBlank())
        {
            throw new IllegalArgumentException("Theme cannot be empty.");
        }

        this.theme = theme;
        this.foodProvided = foodProvided;
    }

    public String getTheme() { return theme; }
    public boolean isFoodProvided() { return foodProvided; }

    @Override
    public void displayDetails()
    {
        System.out.println("Social Event: " + getName());
        System.out.println("Description: " + getDescription());
        System.out.println("Date: " + getDate() + " at " + getTime());
        System.out.println("Location: " + getLocation());
        System.out.println("Organizer: " + getOrganizerId());
        System.out.println("Capacity: " + getCapacity());
        System.out.println("Theme: " + theme);
        System.out.println("Food Provided: " + (foodProvided ? "Yes" : "No"));
    }
}
