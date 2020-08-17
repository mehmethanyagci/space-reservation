package com.techelevator;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

import org.apache.commons.dbcp2.BasicDataSource;

import com.techelevator.menu.Menu;
import com.techelevator.model.Reservation;
import com.techelevator.model.ReservationDAO;
import com.techelevator.model.Space;
import com.techelevator.model.SpaceDAO;
import com.techelevator.model.Venue;
import com.techelevator.model.VenueDAO;
import com.techelevator.model.jdbc.JDBCReservationDAO;
import com.techelevator.model.jdbc.JDBCSpaceDAO;
import com.techelevator.model.jdbc.JDBCVenueDAO;

public class ExcelsiorCLI {

	private static final String MAIN_MENU_OPTION_LIST_VENUES = "List Venues";
	private static final String MAIN_MENU_OPTION_QUIT = "Quit";
	private static final String[] MAIN_MENU_OPTIONS = new String[] { MAIN_MENU_OPTION_LIST_VENUES,
			MAIN_MENU_OPTION_QUIT };

	private static final String MENU_OPTION_RETURN_TO_PREVIOUS_SCREEN = "Return to Previous Screen";

	private static final String VENUE_DETAILS_MENU_OPTION_VIEW_SPACES = "View Spaces";
	private static final String VENUE_DETAILS_MENU_OPTION_SEARCH_FOR_RESERVATION = "Search for Reservation";
	private static final String[] VENUE_DETAILS_LIST_MENU_OPTIONS = new String[] {
			VENUE_DETAILS_MENU_OPTION_VIEW_SPACES, VENUE_DETAILS_MENU_OPTION_SEARCH_FOR_RESERVATION,
			MENU_OPTION_RETURN_TO_PREVIOUS_SCREEN };

	private static final String SPACE_DETAILS_MENU_OPTION_RESERVE_A_SPACE = "Reserve a Space";
	private static final String[] SPACE_DETAILS_LIST_MENU_OPTIONS = new String[] {
			SPACE_DETAILS_MENU_OPTION_RESERVE_A_SPACE, MENU_OPTION_RETURN_TO_PREVIOUS_SCREEN };

	private static final String[] MONTHS = new String[] { "", "JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG",
			"SEP", "OCT", "NOV", "DEC" };

	private Menu menu;
	private ReservationDAO reservationDAO;
	private VenueDAO venueDAO;
	private SpaceDAO spaceDAO;

	public static void main(String[] args) {

		ExcelsiorCLI application = new ExcelsiorCLI();
		application.run();
	}

	public ExcelsiorCLI() {
		this.menu = new Menu(System.in, System.out);

		BasicDataSource dataSource = new BasicDataSource();
		dataSource.setUrl("jdbc:postgresql://localhost:5432/excelsior-venues");
		dataSource.setUsername("postgres");
		dataSource.setPassword("postgres1");

		reservationDAO = new JDBCReservationDAO(dataSource);
		spaceDAO = new JDBCSpaceDAO(dataSource);
		venueDAO = new JDBCVenueDAO(dataSource);
	}

	public void run() {
		while (true) {
			System.out.println("What would you like to do?");
			String choice = (String) menu.getChoiceFromOptions(MAIN_MENU_OPTIONS);

			if (choice.equals(MAIN_MENU_OPTION_LIST_VENUES)) {
				handleVenues();
			} else if (choice.equals(MAIN_MENU_OPTION_QUIT)) {
				System.exit(0);
			}
		}
	}

	private void handleVenues() {

		List<Venue> allVenues = venueDAO.getAllVenues();

		System.out.println();
		System.out.println("Which venue would you like to view?");
		Venue choice = (Venue) menu.getChoiceFromOptions(allVenues);
		int venueId = choice.getVenueId();
		handleDisplayVenue(venueId);

	}

	private void handleDisplayVenue(int venueId) {

		Venue chosenVenue = venueDAO.getVenueById(venueId);

		System.out.println();
		System.out.println(chosenVenue.getName());
		System.out.println();
		System.out.println("Location: " + chosenVenue.getVenueCity() + ", " + chosenVenue.getVenueState());
		chosenVenue.getCategoryList();
		System.out.print("Category: ");
		for (String category : chosenVenue.getCategoryList()) {
			System.out.print(category + " ");
		}
		System.out.println();
		System.out.println(chosenVenue.getDescription());
		System.out.println();

		handleDetails(venueId);
	}

