-- Create users table
CREATE TABLE IF NOT EXISTS users (
    id BIGSERIAL PRIMARY KEY,
    full_name VARCHAR(255),
    username VARCHAR(255),
    avatar_url VARCHAR(500),
    password VARCHAR(255),
    email VARCHAR(40) UNIQUE,
    email_verified BOOLEAN,
    locale VARCHAR(10),
    google_sub VARCHAR(255) UNIQUE,
    role VARCHAR(20) NOT NULL DEFAULT 'USER'
);

-- Create trip table
CREATE TABLE IF NOT EXISTS trip (
    id BIGSERIAL PRIMARY KEY,
    bus_company VARCHAR(255),
    bus_logo VARCHAR(500),
    departure_city VARCHAR(255),
    destination_city VARCHAR(255),
    date DATE,
    departure_time TIME,
    destination_time TIME,
    price DOUBLE PRECISION,
    total_seats INTEGER NOT NULL
);

-- Create table for trip equipment (ElementCollection)
CREATE TABLE IF NOT EXISTS trip_bus_equipments (
    trip_id BIGINT NOT NULL,
    bus_equipments VARCHAR(50),
    FOREIGN KEY (trip_id) REFERENCES trip(id) ON DELETE CASCADE
);

-- Create table for available seat numbers (ElementCollection)
CREATE TABLE IF NOT EXISTS trip_available_seat_numbers (
    trip_id BIGINT NOT NULL,
    available_seat_numbers INTEGER,
    FOREIGN KEY (trip_id) REFERENCES trip(id) ON DELETE CASCADE
);

-- Create reservation table
CREATE TABLE IF NOT EXISTS reservation (
    id BIGSERIAL PRIMARY KEY,
    trip_id BIGINT,
    user_id BIGINT,
    seat_number INTEGER,
    full_name VARCHAR(255),
    email VARCHAR(255),
    phone VARCHAR(50),
    amount DOUBLE PRECISION,
    currency VARCHAR(10),
    paypal_order_id VARCHAR(255),
    payment_method VARCHAR(50),
    reservation_status VARCHAR(50),
    created_at TIMESTAMP NOT NULL,
    FOREIGN KEY (trip_id) REFERENCES trip(id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE SET NULL
);

-- Create indexes for better query performance
CREATE INDEX idx_users_email ON users(email);
CREATE INDEX idx_users_google_sub ON users(google_sub);
CREATE INDEX idx_trip_departure_city ON trip(departure_city);
CREATE INDEX idx_trip_destination_city ON trip(destination_city);
CREATE INDEX idx_trip_date ON trip(date);
CREATE INDEX idx_reservation_trip_id ON reservation(trip_id);
CREATE INDEX idx_reservation_user_id ON reservation(user_id);
CREATE INDEX idx_reservation_status ON reservation(reservation_status);
