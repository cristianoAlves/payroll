# Example of RESTful service
https://spring.io/guides/tutorials/rest

## There are 3 options to run this RESTful service
### Option1: Running the application standalone
#### building the jar
```
mvn clean install
```
#### building docker image
```
docker build -t payroll-api .
```
#### Start the PostgreSQL Docker Container
#### Running postgres standalone
```
docker run --name payroll-postgres \
  -e POSTGRES_DB=payrolldb \
  -e POSTGRES_USER=postgres \
  -e POSTGRES_PASSWORD=mysecretpassword \
  -p 5432:5432 \
  -d postgres:15
```
#### running the payroll app
```
docker run -p 8081:8081 payroll-api
```
### Option2:  Running the application using docker compose
#### From your root directory:
```
docker compose up -d --build
```
### Option3:  Running the application using kubernetes
https://minikube.sigs.k8s.io/docs/start/
#### What you’ll need
* docker
* kubectl
* minikube
#### Start your cluster
```
minikube start
```
#### Build application image
From project root:
```
docker build -t payroll-api .
```
Upload the image to minikube:
```
minikube image load payroll-api:latest
```
From `payroll/others/kubernetes`
#### Deploy all manifest files
```
kubectl apply -f .
```
Get cluster ip
```
minikube ip
```
API is running on port 30081

`http://{minikube-pi}:30081/employees`

#### Test API
`curl http://{minikube-pi}:30081/employees`