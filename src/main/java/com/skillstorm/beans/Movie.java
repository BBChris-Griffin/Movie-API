package com.skillstorm.beans;

import java.sql.Date;

public class Movie {
	private int id;
	private String name;
	private String genre;
	private String movie_id;
	private boolean availability;
	private Date next_available_time;
	
	
	
	public Movie() {
		super();
	}
	public Movie(String name, String genre, String movie_id, boolean availability, Date next_available_time) {
		super();
		this.name = name;
		this.genre = genre;
		this.movie_id = movie_id;
		this.availability = availability;
		this.next_available_time = next_available_time;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getGenre() {
		return genre;
	}
	public void setGenre(String genre) {
		this.genre = genre;
	}
	public String getMovie_id() {
		return movie_id;
	}
	public void setMovie_id(String movie_id) {
		this.movie_id = movie_id;
	}
	public boolean isAvailability() {
		return availability;
	}
	public void setAvailability(boolean availability) {
		this.availability = availability;
	}
	public Date getNext_available_time() {
		return next_available_time;
	}
	public void setNext_available_time(Date next_available_time) {
		this.next_available_time = next_available_time;
	}
	@Override
	public String toString() {
		return "Movie [id=" + id + ", name=" + name + ", genre=" + genre + ", movie_id=" + movie_id + ", availaiblity="
				+ availability + ", next_available_time=" + next_available_time + "]";
	}
	
}
