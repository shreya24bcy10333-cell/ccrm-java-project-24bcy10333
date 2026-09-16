package edu.ccrm.domain;

public class Course {
    private final CourseCode code;
    private final String title;
    private final int credits;
    private String instructorId;
    private Semester semester;
    private String department;

    private Course(Builder b) {
        this.code = b.code;
        this.title = b.title;
        this.credits = b.credits;
        this.instructorId = b.instructorId;
        this.semester = b.semester;
        this.department = b.department;
    }

    public CourseCode getCode() { return code; }
    public String getTitle() { return title; }
    public int getCredits() { return credits; }
    public String getInstructorId() { return instructorId; }
    public Semester getSemester() { return semester; }
    public String getDepartment() { return department; }

    @Override
    public String toString() {
        return String.format("%s - %s (%d credits) [%s] Dept:%s", code, title, credits, semester, department);
    }

    public static class Builder {
        private final CourseCode code;
        private final String title;
        private int credits;
        private String instructorId;
        private Semester semester;
        private String department;

        public Builder(CourseCode code, String title) { this.code = code; this.title = title; }
        public Builder credits(int c) { this.credits = c; return this; }
        public Builder instructor(String id) { this.instructorId = id; return this; }
        public Builder semester(Semester s) { this.semester = s; return this; }
        public Builder department(String d) { this.department = d; return this; }
        public Course build() { return new Course(this); }
    }
}
