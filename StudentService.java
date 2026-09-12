package edu.ccrm.service;

import edu.ccrm.domain.Student;
import java.util.List;

public interface StudentService {
    void addStudent(String id, String regNo, String fullName, String email, String dob);
    List<Student> listStudents();
    String printTranscript(String studentId) throws Exception;
}
