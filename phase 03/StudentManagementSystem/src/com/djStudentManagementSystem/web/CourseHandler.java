package com.djStudentManagementSystem.web;

import com.djStudentManagementSystem.Course;
import com.djStudentManagementSystem.Teacher; // Make sure to import this!
import com.djStudentManagementSystem.dao.CourseDAO;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class CourseHandler implements HttpHandler {
    private CourseDAO courseDAO;

    public CourseHandler(CourseDAO courseDAO) {
        this.courseDAO = courseDAO;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();

        if ("GET".equalsIgnoreCase(method)) {
            // --- GET LOGIC ---
            ArrayList<Course> courses = courseDAO.getAllCourses();
            StringBuilder json = new StringBuilder("[");
            for (int i = 0; i < courses.size(); i++) {
                Course c = courses.get(i);
                String tName = (c.getTeacher() != null) ? c.getTeacher().getName() : "None";
                json.append(String.format("{\"id\":%d,\"name\":\"%s\",\"teacher\":\"%s\"}",
                        c.getCourseId(), c.getCourseName(), tName));
                if (i < courses.size() - 1) json.append(",");
            }
            json.append("]");
            sendResponse(exchange, 200, json.toString());

        } else if ("POST".equalsIgnoreCase(method)) {
            // --- POST LOGIC (This was missing!) ---
            try {
                InputStream is = exchange.getRequestBody();
                String body = new String(is.readAllBytes(), StandardCharsets.UTF_8);

                // Manual Parsing
                int id = Integer.parseInt(extractJsonValue(body, "id"));
                String name = extractJsonValue(body, "name");

                // Note: We are keeping it simple and not assigning a teacher on creation
                // just to satisfy the basic "Add Course" requirement.
                Course newCourse = new Course(id, name);

                if (courseDAO.addCourse(newCourse)) {
                    sendResponse(exchange, 201, "{\"message\": \"Course added successfully\"}");
                } else {
                    sendResponse(exchange, 500, "{\"error\": \"Database error\"}");
                }
            } catch (Exception e) {
                e.printStackTrace();
                sendResponse(exchange, 400, "{\"error\": \"Invalid JSON Data\"}");
            }

        } else {
            // This is the error you were seeing
            sendResponse(exchange, 405, "{\"error\": \"Method not allowed\"}");
        }
    }

    private void sendResponse(HttpExchange exchange, int statusCode, String response) throws IOException {
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        exchange.sendResponseHeaders(statusCode, response.length());
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }

    private String extractJsonValue(String json, String key) {
        String search = "\"" + key + "\":";
        int start = json.indexOf(search);
        if (start == -1) return "";
        start += search.length();
        while (start < json.length() && (json.charAt(start) == ' ' || json.charAt(start) == '"')) start++;
        int end = start;
        while (end < json.length() && json.charAt(end) != '"' && json.charAt(end) != ',' && json.charAt(end) != '}') end++;
        return json.substring(start, end);
    }
}