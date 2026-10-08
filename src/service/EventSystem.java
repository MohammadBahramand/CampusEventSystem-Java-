//=============EventSystem.java============
//coordinator class, owns the collections of students
//=========================================
package service;

import model.Student;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class EventSystem {

    //a resizable list that keeps students in the order
    //they were added
    //the list can ONLY hold Students.
    private ArrayList<Student> students = new ArrayList<>();

    //==========addStudent==========
    //Adds a new student only if the ID 
    //is not already used.
    //Returns true if added,false if it was rejected
    //===============================
    public boolean addStudent(String id, String name, String email, String major) {
       
        if (id == null || id.trim().isEmpty() || name == null || name.trim().isEmpty()) {
            System.out.println("Error: student ID and name cannot be blank.");
            return false;
        }

        //Refuse duplicates before creating anything.
        // findStudent returns null when the ID is not 
        //in the list,
        
        if (findStudent(id) != null) {
            System.out.println("Error: a student with ID " + id + " already exists.");
            return false;   
        }

      
        students.add(new Student(id, name, email, major));
        return true;        // student was added
    }

    //==============findStudent================
    // Searches the list one student at a time 
    //
    // Returns the Student, or null if no one has that ID.
    //==========================================
    public Student findStudent(String id) {
       
        for (Student s : students) {
            //while .equals() checks whether the text 
            //is the same.
            if (s.getStudentId().equals(id)) {
                return s;   // found: hand back the reference
            }
        }
        return null;        // checked everyone, no match
    }

    //============displayStudent============
    //
    //=======================================
    public void displayStudent(String id) {
        Student s = findStudent(id);

        if (s == null) {
            System.out.println("Student not found.");
            return;
        }

        s.display();
    }

    //============displayAllStudents=============
    //
    //===========================================
    public void displayAllStudents() {
        //handle the empty list so the user isn't 
        //shown a blank screen.
        if (students.isEmpty()) {
            System.out.println("No students in the system.");
            return;
        }

        int count = 1;   
        for (Student s : students) {
            System.out.println("--- Student " + count + " ---");
            s.display();
            count++;
        }
    }

   
    //========getStudents===================
    // Lets other classes read the list when saving.
    //==========================================
    public List<Student> getStudents() {
        return Collections.unmodifiableList(students);
    }//end of getStudents
}
