package com.rms;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;

@WebServlet("/InvoiceServlet")
public class InvoiceServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        
        // Retrieve the booking and seat details from session
        Booking booking = (Booking) session.getAttribute("booking");
        Integer numberOfSeats = (Integer) session.getAttribute("numberOfSeats");

        if (booking != null) {
            try {
                Connection conn = DatabaseConnection.initializeDatabase();

                // Update booking status to 'Pending' for cash payment
                String updateBookingSql = "UPDATE bookings SET booking_status = 'Pending' WHERE booking_id = ?";
                PreparedStatement bookingStmt = conn.prepareStatement(updateBookingSql);
                bookingStmt.setInt(1, booking.getBookingId());
                bookingStmt.executeUpdate();

                // Update the flight seat availability
                String updateSeatsSql = "UPDATE flights SET available_seats = available_seats - ? WHERE flight_id = ?";
                PreparedStatement seatsStmt = conn.prepareStatement(updateSeatsSql);
                seatsStmt.setInt(1, numberOfSeats);
                seatsStmt.setInt(2, booking.getFlightId());
                seatsStmt.executeUpdate();

                conn.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // Pass booking information to the request for the invoice
        request.setAttribute("booking", booking);
        request.setAttribute("numberOfSeats", numberOfSeats);
        RequestDispatcher dispatcher = request.getRequestDispatcher("invoice.jsp");
        dispatcher.forward(request, response);
    }
}
