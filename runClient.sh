#TestClient BNAME NREADERS NWRITERS IDMIN IDMAX
java  -classpath .:postgresql.jar TestClient http://localhost:8000/accounts/ 100 10 1480 1490
