<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.rms.Flight" %>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Manage Flights</title>
    <style>
         body {
            font-family: 'Arial', sans-serif;
            background: url('media/8442134.jpg') no-repeat center center fixed;
            background-size: cover;
            color: #333;
            margin: 0;
            padding: 20px;
        }

        h2, h3 {
            color: #ffffff;
            text-align: center;
            font-weight: bold;
            margin-bottom: 20px;
        }

        table {
            width: 100%;
            max-width: 1200px;
            margin: 20px auto;
            border-collapse: collapse;
            background-color: rgba(255, 255, 255, 0.9);
            border-radius: 8px;
            overflow: hidden;
            box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1);
        }

        th {
            background-color: #4CAF50;
            color: white;
            padding: 12px;
            text-align: left;
        }

        td {
            padding: 12px;
            text-align: left;
            border-bottom: 1px solid #ddd;
        }

        tr:hover {
            background-color: #f1f1f1;
        }

        th, td {
            border-right: 1px solid #ddd;
        }

        td:last-child {
            border-right: none;
        }

        button {
            background-color: #4CAF50;
            color: white;
            border: none;
            padding: 8px 12px;
            border-radius: 5px;
            cursor: pointer;
            transition: background-color 0.3s ease;
            
        }

        button:hover {
            background-color: #45a049;
        }

        .error-message {
            color: red;
            text-align: center;
            margin-bottom: 20px;
        }

        .add-button, .admin-link {
            display: flex;
            justify-content: center;
            margin-top: 30px;
        }

        .add-button button, .admin-link a {
            font-size: 16px;
            text-decoration: none;
            background-color: #00796b;
            color: white;
            padding: 10px 20px;
            border-radius: 5px;
        }

        .add-button button:hover, .admin-link a:hover {
            background-color: #004d40;
        }
    </style>
</head>
<body>
    <h2>Manage Flights</h2>

    <%
        List<Flight> flights = (List<Flight>) request.getAttribute("flights");
        String errorMessage = (String) request.getAttribute("errorMessage");
        if (errorMessage != null) {
    %>
        <div style="color: red;"><%= errorMessage %></div>
    <%
        }
    %>

    <table>
        <thead>
        <tr>
            <th>Flight ID</th>
            <th>Flight Number</th>
            <th>Airline</th>
            <th>Origin</th>
            <th>Destination</th>
            <th>Departure Time</th>
            <th>Arrival Time</th>
            <th>Duration</th>
            <th>Status</th>
            <th>Actions</th>
        </tr>
        </thead>
        <tbody>
        <%
            if (flights != null && !flights.isEmpty()) {
                for (Flight flight : flights) {
        %>
                    <tr>
                        <td><%= flight.getFlightId() %></td>
                        <td><%= flight.getFlightNumber() %></td>
                        <td><%= flight.getAirline() %></td>
                        <td><%= flight.getOrigin() %></td>
                        <td><%= flight.getDestination() %></td>
                        <td><%= flight.getDepartureTime() %></td>
                        <td><%= flight.getArrivalTime() %></td>
                        <td><%= flight.getDuration() %></td>
                        <td><%= flight.getStatus() %></td>
                        <td>
                            <button onclick="location.href='UpdateFlightServlet?flightId=<%= flight.getFlightId() %>'">Update</button>
                            <button onclick="location.href='DeleteFlightServlet?flightId=<%= flight.getFlightId() %>'">Delete</button>
                        </td>
                    </tr>
        <%
                }
            } else {
        %>
            <tr>
                <td colspan="10">No flights found.</td>
            </tr>
        <%
            }
        %>
        </tbody>
    </table>

    <h3>Add New Flight</h3>
    <button onclick="location.href='AddFlight.jsp'" style="margin-left : 690px;" >Insert New Flight</button>

    <h3> <button onclick="location.href='admin.html'">Back to Admin Panel</a></button></h3>
</body>
</html>