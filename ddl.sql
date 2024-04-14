CREATE TABLE regular_member (
	id SERIAL PRIMARY KEY,
	username VARCHAR(255),
	first_name VARCHAR(255),
  last_name VARCHAR(255),
  email VARCHAR(255),
	date_of_birth date,
  password VARCHAR(255)
);

CREATE TABLE trainer (
	id SERIAL PRIMARY KEY,
	username VARCHAR(255),
	first_name VARCHAR(255),
  last_name VARCHAR(255),
  email VARCHAR(255),
  password VARCHAR(255)
);

CREATE TABLE administrator (
	id SERIAL PRIMARY KEY,
	username VARCHAR(255),
  password VARCHAR(255)
);

CREATE TABLE health_stats (
	id SERIAL PRIMARY KEY,
	member_id INT,
  date DATE,
  height FLOAT,
  weight FLOAT,
  body_fat_percentage FLOAT,
  muscle_mass_percentage FLOAT,
  FOREIGN KEY (member_id) REFERENCES regular_member(id)
);

CREATE TABLE health_goals (
	id SERIAL PRIMARY KEY,
  member_id INT,
  date DATE,
  goal VARCHAR(255),
	FOREIGN KEY (member_id) REFERENCES regular_member(id)
);

CREATE TABLE fitness_stats (
	id SERIAL PRIMARY KEY,
  member_id INT,
  date DATE,
  bench_press FLOAT,
  squat FLOAT,
  deadlift FLOAT,
  barbell_row FLOAT,
  military_press FLOAT,
  FOREIGN KEY (member_id) REFERENCES regular_member(id)
);

CREATE TABLE fitness_goals (
	id SERIAL PRIMARY KEY,
	member_id INT,
  date DATE,
  goal VARCHAR(255),
  FOREIGN KEY (member_id) REFERENCES regular_member(id)
);

CREATE TABLE health_and_fitness_achievements (
	id SERIAL PRIMARY KEY,
  member_id INT,
  starting_date DATE,
  completion_date DATE,
  goal VARCHAR(255),
  FOREIGN KEY (member_id) REFERENCES regular_member(id)
);

CREATE TABLE room_booking (
	id SERIAL PRIMARY KEY,
  room_number VARCHAR(255),
  start_date_booking DATE,
  end_date_booking DATE,
  trainer_id INT,
  FOREIGN KEY (trainer_id) REFERENCES trainer(id)
);

CREATE TABLE training_session (
	id SERIAL PRIMARY KEY,
  trainer_id INT,
  room_booking_id INT,
  start_date_time TIMESTAMP,
  end_date_time TIMESTAMP,
  maximum_capacity INT,
  num_users_registered INT,
  FOREIGN KEY (trainer_id) REFERENCES trainer(id),
  FOREIGN KEY (room_booking_id) REFERENCES room_booking(id)
);

CREATE TABLE training_session_for_members (
	session_id INT,
  member_id INT,
  FOREIGN KEY (session_id) REFERENCES training_session(id),
  FOREIGN KEY (member_id) REFERENCES regular_member(id),
  PRIMARY KEY (session_id, member_id)
);

CREATE TABLE equipment_maintenance (
  id SERIAL PRIMARY KEY,
  date_last_maintained DATE,
  description VARCHAR(255)
);

CREATE TABLE billing (
  id SERIAL PRIMARY KEY,
  member_id INT,
  payment_amount FLOAT,
  payment_date DATE,
  FOREIGN KEY (member_id) REFERENCES regular_member(id)
);