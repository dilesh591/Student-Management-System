package com.djStudentManagementSystem.web;

import com.djStudentManagementSystem.Teacher;
import com.djStudentManagementSystem.dao.TeacherDAO;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class TeacherHandler implements HttpHandler {
    private TeacherDAO teacherDAO;

    public TeacherHandler(TeacherDAO teacherDAO) {
        this.teacherDAO = teacherDAO;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();

        if ("GET".equalsIgnoreCase(method)) {
            // --- GET LOGIC ---
            ArrayList<Teacher> teachers = teacherDAO.getAllTeachers();
            StringBuilder json = new StringBuilder("[");
            for (int i = 0; i < teachers.size(); i++) {
                Teacher t = teachers.get(i);
                json.append(String.format("{\"id\":%d,\"name\":\"%s\",\"email\":\"%s\"}",
                        t.getId(), t.getName(), t.getEmail()));
                if (i < teachers.size() - 1) json.append(",");
            }
            json.append("]");
            sendResponse(exchange, 200, json.toString());

        } else if ("POST".equalsIgnoreCase(method)) {
            // --- POST LOGIC (This was missing!) ---
            try {
                InputStream is = exchange.getRequestBody();
                String body = new String(is.readAllBytes(), StandardCharsets.UTF_8);

                int id = Integer.parseInt(extractJsonValue(body, "id"));
                String name = extractJsonValue(body, "name");
                String email = extractJsonValue(body, "email");

                if (teacherDAO.addTeacher(new Teacher(id, name, email))) {
                    sendResponse(exchange, 201, "{\"message\": \"Teacher added successfully\"}");
                } else {
                    sendResponse(exchange, 500, "{\"error\": \"Database error\"}");
                }
            } catch (Exception e) {
                sendResponse(exchange, 400, "{\"error\": \"Invalid JSON Data\"}");
            }

        } else {
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