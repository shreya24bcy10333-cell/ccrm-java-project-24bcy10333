package edu.ccrm.domain;

public final class CourseCode {
    private final String value;

    public CourseCode(String value) {
        if (value == null) throw new IllegalArgumentException("Course code cannot be null");
        this.value = value.trim().toUpperCase();
    }

    public String getValue() { return value; }

    @Override
    public String toString() { return value; }

    @Override
    public boolean equals(Object o) {
        return (o instanceof CourseCode) && ((CourseCode) o).value.equals(value);
    }

    @Override
    public int hashCode() { return value.hashCode(); }
}
