<%-- 
    Document   : ImageNotFound
    Created on : 18 oct. 2016, 22:06:15
    Author     : Louis-Marie Matthews
--%>

<%@page import="uk.ac.dundee.computing.aec.instagrim.stores.LoggedIn"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>The image has not been found</title>
  </head>
  <body>
    <nav>
      <h1>The requested image has not been found</h1>
      <%
          LoggedIn lg = (LoggedIn) session.getAttribute("LoggedIn");
          if (lg != null) {
            String username = lg.getUsername();
            if (lg.isLoggedIn())
      %>
      <p>Hello, <%= lg.getUsername() %>.</p>
      <%
          }
      %>
    </nav>
  </body>
</html>
