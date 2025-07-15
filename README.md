# Smart Online Examination - Backend

This is the **backend API** for the **Smart Online Examination System**, developed as the **Final Project for the Oracle Database Subject** at ACLEDA University of Business.

It provides **RESTful APIs**, **authentication**, **real-time messaging**, and manages data in an **Oracle Database**.

---

## 📚 Project Information

- **Project Name:** Smart Online Examination - Backend  
- **Course:** Oracle Database Final Project  
- **University:** ACLEDA University of Business  
- **Supervisor:** Mr. KAY HENG

---

## 🔗 Related Frontend Project

This backend works together with the frontend built using Vue.js.

👉 **Frontend GitHub Repo:**  
https://github.com/VathanaProgrammer/Oracle_Project_Frontend

---

## 👥 Team Members

| Name | Role | Responsibility |
|---|---|---|
| **Sieng Vathana** | **Team Leader / System Analyst / Fullstack Developer** | Managed the team, designed backend API structure, built core features, JWT authentication, and WebSocket integration. |
| Hak Vuttey | Fullstack Developer (Learning Stage) | Implemented teacher-related APIs (exam management, exam result APIs). |
| Yann Sreyvin | Fullstack Developer (Learning Stage) | Assisted with student-related APIs and testing API endpoints. |
| Peang Theansing | Fullstack Developer (Learning Stage) | Helped with student exam APIs, notifications, and file management logic. |

---

## 🎯 Main Objectives

- Provide RESTful API endpoints for the exam system
- Use **Spring Boot** for backend development  
- Use **Oracle Database** for data management  
- Handle **JWT authentication (HttpOnly cookies)**  
- Support **real-time notifications using WebSocket (STOMP)**  
- Manage roles: **Admin**, **Teacher**, **Student**

---

## 🚀 Tech Stack

| Technology | Purpose |
|------------|---------|
| Spring Boot | Backend Framework |
| Oracle XE 21c | Database |
| JWT | Authentication |
| WebSocket (STOMP) | Real-time communication |
| Lombok | Boilerplate code reduction |
| Maven | Dependency Management |

---

## 🗂️ Project Structure
```
src/main/java/
├── controllers/ # REST API Controllers
├── services/ # Business Logic
├── models/ # Entities / DTOs
├── repositories/ # Data Access Layer
├── config/ # Security & WebSocket config
└── OneTransitionDemoApplication.java
```
---

## ⚙️ Installation & Setup

1️⃣ **Clone the project**

git clone https://github.com/VathanaProgrammer/Oracle_Project_Backend
cd Oracle_Project_Backend

2️⃣ Set up Oracle Database

Make sure you have Oracle XE 21c running.

Update your src/main/resources/application.properties like this:
```
spring.application.name=OneTransitionDemo
server.port=8080

spring.datasource.url=jdbc:oracle:thin:@YourHost:YourPort/YourSession
spring.datasource.username=YourUserName
spring.datasource.password=YourPassword
spring.datasource.driver-class-name=oracle.jdbc.OracleDriver

spring.jpa.hibernate.ddl-auto=update
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.OracleDialect
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
logging.level.org.hibernate.SQL=DEBUG
```

3️⃣ Run the project

Use an IDE (IntelliJ / Eclipse) or terminal:

./mvnw spring-boot:run

---

## 🔐 API Features
* User Authentication (JWT with HttpOnly Cookies)

* Role-based Access Control (Admin, Teacher, Student)

* Exam & Assignment Management

* Real-Time Notifications (WebSocket)

* File Upload (Profile & Exam Files)

* User Activity Log (Login, Logout)

---

| Method | Endpoint               | Description                  |
| ------ | ---------------------- | ---------------------------- |
| POST   | `/api/user/login`      | Login user                   |
| GET    | `/api/user/me`         | Get current user             |
| POST   | `/api/exams/create`    | Teacher creates an exam      |
| GET    | `/api/exams/student`   | Student views assigned exams |
| POST   | `/api/uploads/profile` | Upload profile picture       |

---

## 📄 **LICENSE (MIT)**

```text
MIT License

Copyright (c) 2025 Sieng Vathana

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
