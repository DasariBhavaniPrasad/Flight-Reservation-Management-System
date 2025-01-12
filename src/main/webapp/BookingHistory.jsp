<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.sql.Connection" %>
<%@ page import="java.sql.PreparedStatement" %>
<%@ page import="java.sql.ResultSet" %>
<%@ page import="java.sql.DriverManager" %>
<%@ page import="com.rms.Booking" %>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Booking History</title>
    <style>
        <style>
    body {
        font-family: Arial, sans-serif;
        margin: 0;
        padding: 0;
        background: url('media/image1.jpg') no-repeat center center fixed;; /* Replace with your image path */
        background-size: cover; /* Cover the entire viewport */
        background-position: center; /* Center the background image */
        color: #fff; /* Text color for better contrast */
    }
    
    h1 {
        text-align: center;
        margin-top: 50px; /* Add space above the heading */
    }

    table {
        width: 80%; /* Set table width to 80% for better layout */
        margin: 20px auto; /* Center the table on the page */
        border-collapse: collapse;
        /* Semi-transparent background for tables */
        border-radius: 10px; /* Rounded corners */
        overflow: hidden; /* Prevent overflow from border radius */
    }

    th, td {
        padding: 15px; /* Increase padding for better spacing */
        border: 1px solid #ccc;
        text-align: left;
        color: black /* Text color for table cells */
    }

    th {
        background-color: rgba(255, 255, 255, 0); /* Light background for table headers */
    }

    h3 {
        text-align: center; /* Center-align booking ID headers */
        color: #f0f0f0; /* Header color for better contrast */
        text-decoration:none;
    }

    a {
        color: #00ffcc; /* Link color */
        text-decoration: none; /* Remove underline from links */
    }

    a:hover {
        text-decoration: underline; /* Underline on hover */
    }

    div {
        text-align: center; /* Center-align text in divs */
        margin: 10px; /* Add margin to divs */
    }
</style>

    </style>
</head>
<body>
    <h1>Your Booking History</h1>

    <%
        // Get the username from the session
        String username = (String) session.getAttribute("username");
        if (username == null) {
            out.println("<div style='color: red;'>Please log in to view your booking history.</div>");
            return; // Exit the JSP
        }

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            // Connect to the database
            String jdbcURL = "jdbc:mysql://localhost:3306/reservation";
            String jdbcUsername = "root";
            String jdbcPassword = "Bl@091602";
            conn = DriverManager.getConnection(jdbcURL, jdbcUsername, jdbcPassword);

            // Query to fetch user_id based on username
            String userSql = "SELECT user_id FROM users WHERE username = ?";
            stmt = conn.prepareStatement(userSql);
            stmt.setString(1, username);
            rs = stmt.executeQuery();

            int userId = -1;
            if (rs.next()) {
                userId = rs.getInt("user_id");
            } else {
                out.println("<div style='color: red;'>User not found.</div>");
                return; // Exit the JSP
            }

            // Reset statement and result set for the next query
            stmt.close();
            rs.close();

            // Query to fetch all booking history for the user
            String bookingSql = "SELECT * FROM bookings WHERE user_id = ?";
            stmt = conn.prepareStatement(bookingSql);
            stmt.setInt(1, userId);
            rs = stmt.executeQuery();

            // Check if any bookings exist
            boolean bookingsFound = false;
            while (rs.next()) {
                bookingsFound = true;

                // Populate the booking object
                Booking booking = new Booking();
                booking.setBookingId(rs.getInt("booking_id"));
                booking.setFlightId(rs.getInt("flight_id"));
                booking.setSeatId(rs.getInt("seat_id"));
                booking.setBookingStatus(rs.getString("booking_status"));
                booking.setTotalAmount(rs.getBigDecimal("total_amount"));
                booking.setBookingDate(rs.getTimestamp("booking_date"));

                // Display booking details in a table
                %>
                <h3 style="color:black;">Booking ID: <%= booking.getBookingId() %></h3>
                <table>
                    <tr>
                        <th>Flight ID</th>
                        <td><%= booking.getFlightId() %></td>
                    </tr>
                    <tr>
                        <th>Seat ID</th>
                        <td><%= booking.getSeatId() %></td>
                    </tr>
                    <tr>
                        <th>Status</th>
                        <td><%= booking.getBookingStatus() %></td>
                    </tr>
                    <tr>
                        <th>Total Amount</th>
                        <td><%= booking.getTotalAmount() %></td>
                    </tr>
                    <tr>
                        <th>Booking Date</th>
                        <td><%= booking.getBookingDate() %></td>
                    </tr>
                </table>
                <hr>
                <%
            }

            if (!bookingsFound) {
                out.println("<div>No booking history found. Would you like to <a href='BookFlight.jsp'>book a flight?</a></div>");
            }

        } catch (Exception e) {
            e.printStackTrace();
            out.println("<div style='color: red;'>Error retrieving booking history: " + e.getMessage() + "</div>");
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    %>

    <h3><button ><a href="ManageBookings.jsp">Back to Manage Bookings</a></button></h3>
</body>
</html>
