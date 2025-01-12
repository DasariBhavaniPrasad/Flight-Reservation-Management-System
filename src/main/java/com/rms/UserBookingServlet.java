package com.rms;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/UserBookingServlet")
public class UserBookingServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Get user ID from session (assuming user is logged in)
        int userId = (int) request.getSession().getAttribute("userId");

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        List<Booking> bookingList = new ArrayList<>();

        try {
            // Connect to the database
            String jdbcURL = "jdbc:mysql://localhost:3306/reservation";
            String jdbcUsername = "root";
            String jdbcPassword = "password";
            conn = DriverManager.getConnection(jdbcURL, jdbcUsername, jdbcPassword);

            // Query to get user bookings
            String sql = "SELECT * FROM bookings WHERE user_id = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, userId);
            rs = stmt.executeQuery();

            // Populate bookings list
            while (rs.next()) {
                Booking booking = new Booking();
                booking.setBookingId(rs.getInt("booking_id"));
                booking.setFlightId(rs.getInt("flight_id"));
                booking.setSeatId(rs.getInt("seat_id"));
                booking.setBookingReference(rs.getString("booking_reference"));
                booking.setBookingStatus(rs.getString("booking_status"));
                booking.setTotalAmount(rs.getBigDecimal("total_amount"));
                booking.setBookingDate(rs.getTimestamp("booking_date"));

                bookingList.add(booking);
            }

            request.setAttribute("bookingList", bookingList);
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Error retrieving booking details.");
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        // Forward to JSP page to display bookings
        request.getRequestDispatcher("UserBookings.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int bookingId = Integer.parseInt(request.getParameter("bookingId"));
        int seatId = Integer.parseInt(request.getParameter("seat_id"));
        BigDecimal totalAmount = new BigDecimal(request.getParameter("total_amount"));
        String bookingStatus = request.getParameter("booking_status");

        try {
            // Connect to the database
            String jdbcURL = "jdbc:mysql://localhost:3306/reservation";
            String jdbcUsername = "root";
            String jdbcPassword = "Bl@091602";
            Connection conn = DriverManager.getConnection(jdbcURL, jdbcUsername, jdbcPassword);

            // Update the booking in the database
            String sql = "UPDATE bookings SET seat_id = ?, total_amount = ?, booking_status = ? WHERE booking_id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, seatId);
            stmt.setBigDecimal(2, totalAmount);
            stmt.setString(3, bookingStatus);
            stmt.setInt(4, bookingId);
            stmt.executeUpdate();

            // Close resources
            stmt.close();
            conn.close();

            // Redirect or show success message
            response.sendRedirect("ManageReservations.jsp?success=Booking updated successfully!");
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Error updating booking: " + e.getMessage());
            request.getRequestDispatcher("UpdateBooking.jsp").forward(request, response);
        }
    }

}
