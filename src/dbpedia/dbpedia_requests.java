package dbpedia;
import java.util.ArrayList;
import java.util.List;
import javax.management.Query;
import org.apache.jena.query.ParameterizedSparqlString;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.query.ResultSetFormatter;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.ResourceFactory;


public class dbpedia_requests {
	public static final String SERVICE = "http://dbpedia.org/sparql";
	
	public static final String PREFIX = ""     
            + "prefix rdfs:    <http://www.w3.org/2000/01/rdf-schema#>\n" 
			+ "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>"
            + "PREFIX dbo:     <http://dbpedia.org/ontology/>"
            + "PREFIX foaf: <http://xmlns.com/foaf/0.1/>"
            + "PREFIX : <http://dbpedia.org/resource/> \n" ;	
	
	public static List<String>  getActeurs(String title) {
		String acteur = "";
		List<String> acteurs_list = new ArrayList<String>();
		String string_query = "SELECT ?aname WHERE {\n" + 
				"?film a dbo:Film ;\n" + 
				"         foaf:name \"MOVIE_TITLE\"@en ;\n".replaceAll("MOVIE_TITLE",title) +
				"         dbo:starring ?actor .\n" + 
				"?actor foaf:name ?aname.\n}";
		

        ParameterizedSparqlString  qs = new ParameterizedSparqlString(PREFIX +string_query);
        QueryExecution exec = QueryExecutionFactory.sparqlService(SERVICE, qs.asQuery());
        ResultSet results = exec.execSelect();
        
        while (results.hasNext()) {
        	QuerySolution solution = results.nextSolution();
        	acteur = solution.getLiteral("?aname").toString().replace("@en", "");
            acteurs_list.add(acteur);
        }
		return acteurs_list;
	}
	
	public static List<String> getProducteurs(String title) {
		String acteur = "";
		List<String> acteurs_list = new ArrayList<String>();
		String string_query = "SELECT ?colname WHERE { \n" +
				"?film a dbo:Film ; \n" + 
				"foaf:name \"MOVIE_TITLE\"@en ; \n".replaceAll("MOVIE_TITLE",title) + 
				"dbo:producer ?producer . \n" + 
				"?producer foaf:name ?colname . }";
		

        ParameterizedSparqlString  qs = new ParameterizedSparqlString(PREFIX +string_query);
        QueryExecution exec = QueryExecutionFactory.sparqlService(SERVICE, qs.asQuery());
        ResultSet results = exec.execSelect();
        
        while (results.hasNext()) {
        	QuerySolution solution = results.nextSolution();
        	System.out.println(solution.toString());
        	acteur = solution.getLiteral("?colname").toString().replace("@en", "");
            acteurs_list.add(acteur);
        }
		return acteurs_list;
	}
	
	public static List<String> getRealisateur(String title) {
		String realisateur="";
		List<String> realisateur_list = new ArrayList<String>();
		String string_query = "SELECT $name WHERE {\n" + 
				"$film a dbo:Film ;\n" + 
				"         foaf:name \"MOVIE_TITLE\"@en .\n".replace("MOVIE_TITLE",title) + 
				"$film dbo:director $name .\n}";

        ParameterizedSparqlString  qs = new ParameterizedSparqlString(PREFIX +string_query);
        QueryExecution exec = QueryExecutionFactory.sparqlService(SERVICE, qs.asQuery());
        ResultSet results = exec.execSelect();
        while (results.hasNext()) {
        	String[] solution = results.nextSolution().toString().split("/");
        	realisateur = solution[solution.length-1].replace("MOVIE_TITLE",title);
        	realisateur = realisateur.replace("_"," "); realisateur = realisateur.replace("> )",""); 
        	realisateur_list.add(realisateur);
        }
		
		return realisateur_list;
	}
	
	// Take an actor name and returns a list of movies where he plays in.
	public static List<String> getFilms(String acteur) {
		String movie = "";
		List<String> movies_list = new ArrayList<String>();
		String string_query = "SELECT $title WHERE {\n $film rdf:type dbo:Film;\n" + 
                "         foaf:name $title;\n dbo:starring $acteur.\n" + 
                "$acteur foaf:name \"ACTOR_NAME\"@en\n}".replaceAll("ACTOR_NAME",acteur);
		

        ParameterizedSparqlString  qs = new ParameterizedSparqlString(PREFIX +string_query);
        QueryExecution exec = QueryExecutionFactory.sparqlService(SERVICE, qs.asQuery());
        ResultSet results = exec.execSelect();
        
        while (results.hasNext()) {
        	QuerySolution solution = results.nextSolution();
        	movie = solution.getLiteral("?title").toString().replace("@en", "");
        	movies_list.add(movie);
        }
		
		return movies_list;
	}

	public static void main(String[] args) {
		//System.out.println(getProducteurs("Aliens"));
		//System.out.println(getActeurs("Braveheart"));
		//System.out.println(getFilms("Angelina Jolie"));
		System.out.println(getRealisateur("Ed Wood"));
    }


}
