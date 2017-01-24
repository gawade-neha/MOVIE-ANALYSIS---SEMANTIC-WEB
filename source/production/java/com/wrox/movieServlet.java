package com.wrox;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.util.List;
import java.io.IOException;
import java.util.HashMap;
import org.apache.jena.query.ParameterizedSparqlString;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.ResultSet;
import org.apache.jena.query.ResultSetFormatter;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.ResourceFactory;
import org.apache.jena.query.QuerySolution;


@WebServlet(
        name = "movieServlet",
        urlPatterns = "/"
)
public class movieServlet extends HttpServlet
{
    
	private static final long serialVersionUID = 1L;

	HashMap<String, HashMap<String,String>> movies = new HashMap<String,HashMap<String,String>>();
	HashMap<String, HashMap<String,String>> movies2 = new HashMap<String,HashMap<String,String>>();
	HashMap<String, HashMap<String,String>> movies3 = new HashMap<String,HashMap<String,String>>();

    public movieServlet()
    {
        
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
    {
        String action = request.getParameter("action");
        if(action == null)
            action = "movie";

        switch(action)
        {
          

            case "movie":
            default:
            	this.getMoviepage(request, response);
            	break;
                   
            
            case "browse":
               // this.browse(request, response);
                break;
        }
    }

    private void getMoviepage(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
            {
    	
    	movies.clear();
    	
//highest grossing code-----------------------------

    	ParameterizedSparqlString qs = new ParameterizedSparqlString(""
                + "prefix rdfs:    <http://www.w3.org/2000/01/rdf-schema#>\n"
                + "PREFIX dbo:     <http://dbpedia.org/ontology/>"
                +"PREFIX dbpprop:<http://dbpedia.org/property/>"
                +"PREFIX dbp:<http://dbpedia.org/property/>"
                +"PREFIX foaf:<http://xmlns.com/foaf/0.1/>"
                +"PREFIX xsd:<http://www.w3.org/2001/XMLSchema#>"
                 + "\n"
                + "select distinct (str(?resource) as ?movietitle)  (str(?grossincome) as ?grossincome1) (str(?Country) as ?country1) ?ReleaseDate where {\n"
                 + "  ?movie foaf:name ?resource.\n "
                 +"?movie dbpprop:gross ?grossincome.\n"
                +"?movie dbp:country ?Country.\n"
                +" ?movie dbo:releaseDate ?ReleaseDate.\n"
                 + "  FILTER (lang(?resource) = 'en').\n"
                +"  FILTER ((?Country = \"United States\"@en) || (?Country = \":United_States\")) .\n "
                 + " FILTER ((?ReleaseDate >= \"2006-01-01\"^^xsd:date) && (?ReleaseDate < \"2016-11-01\"^^xsd:date)).}"
                +"ORDER BY DESC(?grossincome) LIMIT 100");
    	
QueryExecution exec = QueryExecutionFactory.sparqlService("http://dbpedia.org/sparql", qs.asQuery());

        
        ResultSet results = exec.execSelect();

          
        List<String> em;
        em = results.getResultVars();
       System.out.println(em.toString());

       // System.out.println(results);
        
//        while(results.hasNext()){
//        	System.out.println(results.next().get("Movie"));
//        }
        int i=0;

        while (results.hasNext()&&i<10) {

        	i=movies.size();
        	QuerySolution bindings=results.next();

        	HashMap<String,String> moviedata = new HashMap<String,String>();
        	String title=bindings.get("movietitle").toString();
        	System.out.println("Title: "+title);
        	title=title.replaceAll("@en", "");
        	moviedata.put("title", title);

        	
        	String gross=bindings.get("grossincome1").toString();
        	String gross1=gross.replace("^^http://www.w3.org/2001/XMLSchema#float","");
            System.out.println("Gross: "+gross1);
        	moviedata.put("gross", gross1);
        	
        	
        	String country=bindings.get("country1").toString();
        	country=country.replace("http://dbpedia.org/resource/", "");
            System.out.println("Country: "+country);
        	moviedata.put("country", country);
        	
        	String year=bindings.get("ReleaseDate").toString();
        	String year1=year.replace("^^http://www.w3.org/2001/XMLSchema#date", "");
        	moviedata.put("year",year1);
            System.out.println("Year:"+year1);
            movies.put(title, moviedata);
            

        }
        System.out.println(movies.size());
        //ResultSetFormatter.out(results);
   
        //highest grossing code ends
        
 
        
        if(movies.size()==0){
        	request.setAttribute("movieData", this.movies);
        	
        	System.out.println("hi");
            
        }else{
        
        //System.out.println("result as text: "+ResultSetFormatter.asText(results));
    	//System.out.println("inside search movie: length of arr "+arr.length);
        	request.setAttribute("movieData", this.movies);
        	
        	System.out.println("done");
        
        }
    	
    	
    			request.getRequestDispatcher("/WEB-INF/jsp/view/movieForm.jsp")
    					.forward(request, response);
            }
    
    
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
    {
    	String action = request.getParameter("action");
        if(action == null)
            action = "search";

        switch(action)
        {
            case "search":
                this.searchMovie(request, response);
                break;
            
            case "searchdir":
                this.searchMovieDir(request, response);
                break;
              
            case "searchdis":
                this.searchMovieDis(request, response);
                break;
                
            case "searchdate":
                this.searchMovieYear(request, response);
                break;
            
            case "searchgenre":
                this.searchMovieGenre(request, response);
                break;
             
            case "searchcountry":
                this.searchMovieCountry(request, response);
                break;
                
            case "profit":
                this.searchMovieProfit(request, response);
                break;
                
            case "browse":
                break;
        }
    }
    
    
    private void searchMovieProfit(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException{
    	
    	movies2.clear();
    	movies3.clear();
    	
    	//highest profit code--------------------------

        
        ParameterizedSparqlString qs2 = new ParameterizedSparqlString(""
        		+ "\n"
                + "PREFIX rdfs:    <http://www.w3.org/2000/01/rdf-schema#>\n"
                + "PREFIX dbo:     <http://dbpedia.org/ontology/>"
                +"PREFIX dbpprop:<http://dbpedia.org/property/>"
                +"PREFIX dbp:<http://dbpedia.org/property/>"
                +"PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>"
                +"PREFIX foaf:<http://xmlns.com/foaf/0.1/>"
                 + "\n"
                + "select distinct (str(?resource) as ?movietitle) (xsd:float(?budget) as ?budget1) (xsd:float(?grossincome) as ?grossincome1) (xsd:float(?grossincome) - xsd:float(?budget) as ?result) (str(?Country) as ?country1)  ?ReleaseDate where {\n"
                 + "  ?movie foaf:name ?resource.\n "
                +"?movie dbo:budget ?budget.\n"
                 +"?movie dbpprop:gross ?grossincome.\n"
                +"?movie dbp:country ?Country.\n"
                +" ?movie dbo:releaseDate ?ReleaseDate.\n"
                 + "FILTER (lang(?resource) = 'en')"
                 + "FILTER (datatype(?grossincome) != '')"
                 + "FILTER (!regex(?grossincome, '-', 'i'))"
               +"  FILTER ((?Country = \"United States\"@en) || (?Country = \":United_States\"))  "
                + " FILTER ((?ReleaseDate >= \"2006-01-01\"^^xsd:date) && (?ReleaseDate < \"2016-11-01\"^^xsd:date)).}"
               +"ORDER BY DESC(?result) LIMIT 100");
    	
        QueryExecution exec2 = QueryExecutionFactory.sparqlService("http://dbpedia.org/sparql", qs2.asQuery());

        
        ResultSet results2 = exec2.execSelect();

          
        List<String> em2;
        em2 = results2.getResultVars();
       System.out.println(em2.toString());
        
       int i=0;
        while (results2.hasNext()&&i<10) {
        	i=movies2.size();
        	
        	QuerySolution bindings2=results2.next();

        	HashMap<String,String> moviedata = new HashMap<String,String>();
        	String title=bindings2.get("movietitle").toString();
        	System.out.println("Title: "+title);
        	title=title.replaceAll("@en", "");
        	moviedata.put("title", title);
        	
        	String budget=bindings2.get("budget1").toString();
        	String budget1=budget.replace("^^http://www.w3.org/2001/XMLSchema#float","");
            System.out.println("Budget: "+budget1);
        	moviedata.put("budget", budget1);
        	
        	String gross=bindings2.get("grossincome1").toString();
        	String gross1=gross.replace("^^http://www.w3.org/2001/XMLSchema#float","");
            System.out.println("Gross: "+gross1);
        	moviedata.put("gross", gross1);
        	
        	
        	String country=bindings2.get("country1").toString();
        	country=country.replace("http://dbpedia.org/resource/", "");
            System.out.println("Country: "+country);
        	moviedata.put("country", country);
        	
        	String year=bindings2.get("ReleaseDate").toString();
        	String year1=year.replace("^^http://www.w3.org/2001/XMLSchema#date", "");
        	moviedata.put("year",year1);
            System.out.println("Year:"+year1);
            movies2.put(title, moviedata);
            

        }
        System.out.println(movies2.size());
        //ResultSetFormatter.out(results);

        //highest profit code ends
        
        
        
        //lowest profit code--------------------------
   
     
        ParameterizedSparqlString qs3 = new ParameterizedSparqlString(""
        		+ "\n"
                + "PREFIX rdfs:    <http://www.w3.org/2000/01/rdf-schema#>\n"
                + "PREFIX dbo:     <http://dbpedia.org/ontology/>"
                +"PREFIX dbpprop:<http://dbpedia.org/property/>"
                +"PREFIX dbp:<http://dbpedia.org/property/>"
                +"PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>"
                +"PREFIX foaf:<http://xmlns.com/foaf/0.1/>"
                 + "\n"
                + "select distinct (str(?resource) as ?movietitle) (xsd:float(?budget) as ?budget1) (xsd:float(?grossincome) as ?grossincome1) (xsd:float(?grossincome) - xsd:float(?budget) as ?result) (str(?Country) as ?country1)  ?ReleaseDate where {\n"
                 + "  ?movie foaf:name ?resource.\n "
                +"?movie dbo:budget ?budget.\n"
                 +"?movie dbpprop:gross ?grossincome.\n"
                +"?movie dbp:country ?Country.\n"
                +" ?movie dbo:releaseDate ?ReleaseDate.\n"
                 + "FILTER (lang(?resource) = 'en')"
                 + "FILTER (datatype(?grossincome) != '')"
                 + "FILTER (!regex(?grossincome, '-', 'i'))"
               +"  FILTER ((?Country = \"United States\"@en) || (?Country = \":United_States\"))  "
                + " FILTER ((?ReleaseDate >= \"2006-01-01\"^^xsd:date) && (?ReleaseDate < \"2016-11-01\"^^xsd:date)).}"
               +"ORDER BY ASC(?result) LIMIT 100");
    	
QueryExecution exec3 = QueryExecutionFactory.sparqlService("http://dbpedia.org/sparql", qs3.asQuery());

        
        ResultSet results3 = exec3.execSelect();

          
        List<String> em3;
        em3 = results3.getResultVars();
       System.out.println(em3.toString());

       // System.out.println(results);
        
//        while(results.hasNext()){
//        	System.out.println(results.next().get("Movie"));
//        }
        
       int j=0;
        while (results3.hasNext()&&j<10) {

        	j=movies3.size();
        	QuerySolution bindings3=results3.next();

        	HashMap<String,String> moviedata = new HashMap<String,String>();
        	String title=bindings3.get("movietitle").toString();
        	System.out.println("Title: "+title);
        	title=title.replaceAll("@en", "");
        	moviedata.put("title", title);

        	
        	
        	String budget=bindings3.get("budget1").toString();
        	String budget1=budget.replace("^^http://www.w3.org/2001/XMLSchema#float","");
            System.out.println("Budget: "+budget1);
        	moviedata.put("budget", budget1);
        	
        	String gross=bindings3.get("grossincome1").toString();
        	String gross1=gross.replace("^^http://www.w3.org/2001/XMLSchema#float","");
            System.out.println("Gross: "+gross1);
        	moviedata.put("gross", gross1);
        	
        	
        	String country=bindings3.get("country1").toString();
        	country=country.replace("http://dbpedia.org/resource/", "");
            System.out.println("Country: "+country);
        	moviedata.put("country", country);
        	
        	String year=bindings3.get("ReleaseDate").toString();
        	String year1=year.replace("^^http://www.w3.org/2001/XMLSchema#date", "");
        	moviedata.put("year",year1);
            System.out.println("Year:"+year1);
            movies3.put(title, moviedata);
            

        }
        System.out.println(movies3.size());
        //ResultSetFormatter.out(results);

        
        //lowest profit code ends
 
        
        if(movies2.size()==0||movies3.size()==0){
        	request.setAttribute("movieData2", this.movies2);
        	request.setAttribute("movieData3", this.movies3);

        	System.out.println("hi");
            
        }else{
        
        //System.out.println("result as text: "+ResultSetFormatter.asText(results));
    	//System.out.println("inside search movie: length of arr "+arr.length);
        	request.setAttribute("movieData2", this.movies2);
        	request.setAttribute("movieData3", this.movies3);
        	System.out.println("done");
        
        }
    	
    	
    			request.getRequestDispatcher("/WEB-INF/jsp/view/Moviestats.jsp")
    					.forward(request, response);
    	
    	
    }
    
    private void searchMovieCountry(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException{
    	
    	movies.clear();
    	String country=request.getParameter("movienamecountry");

    	System.out.println("country:  "+country);
    	ParameterizedSparqlString qs = new ParameterizedSparqlString(""
                + "prefix rdfs:    <http://www.w3.org/2000/01/rdf-schema#>\n"
                + "PREFIX dbo:     <http://dbpedia.org/ontology/>"
                +"PREFIX dbpprop:<http://dbpedia.org/property/>"
                +"PREFIX dbp:<http://dbpedia.org/property/>"
                +"PREFIX foaf:<http://xmlns.com/foaf/0.1/>"
                +"PREFIX xsd:<http://www.w3.org/2001/XMLSchema#>"

                 + "\n"
                 + "select distinct (str(?resource) as ?movietitle) (xsd:float(?budget) as ?budget1) (xsd:float(?grossincome) as ?grossincome1) (xsd:float(?grossincome) - xsd:float(?budget) as ?result) (str(?Country) as ?country1) ?Director ?Distributor ?ReleaseDate where {\n"
                //+ "  ?movie a dbo:Film.\n"
                 + "  ?movie foaf:name ?resource.\n "
                +"?movie dbo:budget ?budget.\n"
                 +"?movie dbpprop:gross ?grossincome.\n"
                +"?movie dbp:country ?Country.\n"
                 +"?movie dbo:director ?dir."
                 +"?dir foaf:name ?Director.\n"
                +" ?movie dbp:distributor ?dist."
                +"?dist foaf:name ?Distributor."
                +" ?movie dbo:releaseDate ?ReleaseDate.\n"
                 + "  FILTER (lang(?resource) = 'en')"
                 + "FILTER (datatype(?grossincome) != '')"
                 + "FILTER (!regex(?grossincome, '-', 'i'))"
               +"FILTER regex(?Country, '"+country+"' , 'i').\n }");
    	
    	
    	
    	
    	
QueryExecution exec = QueryExecutionFactory.sparqlService("http://dbpedia.org/sparql", qs.asQuery());

        
        ResultSet results = exec.execSelect();

          
        List<String> em;
        em = results.getResultVars();
       System.out.println("inside search  country"+em.toString());

       // System.out.println(results);
        
//        while(results.hasNext()){
//        	System.out.println(results.next().get("Movie"));
//        }
        
       
       while (results.hasNext()) {

        	
        	QuerySolution bindings=results.next();

        	HashMap<String,String> moviedata = new HashMap<String,String>();
        	String title=bindings.get("movietitle").toString();
        	System.out.println("Title: "+title);
        	title=title.replaceAll("@en", "");
        	moviedata.put("title", title);

        	String dir=bindings.get("Director").toString();
        	dir=dir.replaceAll("@en", "");
        	System.out.println("Director: "+dir);
        	moviedata.put("director", dir);
        	
        	String dis=bindings.get("Distributor").toString();
        	dis=dis.replaceAll("@en", "");
        	System.out.println("Distributor: "+dis);
        	moviedata.put("distributor", dis);
        	
        	String budget=bindings.get("budget1").toString();
        	String budget1=budget.replace("^^http://www.w3.org/2001/XMLSchema#float","");
            System.out.println("Budget: "+budget1);
        	moviedata.put("budget", budget1);
        	
        	String gross=bindings.get("grossincome1").toString();
        	String gross1=gross.replace("^^http://www.w3.org/2001/XMLSchema#float","");
            System.out.println("Gross: "+gross1);
        	moviedata.put("gross", gross1);
        	
        	String result=bindings.get("result").toString();
        	String result1=result.replace("^^http://www.w3.org/2001/XMLSchema#float","");
        	if(result1.contains("-")){
        		result1="Loss :"+result1.replace("-", "");
        	}else{
        		result1="Profit :"+result1;	
        	}
            System.out.println("result: "+result1);
        	moviedata.put("result", result1);
        	
        	String country2=bindings.get("country1").toString();
        	country2=country2.replace("http://dbpedia.org/resource/", "");
            System.out.println("Country: "+country2);
        	moviedata.put("country", country2);
        	
        	String year=bindings.get("ReleaseDate").toString();
        	String year1=year.replace("^^http://www.w3.org/2001/XMLSchema#date", "");
        	moviedata.put("year",year1);
            System.out.println("Year:"+year1);
            movies.put(title, moviedata);
            
            

        }
        System.out.println("mmmmmmmmmmmmmmmmmm"+movies.size());
        //ResultSetFormatter.out(results);

        if(movies.size()==0){
        	request.setAttribute("nomovie", true);
        	System.out.println("hi");
        	request.setAttribute("movieData", this.movies);
            request.getRequestDispatcher("/WEB-INF/jsp/view/moviepage.jsp")
                   .forward(request, response);
        }else{
        
        //System.out.println("result as text: "+ResultSetFormatter.asText(results));
    	//System.out.println("inside search movie: length of arr "+arr.length);
        request.setAttribute("nomovie", false);
    	System.out.println("done");
    	request.setAttribute("movieData", this.movies);
        request.getRequestDispatcher("/WEB-INF/jsp/view/moviepage.jsp")
               .forward(request, response);
        }

    	
    	
    }
    
    
    private void searchMovieYear(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException{
    	
    	movies.clear();
    	int N=Integer.parseInt(request.getParameter("movienamedate"));
        System.out.println("N:"+N);
        int m=N+1;

    	ParameterizedSparqlString qs = new ParameterizedSparqlString(""
                + "prefix rdfs:    <http://www.w3.org/2000/01/rdf-schema#>\n"
                + "PREFIX dbo:     <http://dbpedia.org/ontology/>"
                +"PREFIX dbpprop:<http://dbpedia.org/property/>"
                +"PREFIX dbp:<http://dbpedia.org/property/>"
                +"PREFIX foaf:<http://xmlns.com/foaf/0.1/>"
                +"PREFIX xsd:<http://www.w3.org/2001/XMLSchema#>"

                 + "\n"
                 + "select distinct (str(?resource) as ?movietitle) (xsd:float(?budget) as ?budget1) (xsd:float(?grossincome) as ?grossincome1) (xsd:float(?grossincome) - xsd:float(?budget) as ?result) (str(?Country) as ?country1) ?Director ?Distributor ?ReleaseDate where {\n"
                //+ "  ?movie a dbo:Film.\n"
                 + "  ?movie foaf:name ?resource.\n "
                +"?movie dbo:budget ?budget.\n"
                 +"?movie dbpprop:gross ?grossincome.\n"
                +"?movie dbp:country ?Country.\n"
                 +"?movie dbo:director ?dir."
                 +"?dir foaf:name ?Director.\n"
                +" ?movie dbp:distributor ?dist."
                +"?dist foaf:name ?Distributor."
                +" ?movie dbo:releaseDate ?ReleaseDate.\n"
                 + "  FILTER (lang(?resource) = 'en')"
                 + "FILTER (datatype(?grossincome) != '')"
                 + "FILTER (!regex(?grossincome, '-', 'i'))"
               +"FILTER((?ReleaseDate >= \""+N+"-01-01\"^^xsd:date) && (?ReleaseDate < \""+m+"-01-01\"^^xsd:date)).}");
    	
    	
    	
    	
    	
QueryExecution exec = QueryExecutionFactory.sparqlService("http://dbpedia.org/sparql", qs.asQuery());

        
        ResultSet results = exec.execSelect();

          
        List<String> em;
        em = results.getResultVars();
       System.out.println("inside search movie year"+em.toString());

       // System.out.println(results);
        
//        while(results.hasNext()){
//        	System.out.println(results.next().get("Movie"));
//        }
        
      

       while (results.hasNext()) {

        	
        	QuerySolution bindings=results.next();

        	HashMap<String,String> moviedata = new HashMap<String,String>();
        	String title=bindings.get("movietitle").toString();
        	System.out.println("Title: "+title);
        	title=title.replaceAll("@en", "");
        	moviedata.put("title", title);

        	String dir=bindings.get("Director").toString();
        	dir=dir.replaceAll("@en", "");
        	System.out.println("Director: "+dir);
        	moviedata.put("director", dir);
        	
        	String dis=bindings.get("Distributor").toString();
        	dis=dis.replaceAll("@en", "");
        	System.out.println("Distributor: "+dis);
        	moviedata.put("distributor", dis);
        	
        	String budget=bindings.get("budget1").toString();
        	String budget1=budget.replace("^^http://www.w3.org/2001/XMLSchema#float","");
            System.out.println("Budget: "+budget1);
        	moviedata.put("budget", budget1);
        	
        	String gross=bindings.get("grossincome1").toString();
        	String gross1=gross.replace("^^http://www.w3.org/2001/XMLSchema#float","");
            System.out.println("Gross: "+gross1);
        	moviedata.put("gross", gross1);
        	
        	String result=bindings.get("result").toString();
        	String result1=result.replace("^^http://www.w3.org/2001/XMLSchema#float","");
        	if(result1.contains("-")){
        		result1="Loss :"+result1.replace("-", "");
        	}else{
        		result1="Profit :"+result1;	
        	}
            System.out.println("result: "+result1);
        	moviedata.put("result", result1);
        	
        	String country=bindings.get("country1").toString();
        	country=country.replace("http://dbpedia.org/resource/", "");
            System.out.println("Country: "+country);
        	moviedata.put("country", country);
        	
        	String year=bindings.get("ReleaseDate").toString();
        	String year1=year.replace("^^http://www.w3.org/2001/XMLSchema#date", "");
        	moviedata.put("year",year1);
            System.out.println("Year:"+year1);
            movies.put(title, moviedata);
            
            

        }
        System.out.println("inside search movie year"+movies.size());
        //ResultSetFormatter.out(results);

        if(movies.size()==0){
        	request.setAttribute("nomovie", true);
        	System.out.println("hi");
        	request.setAttribute("movieData", this.movies);
            request.getRequestDispatcher("/WEB-INF/jsp/view/moviepage.jsp")
                   .forward(request, response);
        }else{
        
        //System.out.println("result as text: "+ResultSetFormatter.asText(results));
    	//System.out.println("inside search movie: length of arr "+arr.length);
        request.setAttribute("nomovie", false);
    	System.out.println("done");
    	request.setAttribute("movieData", this.movies);
        request.getRequestDispatcher("/WEB-INF/jsp/view/moviepage.jsp")
               .forward(request, response);
        }

    	
    	
    }
    
    private void searchMovieGenre(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException{
    	
    	movies.clear();
    	String genre=request.getParameter("movienamegenre");
    	System.out.println(genre);
    	ParameterizedSparqlString qs = new ParameterizedSparqlString(""
                + "prefix rdfs:    <http://www.w3.org/2000/01/rdf-schema#>\n"
                + "PREFIX dbo:     <http://dbpedia.org/ontology/>"
                +"PREFIX dbpprop:<http://dbpedia.org/property/>"
                +"PREFIX dbp:<http://dbpedia.org/property/>"
                +"PREFIX foaf:<http://xmlns.com/foaf/0.1/>"
                +"PREFIX xsd:<http://www.w3.org/2001/XMLSchema#>"
                +"PREFIX mdb: <http://data.linkedmdb.org/resource/film>"
                +"PREFIX dc: <http://purl.org/dc/terms/>"
                +"PREFIX movie: <http://data.linkedmdb.org/resource/movie/>"
                 + "\n"
                 + " SELECT ?Title ?Date WHERE {\n"
                 + " ?o1 <http://data.linkedmdb.org/resource/movie/film_genre_name> ?value.\n"
                 + " ?m1 <http://data.linkedmdb.org/resource/movie/genre> ?o1.\n"
                		 + " ?m1 rdfs:label ?Title . \n"
                	+ " ?m1 <http://data.linkedmdb.org/resource/movie/initial_release_date> ?Date\n"
                	+ " FILTER regex(?value,'"+genre+"','i')}LIMIT 25\n");
    	
    	
    	QueryExecution exec = QueryExecutionFactory.sparqlService("http://www.linkedmdb.org/sparql", qs.asQuery());
    	exec.setTimeout(999999999);
        
        ResultSet results = exec.execSelect();

          
        List<String> em;
        em = results.getResultVars();
       System.out.println("inside search genre"+em.toString());

       // System.out.println(results);
        
//        while(results.hasNext()){
//        	System.out.println(results.next().get("Movie"));
//        }
        
      
       while (results.hasNext()) {

        	
        	QuerySolution bindings=results.next();

        	HashMap<String,String> moviedata = new HashMap<String,String>();
        	String title=bindings.get("Title").toString();
        	System.out.println("Title: "+title);
        	title=title.replaceAll("@en", "");
        	moviedata.put("title", title);

        	
        	String year=bindings.get("Date").toString();
        	String year1=year.replace("^^http://www.w3.org/2001/XMLSchema#date", "");
        	moviedata.put("year",year1);
            System.out.println("Year:"+year1);
            movies.put(title, moviedata);
            
            

        }
        System.out.println("inside search movie year"+movies.size());
        //ResultSetFormatter.out(results);

        if(movies.size()==0){
        	request.setAttribute("nomovie", true);
        	System.out.println("hi");
        	request.setAttribute("movieData", this.movies);
            request.getRequestDispatcher("/WEB-INF/jsp/view/movielist.jsp")
                   .forward(request, response);
        }else{
        
        //System.out.println("result as text: "+ResultSetFormatter.asText(results));
    	//System.out.println("inside search movie: length of arr "+arr.length);
        request.setAttribute("nomovie", false);
    	System.out.println("done");
    	request.setAttribute("movieData", this.movies);
        request.getRequestDispatcher("/WEB-INF/jsp/view/movielist.jsp")
               .forward(request, response);
        }

    	
    	
    }
    
    private void searchMovieDis(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
    {
    	String disname = request.getParameter("movienamedis");
    	movies.clear();
    	ParameterizedSparqlString qs = new ParameterizedSparqlString(""
                + "prefix rdfs:    <http://www.w3.org/2000/01/rdf-schema#>\n"
                + "PREFIX dbo:     <http://dbpedia.org/ontology/>"
                +"PREFIX dbpprop:<http://dbpedia.org/property/>"
                +"PREFIX dbp:<http://dbpedia.org/property/>"
                +"PREFIX foaf:<http://xmlns.com/foaf/0.1/>"
                +"PREFIX xsd:<http://www.w3.org/2001/XMLSchema#>"

                 + "\n"
                 + "select distinct (str(?resource) as ?movietitle) (xsd:float(?budget) as ?budget1) (xsd:float(?grossincome) as ?grossincome1) (xsd:float(?grossincome) - xsd:float(?budget) as ?result) (str(?Country) as ?country1) ?Director ?Distributor ?ReleaseDate where {\n"
                //+ "  ?movie a dbo:Film.\n"
                 + "  ?movie foaf:name ?resource.\n "
                +"?movie dbo:budget ?budget.\n"
                 +"?movie dbpprop:gross ?grossincome.\n"
                +"?movie dbp:country ?Country.\n"
                 +"?movie dbo:director ?dir."
                 +"?dir foaf:name ?Director.\n"
                +" ?movie dbp:distributor ?dist."
                +"?dist foaf:name ?Distributor."
                +" ?movie dbo:releaseDate ?ReleaseDate.\n"
                 + "  FILTER (lang(?resource) = 'en')"
                 + "FILTER (datatype(?grossincome) != '')"
                 + "FILTER (!regex(?grossincome, '-', 'i'))"
               +"FILTER regex(?Distributor, '"+disname+"' , 'i').}");
    	
    	
    	
    	
    	
QueryExecution exec = QueryExecutionFactory.sparqlService("http://dbpedia.org/sparql", qs.asQuery());

        
        ResultSet results = exec.execSelect();

          
        List<String> em;
        em = results.getResultVars();
       System.out.println(em.toString());

       // System.out.println(results);
        
//        while(results.hasNext()){
//        	System.out.println(results.next().get("Movie"));
//        }
        

        while (results.hasNext()) {

        	
        	QuerySolution bindings=results.next();

        	HashMap<String,String> moviedata = new HashMap<String,String>();
        	String title=bindings.get("movietitle").toString();
        	System.out.println("Title: "+title);
        	title=title.replaceAll("@en", "");
        	moviedata.put("title", title);

        	String dir=bindings.get("Director").toString();
        	dir=dir.replaceAll("@en", "");
        	System.out.println("Director: "+dir);
        	moviedata.put("director", dir);
        	
        	String dis=bindings.get("Distributor").toString();
        	dis=dis.replaceAll("@en", "");
        	System.out.println("Distributor: "+dis);
        	moviedata.put("distributor", dis);
        	
        	String budget=bindings.get("budget1").toString();
        	String budget1=budget.replace("^^http://www.w3.org/2001/XMLSchema#float","");
            System.out.println("Budget: "+budget1);
        	moviedata.put("budget", budget1);
        	
        	String gross=bindings.get("grossincome1").toString();
        	String gross1=gross.replace("^^http://www.w3.org/2001/XMLSchema#float","");
            System.out.println("Gross: "+gross1);
        	moviedata.put("gross", gross1);
        	
        	String result=bindings.get("result").toString();
        	String result1=result.replace("^^http://www.w3.org/2001/XMLSchema#float","");
        	if(result1.contains("-")){
        		result1="Loss :"+result1.replace("-", "");
        	}else{
        		result1="Profit :"+result1;	
        	}
            System.out.println("result: "+result1);
        	moviedata.put("result", result1);
        	
        	String country=bindings.get("country1").toString();
        	country=country.replace("http://dbpedia.org/resource/", "");
            System.out.println("Country: "+country);
        	moviedata.put("country", country);
        	
        	String year=bindings.get("ReleaseDate").toString();
        	String year1=year.replace("^^http://www.w3.org/2001/XMLSchema#date", "");
        	moviedata.put("year",year1);
            System.out.println("Year:"+year1);
            movies.put(title, moviedata);
            
            

        }
        System.out.println(movies.size());
        //ResultSetFormatter.out(results);

        if(movies.size()==0){
        	request.setAttribute("nomovie", true);
        	System.out.println("hi");
        	request.setAttribute("movieData", this.movies);
            request.getRequestDispatcher("/WEB-INF/jsp/view/moviepage.jsp")
                   .forward(request, response);
        }else{
        
        //System.out.println("result as text: "+ResultSetFormatter.asText(results));
    	//System.out.println("inside search movie: length of arr "+arr.length);
        request.setAttribute("nomovie", false);
    	System.out.println("done");
    	request.setAttribute("movieData", this.movies);
        request.getRequestDispatcher("/WEB-INF/jsp/view/moviepage.jsp")
               .forward(request, response);
        }
    }
    
    private void searchMovieDir(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
    {
    	String dirname = request.getParameter("movienamedir");
    	movies.clear();
    	ParameterizedSparqlString qs = new ParameterizedSparqlString(""
                + "prefix rdfs:    <http://www.w3.org/2000/01/rdf-schema#>\n"
                + "PREFIX dbo:     <http://dbpedia.org/ontology/>"
                +"PREFIX dbpprop:<http://dbpedia.org/property/>"
                +"PREFIX dbp:<http://dbpedia.org/property/>"
                +"PREFIX foaf:<http://xmlns.com/foaf/0.1/>"
                +"PREFIX xsd:<http://www.w3.org/2001/XMLSchema#>"

                 + "\n"
                 + "select distinct (str(?resource) as ?movietitle) (xsd:float(?budget) as ?budget1) (xsd:float(?grossincome) as ?grossincome1) (xsd:float(?grossincome) - xsd:float(?budget) as ?result) (str(?Country) as ?country1) ?Director ?Distributor ?ReleaseDate where {\n"
                //+ "  ?movie a dbo:Film.\n"
                 + "  ?movie foaf:name ?resource.\n "
                +"?movie dbo:budget ?budget.\n"
                 +"?movie dbpprop:gross ?grossincome.\n"
                +"?movie dbp:country ?Country.\n"
                 +"?movie dbo:director ?dir."
                 +"?dir foaf:name ?Director.\n"
                +" ?movie dbp:distributor ?dist."
                +"?dist foaf:name ?Distributor."
                +" ?movie dbo:releaseDate ?ReleaseDate.\n"
                 + "  FILTER (lang(?resource) = 'en')"
                 + "FILTER (datatype(?grossincome) != '')"
                 + "FILTER (!regex(?grossincome, '-', 'i'))"
               +"FILTER regex(?Director, '"+dirname+"' , 'i').}");
    	
    	
QueryExecution exec = QueryExecutionFactory.sparqlService("http://dbpedia.org/sparql", qs.asQuery());

        
        ResultSet results = exec.execSelect();

          
        List<String> em;
        em = results.getResultVars();
       System.out.println(em.toString());

       // System.out.println(results);
        
//        while(results.hasNext()){
//        	System.out.println(results.next().get("Movie"));
//        }
        

        while (results.hasNext()) {

        	
        	QuerySolution bindings=results.next();

        	HashMap<String,String> moviedata = new HashMap<String,String>();
        	String title=bindings.get("movietitle").toString();
        	title=title.replaceAll("@en", "");
        	System.out.println("Title: "+title);
        	moviedata.put("title", title);

        	String dir=bindings.get("Director").toString();
        	dir=dir.replaceAll("@en", "");
        	System.out.println("Director: "+dir);
        	moviedata.put("director", dir);
        	
        	String dis=bindings.get("Distributor").toString();
        	dis=dis.replaceAll("@en", "");
        	System.out.println("Distributor: "+dis);
        	moviedata.put("distributor", dis);
        	
        	String budget=bindings.get("budget1").toString();
        	String budget1=budget.replace("^^http://www.w3.org/2001/XMLSchema#float","");
            System.out.println("Budget: "+budget1);
        	moviedata.put("budget", budget1);
        	
        	String gross=bindings.get("grossincome1").toString();
        	String gross1=gross.replace("^^http://www.w3.org/2001/XMLSchema#float","");
            System.out.println("Gross: "+gross1);
        	moviedata.put("gross", gross1);
        	
        	String result=bindings.get("result").toString();
        	String result1=result.replace("^^http://www.w3.org/2001/XMLSchema#float","");
        	if(result1.contains("-")){
        		result1="Loss :"+result1.replace("-", "");
        	}else{
        		result1="Profit :"+result1;	
        	}
            System.out.println("result: "+result1);
        	moviedata.put("result", result1);
        	
        	String country=bindings.get("country1").toString();
        	country=country.replace("http://dbpedia.org/resource/", "");
            System.out.println("Country: "+country);
        	moviedata.put("country", country);
        	
        	String year=bindings.get("ReleaseDate").toString();
        	String year1=year.replace("^^http://www.w3.org/2001/XMLSchema#date", "");
        	moviedata.put("year",year1);
            System.out.println("Year:"+year1);
            movies.put(title, moviedata);
            
            

        }
        System.out.println(movies.size());
        //ResultSetFormatter.out(results);

        if(movies.size()==0){
        	request.setAttribute("nomovie", true);
        	System.out.println("hi");
        	request.setAttribute("movieData", this.movies);
            request.getRequestDispatcher("/WEB-INF/jsp/view/moviepage.jsp")
                   .forward(request, response);
        }else{
        
        //System.out.println("result as text: "+ResultSetFormatter.asText(results));
    	//System.out.println("inside search movie: length of arr "+arr.length);
        request.setAttribute("nomovie", false);
    	System.out.println("done");
    	request.setAttribute("movieData", this.movies);
        request.getRequestDispatcher("/WEB-INF/jsp/view/moviepage.jsp")
               .forward(request, response);
        }
    	
    }
    
    private void searchMovie(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
    {
    	String moviename = request.getParameter("moviename");
    	movies.clear();
    	System.out.println("Inside search movie method: first line");

    	ParameterizedSparqlString qs = new ParameterizedSparqlString(""
               + "prefix rdfs:    <http://www.w3.org/2000/01/rdf-schema#>\n"
               + "PREFIX dbo:     <http://dbpedia.org/ontology/>"
               +"PREFIX dbpprop:<http://dbpedia.org/property/>"
               +"PREFIX dbp:<http://dbpedia.org/property/>"
               +"PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>"

               +"PREFIX foaf:<http://xmlns.com/foaf/0.1/>"
                + "\n"
               + "select distinct (str(?resource) as ?movietitle) (xsd:float(?budget) as ?budget1) (xsd:float(?grossincome) as ?grossincome1) (xsd:float(?grossincome) - xsd:float(?budget) as ?result) (str(?Country) as ?country1) ?Director ?Distributor ?ReleaseDate where {\n"
               + "  ?movie a dbo:Film.\n"
                + "  ?movie foaf:name ?resource.\n "
               +"?movie dbo:budget ?budget.\n"
                +"?movie dbpprop:gross ?grossincome.\n"
               +"?movie dbp:country ?Country.\n"
                +"?movie dbo:director ?dir."
                +"?dir foaf:name ?Director.\n"
                +" ?movie dbp:distributor ?dist."
                +"?dist foaf:name ?Distributor."
               +" ?movie dbo:releaseDate ?ReleaseDate.\n"
                + "  FILTER (lang(?resource) = 'en')"
                + "FILTER (datatype(?grossincome) != '')"
                + "FILTER (!regex(?grossincome, '-', 'i'))"
              +"FILTER regex(?resource, '"+moviename+"' , 'i').}");


    	QueryExecution exec = QueryExecutionFactory.sparqlService("http://dbpedia.org/sparql", qs.asQuery());

        
        ResultSet results = exec.execSelect();

          
        List<String> em;
        em = results.getResultVars();
       System.out.println(em.toString());

       // System.out.println(results);
        
//        while(results.hasNext()){
//        	System.out.println(results.next().get("Movie"));
//        }
        

        while (results.hasNext()) {

        	
        	QuerySolution bindings=results.next();

        	HashMap<String,String> moviedata = new HashMap<String,String>();
        	String title=bindings.get("movietitle").toString();
        	System.out.println("Title: "+title);
        	title=title.replaceAll("@en", "");
        	moviedata.put("title", title);

        	String dir=bindings.get("Director").toString();
        	dir=dir.replaceAll("@en", "");
        	System.out.println("Director: "+dir);
        	moviedata.put("director", dir);
        	
        	String dis=bindings.get("Distributor").toString();
        	dis=dis.replaceAll("@en", "");
        	System.out.println("Distributor: "+dis);
        	moviedata.put("distributor", dis);
        	
        	String budget=bindings.get("budget1").toString();
        	String budget1=budget.replace("^^http://www.w3.org/2001/XMLSchema#float","");
            System.out.println("Budget: "+budget1);
        	moviedata.put("budget", budget1);
        	
        	String gross=bindings.get("grossincome1").toString();
        	String gross1=gross.replace("^^http://www.w3.org/2001/XMLSchema#float","");
            System.out.println("Gross: "+gross1);
        	moviedata.put("gross", gross1);
        	
        	String result=bindings.get("result").toString();
        	String result1=result.replace("^^http://www.w3.org/2001/XMLSchema#float","");
        	if(result1.contains("-")){
        		result1="Loss :"+result1.replace("-", "");
        	}else{
        		result1="Profit :"+result1;	
        	}
            System.out.println("result: "+result1);
        	moviedata.put("result", result1);
        	
        	String country=bindings.get("country1").toString();
        	country=country.replace("http://dbpedia.org/resource/", "");
            System.out.println("Country: "+country);
        	moviedata.put("country", country);
        	
        	String year=bindings.get("ReleaseDate").toString();
        	String year1=year.replace("^^http://www.w3.org/2001/XMLSchema#date", "");
        	moviedata.put("year",year1);
            System.out.println("Year:"+year1);
            movies.put(title, moviedata);
            

        }
        System.out.println(movies.size());
        //ResultSetFormatter.out(results);

        if(movies.size()==0){
        	request.setAttribute("nomovie", true);
        	System.out.println("hi");
        	request.setAttribute("movieData", this.movies);
            request.getRequestDispatcher("/WEB-INF/jsp/view/moviepage.jsp")
                   .forward(request, response);
        }else{
        
        //System.out.println("result as text: "+ResultSetFormatter.asText(results));
    	//System.out.println("inside search movie: length of arr "+arr.length);
        request.setAttribute("nomovie", false);
    	System.out.println("done");
    	request.setAttribute("movieData", this.movies);
        request.getRequestDispatcher("/WEB-INF/jsp/view/moviepage.jsp")
               .forward(request, response);
        }
    }
    

    
}
