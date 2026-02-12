package com.djStudentManagementSystem.dao;

import com.djStudentManagementSystem.Course;
import com.djStudentManagementSystem.Teacher;
import java.sql.*;
import java.util.ArrayList;

public class CourseDAO {
    private Connection connection;
    private TeacherDAO teacherDAO;

    public CourseDAO(Connection connection) {
        this.connection = connection;
        this.teacherDAO = new TeacherDAO(connection);
    }

    public boolean addCourse(Course course) {
        String sql = "INSERT INTO course (course_id, course_name, teacher_id) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, course.getCourseId());
            // FIX: Removed incorrect parameter 'name'
            pstmt.setString(2, course.getCourseName());

            if (course.getTeacher() != null) {
                pstmt.setInt(3, course.getTeacher().getId());
            } else {
                pstmt.setNull(3, Types.INTEGER);
            }
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error adding course: " + e.getMessage());
            return false;
        }
    }

    public Course findCourse(int courseId) {
        String sql = "SELECT * FROM course WHERE course_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, courseId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                Course course = new Course(rs.getInt("course_id"), rs.getString("course_name"));
                int teacherId = rs.getInt("teacher_id");
                if (!rs.wasNull()) {
                    course.setTeacher(teacherDAO.findTeacher(teacherId));
                }
                return course;
            }
        } catch (SQLException e) {
            System.err.println("Error finding course: " + e.getMessage());
        }
        return null;
    }

    public ArrayList<Course> getAllCourses() {
        ArrayList<Course> courses = new ArrayList<>();
        String sql = "SELECT * FROM course ORDER BY course_id";
        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Course course = new Course(rs.getInt("course_id"), rs.getString("course_name"));
                int tId = rs.getInt("teacher_id");
                if (!rs.wasNull()) {
                    course.setTeacher(teacherDAO.findTeacher(tId));
                }
                courses.add(course);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return courses;
    }

    public boolean updateCourse(Course course) {
        String sql = "UPDATE course SET course_name = ?, teacher_id = ? WHERE course_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            // FIX: Removed incorrect parameter 'name'
            pstmt.setString(1, course.getCourseName());

            if (course.getTeacher() != null) pstmt.setInt(2, course.getTeacher().getId());
            else pstmt.setNull(2, Types.INTEGER);

            pstmt.setInt(3, course.getCourseId());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) { return false; }
    }

    public boolean deleteCourse(int courseId) {
        String sql = "DELETE FROM course WHERE course_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, courseId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) { return false; }
    }

    public boolean assignTeacherToCourse(int courseId, int teacherId) {
        String sql = "UPDATE course SET teacher_id = ? WHERE course_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, teacherId);
            pstmt.setInt(2, courseId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) { return false; }
    }
}