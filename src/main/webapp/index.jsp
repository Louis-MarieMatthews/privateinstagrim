<%-- 
  Document   : index
  Created on : Sep 28, 2014, 7:01:44 PM
  Author   : Andy Cobley
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="uk.ac.dundee.computing.aec.instagrim.stores.*" %>
<!DOCTYPE html>
<html>
  <head>
    <title>Instagrim</title>
    <link rel="stylesheet" type="text/css" href="Styles.css" />
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
  </head>
  <body>
    <header>
      <h1>InstaGrim ! </h1>
      <h2>Your world in Black and White</h2>
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
    </header>
    <nav>
      <ul>
        <li><a href="upload.jsp">Upload</a></li>
          <%
            if (lg != null) {
              String username = lg.getUsername();
              if (lg.isLoggedIn()) {
          %>
        <li><a href="/Instagrim/Images/<%=lg.getUsername()%>">Your Images</a></li>
          <%
              }
            }
            else {
          %>
        <li><a href="register.jsp">Register</a></li>
        <li><a href="login.jsp">Login</a></li>
          <%
            }
          %>
      </ul>
    </nav>
    <footer>
      <ul>
        <li class="footer"><a href="/Instagrim">Home</a></li>
        <li>&COPY; Andy C</li>
      </ul>
    </footer>
  </body>
</html>