<%-- 
    Document   : edit-image
    Created on : 23 oct. 2016, 17:18:00
    Author     : Louis-Marie Matthews
--%>

<%@page import="uk.ac.dundee.computing.aec.instagrim.models.ImageModel"%>
<%@page import="java.util.UUID"%>
<%@page import="java.util.Iterator"%>

<%
  UUID uuid = (UUID) request.getAttribute( "uuid" );
  String context = ((HttpServletRequest)request).getContextPath();
%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
  <head>
    <% request.setAttribute("pageName", "Edit Image"); %>
    <%@include file="/WEB-INF/jspf/commonhead.jspf" %>
  </head>
  <body>
    <%@include file="/WEB-INF/jspf/commonheader.jspf" %>
    <%@include file="/WEB-INF/jspf/commonnav.jspf" %>
    
    <main>
      <h1>Edit Image</h1>
      <a href="<%=context%>/image/<%=uuid%>">
        <img src="<%=context%>/thumb/<%=uuid%>" />
      </a>
      <form method="POST" action="<%=context%>/edit-image/<%=uuid%>">
        <label for="title">Title: </label><input type="text" name="title" id="title" value="<%=ImageModel.getTitle(uuid)%>" />
        <input type="submit" value="Update" />
        <input type="reset" value="Reset" />
      </form>
      <form method="POST" action="<%=context%>/delete-image/<%=uuid%>">
        <input type="submit" value="Delete image" />
      </form>
    </main>
    
    <%@include file="/WEB-INF/jspf/commonfooter.jspf" %>
  </body>
</html>
