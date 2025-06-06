# Example of RESTful service
https://spring.io/guides/tutorials/rest

## There are 2 options to run this RESTful service
### Option1:  Running the application using docker compose
#### From app root directory:
```
docker compose up -d --build
```
### Option2:  Running the application using kubernetes
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