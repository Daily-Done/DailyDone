# DailyDone

**DailyDone** is a community-driven micro-task platform where users can post tasks they need completed, and other active community members can instantly accept and complete those tasks to earn payments.  
It enables real-time task matching, secure user interactions, and a streamlined workflow for posting, accepting, completing, and paying for tasks — making it a fast and reliable peer-to-peer task execution system.

## ✨ Features

- **Task Creation:** Users can post tasks with details such as title, description, category, location, and payout amount.
- **Real-Time Task Acceptance:** Active community members can instantly view and accept available tasks.
- **Cash-on-Completion Model:** Once a task is completed and verified, the task creator pays the task doer directly (offline cash flow).
- **Secure Task Workflow:** Structured flow for posting → accepting → completing → verifying.
- **User Roles:** Supports separate roles for task creators and task doers.
- **Task Status Tracking:** Tasks move through defined states (Pending, Accepted, Completed, Verified).
- **Authentication & Authorization:** Secure login and protected endpoints using Spring Security.
- **Scalable Backend Architecture:** Follows a clean layered structure (Controller → Service → Repository → Entity).
- **Error Handling & Validation:** Strong validations and descriptive error responses across all endpoints.

## 🛠 Tech Stack

- **Java** (version 17 or your version)
- **Spring Boot** (REST APIs, application configuration)
- **Spring Security** (authentication & authorization)
- **Spring Data JPA** (database access layer)
- **PostgreSQL** (your database)
- **Hibernate** (ORM for entity mapping)
- **Maven** (build & dependency management)
- **Lombok** (boilerplate code reduction)
- **JWT / Session-Based Auth**

## 🧩 Project Architecture

The backend follows a clean, scalable, and production-style layered architecture:

### **1. Controller Layer**
Handles incoming HTTP requests, validates input, and forwards requests to the service layer.

### **2. Service Layer**
Contains core business logic for:
- Task creation
- Task acceptance
- Task completion & verification
- User role handling
- Workflow transitions

### **3. Repository Layer**
Uses Spring Data JPA to interact with the database.  
Handles CRUD operations for all entities such as:
- Task
- User
- AcceptedTask (if used)
- Verification records (if any)

### **4. Entity Layer**
Defines database models with proper relationships, constraints, and mappings.

### **5. Security Layer**
Implements:
- Authentication
- Authorization
- Route protection
- Role-based access
(using Spring Security + JWT or session-based auth depending on your setup)

### **6. Exception Handling Layer**
Provides global exception handling and clean error responses to API clients.

This architecture ensures high readability, easy maintenance, and long-term scalability.

## 🖧 Workflow Overview

1. A user creates a task with required details.
2. Other active users browse available tasks.
3. A user accepts a task (first-come-first-serve).
4. The doer completes the task.
5. The creator verifies the completion.
6. Cash payment is exchanged offline between both parties.

## 🏁 Getting Started (Setup Instructions)

## 1. Clone the repository
git clone https://github.com/your-username/your-repo.git
cd your-repo

## 2. Configure MySQL
spring.datasource.url=jdbc:mysql://localhost:3306/dailydone
spring.datasource.username=root
spring.datasource.password=1234

## 3. Install dependencies
mvn clean install

## 4. Run application
mvn spring-boot:run

### 📡 Core API Endpoints

### 🔐 Auth
- **POST** `/auth/register` — Register a new user  
- **POST** `/auth/login` — Authenticate user and start session/token  

---

### 📋 Tasks
- **POST** `/tasks` — Create a new task  
- **GET** `/tasks` — Get all available tasks  
- **PUT** `/tasks/{id}/accept` — Accept a task  
- **PUT** `/tasks/{id}/complete` — Mark a task as completed  
- **DELETE** `/tasks/{id}/Cancel` — Verify the completion of a task  

### 👤 Profile APIs
- **POST** `/users/create` — Create user profile  
- **GET** `/users/{id}/tasks/created` — Get tasks created by the user  
- **GET** `/users/{id}/tasks/completed` — Get tasks completed by the user  
- **GET** `/users/{id}/rating` — Get user’s average rating  

---

### ⭐ Rating APIs
- **POST** `/ratings/rate-user/{taskId}` — Rate the task creator  
- **POST** `/ratings/rate-helper/{taskId}` — Rate the task doer  

---

###  Money Record API
- **GET** `/money/{userId}` — Get the complete money record of a particular user

## 📁 Folder Structure

```
src/  
 └── main/  
      ├── java/com/dailydone/  
      │      ├── controller/        # Handles API requests  
      │      ├── service/           # Business logic  
      │      ├── repository/        # JPA repositories  
      │      ├── entity/            # Database models  
      │      ├── security/          # Auth & authorization  
      │      └── exception/         # Global exception handling  
      │
      └── resources/  
             ├── application.properties  
             └── static/  
```

  
