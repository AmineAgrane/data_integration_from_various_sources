package local_db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.mysql.jdbc.Statement;

public class JdbcConnection {
	
	// Local database data
	private static String host = "localhost";
	private static String base = "ied";
	private static String user = "root";
	private static String password = "";
	private static String url = "jdbc:mysql://"+host+"/"+ base;


	// Establish connection with the local database.
	public static Connection getConnection() {
		Connection connection = null;
			try {
				DriverManager.registerDriver(new com.mysql.jdbc.Driver());
				connection = DriverManager.getConnection(url, user, password);
			} 
			catch (Exception e) {
				System.err.println("Connection to local database failed : " + e.getMessage());
			}
		return connection;
	}
	
	// SQL Query to get movie's budget infos.
	public static String[] getMovieBudget(String movie_name) throws SQLException {
		 String movie_budget_query = "SELECT * FROM moviesBudgets WHERE Movie='"+movie_name+"'";
		 Connection connection = getConnection();
		 Statement statement = (Statement) connection.createStatement();
		 ResultSet rs = statement.executeQuery(movie_budget_query);
		 if (rs.next() == false) {
		      System.out.println("No budget for this movie\n");
		      return null;
		    }
		 else {
			 String[] movie_budget = { rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6)};
		     return movie_budget;
		 }
		}
	
	
	//SQL Query to get movie's genre infos.
	public static String[] getMovieGenre(String movie_name) throws SQLException {
		 String movie_genre_query = "SELECT * FROM moviesGenres WHERE movie_name='"+movie_name+"'";
		 Connection connection = getConnection();
		 Statement statement = (Statement) connection.createStatement();
		 ResultSet result = statement.executeQuery(movie_genre_query);
		 if (result.next() == false) {
		      System.out.println("No genre for this movie\n");
		      return null;
		    }
		 else {
			 String[] movie_genre = { result.getString(1), result.getString(2), result.getString(3), result.getString(4)}; 
			 return movie_genre;
		 }  
		}
	
	//Get all available informations about the given movie from the local database
	public static ArrayList<String> getMovieInfosDB(String movie_name) throws SQLException {
		ArrayList<String> movie_infos = new ArrayList<String>();
		String[] movie_budget = getMovieBudget(movie_name);
		String[] movie_genre = getMovieGenre(movie_name);
		if(movie_budget != null) {
			movie_infos.add("Release Date : "+movie_budget[1]);
			movie_infos.add("Production Budget : "+movie_budget[3]);
			movie_infos.add("Domestic Gross : "+movie_budget[4]);
			movie_infos.add("Worldwide Gross : "+movie_budget[5]);
		}
		else { movie_infos.add(null);movie_infos.add(null);movie_infos.add(null);movie_infos.add(null); }
		
		if(movie_genre != null) {
			movie_infos.add("Movie Distributor : "+movie_genre[2]);
			movie_infos.add("Movie Genre : "+movie_genre[3]);
		}
		else { movie_infos.add(null);movie_infos.add(null); }
		
		return movie_infos;
	}
	
	
	public static void main(String[] args) throws Exception {
		ArrayList<String> movie_infos = getMovieInfosDB("The Future");
		for(String s:movie_infos) {
			System.out.println(s);
		}

	}
}