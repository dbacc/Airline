CREATE TABLE IF NOT EXISTS users
(
   id BIGINT (20) NOT NULL AUTO_INCREMENT,
   username VARCHAR (50) NOT NULL,
   password VARCHAR (100) NOT NULL,
   enabled TINYINT NOT NULL DEFAULT 1,
   PRIMARY KEY (id),
   UNIQUE (username)
);
CREATE TABLE IF NOT EXISTS authorities
(
   id BIGINT (20) NOT NULL AUTO_INCREMENT,
   username VARCHAR (50) NOT NULL,
   role VARCHAR (50) NOT NULL,
   PRIMARY KEY (id),
   FOREIGN KEY (username) REFERENCES users (username)
);
CREATE TABLE IF NOT EXISTS tickets
(
   id BIGINT (20) NOT NULL AUTO_INCREMENT,
   username VARCHAR (50) NOT NULL,
   flight_id BIGINT (20) NOT NULL,
   price DECIMAL
   (
      15,
      2
   )
   NOT NULL,
   seat INTEGER (4) NOT NULL,
   PRIMARY KEY (id),
   FOREIGN KEY (username) REFERENCES users (username),
   FOREIGN KEY (flight_id) REFERENCES flight (id)
);
CREATE TABLE IF NOT EXISTS traffic
(
   id BIGINT (20) NOT NULL AUTO_INCREMENT,
   day DATE NOT NULL,
   minute TIME NOT NULL,
   traffic BIGINT (20) NOT NULL,
   PRIMARY KEY (id)
);
CREATE TABLE IF NOT EXISTS traffic_profiles
(
   id BIGINT (20) NOT NULL AUTO_INCREMENT,
   hour TIME NOT NULL,
   traffic BIGINT (20) NOT NULL,
   PRIMARY KEY (id),
   UNIQUE (hour)
);
CREATE TABLE IF NOT EXISTS login_fails
(
   id BIGINT (20) NOT NULL AUTO_INCREMENT,
   day DATE NOT NULL,
   hour TIME NOT NULL,
   fail_count BIGINT (20) NOT NULL,
   PRIMARY KEY (id)
);
CREATE TABLE IF NOT EXISTS login_profiles
(
   id BIGINT (20) NOT NULL AUTO_INCREMENT,
   hour TIME NOT NULL,
   fail_attempts BIGINT (20) NOT NULL,
   PRIMARY KEY (id),
   UNIQUE (hour)
);
CREATE TABLE IF NOT EXISTS ip_traffic
(
   id BIGINT (20) NOT NULL AUTO_INCREMENT,
   ip VARCHAR (39) NOT NULL,
   minute TIME NOT NULL,
   traffic BIGINT (20) NOT NULL,
   PRIMARY KEY (id)
);
CREATE TABLE IF NOT EXISTS blocked_ips
(
   id BIGINT (20) NOT NULL AUTO_INCREMENT,
   ip VARCHAR (39) NOT NULL,
   PRIMARY KEY (id)
);