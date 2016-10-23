<%-- 
  Document   : login.jsp
  Created on : Sep 28, 2014, 12:04:14 PM
  Author   : Andy Cobley, Louis-Marie Matthews
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
  <head>
    <% request.setAttribute("pageName", "Login"); %>
    <%@include file="/WEB-INF/jspf/commonhead.jspf" %>
  </head>
  <body>
    <%@include file="/WEB-INF/jspf/commonheader.jspf" %>
    <%@include file="/WEB-INF/jspf/commonnav.jspf" %>

    <main>
      <h3>Login</h3>
        <%
          if ( request.getAttribute("details_error") != null ) {
            %>
            <p><%=request.getAttribute("details_error") %></p>
            <%
          }  
            %>
      <form method="POST"  action="/Instagrim/login">
        <ul>
          <li>User Name <input type="text" name="username" required></li>
          <li>Password <input type="password" name="password" required></li>
        </ul>
        <br/>
        <input type="submit" value="Login"> 
      </form>

    </main>
    
    <%@include file="/WEB-INF/jspf/commonfooter.jspf" %>
  </body>
</html>