	private void handleDetails(int venueId) {
		while (true) {
			System.out.println("What would you like to do next?");
			String choice = (String) menu.getChoiceFromOptions(VENUE_DETAILS_LIST_MENU_OPTIONS);
			if (choice.equals(VENUE_DETAILS_MENU_OPTION_VIEW_SPACES)) {
				handleDisplaySpaces(venueId);
			} else if (choice.equals(VENUE_DETAILS_MENU_OPTION_SEARCH_FOR_RESERVATION)) {
				handleBeginReservation(venueId);
			} else if (choice.equals(MENU_OPTION_RETURN_TO_PREVIOUS_SCREEN)) {
				break;
			}
		}
	}

	private void handleDisplaySpaces(int venueId) {

		List<Space> allSpaces = spaceDAO.getSpaceByVenueId(venueId);
		listSpaces(allSpaces, venueId);

		handleVenueSpaces(venueId);
	}

	private void handleVenueSpaces(int venueId) {
		while (true) {
			System.out.println("What would you like to do next?");
			String choice = (String) menu.getChoiceFromOptions(SPACE_DETAILS_LIST_MENU_OPTIONS);
			if (choice.equals(SPACE_DETAILS_MENU_OPTION_RESERVE_A_SPACE)) {
				handleBeginReservation(venueId);
			} else if (choice.equals(MENU_OPTION_RETURN_TO_PREVIOUS_SCREEN)) {
				break;
			}
		}
	}

	private void handleBeginReservation(int venueId) {
		
		String startDate = getUserInput("When do you need the space? Please enter in format(MM/DD/YYYY)");
		Date currentTime = new Date();
		Date startDate1 = new Date();
		while (true) {
			try {
				startDate1 = new SimpleDateFormat("MM/dd/yyyy").parse(startDate);
			} catch (ParseException e) {
				System.out.println("\nPlease enter a valid date.\n");
				break;
			}
			if (startDate1.compareTo(currentTime) > 0) {
				getValidDate(startDate, venueId);
			} else {
				System.out.println("\nYou cannot make a reservation in the past. Please enter a future date.\n");
				break;
			}

			int daysToStay = 0;

			int numberOfPeople = 0;

			daysToStay = getNumberOfDays();

			numberOfPeople = getNumberOfPeople();

			ZoneId defaultZoneId = ZoneId.systemDefault();

			Instant instant = getValidDate(startDate, venueId).toInstant();

			LocalDate reservationDate = instant.atZone(defaultZoneId).toLocalDate();

			LocalDate checkOutDate = reservationDate.plusDays(daysToStay);

			List<Space> availableSpacesToReserve = spaceDAO.searchAvailableSpaces(numberOfPeople, reservationDate,
					checkOutDate, venueId);

			listAvailableSpaces(availableSpacesToReserve, daysToStay);

			String spaceToBeReserved = getValidSpaceId(availableSpacesToReserve);

			while (true) {
				if (Integer.parseInt(spaceToBeReserved) != 0) {

					String reservedFor = getUserInput("Who is this reservation for?");

					Reservation reservation = reservationDAO.createReservation(Integer.parseInt(spaceToBeReserved),
							numberOfPeople, reservationDate, checkOutDate, reservedFor);

					Venue chosenVenue = venueDAO.getVenueById(venueId);

					handleReceipt(reservation, availableSpacesToReserve, spaceToBeReserved, chosenVenue, daysToStay);

				} else {
					break;
				}
			}
		}
	}

	private String getValidSpaceId(List<Space> availableSpacesToReserve) {

		boolean isDone = false;
		String spaceToBeReserved = "";

		while (!isDone) {
			try {
				spaceToBeReserved = getUserInput("Which space would you like to reserve? (O to cancel)");
				if (spaceToBeReserved.equals("0")) {
					System.exit(0);
				}
			} catch (Exception e) {
				System.out.println("Not a valid space id. Please try again.");
				continue;
			}
			try {
				for (Space space : availableSpacesToReserve) {

					if (space.getSpaceId() == Integer.parseInt(spaceToBeReserved)) {
						isDone = true;
					}
				}
			} catch (Exception e) {
				System.out.println();
				System.out.println("Please provide a valid number.");
			}

		}
		return spaceToBeReserved;
	}

