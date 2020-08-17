package com.techelevator.model;

import java.time.LocalDate;
import java.util.List;

public interface ReservationDAO {

	public Reservation createReservation(int spaceToBeReserved, int numberOfPeople, LocalDate reservationDate,
			LocalDate checkOutDate, String reservedFor);

}
