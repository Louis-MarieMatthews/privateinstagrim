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
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import uk.ac.dundee.computing.aec.instagrim.exception.InsufficientPermissionsException;
import uk.ac.dundee.computing.aec.instagrim.exception.InvalidImageTypeException;
import uk.ac.dundee.computing.aec.instagrim.exception.NoUseableSessionException;
import uk.ac.dundee.computing.aec.instagrim.exception.NullSessionException;
import uk.ac.dundee.computing.aec.instagrim.exception.UnavailableSessionException;
import uk.ac.dundee.computing.aec.instagrim.lib.Convertors;
import uk.ac.dundee.computing.aec.instagrim.models.ImageModel;
import uk.ac.dundee.computing.aec.instagrim.stores.LoggedIn;
import uk.ac.dundee.computing.aec.instagrim.stores.UserImage;

/**
 * Allows users to delete any of their image or edit its details.
 * 
 * @author Louis-Marie Matthews
 * @version 1.0
 */
public class EditImage extends HttpServlet
{
  private UUID uuid;
  private String[] params;
  
  
  
  @Override
  public void init(ServletConfig config)
    throws ServletException
  {
    uuid = null;
    params = null;
  }
  
  
  
  @Override
  public void destroy() {
    uuid = null;
    params = null;
  }
  
  
  
  @Override
  public void doGet( HttpServletRequest request, HttpServletResponse response )
    throws IOException, ServletException
  {
    System.out.println( "EditImage#doGet(…): called" );
    
    checkRequest( request, response );
    
    request.setAttribute( "uuid", uuid );
    RequestDispatcher rd = request.getRequestDispatcher( "/WEB-INF/edit-image.jsp" );
    rd.forward( request, response );
  }
  
  
  
  @Override
  public void doPost( HttpServletRequest request, HttpServletResponse response )
    throws IOException, ServletException
  {
    System.out.println( "EditImage#doPost(…): called" );
    
    checkRequest( request, response );
    
    
    if ( params[1].equals( "delete-image" ) ) {
      try {
        ImageModel.delete( uuid, LoggedIn.getUsername( request ) );
        request.setAttribute( "confirmation_message", "Your image has been deleted." );
        RequestDispatcher rd = request.getRequestDispatcher( "/" );
        rd.forward(request, response);
      }
      catch (NoUseableSessionException | InsufficientPermissionsException ex) {
        System.out.println( "EditImage#doPost(…): " + ex );
        // prevent the user from knowing too much about the system.
        response.sendError( HttpServletResponse.SC_INTERNAL_SERVER_ERROR );
      }
    }
    else {
      String title = request.getParameter( "title" );
      try {
        ImageModel.rename( uuid, title, LoggedIn.getUsername(request) );
      }
      catch (NoUseableSessionException | InsufficientPermissionsException ex) {
        System.out.println( "EditImage#doPost(…): " + ex );
        // prevent the user from knowing too much about the system.
        response.sendError( HttpServletResponse.SC_INTERNAL_SERVER_ERROR );
      }
      doGet( request, response );
    }
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
    params = Convertors.splitPath( request.getRequestURI() );
    for ( String c : params ) {
      System.out.println( "EditImage#doGet(…): parameter : " + c );
    }
    
    // if url is not like context/edit-image/uuid
    if ( params.length != 3 ) {
      System.out.println( "EditImage#doGet(…): invalid number of parameters" );
      response.sendError( HttpServletResponse.SC_NOT_FOUND );
      return;
    }
    
    try {
      uuid = UUID.fromString( params[2] );
    }
    catch( IllegalArgumentException e ) {
      System.out.println( "EditImage#doGet(…): The requested image does not exist." );
      RequestDispatcher rd = request.getRequestDispatcher( "WEB-INF/ImageNotFound.jsp" );
      rd.forward(request, response);
    }
    
    UserImage image = null;
    try {
      image = ImageModel.getUserImage( 0, uuid );
    }
    catch ( InvalidImageTypeException | NoUseableSessionException ex ) {
      System.out.println( "EditImage#doGet(…): " + ex );
      response.sendError( HttpServletResponse.SC_INTERNAL_SERVER_ERROR );
      return;
    }
    
    if ( image != null ) {
      System.out.println( "EditImage#doGet(…): The requested image exists." );
    } else {
      System.out.println( "EditImage#doGet(…): THE REQUESTED IMAGE DOES NOT EXIST." );
      RequestDispatcher rd = request.getRequestDispatcher( "/WEB-INF/ImageNotFound.jsp" );
      rd.forward(request, response);
    }
    
    boolean isOwner = false;
    try {
      isOwner = ImageModel.isOwner( uuid, LoggedIn.getUsername( request ) );
    }
    catch ( Throwable t ) {
      System.out.println( "EditImage#doGet(…): " + t );
      response.sendError( HttpServletResponse.SC_INTERNAL_SERVER_ERROR );
      return;
    }
    
    if ( isOwner ) {
      System.out.println( "EditImage#doGet(…): The requested image "
        + "belongs to the currently logged-in user." );
    }
    else {
      System.out.println( "EditImage#doGet(…): NOT THE OWNER OF THE IMAGE." );
      RequestDispatcher rd = request.getRequestDispatcher( "/WEB-INF/ImageNotFound.jsp" );
      rd.forward(request, response);
    }
  }
}