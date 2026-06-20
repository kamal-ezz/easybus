-- Seed users only (trips will come from mock mode or be created via API)
-- Password is 'password' hashed with bcrypt
INSERT INTO users (id, full_name, username, email, email_verified, role, password) VALUES
(1, 'Admin User', 'admin', 'admin@easybus.com', true, 'ADMIN', '$2a$10$N9qo8uLOickgx2ZMRZoMy.MqrqbKs1pC3H6L3StIkEkXN1HVk/E7.'),
(2, 'John Doe', 'johndoe', 'john@example.com', true, 'USER', '$2a$10$N9qo8uLOickgx2ZMRZoMy.MqrqbKs1pC3H6L3StIkEkXN1HVk/E7.'),
(3, 'Jane Smith', 'janesmith', 'jane@example.com', true, 'USER', '$2a$10$N9qo8uLOickgx2ZMRZoMy.MqrqbKs1pC3H6L3StIkEkXN1HVk/E7.')
ON CONFLICT (id) DO NOTHING;

-- Update sequence to continue from seed data
SELECT setval('users_id_seq', (SELECT MAX(id) FROM users));
