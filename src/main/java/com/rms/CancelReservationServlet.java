package com.rms;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/CancelReservation")
public class CancelReservationServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String reservationId = request.getParameter("reservation_id");  // Keep form reference but map it to booking_id

        Connection connection = null;
        PreparedStatement preparedStatement = null;

        String jdbcURL = "jdbc:mysql://localhost:3306/reservation";
        String jdbcUsername = "root";
        String jdbcPassword = "Bl@091602";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(jdbcURL, jdbcUsername, jdbcPassword);

            // Update SQL query to use booking_id
            String sql = "UPDATE bookings SET booking_status = 'cancelled' WHERE booking_id = ?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, Integer.parseInt(reservationId));  // Ensure correct field is used in the query

            int rowsUpdated = preparedStatement.executeUpdate();
            if (rowsUpdated > 0) {
                response.getWriter().println("Reservation cancelled successfully.");
            } else {
                response.getWriter().println("Failed to cancel reservation.");
            }

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            response.getWriter().println("Error cancelling reservation.");
        } finally {
            try {
                if (preparedStatement != null) preparedStatement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
