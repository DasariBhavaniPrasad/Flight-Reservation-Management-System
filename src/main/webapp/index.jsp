<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Flight Reservation System</title>
    <link rel="stylesheet" href="index.css"> <!-- Link to the CSS file -->
    <style>
        /* CSS for the full-screen image slider */
        body, html {
            margin: 0;
            padding: 0;
            height: 100%;
            overflow: hidden;
        }

        .slider {
            position: absolute;
            top: 0;
            left: 0;
            width: 100%;
            height: 100%;
            overflow: hidden;
        }

        .slides {
            display: flex;
            height: 100%;
            transition: transform 1s ease;
        }

        .slide {
            min-width: 100%;
            height: 100%;
            box-sizing: border-box;
        }

        .slide img {
            width: 100%;
            height: 100%;
            object-fit: cover; /* Ensures the image covers the entire slide */
        }

        .content {
            position: relative;
            z-index: 2; /* Ensure content appears above the slider */
            text-align: center;
            padding: 20px;
            color: white; /* Change text color to white for visibility */
        }

        .prev, .next {
            position: absolute;
            /* top: 50%; */
            width: auto;
            padding: 16px;
            
            color: white;
            font-weight: bold;
            font-size: 18px;
            transition: 0.6s ease;
            border-radius: 0 3px 3px 0;
            user-select: none;
            border: 100px white;
            z-index: 3; /* Ensure buttons are above the slider */
        }

        .next {
            right: 0;
            border-radius: 3px 0 0 3px;
        }

        .prev:hover, .next:hover {
            background-color: rgba(0,0,0,0.8);
        }
    </style>
</head>
<body>
    
    <!-- Main Content -->
    <div class="content">
        <!-- <h1>Welcome to the Flight Reservation System</h1> -->
        <a href="http://localhost:8080/ReservationManagement/login.jsp" class="btn">Login</a> &nbsp &nbsp;
        <a href="http://localhost:8080/ReservationManagement/register.jsp" class="btn">Register</a>   
    </div>

    <!-- Full-Screen Image Slider Section -->
    <div class="slider">
        <div class="slides">
            <div class="slide"><img src="media/image1.jpg" alt="Image 1"></div>
            <div class="slide"><img src="media/image10.jpg" alt="Image 2"></div>
            <div class="slide"><img src="media/image2.jpg" alt="Image 3"></div>
            <div class="slide"><img src="media/image11.webp" alt="Image 4"></div>
            <div class="slide"><img src="media/image6.jpg" alt="Image 5"></div>
        </div>
        <a class="prev" onclick="changeSlide(-1)">&#10094;</a>
        <a class="next" onclick="changeSlide(1)">&#10095;</a>
    </div>

    <script>
        let slideIndex = 0;
        showSlides();

        function showSlides() {
            const slides = document.getElementsByClassName("slide");
            for (let i = 0; i < slides.length; i++) {
                slides[i].style.display = "none";  
            }
            slideIndex++;
            if (slideIndex >= slides.length) {slideIndex = 0}    
            slides[slideIndex].style.display = "block";  
            setTimeout(showSlides, 3000); // Change image every 3 seconds
        }

        function changeSlide(n) {
            slideIndex += n;
            if (slideIndex < 0) slideIndex = slides.length - 1; // Loop back to the last slide
            if (slideIndex >= slides.length) slideIndex = 0; // Loop back to the first slide
            showSlides();
        }
    </script>
</body>
</html>