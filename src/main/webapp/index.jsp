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
      <%@include file="/WEB-INF/jspf/commonnotifications.jspf" %>
      <h1>User</h1>
      <p><a href="<%=context%>/profile/<%=randomUser%>"><%=randomUser%></a></p>
    </main>
    
    <%@include file="/WEB-INF/jspf/commonfooter.jspf" %>
  </body>
</html>