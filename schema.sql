DROP DATABASE IF EXISTS flight_mgmt_db;
CREATE DATABASE flight_mgmt_db;
USE flight_mgmt_db;

CREATE TABLE IF NOT EXISTS users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(20) NOT NULL,
    first_name VARCHAR(50),
    last_name VARCHAR(50),
    email VARCHAR(100),
    nic VARCHAR(20),
    passport_number VARCHAR(20)
);

CREATE TABLE IF NOT EXISTS flights (
    flight_id INT AUTO_INCREMENT PRIMARY KEY,
    flight_number VARCHAR(20) UNIQUE NOT NULL,
    source VARCHAR(50) NOT NULL,
    destination VARCHAR(50) NOT NULL,
    flight_date DATE NOT NULL,
    flight_time TIME NOT NULL,
    seats_available INT NOT NULL,
    price DECIMAL(10, 2) NOT NULL,
    status VARCHAR(20) DEFAULT 'ON TIME'
);

CREATE TABLE IF NOT EXISTS bookings (
    booking_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT,
    flight_id INT,
    booking_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    status VARCHAR(20) DEFAULT 'PENDING',
    FOREIGN KEY (user_id) REFERENCES users(user_id),
    FOREIGN KEY (flight_id) REFERENCES flights(flight_id)
);

CREATE TABLE IF NOT EXISTS booked_seats (
    seat_id INT AUTO_INCREMENT PRIMARY KEY,
    booking_id INT,
    seat_number VARCHAR(10) NOT NULL,
    FOREIGN KEY (booking_id) REFERENCES bookings(booking_id) ON DELETE CASCADE
);

INSERT INTO users (user_id, username, password, role, first_name, last_name, email, nic, passport_number) VALUES
(1,'admin','a665a45920422f9d417e4867efdc4fb8a04a1f3fff1fa07e998e86f7f7a27ae3','ADMIN','System','Admin','admin@imairlines.lk','',''),
(2,'usr','a665a45920422f9d417e4867efdc4fb8a04a1f3fff1fa07e998e86f7f7a27ae3','CUSTOMER','Test','User','usr@imairlines.lk','881234567V','P100001'),
(3,'kasun91','a665a45920422f9d417e4867efdc4fb8a04a1f3fff1fa07e998e86f7f7a27ae3','CUSTOMER','Kasun','Silva','kasun@gmail.com','911234567V','N900001'),
(4,'amali92','a665a45920422f9d417e4867efdc4fb8a04a1f3fff1fa07e998e86f7f7a27ae3','CUSTOMER','Amali','Jayasinghe','amali@gmail.com','921234568V','N900002'),
(5,'dilshan93','a665a45920422f9d417e4867efdc4fb8a04a1f3fff1fa07e998e86f7f7a27ae3','CUSTOMER','Dilshan','Kumara','dilshan@gmail.com','931234569V','N900003'),
(6,'tharindu94','a665a45920422f9d417e4867efdc4fb8a04a1f3fff1fa07e998e86f7f7a27ae3','CUSTOMER','Tharindu','Bandara','tharindu@gmail.com','941234570V','N900004'),
(7,'sachini95','a665a45920422f9d417e4867efdc4fb8a04a1f3fff1fa07e998e86f7f7a27ae3','CUSTOMER','Sachini','Wijeratne','sachini@gmail.com','951234571V','N900005'),
(8,'pradeep96','a665a45920422f9d417e4867efdc4fb8a04a1f3fff1fa07e998e86f7f7a27ae3','CUSTOMER','Pradeep','Gunawardena','pradeep@gmail.com','961234572V','N900006'),
(9,'ishara97','a665a45920422f9d417e4867efdc4fb8a04a1f3fff1fa07e998e86f7f7a27ae3','CUSTOMER','Ishara','Rathnayake','ishara@gmail.com','971234573V','N900007'),
(10,'madusha98','a665a45920422f9d417e4867efdc4fb8a04a1f3fff1fa07e998e86f7f7a27ae3','CUSTOMER','Madusha','Senanayake','madusha@gmail.com','981234574V','N900008'),
(11,'chamika99','a665a45920422f9d417e4867efdc4fb8a04a1f3fff1fa07e998e86f7f7a27ae3','CUSTOMER','Chamika','De Silva','chamika@gmail.com','991234575V','N900009'),
(12,'hashini00','a665a45920422f9d417e4867efdc4fb8a04a1f3fff1fa07e998e86f7f7a27ae3','CUSTOMER','Hashini','Abeysekara','hashini@gmail.com','001234576V','N900010');

