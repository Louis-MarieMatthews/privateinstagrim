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
import java.util.UUID;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import uk.ac.dundee.computing.aec.instagrim.exception.InvalidImageTypeException;
import uk.ac.dundee.computing.aec.instagrim.exception.NoUseableSessionException;
import uk.ac.dundee.computing.aec.instagrim.lib.Convertors;
import uk.ac.dundee.computing.aec.instagrim.models.ImageModel;
import uk.ac.dundee.computing.aec.instagrim.models.User;
import uk.ac.dundee.computing.aec.instagrim.stores.LoggedIn;
import uk.ac.dundee.computing.aec.instagrim.stores.UserImage;

/**
 * Allows users to see users' profiles and edit theirs.
 * 
 * @author Louis-Marie Matthews
 * @version 1.0
 */
public class Profile extends HttpServlet
{
  private String[] params;
  private String username;
    
  
  
  @Override
  public void init(ServletConfig config)
    throws ServletException
  {
    params = null;
  }
  
  
  
  @Override
  public void destroy() {
    params = null;
  }
  
  
  
  @Override
  public void doGet( HttpServletRequest request, HttpServletResponse response )
    throws IOException, ServletException
  {
    System.out.println( "#Profile.doGet(…): called" );
    
    checkRequest( request, response );
    
    request.setAttribute( "username", username );
    RequestDispatcher rd = request.getRequestDispatcher( "/WEB-INF/profile.jsp" );
    rd.forward( request, response );
  }



  /**
   * Check that the request is valid (has the correct number of parameters), 
   * that the image exists and that the user is its owner.
   * 
   * @param request
   * @param response
   * @throws IOException
   * @throws ServletException 
   */
  public void checkRequest( HttpServletRequest request,
                            HttpServletResponse response )
    throws IOException, ServletException
  {
    System.out.println( "Profile#checkRequest(…): called" );
    params = Convertors.splitPath( request.getRequestURI() );
    for ( String c : params ) {
      System.out.println( "Profile#checkRequest(…): parameter : " + c );
    }
    
    // if url is not like something/something/something
    if ( params.length != 3 ) {
      System.out.println( "Profile#checkRequest(…): invalid number of parameters" );
      RequestDispatcher rd = request.getRequestDispatcher( "/" );
      rd.forward(request, response);
    }
    
    username = params[2];
    try {
      if ( User.exists( username ) ) {
        System.out.println( "Profile#checkRequest(…): " + username + " exists");
      } else {
        System.out.println( "Profile#checkRequest(…): no user with such username (" + username + ")" );
        RequestDispatcher rd = request.getRequestDispatcher( "/" );
        rd.forward(request, response);
      }
    }
    catch ( NoUseableSessionException e ) {
      System.out.println( "Profile#checkRequest(…): " + e );
      RequestDispatcher rd = request.getRequestDispatcher( "/" );
      rd.forward(request, response);
    }
  }
}
