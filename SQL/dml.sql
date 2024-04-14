-- Inserting data into all of the tables

INSERT INTO regular_member (username, first_name, last_name, email, date_of_birth, password)
VALUES  ('TheJohnDoe', 'John', 'Doe', 'john.doe@example.com', '2000-09-01', 'password123'),
        ('TheJaneSmith', 'Jane', 'Smith', 'jane.smith@example.com', '2000-09-02', 'password1234'),
        ('TheJimBeam', 'Jim', 'Beam', 'jim.beam@example.com', '2001-09-03', 'password1235');

INSERT INTO trainer (username, first_name, last_name, email, password)
VALUES  ('trainerBob',  'Bob', 'him', 'bob@example.com', 'passwordBob'),
        ('trainerChad', 'Chad', 'himothy', 'chad@example.com', 'passwordChad'),
        ('trainerGoat', 'Goat', 'himmler', 'goat@example.com', 'passwordGoat');    

INSERT INTO administrator (username, password)
VALUES  ('admin', 'admin'); 

INSERT INTO health_stats (member_id, date, height, weight, body_fat_percentage, muscle_mass_percentage)
VALUES  (1, '2024-04-13', 181, 140, 18, 43),
        (2, '2024-04-13', 185, 160, 22, 40), 
        (3, '2024-04-13', 173, 130, 13, 57);

INSERT INTO health_goals (member_id, date, goal)
VALUES  (1, '2024-04-13', 'Weigh 150 pounds'),
        (2, '2024-04-13', 'Get muscle mass percentage to 50'), 
        (3, '2024-04-13', 'Weigh 140 pounds');

INSERT INTO fitness_stats (member_id, date, bench_press, squat, deadlift, barbell_row, military_press)
VALUES  (1, '2024-04-13', 135, 160, 175, 135, 90),
        (2, '2024-04-13', 180, 225, 225, 125, 85),
        (3, '2024-04-13', 160, 200, 225, 145, 70);

INSERT INTO fitness_goals (member_id, date, goal)
VALUES  (1, '2024-04-13', 'Bench press 160'),
        (2, '2024-04-13', 'Military press 135'),
        (3, '2024-04-13', 'Get a gf');

INSERT INTO health_and_fitness_achievements (member_id, starting_date, completion_date, goal)
VALUES  (1, '2024-01-1', '2024-04-13', 'Completed fitness goal of bench pressing 160'),
        (2, '2024-01-1', '2024-04-13', 'Completed fitness goal of military pressing 135'),
        (3, '2024-01-1', '2024-04-13', 'Completed the bulk, gained weight');

INSERT INTO room_booking (room_number, start_date_booking, end_date_booking, trainer_id)
VALUES  ('Booking room 1', '2024-04-12', '2024-04-13', 1),
        ('Booking room 2', '2024-04-12', '2024-04-13', 2),
        ('Booking room 3', '2024-04-12', '2024-04-13', 3);  

INSERT INTO training_session (trainer_id, room_booking_id, start_date_time, end_date_time, maximum_capacity, num_users_registered)
VALUES  (1, 1, '2024-04-12 13:00:00', '2024-04-12 15:00:00', 10, 5),
        (2, 2, '2024-04-12 13:00:00', '2024-04-12 15:00:00', 15, 8),
        (3, 3, '2024-04-12 13:00:00', '2024-04-12 15:00:00', 15, 8);    


INSERT INTO training_session_for_members (session_id, member_id)
VALUES  (1, 1),
        (2, 2),
        (2, 3);  

INSERT INTO equipment_maintenance (date_last_maintained, description)
VALUES  ('2024-01-01', 'Smith machine maintenance'),
        ('2024-01-01', 'Squat rack unstable fixed'),
        ('2024-01-01', 'Treadmill maintenance');
        
INSERT INTO billing (member_id, payment_amount, payment_date)
VALUES  (1, 100.00, '2024-04-12'),
        (2, 100.00, '2024-04-12'),
        (3, 100.00, '2024-04-12');