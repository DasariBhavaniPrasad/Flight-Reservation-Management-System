<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.sql.*" %>
<%@ page import="java.math.*" %>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Add New Reservation</title>
    <style>
        body {
            font-family: Arial, sans-serif;
        }
        .container {
            width: 50%;
            margin: auto;
        }
        label {
            display: block;
            margin-top: 10px;
        }
        input, select {
            width: 100%;
            padding: 10px;
            margin-top: 5px;
        }
        button {
            margin-top: 20px;
            padding: 10px;
        }
    </style>
    <script>
        function updateTotalAmount() {
            var seatPrice = parseFloat(document.getElementById("seat_price").value);
            var numberOfSeats = parseInt(document.getElementById("number_of_seats").value);
            var totalAmount = seatPrice * numberOfSeats;
            document.getElementById("total_amount").value = totalAmount.toFixed(2);
        }
    </script>
</head>
<body>
    <div class="container">
        <h2>Add New Reservation</h2>
        <form action="AddReservationServlet" method="post">
            <h3>User Information</h3>
            <label for="username">Username:</label>
            <input type="text" id="username" name="username" required>

            <label for="password">Password:</label>
            <input type="password" id="password" name="password" required>

            <label for="email">Email:</label>
            <input type="email" id="email" name="email" required>

            <label for="first_name">First Name:</label>
            <input type="text" id="first_name" name="first_name" required>

            <label for="last_name">Last Name:</label>
            <input type="text" id="last_name" name="last_name" required>

            <label for="phone_number">Phone Number:</label>
            <input type="text" id="phone_number" name="phone_number" required>

            <label for="flight_id">Flight ID:</label>
            <select id="flight_id" name="flight_id" required>
                <%
                    try {
                        String jdbcURL = "jdbc:mysql://localhost:3306/reservation";
                        String jdbcUsername = "root";
                        String jdbcPassword = "Bl@091602";
                        Connection conn = DriverManager.getConnection(jdbcURL, jdbcUsername, jdbcPassword);

                        String sql = "SELECT flight_id, flight_number FROM flights";
                        PreparedStatement stmt = conn.prepareStatement(sql);
                        ResultSet rs = stmt.executeQuery();

                        while (rs.next()) {
                            int flightId = rs.getInt("flight_id");
                            String flightNumber = rs.getString("flight_number");
                %>
                            <option value="<%= flightId %>"><%= flightNumber %></option>
                <%
                        }

                        rs.close();
                        stmt.close();
                        conn.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                %>
            </select>

            <label for="seat_id">Seat ID:</label>
            <select id="seat_id" name="seat_id" required onchange="updateSeatPrice(this)">
                <option value="">Select a seat</option>
                <%
                    try {
                        String jdbcURL = "jdbc:mysql://localhost:3306/reservation";
                        String jdbcUsername = "root";
                        String jdbcPassword = "Bl@091602";
                        Connection conn = DriverManager.getConnection(jdbcURL, jdbcUsername, jdbcPassword);

                        String sql = "SELECT seat_id, seat_number, price FROM seats WHERE is_available = true";
                        PreparedStatement stmt = conn.prepareStatement(sql);
                        ResultSet rs = stmt.executeQuery();

                        while (rs.next()) {
                            int seatId = rs.getInt("seat_id");
                            String seatNumber = rs.getString("seat_number");
                            BigDecimal price = rs.getBigDecimal("price");
                %>
                            <option value="<%= seatId %>" data-price="<%= price %>"><%= seatNumber %> - $<%= price %></option>
                <%
                        }

                        rs.close();
                        stmt.close();
                        conn.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                %>
            </select>

            <label for="number_of_seats">Number of Seats:</label>
            <input type="number" id="number_of_seats" name="number_of_seats" value="1" min="1" onchange="updateTotalAmount()" required>

            <input type="hidden" id="seat_price" value="0">
            <label for="total_amount">Total Amount:</label>
            <input type="text" id="total_amount" name="total_amount" required readonly>

            <button type="submit">Add Reservation</button>
        </form>
        <button onclick="location.href='ManageReservations.jsp'">Back to Manage Reservations</button>
    </div>

    <script>
        function updateSeatPrice(select) {
            var price = select.options[select.selectedIndex].getAttribute("data-price");
            document.getElementById("seat_price").value = price;
            updateTotalAmount();
        }
    </script>
</body>
</html>