INSERT INTO flights (flight_id, flight_number, source, destination, flight_date, flight_time, seats_available, price, status) VALUES
(1,'UL201','Colombo','Kandy','2025-12-19','08:00:00',60,3500,'ON TIME'),
(2,'UL202','Colombo','Galle','2025-12-20','09:00:00',55,3200,'ON TIME'),
(3,'UL203','Colombo','Jaffna','2025-12-21','10:00:00',70,5000,'ON TIME'),
(4,'UL204','Colombo','Trincomalee','2025-12-22','11:00:00',65,4800,'ON TIME'),
(5,'UL205','Colombo','Batticaloa','2025-12-23','12:00:00',60,4700,'ON TIME'),
(6,'UL206','Colombo','Matara','2025-12-24','13:00:00',55,3000,'ON TIME'),
(7,'UL207','Colombo','Kurunegala','2025-12-25','14:00:00',50,2800,'ON TIME'),
(8,'UL208','Colombo','Anuradhapura','2025-12-26','15:00:00',60,4200,'ON TIME'),
(9,'UL209','Colombo','Hambantota','2025-12-27','16:00:00',65,4500,'ON TIME'),
(10,'UL210','Colombo','Ratmalana','2025-12-28','17:00:00',45,2500,'ON TIME'),
(11,'UL211','Kandy','Colombo','2025-12-29','08:30:00',60,3500,'ON TIME'),
(12,'UL212','Galle','Colombo','2025-12-30','09:30:00',55,3200,'ON TIME'),
(13,'UL213','Jaffna','Colombo','2026-01-01','10:30:00',70,5000,'ON TIME'),
(14,'UL214','Trincomalee','Colombo','2026-01-02','11:30:00',65,4800,'ON TIME'),
(15,'UL215','Batticaloa','Colombo','2026-01-03','12:30:00',60,4700,'ON TIME'),
(16,'UL216','Matara','Colombo','2026-01-04','13:30:00',55,3000,'ON TIME'),
(17,'UL217','Kurunegala','Colombo','2026-01-05','14:30:00',50,2800,'ON TIME'),
(18,'UL218','Anuradhapura','Colombo','2026-01-06','15:30:00',60,4200,'ON TIME'),
(19,'UL219','Hambantota','Colombo','2026-01-07','16:30:00',65,4500,'ON TIME'),
(20,'UL220','Ratmalana','Colombo','2026-01-08','17:30:00',45,2500,'ON TIME'),
(21,'UL221','Colombo','Kandy','2026-01-09','18:00:00',60,3500,'ON TIME'),
(22,'UL222','Colombo','Galle','2026-01-10','19:00:00',55,3200,'ON TIME'),
(23,'UL223','Colombo','Jaffna','2026-01-11','20:00:00',70,5000,'ON TIME'),
(24,'UL224','Colombo','Trincomalee','2026-01-12','21:00:00',65,4800,'ON TIME'),
(25,'UL225','Colombo','Batticaloa','2026-01-13','22:00:00',60,4700,'ON TIME');

INSERT INTO bookings (booking_id, user_id, flight_id, booking_date, status) VALUES
(1,3,1,NOW(),'CONFIRMED'),
(2,4,2,NOW(),'CONFIRMED'),
(3,5,3,NOW(),'CONFIRMED'),
(4,6,4,NOW(),'CANCELLED'),
(5,7,5,NOW(),'CONFIRMED'),
(6,8,6,NOW(),'CONFIRMED'),
(7,9,7,NOW(),'CONFIRMED'),
(8,10,8,NOW(),'CONFIRMED'),
(9,11,9,NOW(),'CANCELLED'),
(10,12,10,NOW(),'CONFIRMED'),
(11,3,11,NOW(),'CONFIRMED'),
(12,4,12,NOW(),'CONFIRMED'),
(13,5,13,NOW(),'CONFIRMED'),
(14,6,14,NOW(),'CONFIRMED'),
(15,7,15,NOW(),'CONFIRMED');

INSERT INTO booked_seats (seat_id, booking_id, seat_number) VALUES
(1,1,'1A'),(2,1,'1B'),
(3,2,'2A'),
(4,3,'3A'),(5,3,'3B'),(6,3,'3C'),
(7,4,'4A'),
(8,5,'5A'),(9,5,'5B'),
(10,6,'6A'),
(11,7,'7A'),(12,7,'7B'),
(13,8,'8A'),
(14,9,'9A'),
(15,10,'10A'),(16,10,'10B'),
(17,11,'11A'),
(18,12,'12A'),(19,12,'12B'),
(20,13,'13A'),
(21,14,'14A'),(22,14,'14B'),
(23,15,'15A'),(24,15,'15B'),(25,15,'15C');
