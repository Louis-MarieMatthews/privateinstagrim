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
      <%@include file="/WEB-INF/jspf/commonnotifications.jspf" %>
      <h3>Login</h3>
        <%
          if ( request.getAttribute("details_error") != null ) {
            %>
            <p><%=request.getAttribute("details_error") %></p>
            <%
          }
            %>
      <form method="POST"  action="<%=((HttpServletRequest)request).getContextPath()%>/login">
        <ul>
          <li><label for="username">Username: </label><input type="text" name="username" id="username" required></li>
          <li><label for="password">Password: </label><input type="password" name="password" id="password" required></li>
        </ul>
        <br/>
        <%
          String previousPage = (String) request.getAttribute( "previous_page" );
          if ( previousPage != null ) {
        %>
        <input type="hidden" name="previous_page" value="<%=previousPage%>">
        <% } %>
        <input type="submit" value="Login"> 
      </form>

    </main>
    
    <%@include file="/WEB-INF/jspf/commonfooter.jspf" %>
  </body>
</html>