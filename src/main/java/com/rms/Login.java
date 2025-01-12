package com.rms;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@WebServlet("/Login")
public class Login extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public Login() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Connection connection;
        PreparedStatement preparedStatement;
        ResultSet resultset;
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/reservation", "root", "Bl@091602");

            preparedStatement = connection.prepareStatement("SELECT * FROM users WHERE username = ? AND password = ?");
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            resultset = preparedStatement.executeQuery();

            response.setContentType("text/html");
            PrintWriter out = response.getWriter();
            if (resultset.next()) {
                int userId = resultset.getInt("user_id"); // Fetch user_id from the result set
                String role = resultset.getString("role"); // Get the role from the result set
                
                HttpSession session = request.getSession();
                session.setAttribute("userId", userId); // Store userId in the session
                session.setAttribute("username", username);
                session.setAttribute("role", role); // Store the role in the session

                // Redirect based on role
                if ("admin".equals(role)) {
                    response.sendRedirect("admin.html"); // Redirect to admin panel
                } else {
                    response.sendRedirect("ManageBookings.jsp"); // Redirect to user dashboard
                }
            } else {
                out.println("<h3>Invalid credentials. Please try again.</h3>");
            }
        } catch (SQLException e) {
            response.getWriter().append("DB is Not Connected");
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
