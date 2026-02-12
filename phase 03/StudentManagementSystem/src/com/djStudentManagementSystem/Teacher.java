package com.djStudentManagementSystem;

/**
 * Represents a Teacher entity in the system.
 */
public class Teacher {
    private int id;
    private String name;
    private String email;

    /**
     * Constructs a new Teacher.
     * @param id The teacher's unique ID.
     * @param name The teacher's full name.
     * @param email The teacher's email address.
     */
    public Teacher(int id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    /**
     * Displays teacher details to the console.
     */
    public void displayInfo() {
        System.out.println("ID: " + id);
        System.out.println("Name: " + name);
        System.out.println("Email: " + email);
    }
}