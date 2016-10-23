<%-- 
  Document   : delete-account.jsp
  Created on : Oct 20, 2016, 18:30 PM
  Author   : Louis-Marie Matthews
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
  <head>
    <% request.setAttribute("pageName", "Delete your account"); %>
    <%@include file="/WEB-INF/jspf/commonhead.jspf" %>
  </head>
  <body>
    <%@include file="/WEB-INF/jspf/commonheader.jspf" %>
    <%@include file="/WEB-INF/jspf/commonnav.jspf" %>

    <main>
      <%@include file="/WEB-INF/jspf/commonnotifications.jspf" %>
      <h3>Account Deletion Page</h3>
        <%
          if ( request.getAttribute("error_message") != null ) {
            %>
            <p><%=request.getAttribute("error_message") %></p>
            <%
          }  
        %>
      <p>This page allows you to delete your account, should the improbable 
      need ever arises. This is an important operation, for that reason you
      are asked to reenter your username and your password.</p>
      <form method="POST"  action="<%=((HttpServletRequest)request).getContextPath()%>/delete-account">
        <ul>
          <li>Username <input type="text" name="username" required></li>
          <li>Password <input type="password" name="password" required></li>
        </ul>
        <br/>
        <input type="submit" value="Delete my account"> 
      </form>

    </main>
    
    <%@include file="/WEB-INF/jspf/commonfooter.jspf" %>
  </body>
</html>
