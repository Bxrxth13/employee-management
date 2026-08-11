# Employee Management System 🚀

A production-ready, clean architecture **Spring Boot 3.x** REST API for Employee Management built with **Java 17**, **PostgreSQL**, **Spring Data JPA**, **Bean Validation**, **OpenAPI / Swagger UI**, **JUnit 5**, **Mockito**, **Docker**, **Docker Compose**, and **Jenkins CI/CD Pipeline**.

---

## 📋 Table of Contents
- [Project Overview](#-project-overview)
- [Technology Stack](#-technology-stack)
- [Folder Structure](#-folder-structure)
- [Installation Steps](#-installation-steps)
- [Database Setup](#-database-setup)
- [Run Locally](#-run-locally)
- [Run with Docker](#-run-with-docker)
- [Run with Docker Compose](#-run-with-docker-compose)
- [Swagger UI & API Documentation](#-swagger-ui--api-documentation)
- [API Examples & cURL Requests](#-api-examples--curl-requests)
- [Git Branching Workflow](#-git-branching-workflow)
- [Merge Conflict Example & Resolution](#-merge-conflict-example--resolution)
- [Jenkins CI/CD Pipeline & Flowchart](#-jenkins-cicd-pipeline--flowchart)
- [Docker Cheat Sheet](#-docker-cheat-sheet)
- [Useful Git Commands](#-useful-git-commands)
- [Troubleshooting](#-troubleshooting)

---

## 🎯 Project Overview
The **Employee Management System** provides a robust, scalable backend for managing employee details, department assignments, designations, joining dates, and salaries. Designed following strict SOLID principles, constructor dependency injection, DTO decoupling, and global exception handling.

### Key Capabilities
- Full CRUD API operations (`POST`, `GET`, `PUT`, `DELETE`).
- Enterprise-grade Bean Validation (`@NotBlank`, `@Email`, `@Positive`, `@NotNull`).
- OpenAPI 3 / Swagger interactive documentation UI.
- Comprehensive Unit & Slice Testing (Service, Controller, Repository) with JUnit 5 & Mockito.
- Quality Gate CI/CD automation via Jenkins pipeline halting on test failures.
- Multi-container containerization via Docker & Docker Compose over a bridge network.

---

## 🛠 Technology Stack
| Layer | Technology / Tool | Version |
| :--- | :--- | :--- |
| **Language** | Java | 17 |
| **Framework** | Spring Boot | 3.2.5 |
| **Build Tool** | Maven | 3.9+ |
| **Database** | PostgreSQL | 15 |
| **ORM** | Spring Data JPA / Hibernate | 6.x |
| **Validation** | Jakarta Bean Validation | 3.x |
| **Boilerplate** | Lombok | 1.18.x |
| **Documentation**| Springdoc OpenAPI / Swagger UI | 2.5.0 |
| **Testing** | JUnit 5, Mockito, AssertJ | 5.x |
| **Containerization**| Docker, Docker Compose | 24+ |
| **CI/CD** | Jenkins Declarative Pipeline | 2.x+ |

---

## 📁 Folder Structure
The codebase follows standard Clean 3-Tier Architecture:

```
employee-management/
├── Dockerfile
├── docker-compose.yml
├── Jenkinsfile
├── pom.xml
├── README.md
├── .gitignore
└── src/
    ├── main/
    │   ├── java/
    │   │   └── com/management/employee/
    │   │       ├── EmployeeManagementApplication.java
    │   │       ├── config/
    │   │       │   └── OpenApiConfig.java
    │   │       ├── controller/
    │   │       │   └── EmployeeController.java
    │   │       ├── dto/
    │   │       │   └── EmployeeDto.java
    │   │       ├── entity/
    │   │       │   └── Employee.java
    │   │       ├── exception/
    │   │       │   ├── DuplicateEmailException.java
    │   │       │   ├── EmployeeNotFoundException.java
    │   │       │   ├── ErrorDetails.java
    │   │       │   └── GlobalExceptionHandler.java
    │   │       ├── repository/
    │   │       │   └── EmployeeRepository.java
    │   │       ├── service/
    │   │       │   ├── EmployeeService.java
    │   │       │   └── impl/
    │   │       │       └── EmployeeServiceImpl.java
    │   │       └── util/
    │   └── resources/
    │       └── application.properties
    └── test/
        └── java/
            └── com/management/employee/
                ├── FailingEmployeeTest.java
                ├── controller/
                │   └── EmployeeControllerTest.java
                ├── repository/
                │   └── EmployeeRepositoryTest.java
                └── service/
                    └── EmployeeServiceTest.java
```

---

## ⚙️ Installation Steps

### Prerequisites
- **JDK 17** or higher installed (`java -version`).
- **Maven 3.8+** installed (`mvn -version`).
- **PostgreSQL 14+** running locally OR **Docker Desktop** installed.

### Clone & Build
```bash
# Clone repository
git clone https://github.com/company/employee-management.git
cd employee-management

# Compile and package application
mvn clean package
```

---

## 🗄 Database Setup
Create PostgreSQL database `employeedb` using psql or PgAdmin:

```sql
CREATE DATABASE employeedb;
CREATE USER postgres WITH ENCRYPTED PASSWORD 'postgres';
GRANT ALL PRIVILEGES ON DATABASE employeedb TO postgres;
```

---

## 🚀 Run Locally
Execute the Spring Boot application using Maven:

```bash
mvn spring-boot:run
```

Or run the packaged JAR directly:
```bash
java -jar target/employee-management-1.0.0.jar
```

The application will start on **port 8080**.

---

## 🐳 Run with Docker

### 1. Build Docker Image
```bash
docker build -t employee-management .
```

### 2. Run Container (Requires local PostgreSQL)
```bash
docker run -d \
  --name employee-app \
  -p 8080:8080 \
  -e SPRING_DATASOURCE_URL=jdbc:postgresql://host.docker.internal:5432/employeedb \
  -e SPRING_DATASOURCE_USERNAME=postgres \
  -e SPRING_DATASOURCE_PASSWORD=postgres \
  employee-management
```

---

## 🐙 Run with Docker Compose
To launch both PostgreSQL and Spring Boot containers simultaneously on a isolated bridge network:

```bash
# Start containers in detached mode
docker-compose up -d

# Check running services
docker-compose ps

# View real-time logs
docker-compose logs -f employee-app

# Tear down containers and networks
docker-compose down
```

---

## 📖 Swagger UI & API Documentation
Once the server is running, access Swagger UI in your browser:
- **Swagger Interactive UI**: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)
- **OpenAPI JSON Spec**: [http://localhost:8080/v3/api-docs](http://localhost:8080/v3/api-docs)

---

## 🧪 API Examples & cURL Requests

### 1. Create Employee (`POST /employees`)
```bash
curl -X POST http://localhost:8080/employees \
  -H "Content-Type: application/json" \
  -d '{
    "firstName": "Alex",
    "lastName": "Rivera",
    "email": "alex.rivera@company.com",
    "department": "Engineering",
    "designation": "Backend Lead",
    "salary": 115000.00,
    "joiningDate": "2024-02-01"
  }'
```

**Response (201 Created):**
```json
{
  "employeeId": 1,
  "firstName": "Alex",
  "lastName": "Rivera",
  "email": "alex.rivera@company.com",
  "department": "Engineering",
  "designation": "Backend Lead",
  "salary": 115000.0,
  "joiningDate": "2024-02-01"
}
```

### 2. Get All Employees (`GET /employees`)
```bash
curl -X GET http://localhost:8080/employees
```

**Response (200 OK):**
```json
[
  {
    "employeeId": 1,
    "firstName": "Alex",
    "lastName": "Rivera",
    "email": "alex.rivera@company.com",
    "department": "Engineering",
    "designation": "Backend Lead",
    "salary": 115000.0,
    "joiningDate": "2024-02-01"
  }
]
```

### 3. Get Employee By ID (`GET /employees/{id}`)
```bash
curl -X GET http://localhost:8080/employees/1
```

### 4. Update Employee (`PUT /employees/{id}`)
```bash
curl -X PUT http://localhost:8080/employees/1 \
  -H "Content-Type: application/json" \
  -d '{
    "firstName": "Alex",
    "lastName": "Rivera",
    "email": "alex.rivera@company.com",
    "department": "Engineering",
    "designation": "Principal Engineer",
    "salary": 130000.00,
    "joiningDate": "2024-02-01"
  }'
```

### 5. Delete Employee (`DELETE /employees/{id}`)
```bash
curl -X DELETE http://localhost:8080/employees/1
```

**Response (200 OK):**
```text
Employee deleted successfully with ID: 1
```

---

## 🔀 Git Branching Workflow

This repository demonstrates concurrent collaboration across three developer feature branches:

```text
main ----------------------------------------------------------> [Release]
  \                                                            /
develop -------------------*-----------*-----------*---------*-> [Integration]
  \                       /           /           /
   ├── developerA -------/           /           /  (Create Employee REST API)
   ├── developerB ------------------/           /   (Modify EmployeeService)
   └── developerC -----------------------------/    (Database Configuration)
```

### Command Sequence for Developer Tasks:
```bash
# 1. Switch to branch
git checkout develop

# 2. Create feature branch
git checkout -b developerA

# 3. Stage modified files
git add .

# 4. Commit changes with standard message
git commit -m "feat(api): create employee endpoint controller"

# 5. Push branch to remote repository
git push origin developerA

# 6. Merge feature branch into develop
git checkout develop
git merge developerA
```

---

## ⚔️ Merge Conflict Example & Resolution

### Conflict Scenario in `EmployeeService.java`
When **Developer A** and **Developer B** modify the same method signature in `EmployeeService.java` simultaneously, Git produces a merge conflict with conflict markers:

```java
<<<<<<< HEAD
    // Main branch implementation by Developer A
    EmployeeDto getEmployeeById(Long employeeId);
=======
    // Developer B implementation adding audit flags
    EmployeeDto getEmployeeById(Long employeeId, boolean includeAuditHistory);
>>>>>>> developerB
```

### Resolution Steps
1. **Identify the conflict file**: Run `git status` to locate unmerged paths.
2. **Open file and inspect conflict markers**: Locate `<<<<<<< HEAD`, `=======`, and `>>>>>>> developerB`.
3. **Decide business intent**: Retain or combine desired code logic.
4. **Edit the file to remove markers**:
```java
    // Resolved method supporting optional audit history
    EmployeeDto getEmployeeById(Long employeeId, boolean includeAuditHistory);
```
5. **Stage and commit resolution**:
```bash
git add src/main/java/com/management/employee/service/EmployeeService.java
git commit -m "fix(merge): resolve conflict in EmployeeService between main and developerB"
```

---

## 🔄 Jenkins CI/CD Pipeline & Flowchart

The declarative `Jenkinsfile` enforces an 8-stage automation pipeline with an strict **Unit Test Quality Gate**:

```text
+-------------------+
|  Stage 1: Checkout|
+---------+---------+
          |
+---------v---------+
| Stage 2: Mvn Clean|
+---------+---------+
          |
+---------v---------+
| Stage 3: Compile  |
+---------+---------+
          |
+---------v---------+       Unit Tests Fail?
| Stage 4: Unit Test| -----------------------------> [STOP PIPELINE IMMEDIATELY]
+---------+---------+                               (No Docker Build / Deployment)
          | Unit Tests Pass
+---------v---------+
| Stage 5: Package  |
+---------+---------+
          |
+---------v---------+
|Stage 6: Docker Bld|
+---------+---------+
          |
+---------v---------+
|Stage 7: Stop Prev |
+---------+---------+
          |
+---------v---------+
| Stage 8: Deploy   |
+-------------------+
```

> [!IMPORTANT]
> **Quality Gate Enforcement**: If any test fails in Stage 4 (`mvn test`), the pipeline terminates immediately. Docker build and deployment stages are bypassed completely.

---

## 🛠 Docker Cheat Sheet
```bash
# Build image
docker build -t employee-management .

# List images
docker images

# Run container
docker run -d --name employee-app -p 8080:8080 employee-management

# View container logs
docker logs -f employee-app

# Stop and remove container
docker stop employee-app && docker rm employee-app

# Execute shell in container
docker exec -it employee-app sh
```

---

## 💡 Useful Git Commands
```bash
# Switch branches
git checkout <branch-name>

# Create & switch branch
git checkout -b <new-branch-name>

# View branch status
git status

# View formatted git graph
git log --graph --oneline --all

# Stash uncommitted changes
git stash
git stash pop
```

---

## ❓ Troubleshooting

| Issue | Root Cause | Solution |
| :--- | :--- | :--- |
| **`PSQLException: Connection refused`** | PostgreSQL server is down or unreachable. | Verify PostgreSQL service is running on 5432 (`docker-compose ps` or `pg_isready`). |
| **`Web server failed to start. Port 8080 occupied`** | Another app is using port 8080. | Stop occupying process or change `server.port` in `application.properties`. |
| **`Jenkins stage 4 failed`** | Unit test failure or disabled test re-enabled. | Run `mvn test` locally to inspect error report in `target/surefire-reports/`. |
```
