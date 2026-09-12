package edu.ccrm.domain;

import java.time.LocalDate;

public abstract class Person {
    protected final String id;
    protected String fullName;
    protected String email;
    protected LocalDate createdAt;

    protected Person(String id, String fullName, String email) {
        if (id == null || id.isBlank()) throw new IllegalArgumentException("ID cannot be null/blank");
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.createdAt = LocalDate.now();
    }

    public String getId() { return id; }
    public String getFullName() { return fullName; }
    public void setFullName(String name) { this.fullName = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public abstract String profileSummary();
}
