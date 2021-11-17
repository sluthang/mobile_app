#!/bin/bash

#Create test database
sqlite3 test_worlds_db.sqlite "CREATE TABLE worlds (
id         INTEGER     NOT NULL PRIMARY KEY AUTOINCREMENT,
name       TEXT        NOT NULL UNIQUE,
size       INTEGER     NOT NULL,
data       VARCHAR     NOT NULL);"

#Add demo quotes
sqlite3 test_worlds_db.sqlite "INSERT INTO worlds (id, name, size, data)
VALUES (1, 'beau', 2, '{\"objects\":[{\"position\":[1,3],\"type\":\"OBSTACLE\"},{\"position\":[1,0],\"type\":\"PIT\"}]}');"