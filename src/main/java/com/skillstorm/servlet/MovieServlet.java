package com.skillstorm.servlet;

import java.io.IOException;
import java.sql.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeSet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.skillstorm.beans.Movie;
import com.skillstorm.data.MovieDAO;

@WebServlet(urlPatterns="/movies")
public class MovieServlet extends HttpServlet{
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		boolean available = false;
		if(req.getParameter("name") == null) {
			TreeSet<Movie> movies = new TreeSet<>();
			try(MovieDAO dao = new MovieDAO()) {
				movies = dao.findAll();
			} catch(Exception e) {
				e.printStackTrace();
			}
			ObjectMapper mapper = new ObjectMapper();
			String json = mapper.writeValueAsString(movies);
			resp.getWriter().print(json);
		}
		else {
			List<Movie> movies = new LinkedList<>();
			try(MovieDAO dao = new MovieDAO()) {
				movies = dao.findByName(req.getParameter("name"));
			} catch(Exception e) {
				e.printStackTrace();
			}
			ObjectMapper mapper = new ObjectMapper();
			String json = mapper.writeValueAsString(movies);
			resp.getWriter().print(json);
		}
		
		System.out.println("Got Movies");
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		Movie movie = mapper.readValue(req.getInputStream(), Movie.class);
		try(MovieDAO dao = new MovieDAO()) {
			Movie newMovie = dao.save(movie);
			String json = mapper.writeValueAsString(newMovie);
			resp.getWriter().print(json);
			resp.setStatus(201);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		if(req.getParameter("id") != null && 
				req.getParameter("next_available_time") == null) {
			int id = Integer.valueOf(req.getParameter("id"));
			try(MovieDAO dao = new MovieDAO()) {
				int i = dao.movieReturned(id);
				resp.setStatus(201);
				resp.getWriter().print(i);
			} catch(Exception e) {
				e.printStackTrace();
			}		
		} else if (req.getParameter("id") != null && req.getParameter("next_available_time") != null) {
			try(MovieDAO dao = new MovieDAO()) {
				int id = Integer.valueOf(req.getParameter("id"));
				Date date = Date.valueOf(req.getParameter("next_available_time"));
				dao.movieRented(id, date);
				resp.setStatus(201);
			} catch(Exception e) {
				e.printStackTrace();
			}		
		}
		
	}
	
	@Override
	protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		int id = Integer.valueOf(req.getParameter("id"));
		try(MovieDAO dao = new MovieDAO()) {
			dao.delete(id);
			resp.getWriter().print(id + " Deleted");
		} catch(Exception e) {
			e.printStackTrace();
		}		
	}
}
