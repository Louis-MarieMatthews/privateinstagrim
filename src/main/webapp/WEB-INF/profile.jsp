<%-- 
    Document   : profile
    Created on : 23 oct. 2016, 19:39:09
    Author     : Louis-Marie Matthews
--%>

<%@page import="uk.ac.dundee.computing.aec.instagrim.models.User"%>
<%
  String username = (String) request.getAttribute( "username" );
  boolean isUser = LoggedIn.getUsername(request).equals( username );
  String context = ((HttpServletRequest)request).getContextPath();
%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
  <head>
    <% request.setAttribute("pageName", "Profile"); %>
    <%@include file="/WEB-INF/jspf/commonhead.jspf" %>
  </head>
  <body>
    <%@include file="/WEB-INF/jspf/commonheader.jspf" %>
    <%@include file="/WEB-INF/jspf/commonnav.jspf" %>
    
    <main>
      <h1><%=username%></h1>
      <p><%=User.getEmail(username)%></p>
      <p><a href="<%=context%>/images/<%=username%>">Images</a></p>
    </main>
    
    <%@include file="/WEB-INF/jspf/commonfooter.jspf" %>
  </body>
</html>