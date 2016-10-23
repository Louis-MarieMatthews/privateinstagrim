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
package uk.ac.dundee.computing.aec.instagrim.filters;

import java.io.File;
import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Louis-Marie Matthews
 * @version 1.0
 */
public class ExtensionConcealmentFilter
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
    System.out.println( "ExtensionConcealmentFilter#doFilter(…) : called." );
    
    String url = ((HttpServletRequest)request).getRequestURI();
    System.out.println( "ExtensionConcealmentFilter#doFilter(…) : url = " + url );
    
    
    if ( needsToBeManagedByServlet ( request ) ) {
      System.out.println( "ExtensionConcealmentFilter#doFilter(…) : Request needs to be managed by a servlet." );
      chain.doFilter(request, response);
      return;
    } else {
      System.out.println( "ExtensionConcealmentFilter#doFilter(…) : THE REQUEST DOES NOT NEED TO BE MANAGED BY A SERVLET." );
    }
    
    if ( nonJspFileExists ( request ) ) {
      if ( url.substring( url.length() - 4).equals( ".jsp" ) ) {
        System.out.println( "ExtensionConcealmentFilter#doFilter(…) : This request is a non-valid request for a jsp file." );
        ((HttpServletResponse)response).sendError( 404 );
      }
      else {
        System.out.println( "ExtensionConcealmentFilter#doFilter(…) : This request is not for a jsp file." );
        chain.doFilter( request, response );
      }
      return;
    } else {
      System.out.println( "ExtensionConcealmentFilter#doFilter(…) : THIS REQUEST IS NOT A NON-JSP FILE." );
    }
    System.out.println( "ExtensionConcealmentFilter#doFilter(…) : URI to forward to : " + getUri( request ) + ".jsp" );
    request.getRequestDispatcher( getUri( request ) + ".jsp" ).forward( request, response );
  }

  @Override
  public void destroy()
  {
  }
  
  private static String[] getAllServletUrlPatterns() {
    String[] getAllServletUrlPatterns = {
      "/delete-account",
      "/image/.*",
      "/thumb/.*",
      "/images/.*",
      "/login",
      "/login/*",
      "/register"
    };
    return getAllServletUrlPatterns;
  }
  
  private static String[] getAllFilterUrlPatterns() {
    String[] getAllFilterUrlPatterns = {
      "/log-out",
      "/upload",
      "/register",
      "/login",
      "/login/*",
      "/log-out",
      "/delete-account"
    };
    return getAllFilterUrlPatterns;
  }
  
  
  
  private static boolean needsToBeManagedByServlet( ServletRequest request )
  {
    boolean notYetTreated;
    Object url = request.getAttribute( "already_treated" );
    if ( url == null ) {
      notYetTreated = true;
    } else {
      notYetTreated = false;
    }
    
    String uri = ((HttpServletRequest)request).getRequestURI();
    boolean uriIsServletPattern = isPatternInArray ( getAllServletUrlPatterns(), uri );
    System.out.println( "ExtensionConcealmentFilter#needsToBeManagedByServletOrFilter(…): uriIsServletPattern: " + uriIsServletPattern );
    System.out.println( "ExtensionConcealmentFilter#needsToBeManagedByServletOrFilter(…): notYetTreated: " + notYetTreated );
    
    return notYetTreated & uriIsServletPattern; // | uriIsFilterPattern );
  }
  
  
  
  private static boolean needsToBeManagedByFilter( String url )
  {
    return isPatternInArray ( getAllFilterUrlPatterns(), url );
  }
  
  
  
  
  
  private static boolean isPatternInArray( String[] array, String pattern ) {
    boolean matchFound = false;
    if ( pattern.charAt(0) == '/' ) {
      pattern = pattern.substring(1, pattern.length());
    }
    for (String row : array) {
      String regex = "(?:(\\/*)Instagrim)?" + row + "(:?\\/)?";
      if ( pattern.matches( regex ) ) {
        matchFound = true;
        // System.out.println( "ExtensionConcealmentFilter#isPatternInArray: " + pattern + " matches " + row + "." );
      } else {
        // System.out.println( "ExtensionConcealmentFilter#isPatternInArray: " + pattern + " DOES NOT MATCH " + row + "." );
      }
    }
    return matchFound;
  }
  
  
  private static boolean nonJspFileExists( ServletRequest request ) {
    String uri = getUri ( request );
    System.out.println( "ExtensionConcealmentFilter#nonJspFileExists: Uri = " + uri );
    File f = new File( request.getServletContext().getRealPath( uri )  + ".jsp" );
    if ( f.exists() & f.isFile() ) {
      return false;
    }
    return true;
  }
  
  public static String getUri( ServletRequest request ) {
    HttpServletRequest hr = ((HttpServletRequest) request);
    String uri = hr.getRequestURI().substring(hr.getContextPath().length());
    return uri;
  }
}