package com.techelevator.model.jdbc;

import java.time.LocalDate;


import javax.sql.DataSource;


import org.springframework.jdbc.core.JdbcTemplate;


import com.techelevator.model.Reservation;
import com.techelevator.model.ReservationDAO;


public class JDBCReservationDAO implements ReservationDAO {

	private JdbcTemplate jdbcTemplate;

	public JDBCReservationDAO(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	@Override
	public Reservation createReservation(int spaceToBeReserved, int numberOfPeople, LocalDate reservationDate,
			LocalDate checkOutDate, String reservedFor) {

		String insertSql = "Insert into reservation(reservation_id, space_id, number_of_attendees, start_date, end_date, reserved_for) VALUES (DEFAULT,?,?,?,?,?) RETURNING reservation_id";

		Integer reservationId = jdbcTemplate.queryForObject(insertSql, Integer.class, spaceToBeReserved, numberOfPeople,
				reservationDate, checkOutDate, reservedFor);
		Reservation newReservation = new Reservation();
		newReservation.setReservationId(reservationId);
		newReservation.setNumberOfAttendees(numberOfPeople);
		newReservation.setEndDate(checkOutDate);
		newReservation.setStartDate(reservationDate);
		newReservation.setReservedFor(reservedFor);
		newReservation.setSpaceId(spaceToBeReserved);

		return newReservation;
	}


}
