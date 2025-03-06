# Student Management System - API Documentation

This document provides detailed API documentation for the **Student Management System**. The system allows you to manage students, teachers, courses, and authenticate users. It uses **JWT Token** for authentication and **Role-based access** (Admin, Teacher, and Student) for restricting access to certain API endpoints.

## Technologies Used

- **Java 21**
- **Spring Boot** 3.5+
- **Spring Security** for Role-based Authentication and JWT Token
- **Spring Data MongoDB** for database access
- **Swagger/OpenAPI** for API documentation
- **Lombok** for reducing boilerplate code

## API Endpoints

### 1. **Student Controller**

#### POST /api/student
Create a new student.

**Request Body**:
```json
{
    "username": "john_doe",
    "name": "John",
    "password": "Hello",
    "courses": [],
    "type":"student"
}
```

**Response**:
```json
{
    "username": "john_doe",
    "name": "John",
    "role": "STUDENT",
    "courses": []
}
```
#### GET /api/student/{username}
Fetching a student.

**Request URL**:
```bash
GET /api/student/john_doe
```

**Response**:
```json
{
    "username": "john_doe",
    "name": "John",
    "role": "STUDENT",
    "courses": []
}
```
#### PUT /api/student/register/{username}/{id}
Patching a course with course id for a particular student with username.

**Request URL**:
```bash
GET PUT /api/student/register/john_doe/2
```
**Response**:
```json
{
    "username": "john_doe",
    "name": "John",
    "role": "STUDENT",
   "courses": [
        {
            "courseId": 2,
            "name": "Mathematics",
            "description":"Maths"
        }
    ]
}
```

### 2. **Course Controller**

#### POST /api/course
Create a new course.

**Request Body**:
```json
{
    "courseId":"1"
    "name": "Physics",
    "description": "Physics course"
}
```

**Response**:
```json
{
    "name": "Physics",
    "description": "Physics course"
}
```
#### GET /api/course/{id}
Fetching a course details.

**Request URL**:
```bash
GET /api/course/1
```

**Response**:
```json
{
    "name": "Physics",
    "description": "Physics course"
}
```
#### PUT /api/course/{id}
Updating a course details.

**Request URL**:
```bash
PUT /api/course/1
```

**Request Body**:
```json
{
    "name": "Astro Physics",
    "description": "Astro Physics course"
}
```

**Response**:
```json
{
    "name": "Astro Physics",
    "description": "Astro Physics course"
}
```

#### DEL /api/course/{id}
Delete a course.

**Request URL**:
```bash
DEL /api/course/1
```

**Response Body**:
```json
{
    "name": "Astro Physics",
    "description": "Astro Physics course"
}
```

### 3. **Admin Controller**

#### POST /api/admin
Create a new admin.

**Request Body**:
```json
{
    "username":"abhi08"
    "password": "Hello",
}
```

**Response**:
```json
{
    "username":"abhi08"
    "password": "sdsjdkhfjkdskfhsdjfhhsf98747t734987eryghrj",
    "role":"ADMIN"
}
```

### 4. **Teacher Controller**

#### POST /api/teacher/course/{id}
Create a new teacher wheread {id} is courseId.

**Request URL**:
```bash
POST /api/teacher/course/1
```

**Request Body**:
```json
{
    "username": "abhi009",
    "name": "Abhi M",
    "password": "password",
    "type":"teacher"
}
```

**Response**:
```json
{
    "username": "abhi009",
    "name": "Abhi M",
    "role":"TEACHER",
    "course": {
         "courseID":1
         "name": "Astro Physics",
         "description": "Astro Physics course"
        }
}
```

### 5. **Authentication Controller**

#### POST /api/login
Login to the system.

**Request Body**:
```json
{
    "username":"abhi08"
    "password": "Hello",
}
```

**Response**:
```json
{
    "jwt":"eyJhbGciOiJIUzUxMiJ9.
          eyJyb2xlIjpbeyJhdXRob3JpdHkiOiJURUFDSEVSIn1dLCJpZCI6IjY3YzAyMDQ3MjdkYzRlNjg2OTM5ODM5NyIsInVzZXIiOiJhYmhpMDA3Iiwic3ViIjoiYWJoaTAwNyIsImlhdCI6MTc0MTI1NzcxMCwiZXhwIjoxNzQxMzQ0MTEwfQ.
          zLcB08uERRB76heZ2PVc8C3at8rPa5JHVAwSmHIjHXynjrEvEmx98Pv_PQaVptjYRNUbj4_3fPnVZE0H0CgDVw"
}
```

## Prerequisites

Before you begin, ensure you have met the following requirements:

- **Java 21** or later
- **Maven** or **Gradle** for building the project
- An IDE such as **IntelliJ IDEA**, **Eclipse**, or **VS Code**

## Getting Started

Follow these instructions to set up the project locally.

### Clone the repository:

```bash
git clone https://github.com/AbhiHyland07/student-management.git
cd student-management
