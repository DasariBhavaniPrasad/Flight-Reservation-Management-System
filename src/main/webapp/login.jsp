<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Login</title>
    <link rel="stylesheet" href="login.css">
</head>
<body>
    <!-- Background Video -->
<video autoplay loop muted playsinline class="bg-video">
    <source src="media/flight.mp4" type="video/mp4">


    Your browser does not support the video tag.
</video>


    <!-- Login Form Container -->
    <div class="container">
        <div class="login-form">
            <h2 style="font-size: 40px;">Login</h2>
            <form action="Login" method="POST">
                <label for="username" style="font-size: 20px; color: black; font-weight: bold;">Username:</label>
                <input type="text" id="username" name="username" required>

                <label for="password" style="font-size: 20px; color: black; font-weight: bold;">Password:</label>
                <input type="password" id="password" name="password" required>

                <input type="submit" value="Login">
            </form>

            <div class="register-link">
                <p>Don't have an account? <a href="register.jsp">Register Here</a></p>
            </div>
        </div>
    </div>
</body>
</html>