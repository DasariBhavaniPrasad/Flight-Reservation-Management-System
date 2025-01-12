<!-- <%@ page contentType="text/html;charset=UTF-8" language="java" %> -->
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="bookings.css">
    <title>Manage Bookings</title>
</head>
<body>
    <div class="container">
        <h1>Manage Your Bookings</h1>

        <div class="panel">
            <div>
                <h2>Modify Bookings</h2>
                <button onclick="location.href='ModifyBooking.jsp'">Modify Your Bookings</button>
            </div>

            <div>
                <h2>View Booking History</h2>
                <button onclick="location.href='BookingHistory.jsp'">View Your Booking History</button>
            </div>

            <div>
                <h2>Book a New Flight</h2>
                <button onclick="location.href='SearchFlight.jsp'">Book a New Flight</button>
            </div>
        </div>
    </div>
</body>
</html>