<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.rms.Booking" %>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Manage Reservations</title>
    <style>
        table {
            width: 100%;
            border-collapse: collapse;
        }
        th, td {
            padding: 10px;
            border: 1px solid #ccc;
            text-align: left;
        }
        .button-container {
            margin-bottom: 20px;
        }
    </style>
</head>
<body>
    <h2>Manage Reservations</h2>

    <div class="button-container">
        <button onclick="location.href='admin.html'">Back to Admin Panel</button>
    </div>

    <%
        List<Booking> bookings = (List<Booking>) request.getAttribute("bookings");
        String errorMessage = (String) request.getAttribute("errorMessage");
        String successMessage = (String) request.getAttribute("successMessage");
        
        if (errorMessage != null) {
    %>
        <div style="color: red;"><%= errorMessage %></div>
    <%
        }
        
        if (successMessage != null) {
    %>
        <div style="color: green;"><%= successMessage %></div>
    <%
        }
    %>

    <table>
        <thead>
        <tr>
            <th>Booking ID</th>
            <th>User ID</th>
            <th>Flight ID</th>
            <th>Seat ID</th>
            <th>Booking Reference</th>
            <th>Booking Status</th>
            <th>Total Amount</th>
            <th>Actions</th>
        </tr>
        </thead>
        <tbody>
        <%
            if (bookings != null && !bookings.isEmpty()) {
                for (Booking booking : bookings) {
        %>
                    <tr>
                        <td><%= booking.getBookingId() %></td>
                        <td><%= booking.getUserId() %></td>
                        <td><%= booking.getFlightId() %></td>
                        <td><%= booking.getSeatId() != null ? booking.getSeatId() : "N/A" %></td>
                        <td><%= booking.getBookingReference() %></td>
                        <td><%= booking.getBookingStatus() %></td>
                        <td><%= booking.getTotalAmount() %></td>
                        <td>
                            <button onclick="location.href='UpdateReservationServlet?bookingId=<%= booking.getBookingId() %>'">Update</button>
                            <button onclick="if(confirm('Are you sure you want to delete this booking?')) { location.href='DeleteReservationServlet?bookingId=<%= booking.getBookingId() %>' }">Delete</button>
                        </td>
                    </tr>
        <%
                }
            } else {
        %>
            <tr>
                <td colspan="8">No reservations found.</td>
            </tr>
        <%
            }
        %>
        </tbody>
    </table>

    <h3>Add New Reservation</h3>
    <button onclick="location.href='AddReservation.jsp'">Insert New Reservation</button>
</body>
</html>
