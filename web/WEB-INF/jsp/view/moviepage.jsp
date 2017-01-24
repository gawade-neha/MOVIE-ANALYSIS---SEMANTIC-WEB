<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ page import="java.util.HashMap" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
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


<title>MovieDetails</title>
</head>
<body>
<div class="container">
<h2>Movie Details</h2>
<div class="container">
<img src="/webapp/resources/movie.jpg" alt="imagenotfound" class=" center-block" height="110" width="535" align="middle"></img>


<a href="movie" class="btn btn-info btn-lg">Go Back</a>
<table border="1" class="table table-striped">
<br>
<br>

<%
         if( ((Boolean)request.getAttribute("nomovie") )  ){
         
             %>
      <h4>Sorry, No movies found with that keyword.</h4>
      <%
         }
         %>
         
<%
            @SuppressWarnings("unchecked")
HashMap<String, HashMap<String,String>> moviedata =(HashMap<String, HashMap<String,String>>)request.getAttribute("movieData");

           
			for(String id : moviedata.keySet())
            {
                %>
                <tr><td>
                
                
                <h3><%= moviedata.get(id).get("title") %></h3><br />
              
                <p class="label label-primary">Movie director:</p>
                <%= moviedata.get(id).get("director") %><br />
                
                <p class="label label-primary">Movie distributor:</p>
                <%= moviedata.get(id).get("distributor") %><br />
                
                <p class="label label-primary">Movie budget:</p>
                <%= moviedata.get(id).get("budget") %>$<br />
                
                <p class="label label-primary">Movie gross:</p>
                <%= moviedata.get(id).get("gross") %>$<br />
                
                <p class="label label-primary">Profit/Loss=Gross-budget:</p>
                <%= moviedata.get(id).get("result") %>$<br />
               
                <p class="label label-primary">Movie country:</p>
                <%= moviedata.get(id).get("country") %><br />
            
                <p class="label label-primary">Movie Release year:</p>
                <%= moviedata.get(id).get("year") %><br />
                
                </td></tr>
                
                <%
            }
        %>
</table>
</div>
</div>
<br><br>

<footer class="footer">
      <div class="container">
        <p class="text-muted">Designed and built by Supra, Shankar & Neha.</p>
      </div>
    </footer>  
</body>
</html>