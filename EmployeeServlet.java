package com.example.servlet;

import java.io.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class EmployeeServlet extends HttpServlet {

    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/companydb";
    private static final String JDBC_USER = "root";
    private static final String JDBC_PASSWORD = "password";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        String empIdParam = request.getParameter("empId");

        out.println("<html><head><title>Employee Records</title></head><body>");
        out.println("<h2>Employee Records</h2>");
        out.println("<form method='get' action='EmployeeServlet'>");
        out.println("Search by Employee ID: <input type='text' name='empId'>");
        out.println("<input type='submit' value='Search'>");
        out.println("</form><br>");

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
            Statement stmt = conn.createStatement();
            ResultSet rs;

            if (empIdParam != null && !empIdParam.isEmpty()) {
                rs = stmt.executeQuery("SELECT * FROM Employee WHERE EmpID=" + empIdParam);
            } else {
                rs = stmt.executeQuery("SELECT * FROM Employee");
            }

            out.println("<table border='1' cellpadding='10'>");
            out.println("<tr><th>EmpID</th><th>Name</th><th>Salary</th></tr>");

            boolean found = false;
            while (rs.next()) {
                found = true;
                out.println("<tr>");
                out.println("<td>" + rs.getInt("EmpID") + "</td>");
                out.println("<td>" + rs.getString("Name") + "</td>");
                out.println("<td>" + rs.getDouble("Salary") + "</td>");
                out.println("</tr>");
            }

            if (!found) {
                out.println("<tr><td colspan='3'>No record found</td></tr>");
            }

            out.println("</table>");
            conn.close();

        } catch (Exception e) {
            out.println("<p style='color:red;'>Error: " + e.getMessage() + "</p>");
            e.printStackTrace(out);
        }

        out.println("</body></html>");
    }
}
