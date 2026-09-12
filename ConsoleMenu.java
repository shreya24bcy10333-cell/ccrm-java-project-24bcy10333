package edu.ccrm.cli;

import java.util.Scanner;
import java.nio.file.Path;
import edu.ccrm.config.AppConfig;
import edu.ccrm.service.*;
import edu.ccrm.io.*;
import java.io.IOException;

public class ConsoleMenu {
    private final Scanner sc;
    private final StudentService studentService = StudentServiceImpl.getInstance();
    private final CourseService courseService = CourseServiceImpl.getInstance();
    private final EnrollmentService enrollmentService = EnrollmentServiceImpl.getInstance();
    private final ImportExportService importExportService = new ImportExportService();
    private final BackupService backupService = new BackupService();

    public ConsoleMenu(Scanner sc) { this.sc = sc; }

    // Main CLI loop
    public void runLoop() {
        boolean running = true;
        Path dataFolder = AppConfig.getInstance().getDataFolder();

        while (running) {
            System.out.println("\n=== CCRM Menu ===");
            System.out.println("1. Manage Students");
            System.out.println("2. Manage Courses");
            System.out.println("3. Enrollment / Grades");
            System.out.println("4. Import Data (CSV)");
            System.out.println("5. Export Data (CSV)");
            System.out.println("6. Backup Exports & Show Size");
            System.out.println("7. Reports");
            System.out.println("0. Exit");
            System.out.print("Choose: ");

            String choice = sc.nextLine().trim();
            switch (choice) {
                case "1" -> studentMenu();
                case "2" -> courseMenu();
                case "3" -> enrollmentMenu();
                case "4" -> doImport(dataFolder);
                case "5" -> doExport(dataFolder);
                case "6" -> doBackup(dataFolder);
                case "7" -> reportsMenu();
                case "0" -> { running = false; System.out.println("Exiting..."); }
                default -> System.out.println("Invalid choice");
            }
        }
    }

    // --- Students Menu ---
    private void studentMenu() {
        System.out.println("\n-- Students --");
        System.out.println("1. Add Student");
        System.out.println("2. List Students");
        System.out.println("3. Print Profile/Transcript");
        System.out.println("0. Back");
        System.out.print("Choose: ");
        String c = sc.nextLine().trim();
        switch (c) {
            case "1" -> addStudent();
            case "2" -> listStudents();
            case "3" -> printStudentTranscript();
            default -> {}
        }
    }

    private void addStudent() {
        System.out.print("Enter id: "); String id = sc.nextLine().trim();
        System.out.print("Enter regNo: "); String reg = sc.nextLine().trim();
        System.out.print("Enter full name: "); String name = sc.nextLine().trim();
        System.out.print("Enter email: "); String email = sc.nextLine().trim();
        System.out.print("Enter dob (yyyy-mm-dd): "); String dob = sc.nextLine().trim();
        try {
            studentService.addStudent(id, reg, name, email, dob);
            System.out.println("Student added.");
        } catch (Exception e) {
            System.out.println("Error adding student: " + e.getMessage());
        }
    }

    private void listStudents() {
        studentService.listStudents().forEach(System.out::println);
    }

    private void printStudentTranscript() {
        System.out.print("Enter student id: "); String id = sc.nextLine().trim();
        try {
            String t = studentService.printTranscript(id);
            System.out.println(t);
        } catch (Exception e) { System.out.println("Error: " + e.getMessage()); }
    }

    // --- Courses Menu ---
    private void courseMenu() {
        System.out.println("\n-- Courses --");
        System.out.println("1. Add Course");
        System.out.println("2. List Courses");
        System.out.println("0. Back");
        System.out.print("Choose: ");
        String c = sc.nextLine().trim();
        switch (c) {
            case "1" -> addCourse();
            case "2" -> listCourses();
            default -> {}
        }
    }

