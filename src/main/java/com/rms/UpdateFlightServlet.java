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
import java.sql.ResultSet;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

@WebServlet("/UpdateFlightServlet")
public class UpdateFlightServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    // Method to handle GET requests (for retrieving flight details)
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int flightId = Integer.parseInt(request.getParameter("flightId"));
        Flight flight = null;

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            // Database connection
            String jdbcURL = "jdbc:mysql://localhost:3306/reservation";
            String jdbcUsername = "root";
            String jdbcPassword = "Bl@091602";
            conn = DriverManager.getConnection(jdbcURL, jdbcUsername, jdbcPassword);

            // SQL query to get flight details
            String sql = "SELECT * FROM flights WHERE flight_id = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, flightId);
            rs = stmt.executeQuery();

            if (rs.next()) {
                // Create Flight object with retrieved details
                flight = new Flight();
                flight.setFlightId(rs.getInt("flight_id"));
                flight.setFlightNumber(rs.getString("flight_number"));
                flight.setAirline(rs.getString("airline"));
                flight.setOrigin(rs.getString("origin"));
                flight.setDestination(rs.getString("destination"));
                flight.setDepartureTime(rs.getTimestamp("departure_time"));
                flight.setArrivalTime(rs.getTimestamp("arrival_time"));
                flight.setDuration(rs.getTime("duration"));
                flight.setStatus(rs.getString("status"));

                // Set flight as an attribute to be accessed in JSP
                request.setAttribute("flight", flight);
            } else {
                request.setAttribute("errorMessage", "Flight not found.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Error retrieving flight details: " + e.getMessage());
        } finally {
            // Close resources
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // Forward to JSP to show the flight details for editing
        request.getRequestDispatcher("UpdateFlight.jsp").forward(request, response);
    }

    // Method to handle POST requests (for updating flight details)
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int flightId = Integer.parseInt(request.getParameter("flightId"));
        String flightNumber = request.getParameter("flightNumber");
        String airline = request.getParameter("airline");
        String origin = request.getParameter("origin");
        String destination = request.getParameter("destination");

        // Handle datetime-local inputs for departure and arrival times
        String departureTimeStr = request.getParameter("departureTime").replace("T", " ") + ":00"; // Format: yyyy-MM-dd HH:mm:ss
        String arrivalTimeStr = request.getParameter("arrivalTime").replace("T", " ") + ":00"; // Format: yyyy-MM-dd HH:mm:ss

        // Handle duration (ensure format is HH:mm:ss)
        String durationStr = request.getParameter("duration") + ":00"; // Append seconds to match format HH:mm:ss

        String status = request.getParameter("status");

        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            // Database connection
            String jdbcURL = "jdbc:mysql://localhost:3306/reservation";
            String jdbcUsername = "root";
            String jdbcPassword = "Bl@091602";
            conn = DriverManager.getConnection(jdbcURL, jdbcUsername, jdbcPassword);

            // Prepare SQL statement to update the flight record
            String sql = "UPDATE flights SET flight_number = ?, airline = ?, origin = ?, destination = ?, "
                       + "departure_time = ?, arrival_time = ?, duration = ?, status = ? WHERE flight_id = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, flightNumber);
            stmt.setString(2, airline);
            stmt.setString(3, origin);
            stmt.setString(4, destination);

            // Convert the strings to proper SQL Timestamp and Time formats
            stmt.setTimestamp(5, Timestamp.valueOf(departureTimeStr)); // Set departure time
            stmt.setTimestamp(6, Timestamp.valueOf(arrivalTimeStr));   // Set arrival time
            stmt.setTime(7, Time.valueOf(durationStr));                // Set duration in HH:mm:ss
            stmt.setString(8, status);                                 // Set status
            stmt.setInt(9, flightId);                                  // Set the flight ID

            // Execute the update statement
            int rowsUpdated = stmt.executeUpdate();

            // Redirect based on update success
            if (rowsUpdated > 0) {
                response.sendRedirect("ManageFlightsServlet"); // Redirect after successful update
            } else {
                request.setAttribute("errorMessage", "Error updating flight. No rows updated.");
                request.getRequestDispatcher("UpdateFlight.jsp").forward(request, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Error updating flight: " + e.getMessage());
            request.getRequestDispatcher("UpdateFlight.jsp").forward(request, response);
        } finally {
            // Close resources
            try {
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
