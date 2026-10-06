package model;

// A CareerEvent IS an Event (job fair, resume workshop, etc.)
public class CareerEvent extends Event
{
    // Company recruiting or hosting at the event
    private String companyName;

    // True if students should bring a resume
    private boolean resumeRequired;

    // Constructor: 7 shared values for Event + 2 values for CareerEvent
    public CareerEvent(int eventId, String name, String description,
                       String date, String time, String location,
                       int capacity, String companyName,
                       boolean resumeRequired)
    {
        // Send the shared info up to Event's constructor (must be first)
        super(eventId, name, description, date, time, location, capacity);

        // Don't allow a career event with no company
        if (companyName == null || companyName.isBlank())
        {
            throw new IllegalArgumentException("Company name cannot be empty.");
        }

        // Set the info only a CareerEvent has
        this.companyName = companyName;
        this.resumeRequired = resumeRequired;
    }

    public String getCompanyName() { return companyName; }

    public boolean isResumeRequired() { return resumeRequired; }

    // Required: Event's displayDetails() is abstract
    @Override
    public void displayDetails()
    {
        System.out.println("Career Event: " + getName());
        System.out.println("Description: " + getDescription());
        System.out.println("Date: " + getDate() + " at " + getTime());
        System.out.println("Location: " + getLocation());
        System.out.println("Capacity: " + getCapacity());
        System.out.println("Company: " + companyName);
        System.out.println("Resume Required: " + (resumeRequired ? "Yes" : "No"));
    }
}
