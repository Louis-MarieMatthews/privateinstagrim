<%-- 
  Document   : index
  Created on : Sep 28, 2014, 7:01:44 PM
  Author   : Andy Cobley, Louis-Marie Matthews
--%>
<%@page import="uk.ac.dundee.computing.aec.instagrim.models.User"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="uk.ac.dundee.computing.aec.instagrim.stores.*" %>

<%
  String randomUser = User.getRandomUser();
  String context = ((HttpServletRequest)request).getContextPath();
%>

<!DOCTYPE html>
<html>
  <head>
    <% request.setAttribute("pageName", "Home"); %>
    <%@include file="/WEB-INF/jspf/commonhead.jspf" %>
  </head>
  <body>
    <%@include file="/WEB-INF/jspf/commonheader.jspf" %>
    <%@include file="/WEB-INF/jspf/commonnav.jspf" %>
    <main>
      <div class="container">
          <h1>User</h1>
          <%@include file="/WEB-INF/jspf/commonnotifications.jspf" %>
          <h2>Check out the pictures of this user!</h2>
          <p><a href="<%=context%>/images/<%=randomUser%>"><%=randomUser%></a></p>
      </div>
    </main>
    
    <%@include file="/WEB-INF/jspf/commonfooter.jspf" %>
  </body>
</html>