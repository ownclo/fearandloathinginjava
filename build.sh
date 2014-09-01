#!/bin/bash

set -e

javac -classpath .:/usr/share/java/servlet-api.jar:postgresql-jdbc3.jar *.java 
java  -classpath .:/usr/share/java/servlet-api.jar:postgresql-jdbc3.jar Main
