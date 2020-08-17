package com.techelevator.model.jdbc;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.techelevator.model.Space;
import com.techelevator.model.SpaceDAO;

public class JDBCSpaceDAO implements SpaceDAO {

	private JdbcTemplate jdbcTemplate;

	public JDBCSpaceDAO(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	@Override
	public List<Space> getSpaceByVenueId(int venueId) {

		List<Space> spaces = new ArrayList<>();

		String selectSql = "SELECT id, venue_id, name, is_accessible, open_from, open_to, Cast(daily_rate as decimal(100,2)), max_occupancy FROM space WHERE venue_id = ?";

		SqlRowSet rows = jdbcTemplate.queryForRowSet(selectSql, venueId);

		while (rows.next()) {
			Space space = mapRowToSpace(rows);
			spaces.add(space);
		}

		return spaces;
	}

	@Override
	public List<Space> searchAvailableSpaces(int numberOfAttendees, LocalDate startDate, LocalDate chectOutDate,
			int venueId) {

		List<Space> spaces = new ArrayList<Space>();

		String selectSql = "Select DISTINCT space.id, space.venue_id, space.open_from, space.open_to, space.name, space.is_accessible, space.max_occupancy, cast(space.daily_rate as numeric)\n"
				+ "from space\n" + "join reservation on reservation.space_id = space.id\n"
				+ "join venue on venue.id = space.venue_id\n"
				+ "WHERE (? <= space.max_occupancy) AND  (? >= reservation.end_date OR ? <= reservation.start_date) AND (space.venue_id = ?) LIMIT 5";

		SqlRowSet rows = jdbcTemplate.queryForRowSet(selectSql, numberOfAttendees, startDate, chectOutDate, venueId);

		while (rows.next()) {
			Space space = mapRowToSpace(rows);
			spaces.add(space);
		}
		return spaces;
	}

	private Space mapRowToSpace(SqlRowSet rows) {

		Space space = new Space();

		space.setSpaceId(rows.getInt("id"));
		space.setVenueId(rows.getInt("venue_id"));
		space.setName(rows.getString("name"));
		space.setAccesible(rows.getBoolean("is_accessible"));
		if (rows.getInt("open_from") % 1 == 0) {
			space.setOpenFrom(Integer.valueOf(rows.getInt("open_from")));
		}
		if (rows.getInt("open_to") % 1 == 0) {
			space.setOpenTo(Integer.valueOf(rows.getInt("open_to")));
		}

		space.setDailyRate(rows.getDouble("daily_rate"));
		space.setMaxOccupancy(rows.getInt("max_occupancy"));

		return space;
	}

}
