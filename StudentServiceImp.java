package edu.ccrm.service;

import edu.ccrm.domain.*;
import edu.ccrm.config.DataStore;
import java.time.LocalDate;
import java.util.*;

public class StudentServiceImpl implements StudentService {
    private final DataStore ds = DataStore.getInstance();
    private static final StudentServiceImpl INSTANCE = new StudentServiceImpl();
    private StudentServiceImpl() {}
    public static StudentServiceImpl getInstance() { return INSTANCE; }

    @Override
    public void addStudent(String id, String regNo, String fullName, String email, String dob) {
        LocalDate d = LocalDate.parse(dob);
        Student s = new Student(id, regNo, fullName, email, d);
        ds.students().put(id, s);
    }

    @Override
    public List<Student> listStudents() { return new ArrayList<>(ds.students().values()); }

    @Override
    public String printTranscript(String studentId) throws Exception {
        Student s = ds.students().get(studentId);
        if (s == null) throw new Exception("Student not found");
        StringBuilder sb = new StringBuilder();
        sb.append("Transcript for ").append(s.getFullName()).append("\n");
        ds.enrollments().stream()
                .filter(e -> e.getStudentId().equals(studentId))
                .forEach(e -> {
                    Course c = ds.courses().get(e.getCourseCode().toString());
                    sb.append(String.format("%s | %s | %s\n",
                            e.getCourseCode(),
                            c == null ? "(unknown course)" : c.getTitle(),
                            e.getGrade()));
                });
        double gpa = Reports.computeGPA(studentId);
        sb.append(String.format("GPA: %.2f\n", gpa));
        return sb.toString();
    }
}
