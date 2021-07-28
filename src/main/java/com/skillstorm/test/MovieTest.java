package com.skillstorm.test;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;
import com.skillstorm.beans.Movie;
import com.skillstorm.data.MovieDAO;

public class MovieTest {

//	@BeforeClass
//	public static void setUp() {
//		try {
//			MovieDAO dao = new MovieDAO();
//		} catch(Exception e) {
//			e.printStackTrace();
//		}
//	}
	
//	@Test
//	public void test() {
//		assertEquals(true, true);
//	}
	
	@Test
	public void testAvailability() {
		try (MovieDAO dao = new MovieDAO()) {
			boolean available = dao.isAvailable("Drunken Master");
			assertEquals(true, available);
			available = dao.isAvailable("Pulp Fiction");
			assertEquals(false, available);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	@Test
	public void testAvilability() {
		//MovieDAO dao = new MovieDAO();
	}

}
