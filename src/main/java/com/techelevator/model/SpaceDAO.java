package com.techelevator.model;

import java.time.LocalDate;
import java.util.List;

public interface SpaceDAO {

	public List<Space> getSpaceByVenueId(int venueId);

	public List<Space> searchAvailableSpaces(int numberOfAttendees, LocalDate startDate, LocalDate chectOutDate,
			int venueId);

}
