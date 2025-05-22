# Example of RESTful service
https://spring.io/guides/tutorials/rest

## Running the application standalone
### building the jar
```
mvn clean install
```
### building docker image
```
docker build -t payroll-api .
```
### running the image
```
docker run -p 8081:8081 payroll-api
```
## Running postgres standalone
### Start the PostgreSQL Docker Container
```
docker run --name payroll-postgres \
  -e POSTGRES_DB=payrolldb \
  -e POSTGRES_USER=payrolluser \
  -e POSTGRES_PASSWORD=payrollpass \
  -p 5432:5432 \
  -d postgres:15
```
## Running the application using docker compose
### From your root directory:
```
docker compose up -d --build
```