    private void addCourse() {
        try {
            System.out.print("Enter code: "); String code = sc.nextLine().trim();
            System.out.print("Enter title: "); String title = sc.nextLine().trim();
            System.out.print("Credits: "); int credits = Integer.parseInt(sc.nextLine().trim());
            System.out.print("Instructor id (or empty): "); String iid = sc.nextLine().trim();
            System.out.print("Semester (SPRING,SUMMER,FALL): "); String sem = sc.nextLine().trim();
            System.out.print("Department: "); String dept = sc.nextLine().trim();

            courseService.addCourse(code, title, credits, iid.isEmpty() ? null : iid, sem, dept);
            System.out.println("Course added.");
        } catch (Exception e) { System.out.println("Error: " + e.getMessage()); }
    }

    private void listCourses() {
        courseService.listCourses().forEach(System.out::println);
    }

    // --- Enrollment Menu ---
    private void enrollmentMenu() {
        System.out.println("\n-- Enrollment & Grades --");
        System.out.println("1. Enroll Student in Course");
        System.out.println("2. Unenroll Student");
        System.out.println("3. Assign Grade");
        System.out.println("0. Back");
        System.out.print("Choose: ");
        String c = sc.nextLine().trim();
        switch (c) {
            case "1" -> enrollStudent();
            case "2" -> unenrollStudent();
            case "3" -> assignGrade();
            default -> {}
        }
    }

    private void enrollStudent() {
        System.out.print("Student id: "); String sid = sc.nextLine().trim();
        System.out.print("Course code: "); String code = sc.nextLine().trim();
        try {
            enrollmentService.enroll(sid, code);
            System.out.println("Enrolled.");
        } catch (Exception e) { System.out.println("Enroll failed: " + e.getMessage()); }
    }

    private void unenrollStudent() {
        System.out.print("Student id: "); String sid = sc.nextLine().trim();
        System.out.print("Course code: "); String code = sc.nextLine().trim();
        try {
            enrollmentService.unenroll(sid, code);
            System.out.println("Unenrolled.");
        } catch (Exception e) { System.out.println("Error: " + e.getMessage()); }
    }

    private void assignGrade() {
        System.out.print("Student id: "); String sid = sc.nextLine().trim();
        System.out.print("Course code: "); String code = sc.nextLine().trim();
        System.out.print("Grade (S,A,B,C,D,E,F): "); String g = sc.nextLine().trim();
        try {
            enrollmentService.assignGrade(sid, code, g);
            System.out.println("Grade assigned.");
        } catch (Exception e) { System.out.println("Error: " + e.getMessage()); }
    }

    // --- Import / Export / Backup ---
    private void doImport(Path dataFolder) {
        try {
            importExportService.importStudents(dataFolder.resolve("test-data/students.csv"));
            importExportService.importCourses(dataFolder.resolve("test-data/courses.csv"));
            System.out.println("Import finished.");
        } catch (IOException e) { System.out.println("Import failed: " + e.getMessage()); }
    }

    private void doExport(Path dataFolder) {
        try {
            Path exports = dataFolder.resolve("exports");
            importExportService.exportStudents(exports.resolve("students_export.csv"));
            importExportService.exportCourses(exports.resolve("courses_export.csv"));
            System.out.println("Export finished to " + exports.toAbsolutePath());
        } catch (IOException e) { System.out.println("Export failed: " + e.getMessage()); }
    }

    private void doBackup(Path dataFolder) {
        try {
            Path exports = dataFolder.resolve("exports");
            Path backup = backupService.backupExportedFiles(exports);
            long size = backupService.directorySizeRecursive(backup);
            System.out.println("Backup created: " + backup + " (size=" + size + " bytes)");
        } catch (IOException e) { System.out.println("Backup failed: " + e.getMessage()); }
    }

    // --- Reports ---
    private void reportsMenu() {
        System.out.println("\n-- Reports --");
        System.out.println("1. GPA Distribution (simple)");
        System.out.println("0. Back");
        System.out.print("Choose: ");
        String c = sc.nextLine().trim();
        if ("1".equals(c)) Reports.printGpaDistribution();
    }
}
