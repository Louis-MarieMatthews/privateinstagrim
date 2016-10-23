<%-- 
  Document   : UsersImages
  Created on : Sep 24, 2014, 2:52:48 PM
  Author   : Andy Cobley, Louis-Marie Matthews
  Page used by the servlet Image. Shouldn't be accessed directly.
--%>

<%@page import="java.util.*"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%-- TODO: No wildcard in import --%>
<%@page import="uk.ac.dundee.computing.aec.instagrim.stores.*" %>
<!DOCTYPE html>
<html>
  <head>
    <% request.setAttribute("pageName", "Your Images"); %>
    <%@include file="/WEB-INF/jspf/commonhead.jspf" %>
  </head>
  <body>
    <%@include file="/WEB-INF/jspf/commonheader.jspf" %>
    <%@include file="/WEB-INF/jspf/commonnav.jspf" %>
    
    <main>
      <h1>Your Images</h1>
      <%
        java.util.LinkedList<UserImage> lsImgs = (java.util.LinkedList<UserImage>) request.getAttribute("images");
        if (lsImgs == null) {
      %>
      <p>No Image found</p>
      <%
      } else {
        Iterator<UserImage> iterator;
        iterator = lsImgs.iterator();
        while (iterator.hasNext()) {
          UserImage img = (UserImage) iterator.next();

      %>
      <a href="/Instagrim/Image/<%=img.getStringUuid()%>" ><img src="/Instagrim/Thumb/<%=img.getStringUuid()%>"></a><br/><%

          }
        }
      %>
    </main>
    
    <%@include file="/WEB-INF/jspf/commonfooter.jspf" %>
  </body>
</html>
