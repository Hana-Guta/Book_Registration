package com.itsc;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {

    private static final String query = "INSERT INTO books(bookname, bookedition, bookprice) VALUES (?, ?, ?)";
    private static final String createTableQuery = "CREATE TABLE IF NOT EXISTS books (id INT PRIMARY KEY AUTO_INCREMENT, bookname VARCHAR(255), bookedition VARCHAR(255), bookprice DOUBLE)";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        PrintWriter pw = null;
        resp.setContentType("text/html");

        String bookName = req.getParameter("bookName");
        String bookEdition = req.getParameter("bookEdition");
        float bookPrice = Float.parseFloat(req.getParameter("bookPrice"));

        try {
            pw = resp.getWriter();
            Class.forName("com.mysql.cj.jdbc.Driver");

            Connection conn = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/bookregister",
                    "root",
                    "root");

            // Create the 'books' table if it doesn't exist
            createTableIfNotExists(conn);

            PreparedStatement ps = conn.prepareStatement(query);

            ps.setString(1, bookName);
            ps.setString(2, bookEdition);
            ps.setFloat(3, bookPrice);

            int count = ps.executeUpdate();

            if (count == 1) {
                pw.println("<h2>Book Registered Successfully.</h2>");
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            pw.println("<h1>" + e.getMessage() + "</h1>");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
            pw.println("<h1>" + e.getMessage() + "</h1>");
        } finally {
            if (pw != null) {
                pw.close();
            }
        }
    }

    private void createTableIfNotExists(Connection connection) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(createTableQuery)) {
            statement.executeUpdate();
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        doGet(req, resp);
    }
}
