<%-- 
  Document   : register.jsp
  Created on : Sep 28, 2014, 6:29:51 PM
  Author   : Andy Cobley, Louis-Marie Matthews
--%>

<%String context = ((HttpServletRequest)request).getContextPath();%>
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
      <div class="container">
        <h2 class="form-signin-heading">Register as a user</h2>
        <%@include file="/WEB-INF/jspf/commonnotifications.jspf" %>
          <%
            if ( request.getAttribute("details_error") != null ) {
              %>
              <p><%=request.getAttribute("details_error") %></p>
              <%
            }
          %>
        <form method="POST" action="<%=((HttpServletRequest)request).getContextPath()%>/register">
          <div class="form-group">
          <label for="username">Username: </label>
          <input type="text" name="username" class="sr-only" placeholder="Username" autofocus required>
          </div>
          <div class="form-group">
          <label for="password" class="sr-only">Password: </label>
          <input type="password" name="password" placeholder="Password" required>
          </div>
          <div class="form-group">
          <button class="btn btn-default" type="submit">Register</button>
          </div>
        </form>
      </div>
    </main>
    
    <%@include file="/WEB-INF/jspf/commonfooter.jspf" %>
  </body>
</html>
