package com.rms;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@WebServlet("/BookingManagement")
public class BookingManagementServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        int userId = (int) session.getAttribute("user_id");

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.println("<h1>Your Bookings</h1>");

        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/reservation", "root", "Bl@091602");
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM bookings WHERE user_id = ?");
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            out.println("<table border='1'><tr><th>Booking ID</th><th>Flight ID</th><th>Seat ID</th><th>Booking Reference</th><th>Status</th><th>Total Amount</th><th>Booking Date</th><th>Actions</th></tr>");

            while (rs.next()) {
                int bookingId = rs.getInt("booking_id");
                int flightId = rs.getInt("flight_id");
                int seatId = rs.getInt("seat_id");
                String bookingReference = rs.getString("booking_reference");
                String status = rs.getString("booking_status");
                double totalAmount = rs.getDouble("total_amount");
                String bookingDate = rs.getString("booking_date");

                out.println("<tr><td>" + bookingId + "</td><td>" + flightId + "</td><td>" + seatId + "</td><td>" + bookingReference + "</td>"
                        + "<td>" + status + "</td><td>" + totalAmount + "</td><td>" + bookingDate + "</td>"
                        + "<td><a href='ModifyBookingServlet?bookingId=" + bookingId + "'>Modify</a> | "
                        + "<a href='CancelBookingServlet?bookingId=" + bookingId + "'>Cancel</a></td></tr>");
            }
            out.println("</table>");
        } catch (SQLException e) {
            e.printStackTrace();
            out.println("<h3>Error retrieving bookings.</h3>");
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
