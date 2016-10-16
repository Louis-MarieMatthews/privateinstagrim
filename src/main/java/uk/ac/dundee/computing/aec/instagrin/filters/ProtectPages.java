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
package uk.ac.dundee.computing.aec.instagrin.filters;

import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import javax.servlet.DispatcherType;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import uk.ac.dundee.computing.aec.instagrim.stores.LoggedIn;

/**
 *
 * @author Administrator
 */
@WebFilter(
  filterName = "ProtectPages",
  urlPatterns = {"/upload.jsp"},
  dispatcherTypes = {
    DispatcherType.REQUEST,
    DispatcherType.FORWARD,
    DispatcherType.INCLUDE
  }
)
public class ProtectPages
  implements Filter
{
  private static final boolean debug = true;

  // The filter configuration object we are associated with.  If
  // this value is null, this filter instance is not currently
  // configured. 
  private FilterConfig filterConfig = null;
  
  
  
  public ProtectPages() {
  }
  
  
  
  private void doBeforeProcessing (ServletRequest request, ServletResponse response)
    throws IOException, ServletException
  {
    if (debug) {
      log("ProtectPages:DoBeforeProcessing");
    }

    // Write code here to process the request and/or response before
    // the rest of the filter chain is invoked.
    // For example, a logging filter might log items on the request object,
    // such as the parameters.
    /*
     for (Enumeration en = request.getParameterNames(); en.hasMoreElements(); ) {
     String name = (String)en.nextElement();
     String values[] = request.getParameterValues(name);
     int n = values.length;
     StringBuffer buf = new StringBuffer();
     buf.append(name);
     buf.append("=");
     for(int i=0; i < n; i++) {
     buf.append(values[i]);
     if (i < n-1)
     buf.append(",");
     }
     log(buf.toString());
     }
     */
  }
  
  
  
  private void doAfterProcessing(ServletRequest request, ServletResponse response)
    throws IOException, ServletException
  {
    if (debug) {
      log("ProtectPages:DoAfterProcessing");
    }

    // Write code here to process the request and/or response after
    // the rest of the filter chain is invoked.
    // For example, a logging filter might log the attributes on the
    // request object after the request has been processed. 
    /*
     for (Enumeration en = request.getAttributeNames(); en.hasMoreElements(); ) {
     String name = (String)en.nextElement();
     Object value = request.getAttribute(name);
     log("attribute: " + name + "=" + value.toString());

     }
     */
    // For example, a filter might append something to the response.
    /*
     PrintWriter respOut = new PrintWriter(response.getWriter());
     respOut.println("<P><B>This has been appended by an intrusive filter.</B>");
     */
  }
  
  
  
  /**
   *
   * @param request The servlet request we are processing
   * @param response The servlet response we are creating
   * @param chain The filter chain we are processing
   *
   * @exception IOException if an input/output error occurs
   * @exception ServletException if a servlet error occurs
   */
  public void doFilter(ServletRequest request, ServletResponse response,
      FilterChain chain)
    throws IOException, ServletException
  {

    if (debug) {
      log("ProtectPages:doFilter()");
    }

    doBeforeProcessing(request, response);
    System.out.println("Doing filter");
    HttpServletRequest httpReq = (HttpServletRequest) request;
    HttpSession session = httpReq.getSession(false);
    LoggedIn li = (LoggedIn) session.getAttribute("LoggedIn");
    System.out.println("Session in filter " + session);
    if ((li == null) || (li.getLoggedIn() == false)) {
      System.out.println("Foward to login");
      RequestDispatcher rd = request.getRequestDispatcher("/login.jsp");
      rd.forward(request, response);
    }
    Throwable problem = null;
    try {
      chain.doFilter(request, response);
    } catch (Throwable t) {
      // If an exception is thrown somewhere down the filter chain,
      // we still want to execute our after processing, and then
      // rethrow the problem after that.
      problem = t;
      t.printStackTrace();
    }

    doAfterProcessing(request, response);

    // If there was a problem, we want to rethrow it if it is
    // a known type, otherwise log it.
    if (problem != null) {
      if (problem instanceof ServletException) {
        throw (ServletException) problem;
      }
      if (problem instanceof IOException) {
        throw (IOException) problem;
      }
      sendProcessingError(problem, response);
    }
  }
  
  
  
  /**
   * Return the filter configuration object for this filter.
   */
  public FilterConfig getFilterConfig()
  {
    return (this.filterConfig);
  }
  
  
  
  /**
   * Set the filter configuration object for this filter.
   *
   * @param filterConfig The filter configuration object
   */
  public void setFilterConfig(FilterConfig filterConfig)
  {
    this.filterConfig = filterConfig;
  }
  
  
  
  /**
   * Destroy method for this filter
   */
  public void destroy()
  {
  }
  
  
  
  /**
   * Init method for this filter
   */
  public void init(FilterConfig filterConfig)
  {
    this.filterConfig = filterConfig;
    if (filterConfig != null) {
      if (debug) {
        log("ProtectPages:Initializing filter");
      }
    }
  }
  
  
  
  /**
   * Return a String representation of this object.
   */
  @Override
  public String toString()
  {
    if (filterConfig == null) {
      return ("ProtectPages()");
    }
    StringBuffer sb = new StringBuffer("ProtectPages(");
    sb.append(filterConfig);
    sb.append(")");
    return (sb.toString());
  }
  
  
  
  private void sendProcessingError(Throwable t, ServletResponse response)
  {
    String stackTrace = getStackTrace(t);

    if (stackTrace != null && !stackTrace.equals("")) {
      try {
        response.setContentType("text/html");
        PrintStream ps = new PrintStream(response.getOutputStream());
        PrintWriter pw = new PrintWriter(ps);
        pw.print("<html>\n<head>\n<title>Error</title>\n</head>\n<body>\n"); //NOI18N

        // PENDING! Localize this for next official release
        pw.print("<h1>The resource did not process correctly</h1>\n<pre>\n");
        pw.print(stackTrace);
        pw.print("</pre></body>\n</html>"); //NOI18N
        pw.close();
        ps.close();
        response.getOutputStream().close();
      } catch (Exception ex) {
      }
    } else {
      try {
        PrintStream ps = new PrintStream(response.getOutputStream());
        t.printStackTrace(ps);
        ps.close();
        response.getOutputStream().close();
      } catch (Exception ex) {
      }
    }
  }
  
  
  
  public static String getStackTrace(Throwable t)
  {
    String stackTrace = null;
    try {
      StringWriter sw = new StringWriter();
      PrintWriter pw = new PrintWriter(sw);
      t.printStackTrace(pw);
      pw.close();
      sw.close();
      stackTrace = sw.getBuffer().toString();
    } catch (Exception ex) {
    }
    return stackTrace;
  }
  
  
  
  public void log(String msg)
  {
    filterConfig.getServletContext().log(msg);
  }
}