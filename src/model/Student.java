//======================Student.java=================
//holds one student's information.
// ===================================================
package model;

public class Student {
  private final String studentId;//unique ID,tells students apart
    private String name;    //full name
    private String email;     //school email address
    private String major;     //their major

//constructor that creates a new student with the given information.
//The studentId is final, so it cannot be changed later.
    public Student(String studentId, String name, String email, String major) {
        this.studentId = studentId;
        this.name = name;
        this.email = email;
        this.major = major;
    }

    //getters:let other classes read the private data.
    public String getStudentId() { return studentId; }
    public String getName()      { return name; }
    public String getEmail()     { return email; }
    public String getMajor()     { return major; }

  
    public void display() {
        System.out.println("ID: " + studentId);
        System.out.println("Name: " + name);
        System.out.println("Email: " + email);
        System.out.println("Major: " + major);
    }

    //Converts the student into 1 line of text 
    // for the save file.
    public String toFileString() {
        return studentId + "|" + name + "|" + email + "|" + major;
    }
}//end of Student class