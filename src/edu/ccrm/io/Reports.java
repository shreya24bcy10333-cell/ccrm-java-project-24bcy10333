package edu.ccrm.service;

import edu.ccrm.config.DataStore;
import edu.ccrm.domain.*;
import java.util.*;
import java.util.stream.*;

public class Reports {

    // Compute GPA for a student
    public static double computeGPA(String studentId) {
        DataStore ds = DataStore.getInstance();
        List<Enrollment> en = ds.enrollments().stream()
                .filter(e -> e.getStudentId().equals(studentId) && e.getGrade() != null)
                .collect(Collectors.toList());

        int totalCredits = 0;
        int totalPoints = 0;
        for (Enrollment e : en) {
            Course c = ds.courses().get(e.getCourseCode().toString());
            if (c == null) continue;
            totalCredits += c.getCredits();
            totalPoints += e.getGrade().getPoints() * c.getCredits();
        }
        return totalCredits == 0 ? 0.0 : (double) totalPoints / totalCredits;
    }

    // Print simple GPA distribution for all students
    public static void printGpaDistribution() {
        DataStore ds = DataStore.getInstance();
        Map<String, Double> map = ds.students().keySet().stream()
                .collect(Collectors.toMap(sid -> sid, Reports::computeGPA));
        map.forEach((k, v) -> System.out.println(k + " -> " + String.format("%.2f", v)));
    }
}
