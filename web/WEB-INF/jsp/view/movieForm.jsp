<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
        <%@ page import="java.util.HashMap" %>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Movie Details</title>

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

<br>
<div class="container">
<img src="/webapp/resources/movie.jpg" alt="imagenotfound" class=" center-block" height="110" width="535" align="middle"></img>

<h3>Top ten movies for the year 2015 are(Highest Grossing):</h3>
<table border="1" class="table table-striped">
<br/>
<br/>
<div class="row">
     <tr>    
<%
            @SuppressWarnings("unchecked")
HashMap<String, HashMap<String,String>> moviedata =(HashMap<String, HashMap<String,String>>)request.getAttribute("movieData");

           
			for(String id : moviedata.keySet())
            {
                %>
                <td>
                <div class="col-sm-1">
                
                <h4><%= moviedata.get(id).get("title") %></h4><br />
              
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

<h3>Check movie lists with highest and lowest profit:</h3>
<form method="POST" action="movie?action=profit">
<input type="submit" value="Find Movies" class="btn btn-info">
</form>

<br>
<h3>Enter movie name to be searched:</h3>
<form method="POST" action="movie?action=search">
<input type="text" name="moviename">
<input type="submit" value="Find Movie" class="btn btn-info">
</form>

<br>

<h3>Enter Director name to be searched :</h3>
<form method="POST" action="movie?action=searchdir">
<input type="text" name="movienamedir">
<input type="submit" value="Find Director" class="btn btn-info">
</form>
<br>

<h3>Enter Distributor name to be searched :</h3>
<form method="POST" action="movie?action=searchdis">
<input type="text" name="movienamedis">
<input type="submit" value="Find Distributor" class="btn btn-info">
</form>

<br>
<h3>See top movies in year:</h3>
<form method="POST" action="movie?action=searchdate">
<input type="text" name="movienamedate">
<input type="submit" value="Find Year" class="btn btn-info">
</form>


<h3>Enter country name to be searched :</h3>
<form method="POST" action="movie?action=searchcountry">
<input type="text" name="movienamecountry">
<input type="submit" value="Find Country" class="btn btn-info">
</form>


<h3>See movies in genre:(like: Romance Film,Drama,Biographical,Indie,Musical,Action,Historical drama,Mystery,Adventure,Martial arts,Teen,Family,Western, Spy)</h3>
<form method="POST" action="movie?action=searchgenre">
<input type="text" name="movienamegenre">
<input type="submit" value="Find Genre" class="btn btn-info">
</form>

</div>

<br><br>

<footer class="footer">
      <div class="container">
        <p class="text-muted">Designed and built by Supra, Shankar & Neha.</p>
      </div>
    </footer>  
</body>
</html>