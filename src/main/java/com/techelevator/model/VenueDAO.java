package com.techelevator.model;

import java.util.List;

public interface VenueDAO {

	public List<Venue> getAllVenues(); //show all venues by primary key
	public Venue getVenueById(int venueId);
	
}
