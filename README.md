# Payroll application
## Core Features & Functionalities
### 🧑‍💼 Employee Management
* Add/Edit/Delete Employee
* View employee details
* Assign contract and bank account
* Unique identifiers (CPF, email, etc.)

### 📄 Contract Management
* Create/assign employment contracts
* Track:
  * Start and end date
  * Salary
  * Status (active/inactive)
  * Validate overlapping or expired contracts

### 💵 Payroll Processing
* Generate payroll monthly or on-demand
* Calculate:
  * Gross salary
  * Deductions (e.g., INSS, taxes)
  * Net salary
* Support for:
  * Fixed salary
  * Overtime
  * Bonuses
  * Vacation pay
* Store payroll history

### 📑 Payslip Generation
* Generate downloadable PDF payslips
* Include:
  * Employee info
  * Contract summary
  * Payment breakdown
  * Email to employees (optional)

## Technologies
* [Spring Boot](https://spring.io/projects/spring-boot)
* [Spring MVC](https://docs.spring.io/spring-framework/reference/web/webmvc.html)
* [Maven](https://maven.apache.org/)
* [Maven Multiple Modules](https://maven.apache.org/guides/index.html)
* [Mapstruct](https://mapstruct.org/)
* [Docker compose](https://docs.docker.com/compose/)
* [Kubernetes](https://kubernetes.io/docs/home/) as deploy management application
* [MiniKube](https://minikube.sigs.k8s.io/docs/start/) as local cluster
* [Springdoc](https://springdoc.org/)
* [Spring Actuator](https://docs.spring.io/spring-boot/docs/2.0.x/actuator-api/html/) as part of Observability
* [Liquibase](https://www.liquibase.com/) as database versioning
* [PostgreSQL](https://www.postgresql.org/) as a database

## Architecture
Based on [Clean Architecture](https://blog.cleancoder.com/uncle-bob/2012/08/13/the-clean-architecture.html)
```
domain
└── src/main/java
    └── com/example/payroll/domain
        |── contract/ 
        │   ├── model
        │   └── port
        ├── employee/
        │   ├── exception
        │   └── model
        │   └── port
        └── payroll/
            ├── model
            └── port
payroll-api
└── src/main/java
    └── com/example/payroll/
        |── adapters/ 
        │   ├── inbound
        │   └── outbound
        ├── application/
        │   └── services
        ├── config/
        └── PayrollApplication.java
```
## Design
![payroll_design](others/etc/docs/payroll-design.png)

## There are 2 options to run this RESTful service
### Option1:  Running the application using docker compose
#### From app root directory:
```
docker compose up -d --build
```
#### Test API
`curl http://localhost:8081/employees`

#### Test using actuator
`curl http://localhost:8081/actuator`

#### API documentation
http://localhost:8081/swagger-ui/index.html

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

`http://{minikube-ip}:30081/employees`

#### Test API
`curl http://{minikube-ip}:30081/employees`

### Test using actuator
`curl http://{minikube-ip}:30081/actuator`

### API documentation
http://{minikube-ip}:30081/swagger-ui/index.html