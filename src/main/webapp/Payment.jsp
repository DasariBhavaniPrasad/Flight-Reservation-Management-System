<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Payment</title>
   <style>
        body {
            font-family: Arial, sans-serif;
            background: url('media/image1.jpg') no-repeat center center fixed; /* Replace with actual image path */
            background-size: cover;
            margin: 0;
            padding: 0;
            height: 100vh;
            display: flex;
            justify-content: center;
            align-items: center;
        }

        .container {
            max-width: 600px;
            background-color: rgba(255, 255, 255, 0); /* Slightly transparent background */
            padding: 30px;
            border-radius: 8px;
            box-shadow: 0 4px 15px rgba(0, 0, 0, 0.2);
            text-align: center;
            margin-left:1200px;
        }

        h1, h2, h3 {
            color: #333;
            margin-bottom: 20px;
        }

        /* Flexbox for label and input fields */
        .form-group {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 15px;
        }

        .form-group label {
            flex: 0 0 40%; /* Adjust label width */
            text-align: left;
            font-size: 18px;
            color: white;
            font-family:bold;
        }

        .form-group input {
            flex: 1;
            padding: 10px;
            border: 1px solid #ccc;
            border-radius: 4px;
            font-size: 16px;
        }

        input[type="submit"] {
            width: 100%;
            background-color: #4CAF50;
            color: white;
            padding: 12px 20px;
            border: none;
            border-radius: 4px;
            cursor: pointer;
            font-size: 16px;
        }

        input[type="submit"]:hover {
            background-color: #45a049;
        }

        ul {
            list-style: none;
            padding: 0;
        }

        li {
            background-color: #f2f2f2;
            padding: 8px;
            margin: 5px 0;
            border-radius: 4px;
            text-align: center;
        }

        /* Responsive Design for smaller screens */
        @media (max-width: 768px) {
            .form-group {
                flex-direction: column;
                align-items: flex-start;
            }

            .form-group label {
                margin-bottom: 5px;
            }

            .container {
                padding: 20px;
            }
        }
   </style>
</head>
<body>
    <div class="container">
        <h1 style="color:green;">Payment Details</h1>
        <form action="PaymentServlet" method="post">
            <div class="form-group">
                <label for="cardNumber">Card Number:</label>
                <input type="text" id="cardNumber" name="cardNumber" required>
            </div>
            
            <div class="form-group">
                <label for="expiryDate">Expiry Date (MM/YY):</label>
                <input type="text" id="expiryDate" name="expiryDate" placeholder="MM/YY" required>
            </div>
            
            <div class="form-group">
                <label for="cvv">CVV:</label>
                <input type="number" id="cvv" name="cvv" required>
            </div>
            
            <h2 style=" color:white;">Selected Seats:</h2>
            <ul>
                <%
                    String[] selectedSeats = (String[]) request.getAttribute("selectedSeats");
                    if (selectedSeats != null) {
                        for (String seat : selectedSeats) {
                            out.println("<li>" + seat + "</li>");
                        }
                    }
                %>
            </ul>
            
            <h3 style=" color:white;">Total Amount: $<%= request.getAttribute("totalAmount") %></h3>
            <input type="submit" value="Pay Now">
        </form>
    </div>
</body>
</html>
