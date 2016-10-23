<%-- 
  Document   : upload
  Created on : Sep 22, 2014, 6:31:50 PM
  Author   : Andy Cobley, Louis-Marie Matthews
--%>

<%String context = ((HttpServletRequest)request).getContextPath();%>
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
      <div class="container">
        <h2>File Upload</h2>
        <%@include file="/WEB-INF/jspf/commonnotifications.jspf" %>
          <%
            if ( request.getAttribute("message") != null ) {
              %>
              <p class="has-feedback"><%=request.getAttribute("message") %></p>
              <%
            }
            String username = LoggedIn.getUsername(request);
          %>
        <form method="POST" enctype="multipart/form-data" action="<%=((HttpServletRequest)request).getContextPath()%>/images/<%=username%>">
          <div class="form-group">
            <label for="upfile">File to upload: </label>
            <input type="file" name="upfile" id="upfile" required>
          </div>
          <div class="form-group">
            <label for="image_title">Image title: </label>
            <input type="text" id="image_title" name="image_title" />
          </div>
          <div class="form-group">
            <button class="btn btn-default" type="submit">Press</button> to upload the file!
          </div>
        </form>
      </div>
    </main>
    
    <%@include file="/WEB-INF/jspf/commonfooter.jspf" %>
</body>
</html>