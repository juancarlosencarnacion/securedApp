# SecuredApp — Spring Boot JWT + OAuth2

REST API with JWT authentication and OAuth2 login (Google, GitHub), built with Spring Boot.

---

## 🧠 Key Highlights

- Implemented stateless authentication using JWT and Spring Security
- Integrated OAuth2 login with Google and GitHub providers
- Designed secure authentication flow using HttpOnly cookies
- Structured project following clean architecture principles

---

## 🚀 Tech Stack

- Java 21
- Spring Boot
- Spring Security
- JWT (jjwt)
- OAuth2 (Google, GitHub)
- PostgreSQL
- Hibernate / JPA
- MapStruct
- Lombok

---

## ✨ Features

- Register and login with JWT
- OAuth2 login with Google and GitHub
- HttpOnly cookie for OAuth2 token delivery
- Role-based access control (RBAC)
- Public and protected endpoints
- Custom `AuthenticationEntryPoint` (returns 401 JSON instead of redirecting)

---

## ⚙️ Getting Started

### Prerequisites

- Java 17+
- PostgreSQL running locally
- Google OAuth2 credentials: https://console.cloud.google.com
- GitHub OAuth2 credentials: https://github.com/settings/developers

---

### 🔧 Configuration

Create an `application.properties` file or set environment variables:

```properties
# Database
spring.datasource.url=jdbc:postgresql://localhost:5432/securedapp
spring.datasource.username=your_user
spring.datasource.password=your_password

# JWT
jwt.secret=your_base64_secret

# OAuth2 - Google
spring.security.oauth2.client.registration.google.client-id=${GOOGLE_CLIENT_ID}
spring.security.oauth2.client.registration.google.client-secret=${GOOGLE_CLIENT_SECRET}
spring.security.oauth2.client.registration.google.scope=email,profile

# OAuth2 - GitHub
spring.security.oauth2.client.registration.github.client-id=${GITHUB_CLIENT_ID}
spring.security.oauth2.client.registration.github.client-secret=${GITHUB_CLIENT_SECRET}
spring.security.oauth2.client.registration.github.scope=read:user,user:email

# Frontend
app.frontend.url=http://localhost:3000
````

---

### ▶️ Run the Application

```bash
mvn clean spring-boot:run
```

---

## 📚 API Reference

### 🔐 Auth

| Method | Endpoint             | Description                      | Auth   |
| ------ | -------------------- | -------------------------------- | ------ |
| POST   | `/api/auth/register` | Register with email and password | Public |
| POST   | `/api/auth/login`    | Login with email and password    | Public |

---

### 🌐 OAuth2

| Method | Endpoint                       | Description           |
| ------ | ------------------------------ | --------------------- |
| GET    | `/oauth2/authorization/google` | Initiate Google login |
| GET    | `/oauth2/authorization/github` | Initiate GitHub login |

After successful OAuth2 login, the JWT is set as an **HttpOnly cookie** and the browser is redirected to:

```
{app.frontend.url}/oauth2/callback
```

---

## 🏗️ Project Structure

```
src/main/java/com/jencarnacion/securedApp/
├── auth/
│   ├── controller/
│   ├── dto/
│   └── service/
├── role/
│   ├── enums/
│   └── repository/
├── security/
│   ├── config/
│   ├── jwt/
│   │   ├── filter/
│   │   └── service/
│   └── oauth2/
│       ├── userinfo/
│       ├── CustomOAuth2UserService
│       ├── OAuth2SuccessHandler
│       └── OAuth2UserInfoFactory
└── user/
    ├── dto/
    ├── enums/
    ├── mapper/
    ├── repository/
    └── User.java
```

---

## 🔒 Security Notes

* Passwords are encoded with BCrypt
* OAuth2 users receive a random (unusable) hashed password
* JWT is delivered via HttpOnly cookie (mitigates XSS attacks)
* Stateless session management (no server-side sessions)
* Custom 401 JSON response for API clients

---

## 📌 Notes

* Make sure your OAuth2 redirect URIs match your backend configuration
* Use strong secrets for JWT in production

