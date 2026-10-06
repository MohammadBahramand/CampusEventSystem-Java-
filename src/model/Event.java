package model;

// Abstract base class for all event types
public abstract class Event
{
    // Unique ID used to identify each event
    private int eventId;

    // Name of the event
    private String name;

    // Short description of the event
    private String description;

    // Date when the event will take place
    private String date;

    // Time when the event will take place
    private String time;

    // Location of the event
    private String location;

    // Maximum number of students allowed to register
    private int capacity;

    // Constructor used to initialize common event information
    public Event(int eventId, String name, String description,
                 String date, String time, String location,
                 int capacity)
    {
        this.eventId = eventId;
        this.name = name;
        this.description = description;
        this.date = date;
        this.time = time;
        this.location = location;
        this.capacity = capacity;
    }

    // Returns the unique event ID
    public int getEventId()
    {
        return eventId;
    }

    // Returns the event name
    public String getName()
    {
        return name;
    }

    // Returns the event description
    public String getDescription()
    {
        return description;
    }

    // Returns the event date
    public String getDate()
    {
        return date;
    }

    // Returns the event time
    public String getTime()
    {
        return time;
    }

    // Returns the event location
    public String getLocation()
    {
        return location;
    }

    // Returns the maximum event capacity
    public int getCapacity()
    {
        return capacity;
    }

    // Each event subclass must provide its own display method
    public abstract void displayDetails();
}