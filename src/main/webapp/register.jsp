<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Register</title>
    <style>
        body, html {
            margin: 0;
            padding: 0;
            font-family: Arial, sans-serif;
            height: 100%;
            overflow: hidden;
        }

        /* Full-screen video container */
        .video-container {
            position: fixed;
            top: 0;
            left: 0;
            width: 100%;
            height: 100%;
            z-index: -1; /* Behind everything */
            overflow: hidden;
        }

        .video-container video {
            min-width: 100%;
            min-height: 100%;
            width: auto;
            height: auto;
            position: absolute;
            top: 50%;
            left: 50%;
            transform: translate(-50%, -50%);
            object-fit: cover;
            z-index: -1;
        }

        .container {
            width: 350px;
            background-color: rgba(255, 255, 255, 0); /* Slightly transparent white background */
            padding: 20px;
            border-radius: 8px;
            box-shadow: 0 4px 15px rgba(0, 0, 0, 0.2);
            text-align: center;
            position: relative;
            z-index: 1;
            margin: auto;
            margin-left:1250px;
            margin-top:50px;
        }

        h2 {
            color: #333;
            margin-bottom: 20px;
        }

        input[type="text"], input[type="password"], input[type="email"], select {
            width: 100%;
            padding: 10px;
            margin: 10px 0;
            border: 1px solid #ccc;
            border-radius: 3px;
            font-size: 14px;
        }

        input[type="submit"] {
            width: 100%;
            padding: 12px;
            background-color: #4CAF50;
            color: white;
            border: none;
            border-radius: 3px;
            cursor: pointer;
            font-size: 16px;
            margin-top: 10px;
        }

        input[type="submit"]:hover {
            background-color: #45a049;
        }

        a {
            color: #333;
            text-decoration: none;
        }

        a:hover {
            text-decoration: underline;
        }

        .login-link {
            margin-top: 20px;
        }

        /* Responsive Design for smaller screens */
        @media (max-width: 768px) {
            .container {
                width: 90%;
                padding: 15px;
            }
        }
    </style>
</head>
<body>
    <!-- Background Video -->
    <div class="video-container">
        <video autoplay muted loop>
            <source src="media/flight.mp4" type="video/mp4">
            Your browser does not support HTML5 video.
        </video>
    </div>

    <div class="container">
        <h2>Register</h2>
        <form action="Register" method="post">
            <label for="username">Username:</label><br>
            <input type="text" id="username" name="username" required><br>

            <label for="password">Password:</label><br>
            <input type="password" id="password" name="password" required><br>

            <label for="email">Email:</label><br>
            <input type="email" id="email" name="email" required><br>

            <label for="firstname">First Name:</label><br>
            <input type="text" id="firstname" name="firstname"><br>

            <label for="lastname">Last Name:</label><br>
            <input type="text" id="lastname" name="lastname"><br>

            <label for="phonenumber">Phone Number:</label><br>
            <input type="text" id="phonenumber" name="phonenumber"><br>

            <label for="role">Role:</label><br>
            <select id="role" name="role">
                <option value="customer">Customer</option>
            </select><br>

            <input type="submit" value="Register">
        </form>
        <div class="login-link">
            <a href="login.jsp">Already have an account? Login here.</a>
        </div>
    </div>
</body>
</html>
