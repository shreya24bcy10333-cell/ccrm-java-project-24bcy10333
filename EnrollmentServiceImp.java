package edu.ccrm.service;

import edu.ccrm.config.DataStore;
import edu.ccrm.domain.*;
import edu.ccrm.exceptions.*;
import java.util.Optional;

public class EnrollmentServiceImpl implements EnrollmentService {
    private final DataStore ds = DataStore.getInstance();
    private static final EnrollmentServiceImpl INSTANCE = new EnrollmentServiceImpl();
    private static final int MAX_CREDITS_PER_SEM = 24;
    private EnrollmentServiceImpl() {}
    public static EnrollmentServiceImpl getInstance() { return INSTANCE; }

    @Override
    public void enroll(String studentId, String courseCodeStr) throws DuplicateEnrollmentException {
        CourseCode courseCode = new CourseCode(courseCodeStr);
        boolean already = ds.enrollments().stream()
                .anyMatch(e -> e.getCourseCode().equals(courseCode) && e.getStudentId().equals(studentId));
        if (already) throw new DuplicateEnrollmentException("Already enrolled");

        int current = ds.enrollments().stream()
                .filter(e -> e.getStudentId().equals(studentId))
                .mapToInt(e -> {
                    Course c = ds.courses().get(e.getCourseCode().toString());
                    return c == null ? 0 : c.getCredits();
                }).sum();

        Course cToAdd = ds.courses().get(courseCode.toString());
        if (cToAdd == null) throw new IllegalArgumentException("Course not found");
        if (current + cToAdd.getCredits() > MAX_CREDITS_PER_SEM)
            throw new MaxCreditLimitExceededException("Exceeds max credits");

        Enrollment e = new Enrollment(studentId, courseCode);
        ds.enrollments().add(e);

        Student s = ds.students().get(studentId);
        if (s != null) s.enroll(courseCode.toString());
    }

    @Override
    public void unenroll(String studentId, String courseCodeStr) {
        CourseCode courseCode = new CourseCode(courseCodeStr);
        ds.enrollments().removeIf(e -> e.getStudentId().equals(studentId) && e.getCourseCode().equals(courseCode));
        Student s = ds.students().get(studentId);
        if (s != null) s.unenroll(courseCode.toString());
    }

    @Override
    public void assignGrade(String studentId, String courseCodeStr, String gradeStr) throws Exception {
        CourseCode courseCode = new CourseCode(courseCodeStr);
        Optional<Enrollment> opt = ds.enrollments().stream()
                .filter(e -> e.getStudentId().equals(studentId) && e.getCourseCode().equals(courseCode))
                .findFirst();
        if (opt.isEmpty()) throw new Exception("Enrollment not found");
        Enrollment en = opt.get();
        Grade g = Grade.valueOf(gradeStr);
        en.assignGrade(g);
    }
}
