CREATE DATABASE kokocashable;
CREATE USER kokouser WITH password 'qwerty';
GRANT ALL PRIVILEGES ON DATABASE kokocashable to kokouser;

-- \q
-- $ psql -h localhost kokocashable kokouser

CREATE TABLE kokoaccaunts (id integer primary key, amount integer);
