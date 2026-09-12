package edu.ccrm.io;

import edu.ccrm.config.DataStore;
import edu.ccrm.domain.*;
import java.nio.file.*;
import java.io.*;
import java.time.LocalDate;
import java.util.stream.*;
import java.util.*;

public class ImportExportService {
    private final DataStore ds = DataStore.getInstance();

    // Import students from CSV
    public void importStudents(Path csvPath) throws IOException {
        if (!Files.exists(csvPath)) {
            System.out.println("Students CSV not found: " + csvPath);
            return;
        }
        try (Stream<String> lines = Files.lines(csvPath)) {
            lines.skip(1).map(l -> l.split(",")).forEach(tokens -> {
                try {
                    String id = tokens[0].trim();
                    String reg = tokens[1].trim();
                    String name = tokens[2].trim();
                    String email = tokens[3].trim();
                    LocalDate dob = LocalDate.parse(tokens[4].trim());
                    ds.students().put(id, new Student(id, reg, name, email, dob));
                } catch (Exception ex) {
                    System.out.println("Failed to parse student: " + Arrays.toString(tokens));
                }
            });
        }
    }

    // Import courses from CSV
    public void importCourses(Path csvPath) throws IOException {
        if (!Files.exists(csvPath)) {
            System.out.println("Courses CSV not found: " + csvPath);
            return;
        }
        try (Stream<String> lines = Files.lines(csvPath)) {
            lines.skip(1).map(l -> l.split(",")).forEach(tokens -> {
                try {
                    String code = tokens[0].trim();
                    String title = tokens[1].trim();
                    int credits = Integer.parseInt(tokens[2].trim());
                    String instructorId = tokens[3].trim();
                    String sem = tokens[4].trim();
                    String dept = tokens[5].trim();
                    CourseCode cc = new CourseCode(code);
                    Course.Builder b = new Course.Builder(cc, title).credits(credits).department(dept);
                    if (!instructorId.isBlank()) b.instructor(instructorId);
                    if (!sem.isBlank()) b.semester(Semester.valueOf(sem));
                    ds.courses().put(cc.toString(), b.build());
                } catch (Exception ex) {
                    System.out.println("Failed to parse course line: " + Arrays.toString(tokens));
                }
            });
        }
    }

    // Export students to CSV
    public void exportStudents(Path outPath) throws IOException {
        Files.createDirectories(outPath.getParent());
        try (BufferedWriter bw = Files.newBufferedWriter(outPath)) {
            bw.write("id,regNo,fullName,email,dob\n");
            ds.students().values().forEach(s -> {
                try {
                    bw.write(String.join(",", s.getId(), s.getRegNo(), s.getFullName(), s.getEmail(), s.getDob().toString()));
                    bw.write("\n");
                } catch (IOException ex) { throw new UncheckedIOException(ex); }
            });
        }
    }

    // Export courses to CSV
    public void exportCourses(Path outPath) throws IOException {
        Files.createDirectories(outPath.getParent());
        try (BufferedWriter bw = Files.newBufferedWriter(outPath)) {
            bw.write("code,title,credits,instructorId,semester,department\n");
            ds.courses().values().forEach(c -> {
                try {
                    bw.write(String.join(",",
                            c.getCode().toString(),
                            c.getTitle(),
                            String.valueOf(c.getCredits()),
                            c.getInstructorId() == null ? "" : c.getInstructorId(),
                            c.getSemester() == null ? "" : c.getSemester().name(),
                            c.getDepartment()));
                    bw.write("\n");
                } catch (IOException ex) { throw new UncheckedIOException(ex); }
            });
        }
    }
}
