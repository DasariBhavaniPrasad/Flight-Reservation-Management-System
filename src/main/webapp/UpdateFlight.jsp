<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.rms.Flight" %>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Update Flight</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 20px;
        }
        label {
            display: block;
            margin: 10px 0 5px;
        }
        input, select {
            width: 100%;
            padding: 8px;
            margin: 5px 0;
        }
        button {
            padding: 10px 15px;
            cursor: pointer;
        }
    </style>
</head>
<body>
    <h2>Update Flight</h2>

    <%
        Flight flight = (Flight) request.getAttribute("flight");
        String errorMessage = (String) request.getAttribute("errorMessage");
        if (errorMessage != null) {
    %>
        <div style="color: red;"><%= errorMessage %></div>
    <%
        } else if (flight != null) { // Check if flight is not null
    %>
        <form action="http://localhost:8080/ReservationManagement/UpdateFlightServlet" method="post">
            <input type="hidden" name="flightId" value="<%= flight.getFlightId() %>">
            
            <label for="flightNumber">Flight Number:</label>
            <input type="text" id="flightNumber" name="flightNumber" value="<%= flight.getFlightNumber() %>" required>

            <label for="airline">Airline:</label>
            <input type="text" id="airline" name="airline" value="<%= flight.getAirline() %>" required>

            <label for="origin">Origin:</label>
            <input type="text" id="origin" name="origin" value="<%= flight.getOrigin() %>" required>

            <label for="destination">Destination:</label>
            <input type="text" id="destination" name="destination" value="<%= flight.getDestination() %>" required>

            <label for="departureTime">Departure Time:</label>
            <input type="datetime-local" id="departureTime" name="departureTime" 
                   value="<%= new java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm").format(flight.getDepartureTime()) %>" required>

            <label for="arrivalTime">Arrival Time:</label>
            <input type="datetime-local" id="arrivalTime" name="arrivalTime" 
                   value="<%= new java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm").format(flight.getArrivalTime()) %>" required>

            <label for="duration">Duration (HH:mm):</label>
            <input type="text" id="duration" name="duration" 
                   value="<%= flight.getDuration() != null ? new java.text.SimpleDateFormat("HH:mm").format(flight.getDuration()) : "00:00" %>" 
                   required placeholder="e.g., 02:30">

            <label for="status">Status:</label>
            <select id="status" name="status" required>
                <option value="Scheduled" <%= flight.getStatus().equals("Scheduled") ? "selected" : "" %>>Scheduled</option>
                <option value="Cancelled" <%= flight.getStatus().equals("Cancelled") ? "selected" : "" %>>Cancelled</option>
                <option value="Delayed" <%= flight.getStatus().equals("Delayed") ? "selected" : "" %>>Delayed</option>
            </select>

            <button type="submit" onclick="location.href='http://localhost:8080/ReservationManagement/ManageFlightsServlet'">Update Flight</button>
        </form>
    <%
        } else {
    %>
        <div style="color: red;">No flight details available for update.</div>
    <%
        }
    %>

    <button onclick="location.href='http://localhost:8080/ReservationManagement/ManageFlightsServlet'">Back to Manage Flights</button>
</body>
</html>
