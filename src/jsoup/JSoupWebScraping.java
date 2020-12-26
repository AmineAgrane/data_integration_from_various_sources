package jsoup;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import com.opencsv.CSVWriter;
import java.util.*; 

public class JSoupWebScraping {
	
	public static final String   WEBSITE_URL = "https://www.the-numbers.com/market/ANNEE/genre/GENRE";
	public static final String[] GENRES_LIST = {"Adventure", "Comedy", "Drama", "Action", "Thriller-or-Suspense", "Romantic-Comedy"};
	public static final String[] YEARS_LIST = {"2000", "2001", "2002", "2003", "2004", "2005", "2006", "2007",
	                                                   "2008", "2009", "2010", "2011", "2012", "2013", "2014", "2015"};
	
   public static ArrayList<ArrayList<String>>  allMoviesbyGenreYear(String genre, String annee) throws Exception {
		System.out.println("Running..."+annee+" : "+ genre);
		ArrayList<ArrayList<String>> MOVIES = new ArrayList<ArrayList<String>>();
		ArrayList<String> movie = new ArrayList<String>();

			
		// Get Document object after parsing the html from given url.
		Document document = Jsoup.connect(WEBSITE_URL.replace("ANNEE", annee).replace("GENRE", genre)).get();
			
		// Query the html tag taht contains the data about the movies
		Elements main = document.select("div#main");
		Elements data = document.select("div#page_filling_chart").get(1).select("tbody > tr");
		
		// Extract data into ArrayList<ArrayList<String>>
		for (Element element : data) {
			if(Character.isDigit(element.text().charAt(0))) {
				movie = new ArrayList<String>();
				movie.add(genre+"_"+annee+"_"+element.select("td").get(0).text());  // id film
				movie.add(element.select("td").get(1).text());  // titre  film
				movie.add(element.select("td").get(3).text());  // distributeur film
				MOVIES.add(movie);
				}
			}	
		return MOVIES;
	}
   
   
   public static ArrayList<ArrayList<String>>  allMoviesGenre(String genre) throws Exception {
	   ArrayList<ArrayList<String>> MOVIES_ALL_YEARS = new ArrayList<ArrayList<String>>();
	   ArrayList<ArrayList<String>> MOVIES_ONE_YEARS = new ArrayList<ArrayList<String>>();
	   
	    // We grab all the movies of the given genre from [2006 to 2015]
		for (String annee : YEARS_LIST) {
			MOVIES_ONE_YEARS = allMoviesbyGenreYear(genre,annee);
			MOVIES_ALL_YEARS.addAll(MOVIES_ONE_YEARS);
	    }
		
	   return MOVIES_ALL_YEARS;
   }
	
   public static void writeMoviesGenretoCSV(ArrayList<ArrayList<String>> MOVIES_ALL_YEARS, String genre) throws IOException 
   {
	       // create FileWriter object with file as parameter 
	       String filePath = "src/jsoup/GENRE.csv".replace("GENRE", genre);
           File file = new File(filePath); 
    	   FileWriter outputfile = new FileWriter(file); 
     
           // create CSVWriter object filewriter object as parameter 
           CSVWriter writer = new CSVWriter(outputfile); 
     
           // adding header to csv 
           String[] header = { "movie_id", "movie_name", "movie_distributor", "movie_genre"}; 
           writer.writeNext(header); 
           
   		   for (ArrayList<String>  movie : MOVIES_ALL_YEARS) {
            String[] row = { movie.get(0), movie.get(1), movie.get(2), genre}; 
            writer.writeNext(row); 
            }

           writer.close(); 
   } 
	
   
public static void main(String[] args) throws Exception {
	
	for (String genre : GENRES_LIST) {
		writeMoviesGenretoCSV(allMoviesGenre(genre),genre);
	}
}
}
