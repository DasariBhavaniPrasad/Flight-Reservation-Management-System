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

@WebServlet("/UpdateBookingServlet")
public class UpdateBookingServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String bookingIdParam = request.getParameter("bookingId"); // Corrected parameter name
        String seatIdParam = request.getParameter("seat_id"); // Match with JSP
        String status = request.getParameter("booking_status"); // Match with JSP

        // Initialize bookingId and seatId
        int bookingId = -1;
        int seatId = -1;

        // Validate bookingId
        if (bookingIdParam != null && !bookingIdParam.trim().isEmpty()) {
            try {
                bookingId = Integer.parseInt(bookingIdParam);
            } catch (NumberFormatException e) {
                request.setAttribute("errorMessage", "Invalid booking ID format.");
                request.getRequestDispatcher("UpdateBooking.jsp").forward(request, response);
                return; // Exit the method
            }
        } else {
            request.setAttribute("errorMessage", "Booking ID is required.");
            request.getRequestDispatcher("UpdateBooking.jsp").forward(request, response);
            return; // Exit the method
        }

        // Validate seatId
        if (seatIdParam != null && !seatIdParam.trim().isEmpty()) {
            try {
                seatId = Integer.parseInt(seatIdParam);
            } catch (NumberFormatException e) {
                request.setAttribute("errorMessage", "Invalid seat ID format.");
                request.getRequestDispatcher("UpdateBooking.jsp").forward(request, response);
                return; // Exit the method
            }
        } else {
            request.setAttribute("errorMessage", "Seat ID is required.");
            request.getRequestDispatcher("ManageBookings.jsp").forward(request, response);
            return; // Exit the method
        }

        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            // Connect to the database
            String jdbcURL = "jdbc:mysql://localhost:3306/reservation";
            String jdbcUsername = "root";
            String jdbcPassword = "Bl@091602";
            conn = DriverManager.getConnection(jdbcURL, jdbcUsername, jdbcPassword);

            // Update booking details
            String sql = "UPDATE bookings SET seat_id = ?, booking_status = ? WHERE booking_id = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, seatId);
            stmt.setString(2, status);
            stmt.setInt(3, bookingId);

            // Execute the update and check for affected rows
            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated == 0) {
                request.setAttribute("errorMessage", "Booking not found.");
                request.getRequestDispatcher("UpdateBooking.jsp").forward(request, response);
                return; // Exit the method
            }

            response.sendRedirect("ManageReservationServlet"); // Redirect back to the booking management page
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Error updating booking: " + e.getMessage());
            request.getRequestDispatcher("UpdateBooking.jsp").forward(request, response);
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
