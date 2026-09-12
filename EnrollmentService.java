package edu.ccrm.service;

import edu.ccrm.exceptions.DuplicateEnrollmentException;
import java.io.IOException;

public interface EnrollmentService {
    void enroll(String studentId, String courseCode) throws DuplicateEnrollmentException, IOException;
    void unenroll(String studentId, String courseCode) throws IOException;
    void assignGrade(String studentId, String courseCode, String grade) throws Exception;
}
