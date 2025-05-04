# Notes & Todo Management System

Notes and Todo Management RESTful web application built with **Spring Boot**. This project supports secure user authentication, notes management with file attachments, task management with reminders, and robust admin/user role-based access.

---

## Technology Stack

- **Backend**: Spring Boot (REST APIs)
- **Database**: MySQL
- **ORM**: Spring Data JPA
- **Security**: Spring Security (JWT & OAuth Login)
- **Monitoring**: Spring Boot Actuator
- **Documentation**: Swagger UI
- **Code Quality**: SonarQube (Dockerized)
- **Scheduling**: Spring Scheduler
- **AOP**: Spring AOP (for logging, etc.)
- **Version Control**: Git (via GitHub Desktop)

---

## 🛠 Requirements

- Java 17 or higher
- Maven 3.8+
- MySQL Server
- Docker (for running SonarQube)

---

## Git Workflow

This project uses a **Feature Branch Workflow**:

- Each new feature or bugfix is developed in a **separate branch**.

- Once the feature is complete and tested, it is **merged into the `dev` branch**.

- The `main` branch contains production-ready code.

---

## Environment Profiles

This project uses **Spring Boot profiles** to separate configuration for different environments:

- `dev` – For local development

- `uat` – For testing

- `prod` – For production deployment

```bash
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev
```
---

## Features

### 1. User Authentication & Authorization
- User registration with email verification
- JWT-based login authentication
- Role-based access control (Admin & User)

### 2. Notes Management
- Create, edit, delete categories
- Create, view, update, delete notes
- Attach files (PDF, images, etc.) to notes
- View or download attached files
- Filter notes by category
- Search notes by title, content, or category
- Mark notes as favorites
- View favorite notes
- Copy notes
- Paginated notes view
- Schedule delete: usually for sending email, notifications, deleting.
- Export:
    - All notes to Excel or PDF
    - Single note to Excel or PDF

### 3.Todo Management
- Create, edit, and delete tasks
- Set priority (Low, Medium, High)
- Set task status (To-Do, In Progress, Completed)
- Set task reminders (Email or Push notifications)

---

### Clone the Repository

```bash
git clone https://github.com/Meelanlama/Enotes_RestApi.git
```

### Configure `application.yml`

Update your MySQL credentials and other properties in `src/main/resources/application.yml` including gmail app and password.

### Run the Application

```bash
./mvnw spring-boot:run
```

### Access Swagger Docs

Navigate to:  
[http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)

## Testing

Run tests using:

```bash
./mvnw test
```

This command executes all tests in the project.

### Generate Code Coverage Report (JaCoCo)

**JaCoCo** is used to measure test coverage (i.e., how much of your code is tested).

**1. Run the `verify` phase** to execute tests and generate the report:

```bash
./mvnw verify
```
**2. Locate the coverage report**:

- HTML report:  
  `target/jacoco-report/index.html` – Open this file in a browser to see a visual summary.

- XML report:  
  `target/jacoco-report/jacoco.xml` – Used for SonarQube integration.

## Code Quality

This project uses **SonarQube** for static code analysis. Run it locally using Docker:

`docker run -d --name sonarqube -p 9000:9000 sonarqube`

Access SonarQube dashboard at [http://localhost:9000](http://localhost:9000)

Login Credentials (default):

- Username: `admin`

- Password: `admin`

### Integrating with SonarQube

To analyze your project with SonarQube:

1. Add the Sonar plugin to your Maven build (if not already added).

2. Ensure JaCoCo is generating the XML report.

3. Run the Sonar analysis:

```bash
   mvnw sonar:sonar -Dsonar.projectKey=your_project_key -Dsonar.host.url=http://localhost:9000 -Dsonar.login=your_token
```

   Replace:

  - `your_project_key` with your defined project key.

  - `your_token` with a personal access token from SonarQube.
