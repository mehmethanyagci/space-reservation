package com.techelevator.model;

import java.text.ParseException;
import java.time.LocalDate;

public class Space {

	private int spaceId;
	private int venueId;
	private String name;
	private boolean isAccesible;
	private int openFrom;
	private int openTo;
	private double dailyRate;
	private int maxOccupancy;

	public int getSpaceId() {
		return spaceId;
	}

	public void setSpaceId(int spaceId) {
		this.spaceId = spaceId;
	}

	public int getVenueId() {
		return venueId;
	}

	public void setVenueId(int venueId) {
		this.venueId = venueId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isAccesible() {
		return isAccesible;
	}

	public void setAccesible(boolean isAccesible) {
		this.isAccesible = isAccesible;
	}

	public int getOpenFrom() {
		return openFrom;
	}

	public void setOpenFrom(int openFrom) {
		this.openFrom = openFrom;
	}

	public int getOpenTo() {
		return openTo;
	}

	public void setOpenTo(int openTo) {
		this.openTo = openTo;
	}

	public double getDailyRate() {
		return dailyRate;
	}

	public void setDailyRate(double dailyRate) {
		this.dailyRate = dailyRate;
	}

	public int getMaxOccupancy() {
		return maxOccupancy;
	}

	public void setMaxOccupancy(int maxOccupancy) {
		this.maxOccupancy = maxOccupancy;
	}

	@Override
	// Will need to add total cost of dailyRate * days needed
	public String toString() {
		return this.spaceId + ", " + this.name + ", " + this.isAccesible + ", " + this.dailyRate + ", "
				+ this.maxOccupancy;
	}

}
