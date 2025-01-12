package com.rms;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.UUID;
import java.math.BigDecimal;

@WebServlet("/PaymentServlet")
public class PaymentServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();

        // Fetching session attributes
        String[] selectedSeats = (String[]) session.getAttribute("selectedSeats");
        BigDecimal totalAmount = (BigDecimal) session.getAttribute("totalAmount");
        Integer userId = (Integer) session.getAttribute("userId");
        Integer flightId = (Integer) session.getAttribute("flightId");

        // Debugging output to check if all values are correctly fetched
        System.out.println("Selected Seats: " + Arrays.toString(selectedSeats));
        System.out.println("Total Amount: " + totalAmount);
        System.out.println("User ID: " + userId);
        System.out.println("Flight ID: " + flightId);

        // Check for missing data
        if (selectedSeats == null || totalAmount == null || userId == null || flightId == null) {
            request.setAttribute("errorMessage", "Missing required information. Please try again.");
            request.getRequestDispatcher("error.jsp").forward(request, response);
            return;
        }

        // Process the payment and store the booking information
        try {
            String bookingReference = UUID.randomUUID().toString(); // Generate unique booking reference
            
            // Establish database connection
            try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/reservation", "root", "Bl@091602")) {
                conn.setAutoCommit(false); // Start transaction

                // Insert booking details into the bookings table
                String insertBookingSQL = "INSERT INTO bookings (user_id, flight_id, booking_reference, total_amount, booking_status) VALUES (?, ?, ?, ?, 'confirmed')";
                int bookingId; // Variable to hold the generated booking ID
                try (PreparedStatement stmt = conn.prepareStatement(insertBookingSQL, PreparedStatement.RETURN_GENERATED_KEYS)) {
                    stmt.setInt(1, userId);
                    stmt.setInt(2, flightId);
                    stmt.setString(3, bookingReference); // Use generated booking reference
                    stmt.setBigDecimal(4, totalAmount);
                    stmt.executeUpdate();

                    // Retrieve the generated booking ID
                    try (var generatedKeys = stmt.getGeneratedKeys()) {
                        if (generatedKeys.next()) {
                            bookingId = generatedKeys.getInt(1); // Get the generated booking ID
                        } else {
                            throw new SQLException("Booking ID retrieval failed.");
                        }
                    }
                }

                // Insert each selected seat into the booked_seats table
                String insertSeatSQL = "UPDATE seats SET is_available = 0 WHERE seat_number = ? AND flight_id = ?";
                for (String seat : selectedSeats) {
                    try (PreparedStatement seatStmt = conn.prepareStatement(insertSeatSQL)) {
                        seatStmt.setString(1, seat);
                        seatStmt.setInt(2, flightId);
                        int rowsUpdated = seatStmt.executeUpdate();
                        if (rowsUpdated == 0) {
                            throw new SQLException("Seat " + seat + " could not be booked or does not exist.");
                        }
                    }
                }

                // Insert payment details into the payments table
                String insertPaymentSQL = "INSERT INTO payments (booking_id, payment_method, payment_status, amount) VALUES (?, ?, ?, ?)";
                try (PreparedStatement paymentStmt = conn.prepareStatement(insertPaymentSQL)) {
                    String paymentMethod = request.getParameter("paymentMethod"); // Retrieve payment method from request
                    if (paymentMethod == null) {
                        paymentMethod = "credit_card"; // Default to credit card if not provided
                    }
                    String paymentStatus = "paid"; // Set to 'paid' for successful payment

                    paymentStmt.setInt(1, bookingId); // Use the retrieved booking ID
                    paymentStmt.setString(2, paymentMethod);
                    paymentStmt.setString(3, paymentStatus);
                    paymentStmt.setBigDecimal(4, totalAmount);
                    paymentStmt.executeUpdate();
                }

                conn.commit(); // Commit transaction
            }

            // Redirect to a confirmation page
            response.sendRedirect("Confirmation.jsp");

        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Error processing payment: " + e.getMessage());
            request.getRequestDispatcher("error.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Unexpected error: " + e.getMessage());
            request.getRequestDispatcher("error.jsp").forward(request, response);
        }
    }
}
