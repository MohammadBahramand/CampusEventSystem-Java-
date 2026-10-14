# Event Subclasses

Author: Ghazal Emami · Branch: `feature/subclasses`

Four event types that extend the abstract `Event` class in `src/model`:

| Class | Extra fields |
|---|---|
| `CareerEvent` | `companyName`, `resumeRequired` |
| `AcademicEvent` | `subject`, `speaker` |
| `ClubEvent` | `clubName`, `membersOnly` |
| `SocialEvent` | `theme`, `foodProvided` |

**How they work**
- The constructor sends the 8 shared values to `Event` with `super(...)`, then saves its own 2 fields.
- It throws `IllegalArgumentException` if the main field (company, subject, club or theme) is blank.
- Each class overrides `displayDetails()` to print its own details (polymorphism).

**Example**
```java
Event e = new CareerEvent(1, "Resume Workshop", "Get your resume reviewed",
        "October 15", "2:00 PM", "SSC 212", "ORG1", 30, "Google", true);
e.displayDetails();
```
