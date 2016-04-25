//DROP TABLE IF EXISTS users;
CREATE TABLE IF NOT EXISTS users (
   id int PRIMARY KEY auto_increment,
   type VARCHAR DEFAULT 'member',
   email VARCHAR UNIQUE,
   firstName VARCHAR,
   lastName VARCHAR,
   password VARCHAR,
   address VARCHAR,
   city VARCHAR,
   state VARCHAR,
   zipCode VARCHAR,
   date_of_birth VARCHAR,
   payment_info VARCHAR
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

//INSERT INTO users (email, password, type) VALUES ('fcamp001@fiu.edu', 'fernando', 'Admin');
