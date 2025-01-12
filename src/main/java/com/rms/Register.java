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
import java.sql.Timestamp;
import java.util.List;

@WebServlet("/Register")
public class Register extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public Register() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Connection connection;
        PreparedStatement preparedStatement;
      

        String uname = request.getParameter("username");
        String pwd = request.getParameter("password");
        String email = request.getParameter("email");
        String fname = request.getParameter("firstname");
        String lname = request.getParameter("lastname");
        String pno = request.getParameter("phonenumber");
        String role = request.getParameter("role"); // Get the role from the request

        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/reservation", "root", "Bl@091602");

            preparedStatement = connection.prepareStatement(
                "INSERT INTO users (username, password, email, first_name, last_name, phone_number, role, created_at, updated_at) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)"
            );
            preparedStatement.setString(1, uname);
            preparedStatement.setString(2, pwd);
            preparedStatement.setString(3, email);
            preparedStatement.setString(4, fname);
            preparedStatement.setString(5, lname);
            preparedStatement.setString(6, pno);
            preparedStatement.setString(7, role); // Set the role
            preparedStatement.setTimestamp(8, new Timestamp(System.currentTimeMillis()));
            preparedStatement.setTimestamp(9, new Timestamp(System.currentTimeMillis()));

            int rowsInserted = preparedStatement.executeUpdate();

            response.setContentType("text/html");
            PrintWriter out = response.getWriter();

            if (rowsInserted > 0) {
            	response.sendRedirect("login1.jsp");
            } else {
                out.println("<h3>Something went wrong! Please try again.</h3>");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            response.getWriter().println("<h3>Database error occurred.</h3>");
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
