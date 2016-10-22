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
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import uk.ac.dundee.computing.aec.instagrim.exception.NoUseableSessionException;
import uk.ac.dundee.computing.aec.instagrim.exception.WrongLoginDetailsException;
import uk.ac.dundee.computing.aec.instagrim.models.User;
import uk.ac.dundee.computing.aec.instagrim.stores.LoggedIn;

/**
 * Servlet to process account deletion requests.
 * 
 * @author Louis-Marie Matthews
 * @version 1.0
 */
public class DeleteAccount extends HttpServlet
{
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException
  {
    request.getRequestDispatcher("/delete-account.jsp").forward(request, response);
  }
  
  
  
  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response)
    throws IOException, ServletException
  {
    System.out.println("DeleteAccount.doFilter(): called.");
    RequestDispatcher rd = request.getRequestDispatcher("/");
    String username = request.getParameter("username");
    String password = request.getParameter("password");
    System.out.println( "DeleteAccount.doPost(…): username = " + username );
    System.out.println( "DeleteAccount.doPost(…): password = " + password );
    try {
      User.delete(username, password);
      LoggedIn.logOut(request);
      request.setAttribute("confirmation_message", "Your account has been "
        + "deleted.");
      System.out.println( "DeleteAccount.doPost(…): try block completely done." );
    }
    catch ( NoUseableSessionException e ) {
      request.setAttribute("error_message", "Sorry, for technical reasons, we"
        + " were not able to delete your account. ");
    }
    catch ( WrongLoginDetailsException e ) {
      rd = request.getRequestDispatcher("/delete-account.jsp");
        request.setAttribute("error_message", "The entered details are incorrect.");
    }
    catch ( Throwable t ) {
      System.out.println( "DeleteAccount.doPost(…): " + t );
    }
    finally {
        rd.forward(request, response);
    }
  }
}
