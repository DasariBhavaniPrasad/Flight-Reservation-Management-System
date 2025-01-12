package com.rms;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;

@WebServlet("/SelectSeatServlet")
public class SelectSeatServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int flightId = Integer.parseInt(request.getParameter("flightId"));
        ArrayList<Seat> availableSeats = new ArrayList<>();

        try {
            // Database connection parameters
            String jdbcURL = "jdbc:mysql://localhost:3306/reservation";
            String jdbcUsername = "root";
            String jdbcPassword = "Bl@091602";

            // Create a connection to the database
            try (Connection conn = DriverManager.getConnection(jdbcURL, jdbcUsername, jdbcPassword)) {
                String sql = "SELECT * FROM seats WHERE flight_id = ? AND is_available = 1";
                try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                    stmt.setInt(1, flightId);
                    try (ResultSet rs = stmt.executeQuery()) {
                        // Retrieve available seats from the database
                        while (rs.next()) {
                            Seat seat = new Seat();
                            seat.setSeatId(rs.getInt("seat_id"));
                            seat.setSeatNumber(rs.getString("seat_number"));
                            seat.setSeatClass(rs.getString("seat_class"));
                            seat.setPrice(rs.getBigDecimal("price"));
                            seat.setAvailable(rs.getBoolean("is_available"));
                            availableSeats.add(seat);
                        }
                    }
                }
            }

            // Store flightId in the session
            HttpSession session = request.getSession();
            session.setAttribute("flightId", flightId);  // Store the flightId in the session

            // Store available seats in the request
            request.setAttribute("seats", availableSeats);

            // Forward to the SelectSeats.jsp page
            request.getRequestDispatcher("SelectSeats.jsp").forward(request, response);

        } catch (SQLException e) {
            e.printStackTrace();
            response.sendRedirect("AvailableFlights.jsp?error=Error retrieving seats");
        } catch (NumberFormatException e) {
            e.printStackTrace();
            response.sendRedirect("AvailableFlights.jsp?error=Invalid flight ID");
        }
    }
}
