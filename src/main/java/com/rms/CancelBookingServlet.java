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

@WebServlet("/CancelBookingServlet")
public class CancelBookingServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int bookingId = Integer.parseInt(request.getParameter("bookingId"));

        try {
            String jdbcURL = "jdbc:mysql://localhost:3306/reservation";
            String jdbcUsername = "root";
            String jdbcPassword = "Bl@091602";

            Connection conn = DriverManager.getConnection(jdbcURL, jdbcUsername, jdbcPassword);
            String sql = "UPDATE bookings SET booking_status = 'cancelled' WHERE booking_id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, bookingId);
            stmt.executeUpdate();

            // Redirect back to ManageBookings.jsp with a success message
            //response.sendRedirect("ModifyBooking.jsp?message=Booking+cancelled+successfully");
            
            request.getSession().setAttribute("cancellationMessage", "Your booking has been canceled.");
            response.sendRedirect("ModifyBooking.jsp");
           
            
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("ModifyBooking.jsp?error=Error+canceling+booking");
        }
    }
}
