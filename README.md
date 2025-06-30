# Academic Document Management System (ADMS)

Academic Document Management System (ADMS) is a platform designed to manage, store, categorize, and share academic documents, supporting both teaching and learning activities in educational environments.

## Features

- **User Management:** Registration, login, email verification, role-based access control, profile update, password change, and password recovery.
- **Class & Subject Management:** Create, update, delete, and search for classes and subjects.
- **Document Management:** Upload, categorize, tag, search, preview, and download academic documents.
- **Book Series, Volumes, and Chapters:** Organize documents in a hierarchical structure (series, volumes, chapters).
- **Lesson & Comment Management:** Create lessons, allow users to comment, and report inappropriate comments.
- **Role & Permission System:** Manage user roles and permissions for different resources.
- **Security:** JWT authentication, API security, centralized error handling.
- **Email Integration:** Send verification and password recovery emails.
- **Advanced Search & Pagination:** Filter, paginate, and search for documents, users, classes, etc.

## Tech Stack

- **Backend:** Java, Spring Boot, Spring Security, JPA/Hibernate
- **API:** RESTful
- **Security:** JWT Authentication
- **Database:** MySQL
- **Others:** Docker, Maven

## Getting Started

### Prerequisites

- Java 17+
- Maven 3.6+
- Docker (optional, for containerized deployment)
- Database (MySQL/PostgreSQL)

### Quick Start

1. **Clone the repository:**
   ```bash
   git clone <https://github.com/tiendung12040155/Academic-Document-Management-System>
   cd Academic-Document-Management-System
   ```

2. **Configure the database:**  
   Edit `src/main/resources/application.properties` with your database credentials.

3. **Run with Docker (recommended):**
   ```bash
   docker compose up --build
   ```

   Or run manually:
   ```bash
   ./mvnw spring-boot:run
   ```

4. **Access the API:**  
   By default, the API is available at: `http://localhost:8080`

## API Documentation

- Swagger/OpenAPI is available at: `http://localhost:8080/swagger-ui.html` (if enabled)

## Contribution

Contributions, bug reports, and feature requests are welcome!  
Please open an [issue](https://github.com/tiendung12040155/Academic-Document-Management-System) or submit a pull request.

## License

This project is licensed under the MIT License.