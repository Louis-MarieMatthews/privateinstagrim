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
import uk.ac.dundee.computing.aec.instagrim.lib.CassandraHosts;
import uk.ac.dundee.computing.aec.instagrim.lib.Convertors;
import uk.ac.dundee.computing.aec.instagrim.models.PicModel;
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
  private HashMap commandsMap = new HashMap();
  
  
  
  /**
   * @see HttpServlet#HttpServlet()
   */
  public Image()
  {
    super();
    // TODO Auto-generated constructor stub
    commandsMap.put("Image", 1);
    commandsMap.put("Images", 2);
    commandsMap.put("Thumb", 3);

  }
  
  
  
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
    for(int i = 0; i < args.length; i++) {
    }
    int command;
    try {
      command = (Integer) commandsMap.get(args[1]);
    } catch (Exception et) {
      error("Bad Operator", response);
      return;
    }
    switch (command) {
      case 1:
        displayImage(Convertors.DISPLAY_PROCESSED, args[2], response);
        break;
      case 2:
        displayImageList(args[2], request, response);
        break;
      case 3:
        displayImage(Convertors.DISPLAY_THUMB, args[2], response);
        break;
      default:
        error("Bad Operator", response);
    }
  }
  
  
  
  private void displayImageList(String user, HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException
  {
    PicModel tm = new PicModel();
    java.util.LinkedList<Pic> lsPics = tm.getPicsForUser(user);
    RequestDispatcher rd = request.getRequestDispatcher("/UsersPics.jsp");
    request.setAttribute("Pics", lsPics);
    rd.forward(request, response);
  }
  
  
  
  private void displayImage(int type, String image, HttpServletResponse response)
    throws ServletException, IOException
  {
    PicModel tm = new PicModel();

    Pic p = tm.getPic(type, java.util.UUID.fromString(image));

    OutputStream out = response.getOutputStream();

    response.setContentType(p.getType());
    response.setContentLength(p.getLength());
    //out.write(Image);
    InputStream is = new ByteArrayInputStream(p.getBytes());
    BufferedInputStream input = new BufferedInputStream(is);
    byte[] buffer = new byte[8192];
    for (int length = 0; (length = input.read(buffer)) > 0;) {
      out.write(buffer, 0, length);
    }
    out.close();
  }
  
  
  
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException
  {
    for (Part part : request.getParts()) {
      System.out.println("Part Name " + part.getName());

      String type = part.getContentType();
      String filename = part.getSubmittedFileName();

      InputStream is = request.getPart(part.getName()).getInputStream();
      int i = is.available();
      HttpSession session = request.getSession();
      LoggedIn lg = (LoggedIn) session.getAttribute("LoggedIn");
      String username = "majed";
      if (lg.getLoggedIn()) {
        username = lg.getUsername();
      }
      if (i > 0) {
        byte[] b = new byte[i + 1];
        is.read(b);
        System.out.println("Length : " + b.length);
        PicModel tm = new PicModel();
        tm.insertPic(b, type, filename, username);

        is.close();
      }
      RequestDispatcher rd = request.getRequestDispatcher("/upload.jsp");
      rd.forward(request, response);
    }
  }
  
  
  
  private void error(String mess, HttpServletResponse response)
    throws ServletException, IOException
  {
    PrintWriter out = null;
    out = new PrintWriter(response.getOutputStream());
    out.println("<h1>You have a na error in your input</h1>");
    out.println("<h2>" + mess + "</h2>");
    out.close();
    return;
  }
}