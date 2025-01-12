package com.rms;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@WebServlet("/FlightServlet")
public class FlightServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Collect parameters from the request
        String flightId = request.getParameter("flight_id");
        String flightNumber = request.getParameter("flight_number");
        String airline = request.getParameter("airline");
        String origin = request.getParameter("origin");
        String destination = request.getParameter("destination");
        String departureTime = request.getParameter("departure_time");
        String arrivalTime = request.getParameter("arrival_time");
        String duration = request.getParameter("duration");
        String status = request.getParameter("status");
        String flightType = request.getParameter("flight_type");

        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            // Connect to the database
            String jdbcURL = "jdbc:mysql://localhost:3306/reservation";
            String jdbcUsername = "root";
            String jdbcPassword = "Bl@091602";
            conn = DriverManager.getConnection(jdbcURL, jdbcUsername, jdbcPassword);

            String sql;
            if (flightId == null || flightId.isEmpty()) {
                // Insert new flight
                sql = "INSERT INTO flights (flight_number, airline, origin, destination, departure_time, arrival_time, duration, status, flight_type) " +
                      "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
                stmt = conn.prepareStatement(sql);
            } else {
                // Update existing flight
                sql = "UPDATE flights SET flight_number=?, airline=?, origin=?, destination=?, departure_time=?, arrival_time=?, duration=?, status=?, flight_type=? " +
                      "WHERE flight_id=?";
                stmt = conn.prepareStatement(sql);
                stmt.setInt(10, Integer.parseInt(flightId));
            }

            // Set parameters
            stmt.setString(1, flightNumber);
            stmt.setString(2, airline);
            stmt.setString(3, origin);
            stmt.setString(4, destination);
            stmt.setString(5, departureTime);
            stmt.setString(6, arrivalTime);
            stmt.setString(7, duration);
            stmt.setString(8, status);
            stmt.setString(9, flightType);

            // Execute the query
            int rowsAffected = stmt.executeUpdate();
            response.setContentType("text/html");
            if (rowsAffected > 0) {
                response.getWriter().println("<h3>Flight details saved successfully!</h3>");
            } else {
                response.getWriter().println("<h3>Error saving flight details. Please try again.</h3>");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            response.getWriter().println("<p>Error: " + e.getMessage() + "</p>");
        } finally {
            // Close resources
            try {
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
