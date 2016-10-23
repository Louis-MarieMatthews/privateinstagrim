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

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.HashMap;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;
import uk.ac.dundee.computing.aec.instagrim.exception.InvalidImageTypeException;
import uk.ac.dundee.computing.aec.instagrim.exception.NoUseableSessionException;
import uk.ac.dundee.computing.aec.instagrim.exception.NullSessionException;
import uk.ac.dundee.computing.aec.instagrim.exception.UnavailableSessionException;
import uk.ac.dundee.computing.aec.instagrim.filters.ExtensionConcealmentFilter;
import uk.ac.dundee.computing.aec.instagrim.lib.Convertors;
import uk.ac.dundee.computing.aec.instagrim.models.ImageModel;
import uk.ac.dundee.computing.aec.instagrim.models.User;
import uk.ac.dundee.computing.aec.instagrim.stores.LoggedIn;
import uk.ac.dundee.computing.aec.instagrim.stores.UserImage;

/**
 * Servlet implementation class Image
 * @author Andy Cobley, Louis-Marie Matthews
 * @version 1.0.1
 */
@MultipartConfig
public class Image extends HttpServlet
{
  // TODO: remove this field?
  private static final long serialVersionUID = 1L;
  private static final int BYTE_BLOCK = 8192;
  
  
  
  /**
   * @see HttpServlet#HttpServlet()
   */
  public Image()
  {
    super();
  }
  
  
  
  @Override
  public void init(ServletConfig config)
    throws ServletException
  {
  }
  
  
  
  /**
   * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
   * response)
   */
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException
  {
    String[] args = Convertors.splitPath(request.getRequestURI());
    for ( String row : args ) {
      System.out.println( "Image#doGet(…): args[] = " + row );
    }
    if (args.length>2) {
      try {
        if ( args[1].equals( "image" ) ) {
            displayImage(Convertors.DISPLAY_PROCESSED, args[2], request, response);
        }
        else if ( args[1].equals( "images" ) ) {
            displayImageList(args[2], request, response);
          return;
        }
        else if ( args[1].equals( "thumb" ) ) {
            displayImage(Convertors.DISPLAY_THUMB, args[2], request, response);
        }
        else {
          response.sendError(500);
        }
      }
      catch (NoUseableSessionException e) {
              response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR ); 
      }
    }
    else {
      response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    }
  }
  
  
  /**
   * Forward to response to UsersImages.jsp along with the images uploaded by a
   * user UsersImages.jsp will display.
   * @param user the use whose images will be displayed
   * @param request
   * @param response
   * @throws ServletException
   * @throws IOException 
   */
  private void displayImageList(String user, HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException, NullSessionException, UnavailableSessionException
  {
    System.out.println( "Image#displayImageList(…): called with user = " + user );
    java.util.LinkedList<java.util.UUID> lsImgsUuid = ImageModel.getImagesUuidForUser(user);
    RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/images.jsp" );
    request.setAttribute( "images_uuid", lsImgsUuid );
    rd.forward( request, response );
  }
  
  
  
  /**
   * Set the response to display to given image.
   * 
   * @param type the type of the image to display (thumbnail, image, processed...)
   * @param image the image to display
   * @param response the response which will display the image
   * @throws ServletException
   * @throws IOException 
   */
  private void displayImage(int type, String image, HttpServletRequest request,
                            HttpServletResponse response)
    throws ServletException, IOException, NullSessionException, UnavailableSessionException
  {
    try {
      UserImage p = ImageModel.getUserImage(type, java.util.UUID.fromString(image));
      if ( p == null ) {
        throw new IllegalArgumentException();
      }
      OutputStream out = response.getOutputStream();
      response.setContentType(p.getType());
      response.setContentLength(p.getLength());
      //out.write(Image);
      InputStream is = new ByteArrayInputStream(p.getBytes());
      BufferedInputStream input = new BufferedInputStream(is);
      byte[] buffer = new byte[BYTE_BLOCK];
      /**
       * Hidrate the byte array buffer from the BufferedInputStram input.
       * input reads its bytes block by block, returns the size of the block 
       * it just read everytime and store what it just read in buffer.
       * While this size isn't 0 nor -1, that means the  bytes were read correctly.
       * Between each reading, the result is added to the response OutputStream.
       * See: https://docs.oracle.com/javase/7/docs/api/java/io/InputStream.html#read(byte[])
       */
      for ( int length = 0; (length = input.read(buffer)) > 0; ) {
        out.write(buffer, 0, length);
      }
      out.close();
    }
    catch(IllegalArgumentException|InvalidImageTypeException exception) {
      response.setContentType("text/html");
      RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/ImageNotFound.jsp");
      rd.forward(request, response);
    }
  }
  
  
  
  /**
   * Adds a new img to the website.
   * 
   * @param request
   * @param response
   * @throws ServletException
   * @throws IOException 
   */
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException
  {
    System.out.println( "Image#doPost( … ): called." );
    
    String title;
    if ( request.getParameter( "image_title") == null ) {
      title = null;
    }
    else {
      title = request.getParameter( "image_title");
    }
    System.out.println( "Image#doPost( … ): title = " + title );
    
    for (Part part : request.getParts()) {
      System.out.println("Image#doPost( … ): part name: " + part.getName());
      if ( ! part.getName().equals( "upfile" ) ) {
        System.out.println("Image#doPost( … ): not the file upload input" );
        continue;
      }
      
      String type = part.getContentType();
      System.out.println( "Image#doPost(…): type = " + part );
      
      String filename = part.getSubmittedFileName();

      InputStream is = request.getPart( part.getName() ).getInputStream();
      int i = is.available();
      String username = LoggedIn.getUsername(request);
      if (i > 0) {
        byte[] b = new byte[i + 1];
        is.read(b);
        System.out.println("Image#doPost(…): b.length = " + b.length);
        try {
          ImageModel.insertUserImage(b, type, filename, username, title );
          // request.setAttribute( "confirmation_message", "Your image has been uploaded sucessfully." );
          System.out.println( "Image#doPost(…): Upload successful. Forwarding…" );
          response.sendRedirect( ((HttpServletRequest)request).getContextPath() + "/images/" + LoggedIn.getUsername( request ) );
        }
        catch ( NoUseableSessionException e) {
          response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR );
        }
        catch ( IllegalArgumentException e ) {
          RequestDispatcher rd = request.getRequestDispatcher( "/upload" );
          request.setAttribute( "error_message", "The file you uploaded is not one "
            + "of the accepted types of image." );
          rd.forward(request, response);
        }
        finally {
          System.out.println( "Image#doPost(…): is.close()…" );
          is.close();
        }
      }
    }
  }
  
  
  
  private void error(String mess, HttpServletResponse response)
    throws ServletException, IOException
  {
    PrintWriter out;
    out = new PrintWriter(response.getOutputStream());
    out.println("<h1>You have a na error in your input</h1>");
    out.println("<h2>" + mess + "</h2>");
    out.close();
    return;
  }
}