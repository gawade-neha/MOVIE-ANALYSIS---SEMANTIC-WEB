<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ page import="java.util.HashMap" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Movie List</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

 <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
      <meta http-equiv="X-UA-Compatible" content="IE=edge">
      <style type="text/css">
         <%@include file="/webapp/resources/css/bootstrap.min.css" %>
         <%@include file="/webapp/resources/css/bootstrap-theme.min.css" %>
      </style>
      <meta name="viewport" content="width=device-width, initial-scale=1.0">
      <script src="/webapp/resources/jquery-3.1.1.min.js"></script>
      <script src="/webapp/resources/js/bootstrap.min.js"></script>

</head>
<body>
<div class="container">


<a href="movie" class="btn btn-info btn-lg">Go Back</a>

<br>
<h3> Highest Profit movies:</h3>
<table border="1" class="table table-striped">
<br/>
<br/>
<div class="row">
     <tr>    
<%
            @SuppressWarnings("unchecked")
HashMap<String, HashMap<String,String>> moviedata2 =(HashMap<String, HashMap<String,String>>)request.getAttribute("movieData2");

           
			for(String id : moviedata2.keySet())
            {
                %>
                <td>
                <div class="col-sm-1">
                
                <h4><%= moviedata2.get(id).get("title") %></h4><br />
              
                <%-- <p class="label label-primary">Movie director:</p>
                <%= moviedata.get(id).get("director") %><br />
                
                <p class="label label-primary">Movie gross:</p>
                <%= moviedata.get(id).get("gross") %>$<br /> --%>
                
                </div></td>
                
                <%
            }
        %>
        </tr>
</div>
</table>

<br>
<h3>Find Lowest Profit movies:</h3>
<table border="1" class="table table-striped">
<br/>
<br/>
<div class="row">
     <tr>    
<%
            @SuppressWarnings("unchecked")
HashMap<String, HashMap<String,String>> moviedata3 =(HashMap<String, HashMap<String,String>>)request.getAttribute("movieData3");

           
			for(String id : moviedata3.keySet())
            {
                %>
                <td>
                <div class="col-sm-1">
                
                <h4><%= moviedata3.get(id).get("title") %></h4><br />
              
                <%-- <p class="label label-primary">Movie director:</p>
                <%= moviedata.get(id).get("director") %><br />
                
                <p class="label label-primary">Movie gross:</p>
                <%= moviedata.get(id).get("gross") %>$<br /> --%>
                
                </div></td>
                
                <%
            }
        %>
        </tr>
</div>
</table>

<br>
</div>
</body>
</html>