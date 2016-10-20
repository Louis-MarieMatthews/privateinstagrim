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
import uk.ac.dundee.computing.aec.instagrim.exception.NoDatabaseConnectionException;
import uk.ac.dundee.computing.aec.instagrim.lib.Convertors;
import uk.ac.dundee.computing.aec.instagrim.models.PicModel;
import uk.ac.dundee.computing.aec.instagrim.models.User;
import uk.ac.dundee.computing.aec.instagrim.stores.LoggedIn;
import uk.ac.dundee.computing.aec.instagrim.stores.Pic;

/**
 * Servlet implementation class Image
 * @author Andy Cobley, Louis-Marie Matthews
 * @version 1.0.1
 */
@WebServlet(
  urlPatterns = {
    "/Image",
    "/Image/*",
    "/Thumb/*",
    "/Images",
    "/Images/*"
  }
)
@MultipartConfig
public class Image extends HttpServlet
{
  private static final long serialVersionUID = 1L;
  private static final int BYTE_BLOCK = 8192;
  
  private final HashMap commandsMap;
  
  
  
  /**
   * @see HttpServlet#HttpServlet()
   */
  public Image()
  {
    super();
    // TODO Auto-generated constructor stub
    commandsMap = new HashMap();
    commandsMap.put("Image", 1);
    commandsMap.put("Images", 2);
    commandsMap.put("Thumb", 3);

  }
  
  
  
  @Override
  public void init(ServletConfig config)
    throws ServletException
  {
    // TODO Auto-generated method stub
  }
  
  
  
  /**
   * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
   * response)
   */
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException
  {
    // TODO Auto-generated method stub
    String[] args = Convertors.splitPath(request.getRequestURI());
    for (String arg : args) {
      ;
    }
    if (args.length>2) {
      int command;
      try {
        command = (Integer) commandsMap.get(args[1]);
      } catch (Exception et) {
        error("Bad Operator", response);
        return;
      }
      switch (command) {
        case 1:
          try {
            displayImage(Convertors.DISPLAY_PROCESSED, args[2], request, response);
          } catch (NoDatabaseConnectionException e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR );
            return;
          }
          break;
        case 2:
          try {
            displayImageList(args[2], request, response);
          } catch (NoDatabaseConnectionException e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR );
          }
          break;
        case 3:
          try {
          displayImage(Convertors.DISPLAY_THUMB, args[2], request, response);
          } catch (NoDatabaseConnectionException e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR );
            return;
          }
          break;
        default:
          error("Bad Operator", response);
      }
    } else {
      try {
        displayImageList(User.DEFAULT_USERNAME, request, response);
      } catch (NoDatabaseConnectionException e) {
        response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR );
        return;
      }
    }
  }
  
  
  /**
   * Forward to response to UsersPics.jsp along with the images uploaded by a
   * user UsersPics.jsp will display.
   * 
   * @param user the use whose images will be displayed
   * @param request
   * @param response
   * @throws ServletException
   * @throws IOException 
   */
  private void displayImageList(String user, HttpServletRequest request, HttpServletResponse response)
    throws NoDatabaseConnectionException, ServletException, IOException
  {
    java.util.LinkedList<Pic> lsPics = PicModel.getPicsForUser(user);
    RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/UsersPics.jsp");
    request.setAttribute("Pics", lsPics);
    rd.forward(request, response);
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
    throws NoDatabaseConnectionException, ServletException, IOException
  {
    try {
      Pic p = PicModel.getPic(type, java.util.UUID.fromString(image));
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
      for (int length = 0; (length = input.read(buffer)) > 0;) {
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
   * Adds a new pic to the website.
   * 
   * @param request
   * @param response
   * @throws ServletException
   * @throws IOException 
   */
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException
  {
    for (Part part : request.getParts()) {
      System.out.println("Part Name " + part.getName());

      String type = part.getContentType();
      System.out.println( "Image.doPost(…): type = " + part );
      
      String filename = part.getSubmittedFileName();

      InputStream is = request.getPart(part.getName()).getInputStream();
      int i = is.available();
      HttpSession session = request.getSession();
      LoggedIn lg = (LoggedIn) session.getAttribute("LoggedIn");
      String username = LoggedIn.getUsername(request);
      if (i > 0) {
        byte[] b = new byte[i + 1];
        is.read(b);
        System.out.println("Length : " + b.length);
        PicModel tm = new PicModel();
        try {
          tm.insertPic(b, type, filename, username);
          request.setAttribute( "message", "Your image has been uploaded sucessfully." );
        } catch (NoDatabaseConnectionException e) {
          response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR );
        }
        is.close();
      }
      RequestDispatcher rd = request.getRequestDispatcher("/upload.jsp");
      rd.forward(request, response);
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