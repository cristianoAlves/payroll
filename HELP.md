# Example of RESTful service
https://spring.io/guides/tutorials/rest

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

