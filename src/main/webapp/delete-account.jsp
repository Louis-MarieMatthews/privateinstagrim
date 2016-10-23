<%-- 
  Document   : delete-account.jsp
  Created on : Oct 20, 2016, 18:30 PM
  Author   : Louis-Marie Matthews
--%>

<%String context = ((HttpServletRequest)request).getContextPath();%>
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
      <div class="container">
        <h2>Account Deletion</h2>
        <%@include file="/WEB-INF/jspf/commonnotifications.jspf" %>
          <%
            if ( request.getAttribute("error_message") != null ) {
              %>
              <p class="has-error"><%=request.getAttribute("error_message") %></p>
              <%
            }  
          %>
        <form method="POST"  action="<%=((HttpServletRequest)request).getContextPath()%>/delete-account">
          <p>This page allows you to delete your account, should the improbable 
          need ever arises. This is an important operation, for that reason you
          are asked to reenter your username and your password.</p>
          <div class="form-group">
            <label for="username">Username :</label>
            <input type="text" name="username" required>
          </div>
          <div class="form-group">
            <label for="password">Password :</label>
            <input type="password" name="password" required>
          </div>
          <div class="form-group">
            <button class="btn btn-danger" type="submit">Press this button to delete your account</button>
          </div>
        </form>
      </div>
    </main>
    
    <%@include file="/WEB-INF/jspf/commonfooter.jspf" %>
  </body>
</html>
