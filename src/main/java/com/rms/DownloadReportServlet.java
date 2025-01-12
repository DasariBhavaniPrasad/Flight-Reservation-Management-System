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

@WebServlet("/DownloadReportServlet")
public class DownloadReportServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Set response content type to CSV
        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment;filename=booking_report.csv");
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

            // Generate CSV report
            out.println("Booking ID,User ID,Flight ID,Seat ID,Booking Reference,Booking Status,Total Amount,Booking Date");

            while (rs.next()) {
                out.print(rs.getInt("booking_id") + ",");
                out.print(rs.getInt("user_id") + ",");
                out.print(rs.getInt("flight_id") + ",");
                out.print((rs.getObject("seat_id") != null ? rs.getInt("seat_id") : "N/A") + ",");
                out.print(rs.getString("booking_reference") + ",");
                out.print(rs.getString("booking_status") + ",");
                out.print(rs.getBigDecimal("total_amount") + ",");
                out.println(rs.getTimestamp("booking_date"));
            }

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
