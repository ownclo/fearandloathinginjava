#!/bin/bash

set -e

javac -classpath .:postgresql.jar:json-simple-1.1.1.jar *.java 
#java  -classpath .:postgresql.jar Main
