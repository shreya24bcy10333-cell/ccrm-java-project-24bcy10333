package edu.ccrm.domain;

import java.time.LocalDate;

public class Enrollment {
    private final String studentId;
    private final CourseCode courseCode;
    private final LocalDate enrolledOn;
    private Grade grade;

    public Enrollment(String studentId, CourseCode courseCode) {
        this.studentId = studentId;
        this.courseCode = courseCode;
        this.enrolledOn = LocalDate.now();
    }

    public String getStudentId() { return studentId; }
    public CourseCode getCourseCode() { return courseCode; }
    public LocalDate getEnrolledOn() { return enrolledOn; }
    public Grade getGrade() { return grade; }
    public void assignGrade(Grade g) { this.grade = g; }

    @Override
    public String toString() {
        return String.format("%s -> %s [%s]", studentId, courseCode, grade == null ? "N/A" : grade);
    }
}
