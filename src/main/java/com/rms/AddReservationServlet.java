package com.rms;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;



@WebServlet("/AddReservationServlet")
public class AddReservationServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String email = request.getParameter("email");
        String firstName = request.getParameter("first_name");
        String lastName = request.getParameter("last_name");
        String phoneNumber = request.getParameter("phone_number");
        String flightId = request.getParameter("flight_id");
        String seatId = request.getParameter("seat_id");
        String numberOfSeats = request.getParameter("number_of_seats");
        BigDecimal totalAmount = new BigDecimal(request.getParameter("total_amount"));

        try {
            // Connect to the database
            String jdbcURL = "jdbc:mysql://localhost:3306/reservation";
            String jdbcUsername = "root";
            String jdbcPassword = "Bl@091602";
            Connection conn = DriverManager.getConnection(jdbcURL, jdbcUsername, jdbcPassword);

            // Insert user into the users table (ensure user doesn't already exist)
            String userSql = "INSERT INTO users (username, password, email, first_name, last_name, phone_number) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement userStmt = conn.prepareStatement(userSql);
            userStmt.setString(1, username);
            userStmt.setString(2, password); // Note: Store hashed password in a real application
            userStmt.setString(3, email);
            userStmt.setString(4, firstName);
            userStmt.setString(5, lastName);
            userStmt.setString(6, phoneNumber);
            userStmt.executeUpdate();
            userStmt.close();

            // Get the user ID of the newly inserted user
            String userIdQuery = "SELECT user_id FROM users WHERE username = ?";
            PreparedStatement userIdStmt = conn.prepareStatement(userIdQuery);
            userIdStmt.setString(1, username);
            ResultSet rs = userIdStmt.executeQuery();
            int userId = 0;
            if (rs.next()) {
                userId = rs.getInt("user_id");
            }
            userIdStmt.close();

            // Insert booking into the bookings table
            String bookingSql = "INSERT INTO bookings (user_id, flight_id, seat_id, booking_reference, total_amount) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement bookingStmt = conn.prepareStatement(bookingSql);
            bookingStmt.setInt(1, userId);
            bookingStmt.setInt(2, Integer.parseInt(flightId));
            bookingStmt.setObject(3, seatId != null && !seatId.isEmpty() ? Integer.parseInt(seatId) : null);
            bookingStmt.setString(4, "BOOK" + System.currentTimeMillis()); // Generate a unique booking reference
            bookingStmt.setBigDecimal(5, totalAmount);
            bookingStmt.executeUpdate();
            bookingStmt.close();

            // Retrieve the booking details to display them
            String bookingDetailsQuery = "SELECT b.booking_id, b.booking_reference, b.total_amount FROM bookings b WHERE b.user_id = ? ORDER BY b.booking_id DESC LIMIT 1";
            PreparedStatement bookingDetailsStmt = conn.prepareStatement(bookingDetailsQuery);
            bookingDetailsStmt.setInt(1, userId);
            ResultSet bookingRs = bookingDetailsStmt.executeQuery();

            if (bookingRs.next()) {
                int bookingId = bookingRs.getInt("booking_id");
                String bookingReference = bookingRs.getString("booking_reference");
                BigDecimal amount = bookingRs.getBigDecimal("total_amount");

                // Set booking details in request for display
                request.setAttribute("bookingId", bookingId);
                request.setAttribute("bookingReference", bookingReference);
                request.setAttribute("totalAmount", amount);
                request.getRequestDispatcher("BookingDetails.jsp").forward(request, response);
            }

            bookingDetailsStmt.close();
            conn.close();

        } catch (SQLException e) {
            e.printStackTrace();
            // Handle error and redirect or show error message
        }
    }
}