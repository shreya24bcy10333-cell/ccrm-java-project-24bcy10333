package edu.ccrm.service;

import edu.ccrm.config.DataStore;
import edu.ccrm.domain.*;
import java.util.*;

public class CourseServiceImpl implements CourseService {
    private final DataStore ds = DataStore.getInstance();
    private static final CourseServiceImpl INSTANCE = new CourseServiceImpl();
    private CourseServiceImpl() {}
    public static CourseServiceImpl getInstance() { return INSTANCE; }

    @Override
    public void addCourse(String code, String title, int credits, String instructorId, String semester, String dept) {
        CourseCode cc = new CourseCode(code);
        Course.Builder b = new Course.Builder(cc, title).credits(credits).department(dept);
        if (instructorId != null) b.instructor(instructorId);
        if (semester != null && !semester.isBlank()) b.semester(Semester.valueOf(semester));
        Course c = b.build();
        ds.courses().put(cc.toString(), c);
    }

    @Override
    public List<Course> listCourses() { return new ArrayList<>(ds.courses().values()); }
}
