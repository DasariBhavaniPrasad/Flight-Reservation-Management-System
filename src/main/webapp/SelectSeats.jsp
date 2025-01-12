<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="com.rms.Seat" %>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Select Seats</title>
  <style>
    body {
        font-family: Arial, sans-serif;
        background: url('media/bookinghistory.webp') no-repeat center center fixed;
        background-size: cover;
        margin: 0;
        padding: 0;
    }

    .container {
        width: 80%;
        margin: 50px auto;
        background-color: rgba(255, 255, 255, 0); /* Semi-transparent background */
        padding: 30px;
        border-radius: 12px;
        box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);
    }

    h2 {
        text-align: center;
        color: #333;
        margin-bottom: 20px;
    }

    table {
        width: 100%;
        border-collapse: collapse;
        margin-top: 20px;
        background-color: rgba(255, 255, 255, 0);
       
    }

    th, td {
        padding: 15px;
        text-align: center;
        background-color: rgba(255, 255, 255, 0);
        font-size:20px;
    }

    th {
        background-color: #007bff;
        color: white;
        font-weight: bold;
        background-color: rgba(255, 255, 255, 0);
    }

    td {
        background-color: #fff;
        color: #333;
        background-color: rgba(255, 255, 255, 0);
    }

    td input[type="checkbox"] {
        transform: scale(1.2); /* Enlarge checkbox */
        cursor: pointer;
    }

    button {
        display: block;
        width: 50%;
        margin: 20px auto;
        padding: 15px;
        background-color: #007bff;
        color: white;
        font-size: 16px;
        font-weight: bold;
        border: none;
        border-radius: 8px;
        cursor: pointer;
        transition: background-color 0.3s ease;
    }

    button:hover {
        background-color: #0056b3;
    }

    p {
        text-align: center;
        color: #333;
    }
</style>


</head>
<body>
    <div class="container">
        <h2>Select Seats</h2>
        <%
            ArrayList<Seat> seats = (ArrayList<Seat>) request.getAttribute("seats");
            if (seats != null && !seats.isEmpty()) {
        %>
       <form action="ConfirmSelectionServlet" method="post">
    <table>
        <tr>
            <th>Seat Number</th>
            <th>Seat Class</th>
            <th>Price</th>
            <th>Select</th>
        </tr>
        <%
            for (Seat seat : seats) {
        %>
        <tr>
            <td><%= seat.getSeatNumber() %></td>
            <td><%= seat.getSeatClass() %></td>
            <td>$<%= seat.getPrice() %></td>
            <td>
                <input type="checkbox" name="selectedSeats" value="<%= seat.getSeatNumber() %>" />
                <input type="hidden" name="price_<%= seat.getSeatNumber() %>" value="<%= seat.getPrice() %>" /> <!-- Added hidden input for price -->
            </td>
        </tr>
        <%
            }
        %>
    </table>
    <input type="hidden" name="flightId" value="<%= request.getParameter("flightId") %>" />
    <button type="submit">Proceed to Payment</button>
</form>

        <% } else { %>
            <p>No seats available for the selected flight.</p>
        <% } %>
    </div>
</body>
</html>
