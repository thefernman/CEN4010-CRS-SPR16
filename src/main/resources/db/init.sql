-- noinspection SqlNoDataSourceInspectionForFile
-- DROP TABLE IF EXISTS users;
CREATE TABLE IF NOT EXISTS users (
   id int PRIMARY KEY auto_increment,
   type VARCHAR DEFAULT 'member',
   email VARCHAR UNIQUE,
   password VARCHAR,
 --  phone_number VARCHAR,
   firstName VARCHAR,
   lastName VARCHAR,
   address VARCHAR,
   city VARCHAR,
   state VARCHAR,
   zipCode VARCHAR,
   date_of_birth VARCHAR,
--    credit_card_number VARCHAR,
--    credit_card_expiration_date VARCHAR,
--    credit_card_zip_code VARCHAR,
--    credit_card_security_code VARCHAR
);
-- DROP TABLE IF EXISTS vehicles;
CREATE TABLE IF NOT EXISTS vehicles (
   id INTEGER PRIMARY KEY auto_increment,
   type VARCHAR,
   year INTEGER,
   manufacturer VARCHAR,
   model VARCHAR,
   reserved boolean
);

-- DROP TABLE IF EXISTS reservations;
CREATE TABLE IF NOT EXISTS reservations (
   id INTEGER PRIMARY KEY auto_increment,
   vehicle_id INTEGER,
   user_id INTEGER,
   dates VARCHAR,
   FOREIGN KEY(vehicle_id) REFERENCES vehicles(id),
   FOREIGN KEY(user_id) REFERENCES users(id)
);

CREATE TABLE IF NOT EXISTS specials (
   id INTEGER PRIMARY KEY auto_increment,
   name VARCHAR,
   discount INTEGER,
);

-- INSERT INTO users (email, password, type) VALUES ('fcamp001@fiu.edu', 'fernando', 'admin');
-- INSERT INTO vehicles ( type, year, manufacturer, model) VALUES
--    ('Compact', 2015, 'Honda', 'Civic'),
--    ('Compact', 2015, 'Honda', 'Urban'),
--    ('Compact', 2015, 'Ford', 'Escape'),
--    ('Midsize', 2015, 'Ford', 'Taurus'),
--    ('Midsize', 2015, 'Ford', 'Fusion'),
--    ('Midsize', 2015, 'Honda', 'Accord'),
--    ('Midsize', 2015, 'Ford', 'Focus'),
--    ('Midsize', 2015, 'Dodge', 'Avenger'),
--    ('SUV', 2015, 'Ford', 'Edge'),
--    ('SUV', 2015, 'Ford', 'Expedition'),
--    ('SUV', 2015, 'Ford', 'Kuga'),
--    ('SUV', 2015, 'Ford', 'Vertrek'),
--    ('SUV', 2015, 'Ford', 'C-Max Hybrid'),
--    ('SUV', 2015, 'Ford', 'Transit Connect Wagon'),
--    ('SUV', 2015, 'Kia', 'Sedona'),
--    ('SUV', 2015, 'Hyundai', 'Santa Fe');
