<%-- 
  Document   : upload
  Created on : Sep 22, 2014, 6:31:50 PM
  Author   : Andy Cobley, Louis-Marie Matthews
--%>

<%@page import="uk.ac.dundee.computing.aec.instagrim.stores.LoggedIn"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
  <head>
    <% request.setAttribute("pageName", "Upload"); %>
    <%@include file="/WEB-INF/jspf/commonhead.jspf" %>
  </head>
  <body>
    <%@include file="/WEB-INF/jspf/commonheader.jspf" %>
    <%@include file="/WEB-INF/jspf/commonnav.jspf" %>

    <main>
      <h3>File Upload</h3>
        <%
          if ( request.getAttribute("message") != null ) {
            %>
            <p><%=request.getAttribute("message") %></p>
            <%
          }
          String username = LoggedIn.getUsername(request);
        %>
      <form method="POST" enctype="multipart/form-data" action="/Instagrim/images/<%=username%>">
        <label for="upfile">File to upload: </label><input type="file" name="upfile" id="upfile" required><br/>
        <br/>
        <input type="submit" value="Press"> to upload the file!
      </form>
    </main>
    
    <%@include file="/WEB-INF/jspf/commonfooter.jspf" %>
</body>
</html>