select * from city;

-- getSpaceByVenueId

SELECT id, venue_id, name, open_from, open_to, daily_rate, max_occupancy FROM space WHERE id = 1;


-- Search Rervation
SELECT * FROM reservation
JOIN space ON reservation.space_id = space.id;

SELECT venue.id, venue.name, venue.city_id, city.state_abbreviation,
city.name, category.name, venue.description FROM venue
JOIN city on venue.city_id = city.id
JOIN category_venue ON venue.id = category_venue.venue_id
JOIN category ON category.id = category_venue.category_id WHERE venue.id = 2;

SELECT venue.id, venue.name, venue.city_id, city.state_abbreviation,
city.name AS city_name, venue.description FROM venue
JOIN city on venue.city_id = city.id
WHERE venue.id = 2;

SELECT name FROM category_venue
JOIN category ON category_venue.category_id = category.id
WHERE category_venue.venue_id = 3;

SELECT reservation.reservation_id, reservation.space_id, reservation.reserved_for, reservation.number_of_attendees, reservation.start_date, reservation.end_date, space.name, space.max_occupancy, space.is_accessible, space.daily_rate FROM reservation
JOIN space ON reservation.space_id = space.id    
JOIN venue ON space.venue_id = venue.id
WHERE (10 <= space.max_occupancy) AND ((date '2020-07-20' + 3 < start_date AND venue_id = 9) OR (date '2020-09-20' > end_date AND venue_id = 9 ));


-- 10 People, venue id 1, 2020-7-20 - 2020-7-25 -- show me available spaces

--where reservation.number_of_attendees = ?, reservation.start_date = ?, reservation.end_date = ?, space.max_occupancy = ?;

-- int numberOfAttendees, String startDate, int daysToStay

-- case 1
-- I check in and out before other people  => my_reservation_date < start_date and my_reservation_date + number_of_stay < start_date

-- case 2
-- I check in and out after other people  => my_reservation_date > end_date

-- case 3

SELECT  reservation.space_id, reservation.reserved_for, space.daily_rate, space.max_occupancy, space.is_accessible, (space.daily_rate * 3) AS total_cost FROM reservation
JOIN space ON reservation.space_id = space.id
JOIN venue ON space.venue_id = venue.id
WHERE (10 <= space.max_occupancy) AND (date '2020-07-20' + 3 <= start_date AND venue_id = 15) OR (date '2020-07-20' >= end_date AND venue_id = 15);


SELECT reservation.space_id, reservation.reserved_for, space.daily_rate, space.max_occupancy, space.is_accessible, (space.daily_rate * (date '2020-07-25' - date '2020-07-20')) AS total_cost FROM reservation
JOIN space ON reservation.space_id = space.id
JOIN venue ON space.venue_id = venue.id
WHERE (10 <= space.max_occupancy) AND (date '2020-07-20' + 5 <= start_date AND venue_id = 1) OR (date '2020-07-20' >= end_date AND venue_id = 1);



SELECT reservation.space_id, reservation.reserved_for, space.max_occupancy, space.is_accessible, venue.id FROM reservation
JOIN space ON reservation.space_id = space.id
JOIN venue ON space.venue_id = venue.id
WHERE (10 <= space.max_occupancy) AND  (date '2020-08-20' >= reservation.end_date OR date '2020-07-25' <= reservation.start_date) AND space.venue_id = 1;

--RETURNING reservation_id

START TRANSACTION;

--SELECT currval('next_reservation_reservation_id_seq');

--SELECT nextval('reservation_reservation_id_seq'::regclass);

Insert into reservation(reservation_id, space_id, number_of_attendees, start_date, end_date, reserved_for) VALUES (DEFAULT, 1, 1, date '2020-07-20', date '2020-07-25', 'me');

ROLLBACK;





