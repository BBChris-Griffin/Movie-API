package com.skillstorm.test;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeSet;
import java.sql.Date;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import com.skillstorm.beans.Movie;
import com.skillstorm.data.MovieDAO;

public class MovieTest {
	
	MovieDAO dao = new MovieDAO();
	private static String url = "jdbc:mysql://localhost:3307/java_project";
	private static String username = "root";
	private static String password = "root";
	
	@BeforeClass
	public static void setup() {
		try {
			Connection conn = DriverManager.getConnection(url, username, password);
			// Creating Table
			String sql = "create table movie (\r\n"
					+ "	id INT NOT NULL auto_increment primary key,\r\n"
					+ "	name VARCHAR(100) NOT NULL,\r\n"
					+ "	genre VARCHAR(50) NOT NULL,\r\n"
					+ "	movie_id VARCHAR(50) NOT NULL,\r\n"
					+ "	availability boolean NOT NULL,\r\n"
					+ "	next_available_time DATE\r\n"
					+ ")";
			Statement stmt = conn.createStatement();
			stmt.execute(sql);
			
			// Making Rows
			sql = "insert into movie (id, name, genre, movie_id, availability, next_available_time) values "
					+ "(1, 'Drunken Master', 'Kung-Fu|Comedy', '0007-4205', true, null);";
			stmt = conn.createStatement();
			stmt.execute(sql);
			stmt.execute("insert into movie (id, name, genre, movie_id, availability, next_available_time) values "
					+ "(2, 'Way Of the Dragon', 'Kung-Fu|Comedy', '1200-4205', false, '2021-08-01')");
			stmt.execute("insert into movie (id, name, genre, movie_id, availability, next_available_time) values "
					+ "(3, 'Ip Man', 'Kung-Fu|Drama', '1340-4205', false, '2021-08-28')");
			stmt.execute("insert into movie (id, name, genre, movie_id, availability, next_available_time) values "
					+ "(4, 'Ong Bak', 'Kung-Fu', '1204-4205', false, '2021-08-09')");
			stmt.execute("insert into movie (id, name, genre, movie_id, availability, next_available_time) values "
					+ "(5, 'Kill Bill', 'Kung-Fu', '1350-4205', false, '2021-08-18')");
			stmt.execute("insert into movie (id, name, genre, movie_id, availability, next_available_time) values "
					+ "(6, 'Police Story', 'Kung-Fu|Comedy', '1200-1205', true, null)");
			stmt.execute("insert into movie (id, name, genre, movie_id, availability, next_available_time) values "
					+ "(7, 'Chocolate', 'Kung-Fu|Drama', '7410-4205', true, null)");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	// GET
	@Test
	public void testFindAll() {
		TreeSet<Movie> expected = new TreeSet<>();
		TreeSet<Movie> actual = new TreeSet<>();

		try {
			Connection conn = DriverManager.getConnection(url, username, password);
			String sql = "SELECT * FROM MOVIE";
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			while(rs.next()) {
				Movie movie = new Movie(rs.getString("NAME"), rs.getString("GENRE"), rs.getString("movie_id"),
						rs.getBoolean("availability"), rs.getDate("next_available_time"));
				movie.setId(rs.getInt("ID"));
				expected.add(movie);
			}
			
			actual = dao.findAll();
			assertEquals(expected, actual);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	// GET
	@Test
	public void testAvailability() {

		try {
			Connection conn = DriverManager.getConnection(url, username, password);
			String sql = "SELECT AVAILABILITY FROM MOVIE WHERE NAME = 'Drunken Master'";
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			rs.next();
			boolean expected = rs.getBoolean(1);
			
			List<Movie> movies = dao.findByName("Drunken Master");
			boolean actual = false;
			for(int i = 0; i < movies.size(); i++) {
				if(movies.get(i).isAvailability()) {
					actual = true;
					break;
				}
			}
			assertEquals(expected, actual);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	// POST
	@Test
	public void Create() {
		try {
			Connection conn = DriverManager.getConnection(url, username, password);
			String sql = "INSERT INTO MOVIE (NAME, GENRE, MOVIE_ID, AVAILABILITY, NEXT_AVAILABLE_TIME)  \r\n"
					+ "VALUES ('Pulp Fiction', 'Crime', '3548-7412', false, '2021-08-11')";
			Statement stmt = conn.createStatement();
			stmt.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
			ResultSet rs = stmt.getGeneratedKeys();
			rs.next();
			int expected = rs.getInt(1);
			
			Movie actual = dao.save(new Movie("Django Unchained", "Western", "451-453", true, null));
			assertEquals(expected, actual.getId() - 1);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	// DELETE
	@Test
	public void Delete() {
		try {
			Connection conn = DriverManager.getConnection(url, username, password);
			String sql = "DELETE FROM Movie WHERE ID = 2";
			Statement stmt = conn.createStatement();
			int expected = stmt.executeUpdate(sql);
			System.out.println(expected);

			int actual = dao.delete(3);
			System.out.println(actual);
			assertEquals(expected, actual);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	// 	PUT
	@Test
	public void Return() {
		try {
			Connection conn = DriverManager.getConnection(url, username, password);
			String sql = "UPDATE MOVIE SET AVAILABILITY = true, NEXT_AVAILABLE_TIME = null"
					+ " WHERE ID = 4";
			Statement stmt = conn.createStatement();
			int expected = stmt.executeUpdate(sql);

			int actual = dao.movieReturned(5);
			assertEquals(expected, actual);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	// PUT
	@Test
	public void Rent() {
		try {
			Connection conn = DriverManager.getConnection(url, username, password);
			String sql = "UPDATE MOVIE SET AVAILABILITY = false, NEXT_AVAILABLE_TIME = '2021-08-20'"
					+ " WHERE ID = 6";
			Statement stmt = conn.createStatement();
			int expected = stmt.executeUpdate(sql);

			int actual = dao.movieRented(7, Date.valueOf("2021-09-30"));
			assertEquals(expected, actual);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	@AfterClass
	public static void cleanup() {
		try {
			Connection conn = DriverManager.getConnection(url, username, password);
			String sql = "DROP TABLE movie";
			Statement stmt = conn.createStatement();
			stmt.executeUpdate(sql);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
