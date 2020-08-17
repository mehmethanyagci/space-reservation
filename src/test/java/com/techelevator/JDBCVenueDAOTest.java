package com.techelevator;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.techelevator.model.Venue;
import com.techelevator.model.VenueDAO;
import com.techelevator.model.jdbc.JDBCVenueDAO;

public class JDBCVenueDAOTest {
	/*
	 * Using this particular implementation of DataSource so that every database
	 * interaction is part of the same database session and hence the same database
	 * transaction
	 */
	private static SingleConnectionDataSource dataSource;

	private VenueDAO dao;
	private JdbcTemplate jdbcTemplate;

	/*
	 * Before any tests are run, this method initializes the datasource for testing.
	 */
	@BeforeClass
	public static void setupDataSource() {
		dataSource = new SingleConnectionDataSource();
		dataSource.setUrl("jdbc:postgresql://localhost:5432/excelsior-venues");
		dataSource.setUsername("postgres");
		dataSource.setPassword("postgres1");
		/*
		 * The following line disables autocommit for connections returned by this
		 * DataSource. This allows us to rollback any changes after each test
		 */
		dataSource.setAutoCommit(false);
	}

	/*
	 * After all tests have finished running, this method will close the DataSource
	 */
	@AfterClass
	public static void closeDataSource() throws SQLException {
		dataSource.destroy();
	}

	/*
	 * After each test, we rollback any changes that were made to the database so
	 * that everything is clean for the next test
	 */
	@After
	public void rollback() throws SQLException {
		dataSource.getConnection().rollback();
	}

	/*
	 * This method provides access to the DataSource for subclasses so that they can
	 * instantiate a DAO for testing
	 */
	protected DataSource getDataSource() {
		return dataSource;
	}

	@Before
	public void setupTest() {
		dao = new JDBCVenueDAO(dataSource);
		jdbcTemplate = new JdbcTemplate(dataSource);
	}

	@Test
	public void get_list_of_all_venues() {

		List<Venue> venues = dao.getAllVenues();

		int originalCount = venues.size();

		Venue venue = new Venue();
		// Act
		venues.add(venue);

		// Assert
		Assert.assertTrue(venues.size() > originalCount);

		Assert.assertEquals(venues.size(), originalCount + 1);
	}

	private Venue createVenue(Venue newVenue) {
		String insertSql = "Insert into venue (name) VALUES (Default,?, ?, ?) RETURNING id";

		int venueID = jdbcTemplate.queryForObject(insertSql, int.class, newVenue.getName(), newVenue.getCityId(),
				newVenue.getDescription());
		newVenue.setVenueId(venueID);
		return newVenue;
	}

	private Venue getTestVenue() {
		Venue venue = new Venue();
		venue.setName("nametest");
		venue.setCityId(1001);
		venue.setDescription("test venue description");
		return venue;

	}
}
