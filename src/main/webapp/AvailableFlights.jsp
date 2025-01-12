<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="com.rms.Flight" %>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Available Flights</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-image: url('media/avilableflight.webp'); /* Update the path to your background image */
            background-size: cover; /* Cover the entire background */
            background-position: center; /* Center the background image */
            margin: 0;
            padding: 0;
            color: #333; /* Default text color */
        }
        .container {
            width: 80%;
            margin: 50px auto;
            background-color: rgba(255, 255, 255, 0); /* Semi-transparent white background */
            padding: 20px;
            border-radius: 10px;
            box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
        }
        h2 {
            text-align: center;
        }
        table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 20px;
        }
        th, td {
            padding: 10px;
            border: none;
            text-align: center;
            font-size:20px;
        }
        th {
           
        }
        button {
            padding: 10px;
            background-color: #007bff;
            color: white;
            border: none;
            cursor: pointer;
        }
        button:hover {
            background-color: #0056b3;
        }
    </style>
</head>
<body>
    <div class="container">
        <h2>Available Flights</h2>
        <%
            ArrayList<Flight> flights = (ArrayList<Flight>) request.getAttribute("flights");
            if (flights != null && !flights.isEmpty()) {
        %>
        <table>
            <tr>
                <th>Flight ID</th>
                <th>Flight Number</th>
                <th>Origin</th>
                <th>Destination</th>
                <th>Departure Time</th>
                <th>Arrival Time</th>
                <th>Action</th>
            </tr>
            <%
                for (Flight flight : flights) {
            %>
            <tr>
                <td><%= flight.getFlightId() %></td>
                <td><%= flight.getFlightNumber() %></td>
                <td><%= flight.getOrigin() %></td>
                <td><%= flight.getDestination() %></td>
                <td><%= flight.getDepartureTime() %></td>
                <td><%= flight.getArrivalTime() %></td>
                <td>
                    <form action="SelectSeatServlet" method="post">
                        <input type="hidden" name="flightId" value="<%= flight.getFlightId() %>" />
                        <button type="submit">Select Flight</button>
                    </form>
                </td>
            </tr>
            <%
                }
            %>
        </table>
        <% } else { %>
            <p>No flights available for the selected criteria.</p>
        <% } %>
        <h3><a href="SearchFlight.jsp">Search Again</a></h3>
    </div>
</body>
</html>
