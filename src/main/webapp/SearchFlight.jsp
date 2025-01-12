<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Search Flights</title>
    <style>
         /* General reset */
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }

        body {
            font-family: Arial, sans-serif;
            background: url('media/flightsearch.webp') no-repeat center center fixed;
            background-size: cover;
            margin: 0;
            padding: 0;
            min-height: 100vh;
            display: flex;
            justify-content: center;
            align-items: center;
        }

        .container {
            background-color: rgba(255, 255, 255, 0); /* Slight transparency for a modern look */
            padding: 30px;
            border-radius: 10px;
            width: 50%;
            max-width: 500px;
            box-shadow: 0 8px 20px rgba(0, 0, 0, 0.2); /* Soft shadow for depth */
            text-align: left;
            margin-right: 750px;
        }

        h2 {
            color: black;
            margin-bottom: 20px;
            font-size: 28px;
            margin-left: 90px;
        }

        label {
            display: block;
            margin-top: 15px;
            font-size: 14px;
            color: white;
            text-align: left;
        }

        input[type="text"], input[type="date"] {
            width: 100%;
            padding: 12px;
            margin-top: 5px;
            border: 1px solid #ccc;
            border-radius: 5px;
            font-size: 16px;
            background-color: #f9f9f9;
        }

        input[type="text"]:focus, input[type="date"]:focus {
            border-color: #00796b;
            outline: none;
        }

        button {
            margin-top: 20px;
            width: 100%;
            padding: 12px;
            background-color: #00796b;
            border: none;
            color: white;
            font-size: 16px;
            border-radius: 5px;
            cursor: pointer;
            transition: background-color 0.3s ease;
        }

        button:hover {
            background-color: #004d40;
        }

        /* Responsive Design */
        @media (max-width: 768px) {
            .container {
                width: 90%;
            }

            h2 {
                font-size: 24px;
            }

            button {
                font-size: 14px;
            }
        }
    </style>
</head>
<body>
    <div class="container">
        <h2>Search for Flights</h2>
        <form action="SearchFlightServlet" method="post">
            <label for="origin">Origin:</label>
            <input type="text" name="origin" required />
            
            <label for="destination">Destination:</label>
            <input type="text" name="destination" required />
            
            <label for="departureTime">Departure Time:</label>
            <input type="date" name="departureTime" required />
            
            <button type="submit">Search Flights</button>
        </form>
    </div>
</body>
</html>