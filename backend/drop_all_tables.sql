-- Drop all tables in the correct order (respecting foreign key constraints)
DROP TABLE IF EXISTS reservation CASCADE;
DROP TABLE IF EXISTS trip_available_seat_numbers CASCADE;
DROP TABLE IF EXISTS trip_bus_equipments CASCADE;
DROP TABLE IF EXISTS trip CASCADE;
DROP TABLE IF EXISTS users CASCADE;
DROP TABLE IF EXISTS flyway_schema_history CASCADE;
