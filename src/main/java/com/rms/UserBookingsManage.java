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
import java.sql.ResultSet;

/**
 * Servlet implementation class UserBookingsManage
 */
@WebServlet("/UserBookingsManage")
public class UserBookingsManage extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UserBookingsManage() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        // Database connection details
        String jdbcURL = "jdbc:mysql://localhost:3306/reservation";
        String dbUser = "root";
        String dbPass = "Bl@091602";
        
        // Query to fetch booking data for the logged-in user
        String sql = "SELECT booking_id, flight_id, seat_id, booking_status FROM bookings WHERE username = ?";

        try (Connection conn = DriverManager.getConnection(jdbcURL, dbUser, dbPass);
             PreparedStatement statement = conn.prepareStatement(sql)) {
             
            // Assuming you have a way to get the logged-in username, e.g., from the session
            String username = (String) request.getSession().getAttribute("username");
            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();

            out.println("<html><body>");
            out.println("<h2>Your Bookings</h2>");
            out.println("<table border='1'><tr><th>Booking ID</th><th>Flight ID</th><th>Seat ID</th><th>Status</th><th>Actions</th></tr>");

            while (resultSet.next()) {
                int bookingId = resultSet.getInt("booking_id");
                String flightId = resultSet.getString("flight_id");
                String seatId = resultSet.getString("seat_id");
                String bookingStatus = resultSet.getString("booking_status");

                out.println("<tr>");
                out.println("<td>" + bookingId + "</td>");
                out.println("<td>" + flightId + "</td>");
                out.println("<td>" + seatId + "</td>");
                out.println("<td>" + bookingStatus + "</td>");
                out.println("<td>");
                out.println("<button onclick=\"location.href='UpdateBookingServlet?id=" + bookingId + "'\">Update</button>");
                out.println("<button onclick=\"location.href='CancelBookingServlet?id=" + bookingId + "'\">Cancel</button>");
                out.println("</td>");
                out.println("</tr>");
            }

            out.println("</table>");
            out.println("</body></html>");
        } catch (Exception e) {
            e.printStackTrace();
            out.println("Error fetching bookings: " + e.getMessage());
        }
    }
}