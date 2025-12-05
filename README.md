Issue Tracker Backend

A backend system built with Spring Boot and MySQL to track issues similar to what you see in simple ticketing systems.
This project focuses on clean architecture, authentication, and API usability rather than UI.

What this project does

Allows users to raise issues (tickets)

Users can view and update their own issues

Admins can view/update/delete any issue

Authentication handled using JWT tokens

API follows a clean layered structure

The application is intentionally kept simple and readable so it can be extended later.

Tech Stack :

1.Java
2.Spring Boot
3.REST APIs
4.MySQL
5.JWT (Authentication)
6.JPA/Hibernate
7.Maven

Why I built this :
I wanted to build something beyond CRUD to understand Proper request/response structure (DTOs)

How JWT fits in a backend system
.controller      → Handles incoming requests and responses
.service         → Business logic
.repository      → Database operations
.model           → JPA entities
.dto             → Request and response objects
.security        → JWT token generation and validation
.exception       → Centralized error handling

How to Run Locally
Requirements:
Java (17+ recommended)
Maven

MySQL running locally
1. Clone this repository
2. Configure MySQL credentials in application.properties
3. Run: mvn clean install
4. Start the application: mvn spring-boot:run 
The backend will run at:
http://localhost:8080

Notes:
This project is intentionally built in a clean and understandable way so that the backend architecture, authentication flow, and coding style can be evaluated easily.
It can serve as a base for production-level extensions.
Developer

Ravi Teja Bollina
LinkedIn: https://linkedin.com/in/ravitejabollina
Developer

Ravi Teja Bollina
LinkedIn: https://linkedin.com/in/ravitejabollina
GitHub: https://github.com/RAVITEJABOLLINA
