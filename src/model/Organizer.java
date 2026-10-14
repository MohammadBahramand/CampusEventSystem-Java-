package model;

public class Organizer {
    private String organizerId;
    private String name;
    private String email;
    private String organization;


    public Organizer(String organizerId, String name, String email, String organization) {
        this.organizerId = organizerId;
        this.name = name;
        this.email = email;
        this.organization = organization;
    }

    public String getOrganizerId() {
        return organizerId;
    }
    public String getName() {
        return name;
    }
    public String getEmail() {
        return email;
    }
    public String getOrganization() {
        return organization;
    }

    public void viewOrganizers() {
        System.out.println("ID: " + organizerId + "\nName: " 
        + name + "\nEmail: " + email + "Organization: " + organization);
    }

    public String toFileString() {
        return organizerId + "|" + name + "|" + email + "|" + organization;
    }
}
