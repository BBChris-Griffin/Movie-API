package com.skillstorm.beans;

import java.sql.Date;
import java.text.DateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;

public class Movie implements Comparable<Movie>{
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
	@Override
	public int hashCode() {
		return Objects.hash(availability, genre, id, movie_id, name, next_available_time);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Movie other = (Movie) obj;
		return availability == other.availability && Objects.equals(genre, other.genre) && id == other.id
				&& Objects.equals(movie_id, other.movie_id) && Objects.equals(name, other.name)
				&& Objects.equals(next_available_time, other.next_available_time);
	}
	@Override
	public int compareTo(Movie other) {
		// TODO Auto-generated method stub
		return this.id - other.id;
	}
	
}
