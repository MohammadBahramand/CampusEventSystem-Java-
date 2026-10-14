Event Subclasses
By Ghazal Emami

This branch has the four event types for our project: CareerEvent, AcademicEvent, ClubEvent and SocialEvent. They all extend the Event class, so they get the shared info like name, date, location, organizer and capacity.

Each one adds two fields of its own:
- CareerEvent: company name and if a resume is required
- AcademicEvent: subject and speaker
- ClubEvent: club name and if it's members only
- SocialEvent: theme and if food is provided

The constructor sends the shared info to Event using super, checks that the main field isn't empty, and then saves its own two fields. Each class also overrides displayDetails() so it prints its own details.
