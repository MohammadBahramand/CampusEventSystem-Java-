package model;

public class CareerEvent extends Event
{
    private String companyName;
    private boolean resumeRequired;

    public CareerEvent(int eventId, String name, String description,
                       String date, String time, String location, String organizerId,
                       int capacity, String companyName,
                       boolean resumeRequired)
    {
        super(eventId, name, description, date, time, location, organizerId, capacity);

        if (companyName == null || companyName.isBlank())
        {
            throw new IllegalArgumentException("Company name cannot be empty.");
        }

        this.companyName = companyName;
        this.resumeRequired = resumeRequired;
    }

    public String getCompanyName() { return companyName; }
    public boolean isResumeRequired() { return resumeRequired; }

    @Override
    public void displayDetails()
    {
        System.out.println("Career Event: " + getName());
        System.out.println("Description: " + getDescription());
        System.out.println("Date: " + getDate() + " at " + getTime());
        System.out.println("Location: " + getLocation());
        System.out.println("Organizer: " + getOrganizerId());
        System.out.println("Capacity: " + getCapacity());
        System.out.println("Company: " + companyName);
        System.out.println("Resume Required: " + (resumeRequired ? "Yes" : "No"));
    }
}
