package com.djStudentManagementSystem.web;
import com.djStudentManagementSystem.dao.*;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.sql.Connection;

    /**
     * Main entry point for the RESTful Web Service.
     * Implements the "Java HTTP server" requirement for Part 3.
     */
    public class SimpleWebServer {

        /**
         * Starts the HTTP Server on port 8000.
         * @param args Command line arguments
         * @throws IOException If server fails to bind to port
         */
        public static void main(String[] args) throws IOException {
            // 1. Initialize Database Connection
            DatabaseConnection dbConnection = new DatabaseConnection();
            Connection conn = dbConnection.getConnection();

            if (conn == null) {
                System.err.println(" Critical Error: Could not connect to database.");
                return;
            }

            // 2. Initialize DAOs
            StudentDAO studentDAO = new StudentDAO(conn);
            TeacherDAO teacherDAO = new TeacherDAO(conn);
            CourseDAO courseDAO = new CourseDAO(conn);

            // 3. Create HTTP Server (Port 8000)
            HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);
            System.out.println(" Starting Student Management Web API...");

            // 4. Register Endpoints
            server.createContext("/api/students", new StudentHandler(studentDAO));
            server.createContext("/api/courses", new CourseHandler(courseDAO));
            server.createContext("/api/teachers", new TeacherHandler(teacherDAO));

            // 5. Start Server
            server.setExecutor(null); // Default executor
            server.start();

            System.out.println(" Server started successfully!");
            System.out.println(" Listening on: http://localhost:8000");
            System.out.println(" Test Students: http://localhost:8000/api/students");
            System.out.println(" Test Courses:  http://localhost:8000/api/courses");
        }
    }

