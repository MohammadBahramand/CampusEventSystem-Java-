package service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import model.Student;

/** Owns students in insertion order; ID queries use linear traversal. */
public class StudentManager {
    private final List<Student> students = new ArrayList<>();

    /** Returns false for a duplicate ID without replacing the existing student. */
    public boolean add(Student student) {
        if (student == null)
            throw new IllegalArgumentException("Student must not be null.");

        if (findById(student.getStudentId()).isPresent()) 
            return false;

        students.add(student);
        return true;
    }


    public void replaceAll(List<Student> objects) {
        if (objects == null)
            throw new IllegalArgumentException("Collection is required.");

        StudentManager candidate = new StudentManager();

        for (Student object : objects) {
            if (!candidate.add(object))
                throw new IllegalArgumentException("Duplicate student ID.");
        }
        
        students.clear();
        students.addAll(candidate.students);
    }

    public Optional<Student> findById(String studentId) {
        for (Student student : students) {
            if (student.getStudentId().equals(studentId)) return Optional.of(student);
        }

        return Optional.empty();
    }

    /** Returns an immutable snapshot in insertion order. */
    public List<Student> getAll() {
        return List.copyOf(students);
    }
}
