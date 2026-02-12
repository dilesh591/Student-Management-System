package com.djStudentManagementSystem;

/**
 * Represents a Course entity in the system.
 */
public class Course {
    private int courseId;
    private String courseName;
    private Teacher teacher;

    /**
     * Constructs a new Course.
     * @param courseId The unique course ID.
     * @param courseName The name of the course.
     */
    public Course(int courseId, String courseName) {
        this.courseId = courseId;
        this.courseName = courseName;
    }

    public int getCourseId() { return courseId; }
    public String getCourseName() { return courseName; }
    public void setCourseName(String courseName) { this.courseName = courseName; }
    public Teacher getTeacher() { return teacher; }
    public void setTeacher(Teacher teacher) { this.teacher = teacher; }

    /**
     * Displays course details to the console.
     */
    public void displayInfo() {
        System.out.println("ID: " + courseId);
        System.out.println("Name: " + courseName);
        System.out.println("Teacher: " + (teacher != null ? teacher.getName() : "None"));
    }
}