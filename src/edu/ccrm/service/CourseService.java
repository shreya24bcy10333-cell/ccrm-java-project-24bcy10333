package edu.ccrm.service;

import edu.ccrm.domain.Course;
import java.util.List;

public interface CourseService {
    void addCourse(String code, String title, int credits, String instructorId, String semester, String dept);
    List<Course> listCourses();
}
