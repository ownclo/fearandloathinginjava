#!/bin/bash

set -e

javac -classpath postgresql-jdbc3.jar *.java 
java -classpath .:postgresql-jdbc3.jar Main
