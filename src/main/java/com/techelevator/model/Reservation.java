package com.techelevator.model;

import java.time.LocalDate;

public class Reservation {

	private int reservationId;
	private int spaceId;
	private int numberOfAttendees;
	private LocalDate startDate;
	private LocalDate endDate;
	private String reservedFor;

	public int getReservationId() {
		return reservationId;
	}

	public void setReservationId(int reservationId) {
		this.reservationId = reservationId;
	}

	public int getSpaceId() {
		return spaceId;
	}

	public void setSpaceId(int spaceId) {
		this.spaceId = spaceId;
	}

	public int getNumberOfAttendees() {
		return numberOfAttendees;
	}

	public void setNumberOfAttendees(int numberOfAttendees) {
		this.numberOfAttendees = numberOfAttendees;
	}

	public LocalDate getStartDate() {
		return startDate;
	}

	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}

	public LocalDate getEndDate() {
		return endDate;
	}

	public void setEndDate(LocalDate endDate) {
		this.endDate = endDate;
	}

	public String getReservedFor() {
		return reservedFor;
	}

	public void setReservedFor(String reservedFor) {
		this.reservedFor = reservedFor;
	}

	@Override
	public String toString() {
		// this will need formating and the inclusion of other data, total cost, venue
		// name, etc
		return "reservationId=" + reservationId + ", spaceId=" + spaceId + ", numberOfAttendees=" + numberOfAttendees
				+ ", startDate=" + startDate + ", endDate=" + endDate + ", reservedFor=" + reservedFor + "";
	}

}
