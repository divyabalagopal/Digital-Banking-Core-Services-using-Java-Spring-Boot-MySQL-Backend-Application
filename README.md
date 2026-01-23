Banking Management System

A Java Spring Boot application for managing basic banking operations including account management, transaction processing, and automated statement generation.

Project Overview

This banking management system demonstrates core banking functionalities with a clean service-oriented architecture. The application handles user account creation, balance inquiries, money transfers, and generates PDF bank statements with email notifications for all operations.

Technology Stack

Backend Framework:
- Java 17
- Spring Boot
- Spring Data JPA
- Maven for dependency management

Database and Persistence:
- MySQL database
- Hibernate ORM for data mapping
- JPA repositories for data access

External Libraries:
- iText library for PDF generation
- Spring Boot Email Service for Email Notifications
- Lombok for reducing boilerplate code
- SLF4J for application logging
- OpenAPI/Swagger for comprehensive API documentation

Project Architecture

The application follows a layered architecture pattern that separates concerns into distinct layers. The controller layer handles incoming requests and delegates business logic to the service layer. The service layer contains all banking business rules and coordinates between different services. The repository layer manages data persistence and retrieval from the database.

Core Features:
1. Account Management: Creation, Prevents duplicate account creation by checking existing email address.
2. Check Balance: Users can check their account balance by providing their account number. Validation exists to ensure users do not attempt to retreive balance of non-existing account.
3. Transaction Processing: Credit, Debit, Transfer(UPI).
4. Statement Generation: Generates PDF bank statements for any date range specified by the user.
5. Email Notifications: Automated e-mail alerts are sent for all major account actvities.

Getting Started

Prerequisites:
- Java 17 or higher installed
- Maven 3.6 or higher
- MySQL database server running
- SMTP email server access for notifications

Setup Steps:
1. Clone the repository to your local development environment
2. Configure database connection properties in application.properties
3. Set up email server configuration for notification services
4. Configure PDF output directory path for statement generation
5. Run Maven clean install to download dependencies
6. Start the application using Maven Spring Boot plugin

Database Setup:
Create a MySQL database for the application and update connection properties with appropriate credentials. The application will automatically create required tables based on JPA entity annotations when first started.

Development Approach

This project demonstrates understanding of enterprise Java development patterns including service layer architecture, repository pattern for data access, and proper separation of concerns. The implementation follows Spring Boot best practices with dependency injection, annotation-driven configuration, and proper exception handling.
