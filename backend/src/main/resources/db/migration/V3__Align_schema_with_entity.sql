-- Align Flyway schema with JPA entities (Trip: operator, from_city, to_city, departure, arrival, availableSeats as strings; Booking table and seat column).
-- This migration handles both populated and empty databases.

-- 1. Trip: rename columns to match Trip entity
ALTER TABLE trip RENAME COLUMN bus_company TO operator;
ALTER TABLE trip RENAME COLUMN departure_city TO from_city;
ALTER TABLE trip RENAME COLUMN destination_city TO to_city;
ALTER TABLE trip RENAME COLUMN departure_time TO departure;
ALTER TABLE trip RENAME COLUMN destination_time TO arrival;
ALTER TABLE trip DROP COLUMN IF EXISTS bus_logo;
ALTER TABLE trip DROP COLUMN IF EXISTS total_seats;

-- 2. Trip equipments: match @ElementCollection List<Equipment> (table trip_equipments, column equipments)
CREATE TABLE IF NOT EXISTS trip_equipments (
    trip_id BIGINT NOT NULL,
    equipments VARCHAR(50),
    FOREIGN KEY (trip_id) REFERENCES trip(id) ON DELETE CASCADE
);

-- Migrate data only if old table has data
INSERT INTO trip_equipments (trip_id, equipments)
SELECT trip_id, bus_equipments FROM trip_bus_equipments
WHERE EXISTS (SELECT 1 FROM trip_bus_equipments LIMIT 1);

DROP TABLE IF EXISTS trip_bus_equipments;

-- 3. Trip available seats: match @ElementCollection List<String> availableSeats (table trip_available_seats, column available_seats)
CREATE TABLE IF NOT EXISTS trip_available_seats (
    trip_id BIGINT NOT NULL,
    available_seats VARCHAR(10),
    FOREIGN KEY (trip_id) REFERENCES trip(id) ON DELETE CASCADE
);

-- Generate seats for existing trips (if any exist)
INSERT INTO trip_available_seats (trip_id, available_seats)
SELECT t.id, (r::text || chr(65 + c))
FROM trip t
CROSS JOIN generate_series(1, 10) AS r
CROSS JOIN generate_series(0, 3) AS c
WHERE EXISTS (SELECT 1 FROM trip LIMIT 1)
ORDER BY t.id, r, c;

DROP TABLE IF EXISTS trip_available_seat_numbers;

-- 4. Reservation -> Booking: entity expects table "booking" with column "seat" (VARCHAR)
ALTER TABLE reservation RENAME TO booking;
ALTER TABLE booking RENAME COLUMN reservation_status TO booking_status;
ALTER TABLE booking ADD COLUMN seat VARCHAR(10);
UPDATE booking
SET seat = (LEAST(10, ((seat_number - 1) / 4) + 1))::text || chr(65 + mod(seat_number - 1, 4))
WHERE seat_number IS NOT NULL;
ALTER TABLE booking DROP COLUMN seat_number;

-- 5. Indexes: match new column names
DROP INDEX IF EXISTS idx_trip_departure_city;
DROP INDEX IF EXISTS idx_trip_destination_city;
CREATE INDEX IF NOT EXISTS idx_trip_from_city ON trip(from_city);
CREATE INDEX IF NOT EXISTS idx_trip_to_city ON trip(to_city);
