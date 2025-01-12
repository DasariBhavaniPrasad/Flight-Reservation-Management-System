package com.rms;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;


@WebServlet("/ConfirmBookingServlet")
public class ConfirmBookingServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Booking booking = (Booking) session.getAttribute("booking");

        if (booking != null) {
            try {
                // Establish database connection
                Connection conn = DatabaseConnection.initializeDatabase();

                // Update the booking status to "confirmed"
                String query = "UPDATE bookings SET booking_status = ?, total_amount = ? WHERE booking_id = ?";
                PreparedStatement pstmt = conn.prepareStatement(query);
                pstmt.setString(1, "confirmed");
                pstmt.setBigDecimal(2, booking.getTotalAmount());
                pstmt.setInt(3, booking.getBookingId());

                // Execute update
                int result = pstmt.executeUpdate();

                // Update successful
                if (result > 0) {
                    response.sendRedirect("BookingConfirmation.jsp");
                } else {
                    response.getWriter().println("Error confirming booking.");
                }

                // Close connections
                pstmt.close();
                conn.close();
            } catch (Exception e) {
                e.printStackTrace();
                response.getWriter().println("Database error: " + e.getMessage());
            }
        } else {
            response.getWriter().println("No booking information found.");
        }
    }
}
