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
import java.sql.Timestamp;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;

@WebServlet("/AddFlightServlet")
public class AddFlightServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String flightNumber = request.getParameter("flightNumber");
        String airline = request.getParameter("airline");
        String origin = request.getParameter("origin");
        String destination = request.getParameter("destination");
        String departureTimeStr = request.getParameter("departureTime");
        String arrivalTimeStr = request.getParameter("arrivalTime");
        String durationStr = request.getParameter("duration");
        String status = request.getParameter("status");

        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            // Parse the datetime strings into Timestamp
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
            Timestamp departureTime = new Timestamp(dateFormat.parse(departureTimeStr).getTime());
            Timestamp arrivalTime = new Timestamp(dateFormat.parse(arrivalTimeStr).getTime());

            // Validate and convert duration
            String[] durationParts = durationStr.split(":");
            if (durationParts.length != 2) {
                throw new IllegalArgumentException("Duration must be in HH:mm format.");
            }
            // Create Time object from duration
            Time duration = Time.valueOf(durationParts[0] + ":" + durationParts[1] + ":00");

            // Connect to the database
            String jdbcURL = "jdbc:mysql://localhost:3306/reservation";
            String jdbcUsername = "root";
            String jdbcPassword = "Bl@091602";
            conn = DriverManager.getConnection(jdbcURL, jdbcUsername, jdbcPassword);

            // SQL query to insert a new flight
            String sql = "INSERT INTO flights (flight_number, airline, origin, destination, departure_time, arrival_time, duration, status) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, flightNumber);
            stmt.setString(2, airline);
            stmt.setString(3, origin);
            stmt.setString(4, destination);
            stmt.setTimestamp(5, departureTime);
            stmt.setTimestamp(6, arrivalTime);
            stmt.setTime(7, duration);
            stmt.setString(8, status);
            stmt.executeUpdate();

            // Redirect to Manage Flights page
            response.sendRedirect("ManageFlightsServlet");
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Invalid input: " + e.getMessage());
            request.getRequestDispatcher("AddFlight.jsp").forward(request, response);
        } catch (ParseException e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Invalid date format: " + e.getMessage());
            request.getRequestDispatcher("AddFlight.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Error adding flight: " + e.getMessage());
            request.getRequestDispatcher("AddFlight.jsp").forward(request, response);
        } finally {
            try {
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
