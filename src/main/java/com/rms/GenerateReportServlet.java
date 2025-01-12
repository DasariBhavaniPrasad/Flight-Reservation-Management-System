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

@WebServlet("/GenerateReportServlet")
public class GenerateReportServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Set response content type to HTML
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            // Connect to the database
            String jdbcURL = "jdbc:mysql://localhost:3306/reservation";
            String jdbcUsername = "root";
            String jdbcPassword = "Bl@091602";
            conn = DriverManager.getConnection(jdbcURL, jdbcUsername, jdbcPassword);

            // Query to fetch bookings
            String sql = "SELECT * FROM bookings";
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();

            // Start HTML table
            out.println("<html><head><title>Booking Report</title></head><body>");
            out.println("<h2>Booking Report</h2>");
            out.println("<table border='1'>");
            out.println("<tr><th>Booking ID</th><th>User ID</th><th>Flight ID</th><th>Seat ID</th><th>Booking Reference</th><th>Booking Status</th><th>Total Amount</th><th>Booking Date</th></tr>");

            // Populate the table with booking data
            while (rs.next()) {
                out.print("<tr>");
                out.print("<td>" + rs.getInt("booking_id") + "</td>");
                out.print("<td>" + rs.getInt("user_id") + "</td>");
                out.print("<td>" + rs.getInt("flight_id") + "</td>");
                out.print("<td>" + (rs.getObject("seat_id") != null ? rs.getInt("seat_id") : "N/A") + "</td>");
                out.print("<td>" + rs.getString("booking_reference") + "</td>");
                out.print("<td>" + rs.getString("booking_status") + "</td>");
                out.print("<td>" + rs.getBigDecimal("total_amount") + "</td>");
                out.print("<td>" + rs.getTimestamp("booking_date") + "</td>");
                out.print("</tr>");
            }
            out.println("</table>");

            // Provide a download link for the CSV report
            out.println("<h3>Download the report as CSV</h3>");
            out.println("<button onclick=\"location.href='DownloadReportServlet'\">Download CSV</button>");
            out.println("</body></html>");

        } catch (Exception e) {
            e.printStackTrace();
            out.println("Error generating report: " + e.getMessage());
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
