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
    <title>Modify Booking</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-image: url('media/bookhistory.jpg'); /* Update the path to your background image */
            background-size: cover; /* Cover the entire background */
            background-position: center; /* Center the background image */
            margin: 0;
            padding: 0;
            color: white; /* Default text color */
        }
        h2 {
            text-align: center;
            margin-top: 20px;
            color: #ffffff; /* Adjust text color for visibility */
        }
        h3 {
            color: #ffffff; /* Adjust text color for visibility */
            margin-left:170px;
            display: inline;
        }
        table {
            width: 80%;
            margin: 20px auto; /* Center the table */
            border-collapse: collapse;
            background-color: rgba(255, 255, 255, 0); /* Semi-transparent white background for tables */
            border-radius: 10px ;
            box-shadow: 0 2px 10px rgba(0, 0, 0, 0.2);
        }
        th, td {
            padding: 10px;
            border: 1px solid black;
            text-align: left;
        }
        th {
           
        }
        button {
            padding: 10px;
            background-color: #007bff;
            color: white;
            border: none;
            cursor: pointer;
            border-radius: 5px; /* Add rounded corners */
        }
        button:hover {
            background-color: #0056b3;
        }
        a {
            color: #ffffff; /* Adjust link color for visibility */
            text-decoration: none; /* Remove underline from links */
        }
        a:hover {
            text-decoration: underline; /* Underline on hover */
        }
        .error-message {
            color: red;
            text-align: center;
            margin: 20px;
        }
    </style>
</head>
<body>
    <h2>Modify Booking</h2>

    <%
        // Get the username from the session (assuming the username is stored in session)
        String username = (String) session.getAttribute("username");
        if (username == null) {
            out.println("<div class='error-message'>Please log in to view your bookings.</div>");
            return; // Exit the JSP
        }

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        int userId = -1; // Initialize userId

        try {
            // Connect to the database
            String jdbcURL = "jdbc:mysql://localhost:3306/reservation";
            String jdbcUsername = "root";
            String jdbcPassword = "Bl@091602";
            conn = DriverManager.getConnection(jdbcURL, jdbcUsername, jdbcPassword);

            // Query to fetch the user_id based on username
            String userSql = "SELECT user_id, first_name, last_name, email, phone_number FROM users WHERE username = ?";
            stmt = conn.prepareStatement(userSql);
            stmt.setString(1, username);
            rs = stmt.executeQuery();

            // Retrieve user details
            if (rs.next()) {
                userId = rs.getInt("user_id");
                String firstName = rs.getString("first_name");
                String lastName = rs.getString("last_name");
                String email = rs.getString("email");
                String phoneNumber = rs.getString("phone_number");
                
                // Display user details
                %>
                <h3>User Details:</h3>
                <table>
                    <tr>
                        <th>First Name</th>
                        <td><%= firstName %></td>
                    </tr>
                    <tr>
                        <th>Last Name</th>
                        <td><%= lastName %></td>
                    </tr>
                    <tr>
                        <th>Email</th>
                        <td><%= email %></td>
                    </tr>
                    <tr>
                        <th>Phone Number</th>
                        <td><%= phoneNumber %></td>
                    </tr>
                </table>
                <%
            } else {
                out.println("<div class='error-message'>User not found.</div>");
                return; // Exit the JSP
            }

            // Reset stmt and rs for the next query
            stmt.close();
            rs.close();

            // Query to fetch the bookings based on user_id
            String bookingSql = "SELECT * FROM bookings WHERE user_id = ?";
            stmt = conn.prepareStatement(bookingSql);
            stmt.setInt(1, userId);
            rs = stmt.executeQuery();
            
            String cancellationMessage = (String) session.getAttribute("cancellationMessage");
            if (cancellationMessage != null) {
                out.println("<script>alert('" + cancellationMessage + "');</script>");
                session.removeAttribute("cancellationMessage"); // Clear the message after showing
            }

           
            boolean bookingsFound = false;
            while (rs.next()) {
                // Check if the booking is cancelled
                String bookingStatus = rs.getString("booking_status");
                if ("cancelled".equalsIgnoreCase(bookingStatus)) {
                    continue; // Skip this booking
                }

                bookingsFound = true;

                // Populate the booking object
                Booking booking = new Booking();
                booking.setBookingId(rs.getInt("booking_id"));
                booking.setFlightId(rs.getInt("flight_id"));
                booking.setSeatId(rs.getInt("seat_id"));
                booking.setBookingStatus(bookingStatus);
                booking.setTotalAmount(rs.getBigDecimal("total_amount"));
                booking.setBookingDate(rs.getTimestamp("booking_date"));

                %>
                <h3>Booking Details:</h3>
                <table>
                    <tr>
                        <th>Booking ID</th>
                        <td><%= booking.getBookingId() %></td>
                    </tr>
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

                <!-- Form to cancel booking -->
                <form action="CancelBookingServlet" method="post">
                    <input type="hidden" name="bookingId" value="<%= booking.getBookingId() %>" />
                    <div>
                        <button type="submit" style="background-color: red; color: white;margin-left:170px;">Cancel Booking</button>
                    </div>
                </form>
                <hr>
                <%
            }
            if (!bookingsFound) {
                out.println("<div>No bookings found. Would you like to <a href='BookFlight.jsp'>book a flight?</a></div>");
            }
        } catch (Exception e) {
            e.printStackTrace();
            out.println("<div class='error-message'>Error retrieving booking: " + e.getMessage() + "</div>");
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

    <h3><button><a href="ManageBookings.jsp">Back to Manage Bookings</a></button></h3>
    <h3><button><a href="SearchFlight.jsp">Book a New Flight</a></button></h3>
</body>
</html>
