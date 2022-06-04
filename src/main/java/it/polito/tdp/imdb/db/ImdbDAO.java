package it.polito.tdp.imdb.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import it.polito.tdp.imdb.model.Actor;
import it.polito.tdp.imdb.model.Adiacenza;
import it.polito.tdp.imdb.model.Director;
import it.polito.tdp.imdb.model.Movie;

public class ImdbDAO {
	
	public List<Actor> listAllActors(){
		String sql = "SELECT * FROM actors";
		List<Actor> result = new ArrayList<Actor>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Actor actor = new Actor(res.getInt("id"), res.getString("first_name"), res.getString("last_name"),
						res.getString("gender"));
				
				result.add(actor);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<Movie> listAllMovies(){
		String sql = "SELECT * FROM movies";
		List<Movie> result = new ArrayList<Movie>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Movie movie = new Movie(res.getInt("id"), res.getString("name"), 
						res.getInt("year"), res.getDouble("rank"));
				
				result.add(movie);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	public void listAllDirectors(Map<Integer,Director> idMap){
		String sql = "SELECT * FROM directors";
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Director director = new Director(res.getInt("id"), res.getString("first_name"), res.getString("last_name"));
				
				if(!idMap.containsKey(director.getId()))
					idMap.put(director.getId(), director);
			}
			conn.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public List<Director> getVertici(int anno) {
		String sql="SELECT d.id, d.first_name, d.last_name "
				+ "FROM directors d, ( "
				+ "SELECT DISTINCT md.director_id AS Tid "
				+ "FROM movies m, movies_directors md "
				+ "WHERE m.id = md.movie_id "
				+ "AND m.year = ?) AS T "
				+ "WHERE d.id = T.Tid";
		List<Director> result = new ArrayList<Director>();
		Connection conn = DBConnect.getConnection();
		
		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, anno);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Director director = new Director(res.getInt("d.id"), res.getString("d.first_name"), res.getString("d.last_name"));
				result.add(director);
				
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		
	}
	
	public List<Adiacenza> getAdiacenze(Map<Integer,Director> idMap,int anno) {
		String sql="SELECT md1.director_id, md2.director_id, COUNT(DISTINCT r1.actor_id) "
				+ "FROM movies_directors md1, movies_directors md2, "
				+ "movies m1, movies m2, "
				+ "roles r1, roles r2 "
				+ "WHERE md1.director_id < md2.director_id "
				+ "AND md1.movie_id = m1.id AND md2.movie_id = m2.id "
				+ "AND m1.year = m2.year AND m1.year = ? "
				+ "AND m1.id = r1.movie_id AND m2.id = r2.movie_id "
				+ "AND r1.actor_id = r2.actor_id "
				+ "GROUP BY md1.director_id, md2.director_id";
		List<Adiacenza> result = new ArrayList<Adiacenza>();
		Connection conn = DBConnect.getConnection();
		
		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, anno);
			ResultSet res = st.executeQuery();
			
			while(res.next()) {
				Adiacenza a = new Adiacenza(idMap.get(res.getInt("md1.director_id")), idMap.get(res.getInt("md2.director_id")), res.getInt("COUNT(DISTINCT r1.actor_id)"));
				result.add(a);
			}
			
			conn.close();
			return result;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	
}
