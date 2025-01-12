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

@WebServlet("/DeleteFlightServlet")
public class DeleteFlightServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int flightId = Integer.parseInt(request.getParameter("flightId")); // Get the flight ID from the request

        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            // Database connection
            String jdbcURL = "jdbc:mysql://localhost:3306/reservation";
            String jdbcUsername = "root";
            String jdbcPassword = "Bl@091602";
            conn = DriverManager.getConnection(jdbcURL, jdbcUsername, jdbcPassword);

            // SQL query to delete the flight by its ID
            String sql = "DELETE FROM flights WHERE flight_id = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, flightId);

            // Execute delete operation
            int rowsDeleted = stmt.executeUpdate();

            // If the delete was successful, redirect to flight management page
            if (rowsDeleted > 0) {
                response.sendRedirect("ManageFlightsServlet"); // Redirect after successful deletion
            } else {
                request.setAttribute("errorMessage", "Error deleting flight. Flight not found.");
                request.getRequestDispatcher("ManageFlights.jsp").forward(request, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Error deleting flight: " + e.getMessage());
            request.getRequestDispatcher("ManageFlights.jsp").forward(request, response);
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

    // Optional: Override doPost if you want to handle POST request for deletion as well
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