	private void handleReceipt(Reservation reservation, List<Space> availableSpacesToReserve, String spaceToBeReserved,
			Venue chosenVenue, int daysToStay) {

		Space selectedSpace = new Space();

		for (Space sa : availableSpacesToReserve) {
			if (sa.getSpaceId() == Integer.parseInt(spaceToBeReserved)) {
				selectedSpace = sa;
			}
		}

		System.out.println();
		System.out.println("Thanks for submitting your reservation! The details for your event are listed below:");
		System.out.println();
		System.out.println("Confirmation #: " + reservation.getReservationId());
		System.out.println("Venue: " + chosenVenue.getName());
		System.out.println("Space: " + selectedSpace.getName());
		System.out.println("Reserved For: " + reservation.getReservedFor());
		System.out.println("Attendess: " + reservation.getNumberOfAttendees());
		System.out.println("Arrival Date: " + reservation.getStartDate());
		System.out.println("Depart Date: " + reservation.getEndDate());
		System.out.print("Total Cost: $");
		System.out.printf("%.2f", selectedSpace.getDailyRate() * daysToStay);
		System.exit(0);
	}

	public Date getValidDate(String date, int venueId) {

		try {
			Date date1 = new SimpleDateFormat("MM/dd/yyyy").parse(date);
			return date1;
		} catch (Exception e) {
			System.out.println("Please enter a valid date");
			handleBeginReservation(venueId);
		}
		return null;
	}

	private int getNumberOfDays() {
		boolean isDone = false;
		int daysToStay = 0;

		while (!isDone) {
			try {
				daysToStay = Integer.parseInt(getUserInput("How many days will you need the space?"));
			} catch (Exception e) {
				System.out.println("Please enter a number");
				continue;
			}
			if (daysToStay >= 0) {
				isDone = true;
			} else {
				System.out.print("Not a valid number of days. Please try again");
			}
		}
		return daysToStay;
	}

	private int getNumberOfPeople() {
		boolean isDone = false;
		int numberOfPeople = 0;

		while (!isDone) {
			try {
				numberOfPeople = Integer.parseInt(getUserInput("How many people will be in attendance?"));
			} catch (Exception e) {
				System.out.println("Please enter a number");
				continue;
			}
			if (numberOfPeople >= 0) {
				isDone = true;
			} else {
				System.out.print("Not a valid number of days. Please try again");
			}
		}
		return numberOfPeople;
	}

	private void listAvailableSpaces(List<Space> spaces, int daysToStay) {

		if (spaces.size() > 0) {
			System.out.println();
			System.out.println("The following spaces are available based on your needs:");
			System.out.println();
			System.out.printf("%-10s", "Space #");
			System.out.printf("%-30s", "Name");
			System.out.printf("%-20s", "Daily Rate");
			System.out.printf("%-15s", "Max. Occup");
			System.out.printf("%-12s", "Accessible?");
			System.out.printf("%-20s", "Total Cost");
			System.out.println();
			for (Space sc : spaces) {
				System.out.printf("%-10d", sc.getSpaceId());
				System.out.printf("%-30s", sc.getName());
				System.out.printf("$%-20.2f", sc.getDailyRate());
				System.out.printf("%-15d", sc.getMaxOccupancy());
				System.out.printf("%-12s", sc.isAccesible());
				System.out.printf("$%-20.2f", sc.getDailyRate() * daysToStay);
				System.out.println();
			}
			System.out.println();
		} else {
			System.out.println("\n*** No results ***");
		}
	}

	private void listSpaces(List<Space> spaces, int venueId) {

		if (spaces.size() > 0) {
			Venue chosenVenue = venueDAO.getVenueById(venueId);
			System.out.println(chosenVenue.getName() + " Spaces");
			System.out.println();
			System.out.printf("%-5s", "");
			System.out.printf("%-30s", "Name");
			System.out.printf("%-10s", "Open");
			System.out.printf("%-10s", "Close");
			System.out.printf("%-20s", "Daily Rate");
			System.out.printf("%-20s", "Max. Occupancy");
			System.out.println();
			for (Space sp : spaces) {
				int index = sp.getSpaceId();
				System.out.printf("%-5d", index);
				System.out.printf("%-30s", sp.getName());
				System.out.printf("%-10s", MONTHS[sp.getOpenFrom()]);
				System.out.printf("%-10s", MONTHS[sp.getOpenTo()]);
				System.out.printf("$%-20.2f", sp.getDailyRate());
				System.out.printf("%-20d", sp.getMaxOccupancy());
				System.out.println();
			}
			System.out.println();
		} else
			System.out.println("\n*** No results ***");
	}

	@SuppressWarnings("resource")
	private String getUserInput(String prompt) {
		System.out.println();
		System.out.print(prompt + " >>> ");
		return new Scanner(System.in).nextLine();
	}
}
