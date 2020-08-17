package com.techelevator;

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

import com.techelevator.model.Space;
import com.techelevator.model.SpaceDAO;
import com.techelevator.model.Venue;
import com.techelevator.model.VenueDAO;
import com.techelevator.model.jdbc.JDBCSpaceDAO;
import com.techelevator.model.jdbc.JDBCVenueDAO;

public class JDBCSpaceDAOTest {
	/*
	 * Using this particular implementation of DataSource so that every database
	 * interaction is part of the same database session and hence the same database
	 * transaction
	 */
	private static SingleConnectionDataSource dataSource;

	private SpaceDAO dao;
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
		dao = new JDBCSpaceDAO(dataSource);
		jdbcTemplate = new JdbcTemplate(dataSource);
	}

	@Test
	public void get_space_by_venue_id() {
		
		// Arrange
		Space space = getTestSpace();
		int venueId = 16;
		createSpace(space, venueId);
		// Act
		List<Space> allSpaces = dao.getSpaceByVenueId(space.getVenueId());
		
		// Assert 
		Assert.assertEquals(1, allSpaces.size());
		Assert.assertEquals(space, allSpaces);

	}
	
	private Space createSpace(Space space, int venueId) { 
		
		String insertSql = "INSERT INTO space VALUES (DEFAULT, ?, ?, ?, ?, ?, CAST(? AS decimal(100, 2)), ?) RETURNING id; ";
		
		Integer spaceId = jdbcTemplate.queryForObject(insertSql, Integer.class, space.getVenueId(), space.getName(), space.isAccesible(), space.getOpenFrom(), space.getOpenTo(), space.getDailyRate(), space.getMaxOccupancy());
		

		space.setSpaceId(spaceId);
		space.setVenueId(venueId);

		return space;
	}

	private Space getTestSpace() {
		Space space = new Space();

		space.setName("test space");
		space.setAccesible(true);
		space.setOpenFrom(2);
		space.setOpenTo(5);
		space.setDailyRate(100.50);
		space.setMaxOccupancy(500);

		return space;
	}

}
