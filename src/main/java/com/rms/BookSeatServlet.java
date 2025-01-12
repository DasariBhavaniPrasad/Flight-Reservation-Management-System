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
import java.util.UUID;

@WebServlet("/BookSeatServlet")
public class BookSeatServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int flightId = Integer.parseInt(request.getParameter("flightId"));
        int seatId = Integer.parseInt(request.getParameter("seatId"));
        int userId = (int) request.getSession().getAttribute("userId"); // Assuming user is logged in and userId is stored in session
        double totalAmount = 0.0;

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            // Database connection
            String jdbcURL = "jdbc:mysql://localhost:3306/reservation";
            String jdbcUsername = "root";
            String jdbcPassword = "Bl@091602";
            conn = DriverManager.getConnection(jdbcURL, jdbcUsername, jdbcPassword);

            // Fetch seat price for calculating total amount
            String seatPriceSQL = "SELECT price FROM seats WHERE seat_id = ? AND is_available = true";
            stmt = conn.prepareStatement(seatPriceSQL);
            stmt.setInt(1, seatId);
            rs = stmt.executeQuery();

            if (rs.next()) {
                totalAmount = rs.getDouble("price");
            } else {
                request.setAttribute("errorMessage", "Selected seat is not available.");
                request.getRequestDispatcher("Error.jsp").forward(request, response);
                return;
            }

            // Generate a unique booking reference
            String bookingReference = UUID.randomUUID().toString().substring(0, 20);

            // Insert into bookings table
            String bookingSQL = "INSERT INTO bookings (user_id, flight_id, seat_id, booking_reference, booking_status, total_amount) VALUES (?, ?, ?, ?, ?, ?)";
            stmt = conn.prepareStatement(bookingSQL);
            stmt.setInt(1, userId);
            stmt.setInt(2, flightId);
            stmt.setInt(3, seatId);
            stmt.setString(4, bookingReference);
            stmt.setString(5, "pending"); // Default status is pending
            stmt.setDouble(6, totalAmount);
            stmt.executeUpdate();

            // Update seat availability to unavailable
            String updateSeatSQL = "UPDATE seats SET is_available = false WHERE seat_id = ?";
            stmt = conn.prepareStatement(updateSeatSQL);
            stmt.setInt(1, seatId);
            stmt.executeUpdate();

            // Set the booking reference and total amount as request attributes
            request.setAttribute("bookingReference", bookingReference);
            request.setAttribute("totalAmount", totalAmount);

            // Redirect to the payment page
            request.getRequestDispatcher("Payment.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Error during seat booking: " + e.getMessage());
            request.getRequestDispatcher("Error.jsp").forward(request, response);
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
