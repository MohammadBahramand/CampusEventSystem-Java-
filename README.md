### Event core class


The Event component uses an abstract "Event" base class to store shared event information such as event ID, name, description, date, time, location, organizer ID, and capacity.

The system supports adding events, finding events by ID, searching events by name, displaying events, preventing duplicate event IDs, validating that an organizer exists, and retrieving events managed by a specific organizer.

Different event types such as "AcademicEvent", "CareerEvent", "ClubEvent", and "SocialEvent" inherit from "Event" and override "displayDetails()" to provide event-specific information.
