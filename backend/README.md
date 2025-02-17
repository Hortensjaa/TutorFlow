# Tutor Flow Backend

## Overview
The backend of the **Tutor Flow** project is built using **Spring Boot** with Java 17. 
It provides a RESTful API for managing users (teachers), students, lessons, and file storage via Google Cloud Storage.

## Production
The backend is deployed on Render using Docker container and can be accessed at 
[https://tutor-deployment-latest.onrender.com](https://tutor-deployment-latest.onrender.com).

## Technologies Used
- **Spring Boot** (Java 17)
- **Spring Security** (OAuth2 authentication with Gmail)
- **JPA/Hibernate** (for database management)
- **Supabase** (PostgreSQL database)
- **Google Cloud Storage** (for file storage)
- **Lombok** (for boilerplate code)
- **Mockito & JUnit** (for tests)
- **Maven** (for dependency management)

## Project Structure
```
TutorFlowBackend/
│-- src/
│   ├── main/
│   │   ├── java/com/tutorflow/
│   │   │   ├── config/                   # Security Configurations
│   │   │   ├── controllers/              # API Controllers
│   │   │   ├── entities/                 # Entity Classes
│   │   │   ├── repositories/             # JPA Repositories
│   │   │   ├── services/                 # Business Logic
│   │   │   ├── models/                   # Request/Response Models
│   │   ├── resources/ 
│   │   │   ├── application.properties    # Configuration Files
│   ├── test/                            # Tests
│-- pom.xml                               # Maven Dependencies
```

## Database Schema
```
User (Teacher)
  ├── user_id (PK)
  ├── username
  ├── email
  ├── students (One-to-Many)
  ├── taught_lessons (One-to-Many)

Student
  ├── student_id (PK)
  ├── name
  ├── teacher (Many-to-One)
  ├── lessons (One-to-Many)

Lesson
  ├── lesson_id (PK)
  ├── topic
  ├── description
  ├── rate
  ├── date
  ├── teacher (Many-to-One)
  ├── student (Many-to-One)
  ├── files (One-to-Many)

File
  ├── file_id (PK)
  ├── path
  ├── lesson (Many-to-One)
```

## API Endpoints

### Lesson Endpoints
- `GET /api/lessons/all` - Retrieve all user's lessons
- `POST /api/lessons/add` - Add a new lesson
- `GET /api/lessons/{id}` - Retrieve a lesson by ID
- `DELETE /api/lessons/{id}/delete` - Delete a lesson

### File Storage
- `POST /api/storage/download` - Download a file

### User Management
- `GET /api/user/add_user` - Add a user
- `GET /api/user/active` - Retrieve the active user
- `GET /api/user/students` - Get a list of students for the teacher
- `PUT /api/user/` - Update user details
- `POST /api/user/add_student` - Add a student
- `DELETE /api/user/delete_student` - Remove a student

## Setup & Running the Backend

### Prerequisites
- Java 17
- Maven
- PostgreSQL (Supabase configured)
- Google Cloud Storage credentials

### Steps to Run
1. Clone the repository:
   ```sh
   git clone https://github.com/Hortensjaa/TutorFlow
   ```
2. Navigate to the project folder:
   ```sh
   cd backend
   ```
3. Configure environment variables in `.env` (or `application.properties`). Example:
   ```properties
    spring.datasource.url=${DATASOURCE_URL}
    spring.datasource.username=${DATABASE_USER}
    spring.datasource.password=${DATABASE_PASSWORD}
    spring.security.oauth2.client.registration.google.client-id=${OAUTH2_CLIENT}
    spring.security.oauth2.client.registration.google.client-secret=${OAUTH2_SECRET}
   ```
   and download the Google Cloud Storage credentials file as `credentials.json` in the `resources` folder.
4. Build and run the application:
   ```sh
   mvn spring-boot:run
   ```
5. The backend should be running at `http://localhost:8080`

## Testing
Run unit tests with:
```sh
mvn test
```


