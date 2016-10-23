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
      <div class="container">
        <h1>Edit Image</h1>
        <p>
          <a href="<%=context%>/image/<%=uuid%>">
            <img src="<%=context%>/thumb/<%=uuid%>" />
          </a>
        </p>
        <form method="POST" action="<%=context%>/edit-image/<%=uuid%>">
          <div class="form-group">
           <label for="title">Title: </label>
           <input type="text" name="title" id="title" value="<%=ImageModel.getTitle(uuid)%>" />
          </div>
          <div class="form-group">
           <button type="submit" class="btn btn-default">Update</button>
            <button type="reset" class="btn btn-default">Reset</button>
          </div>
        </form>
        <form method="POST" action="<%=context%>/delete-image/<%=uuid%>">
          <div class="form-group">
            <button class="btn btn-danger" type="submit">Delete image</button>
          </div>
        </form>
      </div>
    </main>
    
    <%@include file="/WEB-INF/jspf/commonfooter.jspf" %>
  </body>
</html>
