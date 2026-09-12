package edu.ccrm.domain;

import java.time.LocalDate;
import java.util.*;

public class Student extends Person {
    private String regNo;
    private boolean active = true;
    private final Set<String> enrolledCourseCodes = new LinkedHashSet<>();
    private LocalDate dob;

    public Student(String id, String regNo, String fullName, String email, LocalDate dob) {
        super(id, fullName, email);
        this.regNo = regNo;
        this.dob = dob;
    }

    public String getRegNo() { return regNo; }
    public void setRegNo(String regNo) { this.regNo = regNo; }
    public boolean isActive() { return active; }
    public void deactivate() { this.active = false; }

    public void enroll(String courseCode) { enrolledCourseCodes.add(courseCode); }
    public void unenroll(String courseCode) { enrolledCourseCodes.remove(courseCode); }
    public Set<String> getEnrolledCourseCodes() { return Collections.unmodifiableSet(enrolledCourseCodes); }
    public LocalDate getDob() { return dob; }

    @Override
    public String profileSummary() {
        return String.format("Student[%s] %s (%s) - Active: %b", regNo, fullName, email, active);
    }

    @Override
    public String toString() { return profileSummary(); }
}
