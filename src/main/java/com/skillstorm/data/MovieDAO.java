package com.skillstorm.data;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeSet;

import com.mysql.cj.x.protobuf.MysqlxSql.StmtExecute;
import com.skillstorm.beans.Movie;

public class MovieDAO implements MovieDAOInterface, AutoCloseable{
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try(MovieDAO dao = new MovieDAO()) {
			//Movie movie = new Movie("Police Story", "Kung-Fu", "451-453", true, null);
			//dao.save(movie);
			//Movie movie = dao.findFirst();
			TreeSet<Movie> movies = dao.findAll();
//			for(int i = 0; i < movies.size(); i++) {
//				System.out.println(movies.get(i).getName());
//			}
			//dao.delete("ID", 100);
			//dao.update();
			//System.out.println(dao.isAvailable("Spirited Away"));
			//dao.movieReturned(87);
//			String string_date = "30-July-2021";
//
//			SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
//			Date d = null;
//			try {
//			    d = (Date) f.parse(string_date);
//			    //long rented = d.getTime();
//			} catch (ParseException e) {
//			    e.printStackTrace();
//			}
//			dao.movieRented(1, d);

//			Calendar cal;
//			//cal.
//			Date date;
//			date.setYear(2021);
//			Date.valueOf("2021-07-30");
			//dao.movieRented(106, Date.valueOf("2021-07-30"));
			//System.out.println("deleted");
		} catch (Exception e) {
			System.out.println("Failed");
			e.printStackTrace();
		}
		
	}
	
	private Connection connection;
	
	public MovieDAO() {
		
	}
	
	public Connection getConnection() {
		return connection;
	}

	public void connect() throws Exception {
		String url = "jdbc:mysql://localhost:3307/java_project";
		String username = "root";
		String password = "root";
		
		Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
		this.connection = DriverManager.getConnection(url, username, password);
		System.out.println("Connected");
	}
	@Override
	public Movie save(Movie movie) throws SQLException {
		// TODO Auto-generated method stub
		try {
			connect();
		} catch (Exception e) {
			e.printStackTrace();
		}
		String sql = "INSERT INTO MOVIE(NAME, GENRE, MOVIE_ID, AVAILABILITY, NEXT_AVAILABLE_TIME) "
				+ "VALUES (?, ?, ?, ?, ?)";
		PreparedStatement stmt =  connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
		stmt.setString(1, movie.getName());
		stmt.setString(2, movie.getGenre());
		stmt.setString(3, movie.getMovie_id());
		stmt.setBoolean(4, movie.isAvailability());
		stmt.setDate(5, movie.getNext_available_time());
		stmt.executeUpdate();
		ResultSet rs = stmt.getGeneratedKeys();
		rs.next();
		movie.setId(rs.getInt(1));
		System.out.println("Saved");
		return movie;
	}
	
	// Change to TreeSet
	@Override
	public TreeSet<Movie> findAll() throws SQLException {
		// TODO Auto-generated method stub
		try {
			connect();
		} catch (Exception e) {
			e.printStackTrace();
		}
		String sql = "SELECT * FROM MOVIE";
		TreeSet<Movie> movies = new TreeSet<Movie>();
		PreparedStatement stmt = connection.prepareStatement(sql);
		ResultSet rs = stmt.executeQuery();		
		while(rs.next()) {
			Movie movie = new Movie(rs.getString("NAME"), rs.getString("GENRE"), rs.getString("movie_id"),
					rs.getBoolean("availability"), rs.getDate("next_available_time"));
			movie.setId(rs.getInt("ID"));
			if(movie.getNext_available_time() != null) {
				movie.setLocalDate();
			}
			movies.add(movie);
		}
		return movies;
	}
	
	@Override
	public Movie findFirst() throws SQLException {
		// TODO Auto-generated method stub
		try {
			connect();
		} catch (Exception e) {
			e.printStackTrace();
		}
		String sql = "SELECT ID, NAME, GENRE, MOVIE_ID, AVAILABILITY, "
				+ "NEXT_AVAILABLE_TIME FROM MOVIE";
		PreparedStatement stmt = connection.prepareStatement(sql);
		ResultSet rs = stmt.executeQuery();
		rs.next();
		Movie movie = new Movie(rs.getString("NAME"), rs.getString("GENRE"), rs.getString("movie_id"),
				rs.getBoolean("availability"), rs.getDate("next_available_time"));
		movie.setId(rs.getInt("ID"));
		return movie;
	}
	
	@Override
	public void close() throws Exception {
		// TODO Auto-generated method stub
		if(this.connection != null && !connection.isClosed()) {
			this.connection.close();
		}
		
	}

	@Override
	public int delete(int id) throws SQLException {
		// TODO Auto-generated method stub
		try {
			connect();
		} catch (Exception e) {
			e.printStackTrace();
		}
		String sql = "DELETE FROM MOVIE WHERE ID = ?";
		PreparedStatement stmt = connection.prepareStatement(sql);
		stmt.setInt(1, id);
		int rows = stmt.executeUpdate();
		return rows;
	}

	@Override
	public boolean update() throws SQLException {
		// TODO Auto-generated method stub
		try {
			connect();
		} catch (Exception e) {
			e.printStackTrace();
		}
		String sql = "UPDATE MOVIE SET NEXT_AVAILABLE_TIME = NULL WHERE AVAILABILITY = ?";
		PreparedStatement stmt = connection.prepareStatement(sql);
		stmt.setBoolean(1, true);
		int success = stmt.executeUpdate();
		return success > 0 ? true : false;
	}

	@Override
	public boolean isAvailable(String movieName) throws SQLException {
		// TODO Auto-generated method stub
		try {
				connect();
		} catch (Exception e) {
			e.printStackTrace();
		}
		String sql = "SELECT AVAILABILITY, NEXT_AVAILABLE_TIME FROM MOVIE WHERE NAME = ?";
		PreparedStatement stmt = connection.prepareStatement(sql);
		stmt.setString(1, movieName);
		ResultSet rs = stmt.executeQuery();
		rs.next();
		if(rs.getBoolean(1) == true) {
			return true;
		} else {
			System.out.println(rs.getDate(2));
			return false;
		}
	}

	@Override
	public int movieReturned(int id) throws SQLException {
		// TODO Auto-generated method stub
		try {
			connect();
		} catch (Exception e) {
			e.printStackTrace();
		}
		String sql = "UPDATE MOVIE SET AVAILABILITY = true WHERE ID = ?";
		PreparedStatement stmt = connection.prepareStatement(sql);
		stmt.setInt(1, id);
		int success = stmt.executeUpdate();
		this.updateAvailabilty(id, false, null);
		return success;
	}

	@Override
	public boolean updateAvailabilty(int id, boolean rented, Date date) throws SQLException {
		// TODO Auto-generated method stub
		try {
			connect();
		} catch (Exception e) {
			e.printStackTrace();
		}
		String sql = "";
		PreparedStatement stmt;
		if(!rented) {
			sql = "UPDATE MOVIE SET NEXT_AVAILABLE_TIME = null WHERE ID = ?";
			stmt = connection.prepareStatement(sql);
			stmt.setInt(1, id);
		} else {
			sql = "UPDATE MOVIE SET NEXT_AVAILABLE_TIME = ? WHERE ID = ?";
			stmt = connection.prepareStatement(sql);
			stmt.setDate(1, date);
			stmt.setInt(2, id);
		}
		int success = stmt.executeUpdate();
		return success > 0 ? true : false;
	}

	// Sends wrong date...!
	@Override
	public int movieRented(int id, Date date) throws SQLException {
		// TODO Auto-generated method stub
		try {
			connect();
		} catch (Exception e) {
			e.printStackTrace();
		}
		String sql = "UPDATE MOVIE SET AVAILABILITY = false WHERE ID = ?";
		PreparedStatement stmt = connection.prepareStatement(sql);
		stmt.setInt(1, id);
		int success = stmt.executeUpdate();
		this.updateAvailabilty(id, true, date);
		return success;
	}
}

interface MovieDAOInterface {
	public Movie save(Movie movie) throws SQLException;
	
	public TreeSet<Movie> findAll() throws SQLException;
	
	public Movie findFirst() throws SQLException;
	
	public boolean isAvailable(String movieName) throws SQLException;
	
	public int delete(int id) throws SQLException;
	
	public boolean update() throws SQLException;
	
	public int movieReturned(int id) throws SQLException;
	
	public boolean updateAvailabilty(int id, boolean rented, Date nat) throws SQLException;
		
	public int movieRented(int id, Date date) throws SQLException;
}