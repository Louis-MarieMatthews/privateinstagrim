/*
 * The java code follows the Java Programming Style Guidelines 7.0 from 
 * Geotechnical Software Services available at this address:
 * http://geosoft.no/development/javastyle.html .
 * Some rules are still not applied yet.
 * However, some rules won't be followed:
 * 1. No underscore suffix at the end of private variables (r8)
 * 2. No space between a function and its parenthesis (r74)
 * 3. Class and package names (can't be changed, lead to problems) (r3)
 * 4. Abbreviations and the use of init is okay (r17, r24)
 * 5. Statements and variable declarations don't need to be aligned (r77, r78)
 */
package uk.ac.dundee.computing.aec.instagrim.servlets;

import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import uk.ac.dundee.computing.aec.instagrim.lib.CassandraHosts;
import uk.ac.dundee.computing.aec.instagrim.models.User;
import uk.ac.dundee.computing.aec.instagrim.stores.LoggedIn;

/**
 *
 * @author Andy Cobley, Louis-Marie Matthews
 * @version 1.0.1
 */
@WebServlet(
  name = "Login",
  urlPatterns = {
    "/Login",
    "/Login/*"
  }
)
public class Login extends HttpServlet
{
  
  
  
  public void init(ServletConfig config)
    throws ServletException
  {
    // TODO Auto-generated method stub
  }
  
  
  
  /**
   * Handles the HTTP <code>POST</code> method.
   *
   * @param request servlet request
   * @param response servlet response
   * @throws ServletException if a servlet-specific error occurs
   * @throws IOException if an I/O error occurs
   */
  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException
  {
    String username = request.getParameter("username");
    String password = request.getParameter("password");

    User us = new User();
    boolean isValid = us.isValidUser(username, password);
    HttpSession session = request.getSession();
    System.out.println("Session in servlet " + session);
    if (isValid) {
      LoggedIn lg = new LoggedIn();
      lg.setLoggedIn();
      lg.setUsername(username);
      //request.setAttribute("LoggedIn", lg);

      session.setAttribute("LoggedIn", lg);
      System.out.println("Session in servlet " + session);
      RequestDispatcher rd = request.getRequestDispatcher("index.jsp");
      rd.forward(request, response);

    }
    else {
      response.sendRedirect("/Instagrim/login.jsp");
    }

  }
  
  
  
  /**
   * Returns a short description of the servlet.
   *
   * @return a String containing servlet description
   */
  @Override
  public String getServletInfo()
  {
    return "Short description";
  }// </editor-fold>
}