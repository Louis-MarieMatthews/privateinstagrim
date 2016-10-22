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
package uk.ac.dundee.computing.aec.instagrim.filters;

import java.io.IOException;
import javax.servlet.DispatcherType;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import uk.ac.dundee.computing.aec.instagrim.stores.LoggedIn;

/**
 * Filter used to log out the user.
 * 
 * @author Louis-Marie Matthews
 * @version 1.0
 */
public class LogOut
  implements Filter
{
  @Override
  public void init(FilterConfig filterConfig)
    throws ServletException
  {
  }
  
  
  
  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
    throws IOException, ServletException
  {
    System.out.println( "LogOut.doGet(…): Method called." );
    LoggedIn.logOut(request);
    try {
      ((HttpServletResponse)response).sendRedirect("/Instagrim/index.jsp");
    } catch ( Throwable t ) {
      System.out.println( "LogOut.doGet(…): " + t );
    }
  }
  
  
  
  @Override
  public void destroy()
  {
  }
}
