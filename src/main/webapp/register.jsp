<%-- 
  Document   : register.jsp
  Created on : Sep 28, 2014, 6:29:51 PM
  Author   : Andy Cobley, Louis-Marie Matthews
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
  <head>
    <% request.setAttribute("pageName", "Register"); %>
    <%@include file="/WEB-INF/jspf/commonhead.jspf" %>
  </head>
  <body>
    <%@include file="/WEB-INF/jspf/commonheader.jspf" %>
    <%@include file="/WEB-INF/jspf/commonnav.jspf" %>

    <main>
      <h3>Register as user</h3>
        <%
          if ( request.getAttribute("details_error") != null ) {
            %>
            <p><%=request.getAttribute("details_error") %></p>
            <%
          }  
        %>
      <form method="POST"  action="Register">
        <ul>
          <li>User Name <input type="text" name="username"></li>
          <li>Password <input type="password" name="password"></li>
        </ul>
        <br/>
        <input type="submit" value="Regidter"> 
      </form>

    </main>
    
    <%@include file="/WEB-INF/jspf/commonfooter.jspf" %>
  </body>
</html>
