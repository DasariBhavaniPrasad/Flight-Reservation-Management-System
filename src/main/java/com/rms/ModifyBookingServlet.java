package com.rms;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

@WebServlet("/ModifyBookingServlet")
public class ModifyBookingServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int bookingId = Integer.parseInt(request.getParameter("bookingId"));

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.println("<h1>Modify Booking</h1>");
        out.println("<form action='ModifyBooking' method='post'>");
        out.println("<input type='hidden' name='bookingId' value='" + bookingId + "'/>");
        out.println("New Flight ID: <input type='text' name='flightId' required/><br/>");
        out.println("New Seat ID: <input type='text' name='seatId' required/><br/>");
        out.println("<input type='submit' value='Modify Booking'/>");
        out.println("</form>");
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int bookingId = Integer.parseInt(request.getParameter("bookingId"));
        int flightId = Integer.parseInt(request.getParameter("flightId"));
        int seatId = Integer.parseInt(request.getParameter("seatId"));

        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/reservation", "root", "Bl@091602");
            PreparedStatement ps = connection.prepareStatement("UPDATE bookings SET flight_id = ?, seat_id = ? WHERE booking_id = ?");
            ps.setInt(1, flightId);
            ps.setInt(2, seatId);
            ps.setInt(3, bookingId);
            int rowsUpdated = ps.executeUpdate();

            response.setContentType("text/html");
            PrintWriter out = response.getWriter();
            if (rowsUpdated > 0) {
                out.println("<h3>Booking modified successfully!</h3>");
                // Send email notification
                sendEmailNotification(bookingId, "modified");
            } else {
                out.println("<h3>Error modifying booking.</h3>");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void sendEmailNotification(int bookingId, String action) {
        // Implement email sending logic here
        String to = "user@example.com"; // User's email address
        String from = "admin@example.com"; // Your admin email
        String host = "smtp.example.com"; // SMTP server

        // Properties for the mail session
        Properties properties = System.getProperties();
        properties.setProperty("mail.smtp.host", host);
        Session session = Session.getDefaultInstance(properties);

        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
            message.setSubject("Booking " + action);
            message.setText("Your booking with ID " + bookingId + " has been " + action + ".");

            Transport.send(message);
            System.out.println("Email sent successfully.");
        } catch (MessagingException mex) {
            mex.printStackTrace();
        }
    }
}
