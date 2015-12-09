CREATE KEYSPACE IF NOT EXISTS usertest WITH REPLICATION = { 'class' : 'SimpleStrategy', 'replication_factor' : 1 };
USE usertest;
DROP TABLE users;
CREATE TABLE users (
  user_id int,
  fname text,
  lname text,
  email text,
  primary key (user_id)
);
COPY users (user_id, email, lname, fname) FROM '/home/vagrant/data/sample_users.csv' WITH HEADER = true;