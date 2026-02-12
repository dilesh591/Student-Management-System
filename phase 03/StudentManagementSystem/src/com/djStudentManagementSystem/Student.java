package com.djStudentManagementSystem;

/**
 * Represents a Student entity in the system.
 */
public class Student {
    private int id;
    private String name;
    private String email;
    private String enrolledCourse;

    /**
     * Constructs a new Student.
     * @param id The student's unique ID.
     * @param name The student's full name.
     * @param email The student's email address.
     * @param enrolledCourse The name of the course the student is enrolled in.
     */
    public Student(int id, String name, String email, String enrolledCourse) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.enrolledCourse = enrolledCourse;
    }

    /**
     * Overloaded constructor for students not yet enrolled in a course.
     * @param id The student's unique ID.
     * @param name The student's full name.
     * @param email The student's email address.
     */
    public Student(int id, String name, String email) {
        this(id, name, email, "NONE");
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getEnrolledCourse() { return enrolledCourse; }
    public void setEnrolledCourse(String enrolledCourse) { this.enrolledCourse = enrolledCourse; }

    /**
     * Displays student details to the console.
     */
    public void displayInfo() {
        System.out.println("ID: " + id);
        System.out.println("Name: " + name);
        System.out.println("Email: " + email);
        System.out.println("Course: " + enrolledCourse);
    }
}