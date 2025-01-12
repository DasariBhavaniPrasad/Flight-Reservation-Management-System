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
import java.util.ArrayList;
import java.util.List;

@WebServlet("/UpdateReservationServlet")
public class UpdateReservationServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int bookingId = Integer.parseInt(request.getParameter("bookingId"));
        Booking booking = null;

        try {
            // Connect to the database
            String jdbcURL = "jdbc:mysql://localhost:3306/reservation";
            String jdbcUsername = "root";
            String jdbcPassword = "Bl@091602";
            Connection conn = DriverManager.getConnection(jdbcURL, jdbcUsername, jdbcPassword);

            // Query to fetch booking details
            String sql = "SELECT b.*, s.price FROM bookings b JOIN seats s ON b.seat_id = s.seat_id WHERE b.booking_id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, bookingId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                booking = new Booking();
                booking.setBookingId(rs.getInt("booking_id"));
                booking.setUserId(rs.getInt("user_id"));
                booking.setFlightId(rs.getInt("flight_id"));
                booking.setSeatId(rs.getObject("seat_id") != null ? rs.getInt("seat_id") : null);
                booking.setBookingReference(rs.getString("booking_reference"));
                booking.setBookingStatus(rs.getString("booking_status"));
                booking.setTotalAmount(rs.getBigDecimal("total_amount"));
                
                // Set the flight ID to request
                request.setAttribute("flightId", booking.getFlightId());
            }

            // Retrieve available seats for the flight
            if (booking != null) {
                List<Seat> availableSeats = getAvailableSeats(conn, booking.getFlightId());
                request.setAttribute("availableSeats", availableSeats);
            }

            // Set the booking as a request attribute
            request.setAttribute("booking", booking);
            request.getRequestDispatcher("UpdateBooking.jsp").forward(request, response);

            // Close resources
            rs.close();
            stmt.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Error retrieving booking: " + e.getMessage());
            request.getRequestDispatcher("ManageReservations.jsp").forward(request, response);
        }
    }

    private List<Seat> getAvailableSeats(Connection conn, int flightId) throws Exception {
        List<Seat> availableSeats = new ArrayList<>();
        String sql = "SELECT * FROM seats WHERE flight_id = ? AND seat_id NOT IN (SELECT seat_id FROM bookings WHERE flight_id = ? AND booking_status != 'cancelled')";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, flightId);
        stmt.setInt(2, flightId);
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            Seat seat = new Seat();
            seat.setSeatId(rs.getInt("seat_id"));
            seat.setSeatNumber(rs.getString("seat_number"));
            seat.setPrice(rs.getBigDecimal("price"));
            availableSeats.add(seat);
        }

        rs.close();
        stmt.close();
        return availableSeats;
    }
}
