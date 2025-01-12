<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Add Flight</title>
</head>
<body>
    <h2>Add New Flight</h2>
    <form action="AddFlightServlet" method="post">
        <label>Flight Number:</label><br>
        <input type="text" name="flightNumber" required><br>

        <label>Airline:</label><br>
        <input type="text" name="airline" required><br>

        <label>Origin:</label><br>
        <input type="text" name="origin" required><br>

        <label>Destination:</label><br>
        <input type="text" name="destination" required><br>

        <label>Departure Time:</label><br>
        <input type="datetime-local" name="departureTime" required><br>

        <label>Arrival Time:</label><br>
        <input type="datetime-local" name="arrivalTime" required><br>

        <label>Duration:</label><br>
        <input type="time" name="duration" required><br>

        <label>Status:</label><br>
        <select name="status" required>
            <option value="scheduled">Scheduled</option>
            <option value="delayed">Delayed</option>
            <option value="cancelled">Cancelled</option>
        </select><br><br>

        <input type="submit" value="Add Flight">
    </form>

    <h3><a href="ManageFlightsServlet">Back to Flight Management</a></h3>
</body>
</html>
