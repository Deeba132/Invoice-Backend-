---
# 📄 README.md (Backend Repo)

# Invoice Management Backend

Spring Boot REST API for managing items, customer details, and generating invoices.
Deployed on **Railway**, connected to PostgreSQL database.
---

## 🚀 Features

- User authentication (register/login) using JWT token
- Item CRUD endpoints
- Login and Register CRUD endpoints
- Invoice generation endpoint
- Customised cors configuration for integration of react frontend
- Postgresql used to implement CRUD.

---

## 🛠 Tech Stack

- Java, Spring Boot, Hibernate
- Database: PostgreSQL (Railway)
- Deployment: Railway

---

## ⚙️ Setup Instructions

```bash
git clone https://github.com/deeba132/Invoice-Backend-
cd backend
mvn clean package
java -jar target/app.jar

Using Docker:
in bash:
docker build -t botpersona:latest .

then:
docker run -p 8080:8080 \
  -e SPRING_DATASOURCE_URL=jdbc:postgresql://<railway-host>:<port>/<dbname>?sslmode=require \
  -e SPRING_DATASOURCE_USERNAME=<username> \
  -e SPRING_DATASOURCE_PASSWORD=<password> \
  botpersona:latest



📡 API Endpoints:

POST /auth/register → Register new user

POST /auth/login → Login user

GET /items → Fetch all items

POST /items → Add new item

PUT /items/{id} → Update item

DELETE /items/{id} → Delete item

GET /invoice/{id} → Generate invoice

```

Live Demo On Vercel: (https://invoice-frontend-mu-ten.vercel.app/)
