### Event core class


The Event component uses an abstract "Event" base class to store shared event information such as event ID, name, description, date, time, location, organizer ID, and capacity.

The system supports adding events, finding events by ID, searching events by name, displaying events, preventing duplicate event IDs, validating that an organizer exists, and retrieving events managed by a specific organizer.

Different event types such as "AcademicEvent", "CareerEvent", "ClubEvent", and "SocialEvent" inherit from "Event" and override "displayDetails()" to provide event-specific information.


### Data persistence


The data persistence model reads and writes Students, Events, Organizations, and Registrations to text files for persistence so data can be used in other instances.

FileManager focuses on reading info from those text files at the start of the program to load into the objects as well as writing data from the active program into text files for file persistence.

The other classes included focus on a lot of exception handling for file pathing in both reading and writing procedures.