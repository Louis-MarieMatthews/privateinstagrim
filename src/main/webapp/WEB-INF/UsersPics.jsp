<%-- 
  Document   : UsersPics
  Created on : Sep 24, 2014, 2:52:48 PM
  Author   : Andy Cobley, Louis-Marie Matthews
  Page used by the servlet Image. Shouldn't be accessed directly.
--%>

<%@page import="java.util.*"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="uk.ac.dundee.computing.aec.instagrim.stores.*" %>
<!DOCTYPE html>
<html>
  <head>
    <% request.setAttribute("pageName", "Your Pictures"); %>
    <%@include file="/WEB-INF/jspf/commonhead.jspf" %>
  </head>
  <body>
    <%@include file="/WEB-INF/jspf/commonheader.jspf" %>
    <%@include file="/WEB-INF/jspf/commonnav.jspf" %>
    
    <main>
      <h1>Your Pics</h1>
      <%
        java.util.LinkedList<Pic> lsPics = (java.util.LinkedList<Pic>) request.getAttribute("Pics");
        if (lsPics == null) {
      %>
      <p>No Pictures found</p>
      <%
      } else {
        Iterator<Pic> iterator;
        iterator = lsPics.iterator();
        while (iterator.hasNext()) {
          Pic p = (Pic) iterator.next();

      %>
      <a href="/Instagrim/Image/<%=p.getStringUUID()%>" ><img src="/Instagrim/Thumb/<%=p.getStringUUID()%>"></a><br/><%

          }
        }
      %>
    </main>
    
    <%@include file="/WEB-INF/jspf/commonfooter.jspf" %>
  </body>
</html>
