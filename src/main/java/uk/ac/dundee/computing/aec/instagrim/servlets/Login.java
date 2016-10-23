/*
 * The java code follows the Java Programming Style Guidelines 7.0 from 
 * Geotechnical Software Services available at this address:
 * http://geosoft.no/development/javastyle.html .
 * Some rules are still not applied yet.
 * However, some rules won't be followed:
 * 1. No underscore suffix at the end of private variables (r8)
 * 2. No space between a function and its parenthesis (r74). Instead, parenthesis
 * may be wrapped around space. So function ( parameter ) instead of 
 * function (parameter).
 * 4. Abbreviations and the use of init is okay (r17, r24)
 * 5. Statements and variable declarations don't need to be aligned (r77, r78)
 * 6. Class names don't have to be nouns (would make some class' names long and
 * poorly representative for servlets and filters).
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
import uk.ac.dundee.computing.aec.instagrim.exception.NoUseableSessionException;
import uk.ac.dundee.computing.aec.instagrim.models.User;
import uk.ac.dundee.computing.aec.instagrim.stores.LoggedIn;

/**
 *
 * @author Andy Cobley, Louis-Marie Matthews
 * @version 1.0.1
 */
public class Login extends HttpServlet
{
  public void init(ServletConfig config)
    throws ServletException
  {
    // TODO Auto-generated method stub
  }
  
  
  
  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException
  {
    System.out.println("Login.doGet(…) called.");
    request.setAttribute( "already_treated", true );
    RequestDispatcher rd = request.getRequestDispatcher( "/login.jsp" );
    rd.include(request, response);
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
    if ( request.getAttribute( "login_dontdopost") != null ) {
      doGet( request, response);
      return;
    }
    request.setAttribute( "already_treated", true );
    String username = request.getParameter("username");
    String password = request.getParameter("password");
    
    HttpSession session = request.getSession();
    System.out.println("Session in servlet " + session);
    try {
      if (User.isValidUser(username, password)) {
        LoggedIn lg = new LoggedIn(true, username);
        //request.setAttribute("LoggedIn", lg);

        session.setAttribute("LoggedIn", lg);
        System.out.println("Session in servlet " + session);
        response.sendRedirect( "/Instagrim/" );
      }
      else { // if the entered details are not correct
        RequestDispatcher rd = request.getRequestDispatcher("/login/");
        request.setAttribute("details_error", "The entered details are incorrect.");
        request.setAttribute( "login_dontdopost", true);
        rd.forward(request, response);
      }
    }
    catch (NoUseableSessionException e) {
      response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR );
      return;
    }
  }
}