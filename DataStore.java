package edu.ccrm.config;

import edu.ccrm.domain.*;
import java.util.*;

public class DataStore {
    private static final DataStore INSTANCE = new DataStore();

    private final Map<String, Student> students = new LinkedHashMap<>();
    private final Map<String, Course> courses = new LinkedHashMap<>();
    private final List<Enrollment> enrollments = new ArrayList<>();

    private DataStore() {}

    public static DataStore getInstance() { return INSTANCE; }

    public Map<String, Student> students() { return students; }
    public Map<String, Course> courses() { return courses; }
    public List<Enrollment> enrollments() { return enrollments; }
}
