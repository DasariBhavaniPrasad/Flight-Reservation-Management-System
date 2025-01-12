package com.rms;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;

@WebServlet("/SearchFlightServlet")
public class SearchFlightServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String origin = request.getParameter("origin");
        String destination = request.getParameter("destination");
        String departureTime = request.getParameter("departureTime");

        ArrayList<Flight> flights = new ArrayList<>();

        try {
            String jdbcURL = "jdbc:mysql://localhost:3306/reservation";
            String jdbcUsername = "root";
            String jdbcPassword = "Bl@091602";

            Connection conn = DriverManager.getConnection(jdbcURL, jdbcUsername, jdbcPassword);
            String sql = "SELECT * FROM flights WHERE origin = ? AND destination = ? AND departure_time >= ? AND status = 'scheduled'";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, origin);
            stmt.setString(2, destination);
            stmt.setString(3, departureTime);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Flight flight = new Flight();
                flight.setFlightId(rs.getInt("flight_id"));
                flight.setFlightNumber(rs.getString("flight_number"));
                flight.setOrigin(rs.getString("origin"));
                flight.setDestination(rs.getString("destination"));
                flight.setDepartureTime(rs.getTimestamp("departure_time"));
                flight.setArrivalTime(rs.getTimestamp("arrival_time"));
                flight.setStatus(rs.getString("status"));
                flights.add(flight);
            }
            request.setAttribute("flights", flights);
            request.getRequestDispatcher("AvailableFlights.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("SearchFlight.jsp?error=Error retrieving flights");
        }
    }
}
