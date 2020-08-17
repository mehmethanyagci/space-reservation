package com.techelevator.model.jdbc;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.techelevator.model.Venue;
import com.techelevator.model.VenueDAO;

public class JDBCVenueDAO implements VenueDAO {

	private JdbcTemplate jdbcTemplate;

	public JDBCVenueDAO(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	@Override
	public List<Venue> getAllVenues() {
		List<Venue> venues = new ArrayList<Venue>();

		String selectSql = "SELECT venue.id, venue.name, venue.city_id, city.state_abbreviation, city.name AS city_name, venue.description FROM venue\n"
				+ "JOIN city on venue.city_id = city.id";
		SqlRowSet rows = jdbcTemplate.queryForRowSet(selectSql);

		while (rows.next()) {
			Venue venue = mapRowToVenue(rows);
			venues.add(venue);
		}
		return venues;
	}

	@Override
	public Venue getVenueById(int venueId) {

		// Venue venue = null;

		String selectSql = "SELECT venue.id, venue.name, venue.city_id, city.state_abbreviation, city.name AS city_name, venue.description FROM venue\n"
				+ "JOIN city on venue.city_id = city.id WHERE venue.id = ?";

		SqlRowSet rows = jdbcTemplate.queryForRowSet(selectSql, venueId);

		if (rows.next()) {
			return mapRowToVenue(rows);
		}
		return null;
	}

	private Venue mapRowToVenue(SqlRowSet rows) {

		Venue venue = new Venue();

		venue.setVenueCity(rows.getString("city_name"));
		venue.setVenueState(rows.getString("state_abbreviation"));
		venue.setVenueId(rows.getInt("id"));
		venue.setName(rows.getString("name"));
		venue.setCityId(rows.getInt("city_id"));
		venue.setDescription(rows.getString("description"));

		String categorySql = "SELECT name FROM category_venue\n"
				+ "JOIN category ON category_venue.category_id = category.id\n" + "WHERE category_venue.venue_id = ?";

		SqlRowSet categoryRows = jdbcTemplate.queryForRowSet(categorySql, venue.getVenueId());

		List<String> categoryList = new ArrayList<>();

		while (categoryRows.next()) {
			categoryList.add(categoryRows.getString("name"));
		}

		venue.setCategoryList(categoryList);

		return venue;
	}

}
