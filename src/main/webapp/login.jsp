<%-- 
  Document   : login.jsp
  Created on : Sep 28, 2014, 12:04:14 PM
  Author   : Andy Cobley, Louis-Marie Matthews
--%>

<%String context = ((HttpServletRequest)request).getContextPath();%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
  <head>
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <% request.setAttribute("pageName", "Login"); %>
    <%@include file="/WEB-INF/jspf/commonhead.jspf" %>
  </head>
  <body>
    <%@include file="/WEB-INF/jspf/commonheader.jspf" %>
    <%@include file="/WEB-INF/jspf/commonnav.jspf" %>

    <main>
      <div class="container">
        <h2>Please log in.</h2>
        <%@include file="/WEB-INF/jspf/commonnotifications.jspf" %>
          <%
            if ( request.getAttribute("details_error") != null ) {
              %>
              <p class="has-error"><%=request.getAttribute("details_error") %></p>
              <%
            }
              %>
        <form method="POST"  action="<%=((HttpServletRequest)request).getContextPath()%>/login">
          <div class="form-group">
            <label for="username">Username: </label>
            <input type="text" id="username" name="username" placeholder="Username" required autofocus>
          </div>
          <div class="form-group">
            <label for="password">Password: </label>
            <input type="password" id="password" name="password" placeholder="Password" required>
          </div>
            <div class="form-group">
            <button class="btn btn-default" type="submit">Log in</button>
          </div>
        </form>
      </div> <!-- /container -->
    </main>
    
    <%@include file="/WEB-INF/jspf/commonfooter.jspf" %>
    <script>window.jQuery || document.write('<script src="../../assets/js/vendor/jquery.min.js"><\/script>')</script>
  </body>
</html>