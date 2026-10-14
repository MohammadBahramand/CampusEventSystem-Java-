package persistence;

import java.io.File;
import java.io.IOException;

public class FileManager {
    
    //Used the first time the program is run on a system to create the data files to be written to
    public void checkAndCreate() {
        File studentsFile = new File("../../data/students.txt");
        File organizersFile = new File("../../data/organizers.txt");
        File eventsFile = new File("../../data/events.txt");
        File registrationsFile = new File("../../data/registrations.txt");

        //If none of the files exist yet, creates them
        try {
            if (!studentsFile.exists()) {
                studentsFile.createNewFile();
            }
            
            if (!organizersFile.exists()) {
                organizersFile.createNewFile();
            }

            if (!eventsFile.exists()) {
                eventsFile.createNewFile();
            }

            if (!registrationsFile.exists()) {
                registrationsFile.createNewFile();
            }
        } catch (IOException error) {
            System.out.println("There was an error setting up a file: " + error.getMessage());
        }

    }
}
