package com.rms;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;

@WebServlet("/ConfirmSelectionServlet")
public class ConfirmSelectionServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String[] selectedSeats = request.getParameterValues("selectedSeats");
        BigDecimal totalAmount = BigDecimal.ZERO;

        if (selectedSeats != null) {
            ArrayList<String> seatList = new ArrayList<>();
            for (String seat : selectedSeats) {
                seatList.add(seat);
                // Retrieve the price from the request using the correct parameter name
                String priceParam = request.getParameter("price_" + seat); // Get price using seat number
                if (priceParam != null) {
                    BigDecimal price = new BigDecimal(priceParam); // Convert to BigDecimal
                    totalAmount = totalAmount.add(price); // Add to total amount
                }
            }

            // Store selected seats and total amount in session
            HttpSession session = request.getSession();
            session.setAttribute("selectedSeats", seatList.toArray(new String[0]));
            session.setAttribute("totalAmount", totalAmount);

            // Logging for debugging
            System.out.println("Selected Seats: " + Arrays.toString(seatList.toArray(new String[0])));
            System.out.println("Total Amount: " + totalAmount);

            // Retrieve userId and flightId
            String userIdParam = request.getParameter("userId");
            String flightIdParam = request.getParameter("flightId");
            
            if (userIdParam != null && flightIdParam != null) {
                session.setAttribute("userId", Integer.parseInt(userIdParam));  // Convert to Integer
                session.setAttribute("flightId", Integer.parseInt(flightIdParam)); // Convert to Integer
                System.out.println("User ID: " + userIdParam);
                System.out.println("Flight ID: " + flightIdParam);
            }

            // Forward to the Payment page
            request.getRequestDispatcher("Payment.jsp").forward(request, response);
        } else {
            request.setAttribute("errorMessage", "No seats selected.");
            request.getRequestDispatcher("error.jsp").forward(request, response);
        }
    }
}
