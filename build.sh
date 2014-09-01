#!/bin/bash

set -e

javac -classpath .:postgresql.jar *.java 
java  -classpath .:postgresql.jar Main
