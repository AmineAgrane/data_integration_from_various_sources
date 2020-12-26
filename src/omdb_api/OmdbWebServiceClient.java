package omdb_api;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

//import org.json.simple.*;
//import org.json.simple.parser.JSONParser;
//import org.json.simple.parser.ParseException;



public class OmdbWebServiceClient {
	
	// API url 
	public static final String SEARCH_URL_TITLE = "http://www.omdbapi.com/?s=TITLE&apikey=852159f0 ";
	public static final String SEARCH_URL_IMDB =  "http://www.omdbapi.com/?i=IMDB&apikey=852159f0 ";

	
	// Send a get request and returns a response in json string format.
	public static String sendGetRequests(String requestUrl)  {
		StringBuffer response = new StringBuffer();
		try {
		URL url = new URL(requestUrl);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod("GET");
		connection.setRequestProperty("Accept", "*/*");
		connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8 ");
		InputStream stream = connection.getInputStream();
		InputStreamReader reader = new InputStreamReader(stream);
		BufferedReader buffer = new BufferedReader(reader);
		String ligne = "";
		while( (ligne = buffer.readLine()) != null) {
			response.append(ligne);
		}
		buffer.close();
		connection.disconnect(); 
		} catch (IOException e) { e.printStackTrace(); }
		
		return response.toString(); 
	} 
	
	
	// GET request based on movie title. Returns a json string
	public static String searchMovieByTitle(String title) {
		try {
			title = URLEncoder.encode(title,"UTF-8");
		} catch (UnsupportedEncodingException e) { e.printStackTrace();}
		String requestUrl = SEARCH_URL_TITLE 
				.replaceAll("TITLE",title);
		return sendGetRequests(requestUrl);
		}
	
	
	// GET request based on movie IMDB id. Returns a json string
	public static String searchMovieByImdb(String imdb) {
		String requestUrl = SEARCH_URL_IMDB 
				.replaceAll("IMDB",imdb);
		return sendGetRequests(requestUrl);
	}
/*	
	// Take a movie title and returns its corresponding IMDB id
	public static String getMovieImdbId(String title) throws ParseException {
		// Get jsons response in String format
		String jsonResponse = searchMovieByTitle(title);
		
        // Parsing json to extract the fist result IMDB id.
		JSONObject json = (JSONObject) new JSONParser().parse(jsonResponse); 
        JSONArray  json_array = (JSONArray) json.get("Search");
        JSONObject json_first_result = (JSONObject) json_array.get(0);
        
        return json_first_result.get("imdbID").toString();
	}

	
	// Take a movie name and returns its plot.
	public static void getMoviePlot(String title) throws ParseException {
		String movie_imdb_id = getMovieImdbId(title);
		String jsonResponse = searchMovieByImdb(movie_imdb_id);
		
        // Parsing json to extract the plot attribute.
		JSONObject json = (JSONObject) new JSONParser().parse(jsonResponse);
		String plot =  json.get("Plot").toString();
		System.out.println(plot);
		//return plot.toJSONString();
	
	}
	
	public static void main(String[] args) throws ParseException {
		// System.out.println(searchMovieByImdb(getMovieImdbId("The Thing")));
		getMoviePlot("Parasite");

	}
*/
}
