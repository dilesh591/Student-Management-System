package com.djStudentManagementSystem.web;

import com.djStudentManagementSystem.Student;
import com.djStudentManagementSystem.dao.StudentDAO;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

/**
 * Handles HTTP requests for the /api/students endpoint.
 */
public class StudentHandler implements HttpHandler {
    private StudentDAO studentDAO;

    public StudentHandler(StudentDAO studentDAO) {
        this.studentDAO = studentDAO;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        System.out.println("📥 Request received: " + method + " " + exchange.getRequestURI());

        if ("GET".equalsIgnoreCase(method)) {
            handleGet(exchange);
        } else if ("POST".equalsIgnoreCase(method)) {
            handlePost(exchange);
        } else {
            sendResponse(exchange, 405, "{\"error\":\"Method Not Allowed\"}");
        }
    }

    private void handleGet(HttpExchange exchange) throws IOException {
        ArrayList<Student> students = studentDAO.getAllStudents();
        StringBuilder json = new StringBuilder("[");
        for (int i = 0; i < students.size(); i++) {
            Student s = students.get(i);
            json.append(String.format("{\"id\":%d,\"name\":\"%s\",\"email\":\"%s\",\"enrolledCourse\":\"%s\"}",
                    s.getId(), s.getName(), s.getEmail(), s.getEnrolledCourse()));
            if (i < students.size() - 1) json.append(",");
        }
        json.append("]");
        sendResponse(exchange, 200, json.toString());
    }

    private void handlePost(HttpExchange exchange) throws IOException {
        try {
            InputStream is = exchange.getRequestBody();
            String body = new String(is.readAllBytes(), StandardCharsets.UTF_8);

            // Manual JSON parsing
            int id = Integer.parseInt(extractJsonValue(body, "id"));
            String name = extractJsonValue(body, "name");
            String email = extractJsonValue(body, "email");
            String course = extractJsonValue(body, "enrolledCourse");
            if (course.isEmpty()) course = "NONE";

            Student student = new Student(id, name, email, course);
            if (studentDAO.addStudent(student)) {
                sendResponse(exchange, 201, "{\"message\": \"Student created\"}");
            } else {
                sendResponse(exchange, 500, "{\"error\": \"Database error\"}");
            }
        } catch (Exception e) {
            e.printStackTrace();
            sendResponse(exchange, 400, "{\"error\": \"Invalid JSON data\"}");
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
