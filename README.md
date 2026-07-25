# Exam Portal — Spring Boot Backend

A full-stack student examination portal:
- **Spring Boot** REST API (Java 17, Spring Security + JWT, Spring Data JPA, **PostgreSQL**)
- **Static frontend** (plain HTML/CSS/JS, served by Spring Boot itself — no separate frontend server needed)
- Login page shared by admin and students, with **ID + password + captcha**
- **Student dashboard**: view only their own exam records, open hall ticket / result / timetable PDFs
- **Admin panel**: view every student's data, add/edit/delete exam records (every field), upload/replace/remove PDFs, add/remove login users
- **No data exists until the admin adds it** — the database starts empty (aside from the one admin login) and every student account, exam record, and PDF is created by the admin

## Requirements
- Java 17+
- Maven 3.8+ (or use your IDE's built-in Maven)
- **PostgreSQL 13+** running and reachable
- Internet access on first build, to download dependencies from Maven Central

> **Note:** this project was written and organized in a sandboxed environment without access to Maven Central
> or a live PostgreSQL server, so the code has **not** been compiled/run here. It follows standard Spring Boot 3.3
> conventions and should build cleanly with `mvn spring-boot:run` on a normal machine — but if you hit a
> dependency-version hiccup, check `pom.xml` first.

## Set up PostgreSQL

Create an empty database for the app, e.g.:

```sql
CREATE DATABASE examportal;
```

Then either edit `src/main/resources/application.yml` directly, or set environment variables before starting
the app (the yml already reads these, with the values below as defaults):

```bash
export DB_URL=jdbc:postgresql://localhost:5432/examportal
export DB_USERNAME=postgres
export DB_PASSWORD=postgres
```

Hibernate is set to `ddl-auto: update`, so the required tables are created automatically the first time the
app starts against the empty database — no manual schema/migration step needed.

## Run it

```bash
cd exam-portal
mvn spring-boot:run
```

Then open **http://localhost:8080** in your browser (it redirects to the login page).

On first run, the app auto-creates:
- an `uploads/` folder for PDF storage
- **one** admin login (see below) — nothing else. No student accounts, no exam records, no PDFs.
  The admin adds every student login and every exam record from the admin panel; students only ever see
  what the admin has added for them.

## Admin login (auto-created on first run)

| Role  | User ID | Password  |
|-------|---------|-----------|
| Admin | `admin` | `admin123`|

Change these via `ADMIN_ID` / `ADMIN_PASSWORD` env vars (or `app.seed.*` in `application.yml`) before the
first run. Student (and additional admin) accounts are created afterwards from the **User Accounts** panel —
none are seeded automatically.

## Project layout

```
exam-portal/
├── pom.xml
├── src/main/java/com/examportal/
│   ├── ExamPortalApplication.java
│   ├── config/          → JWT, Spring Security, seed data
│   ├── controller/       → REST endpoints
│   ├── entity/           → JPA entities (AppUser, ExamRecord, PdfDocument)
│   ├── repository/       → Spring Data repositories
│   ├── dto/               → request/response payloads
│   └── service/           → CaptchaService
├── src/main/resources/
│   ├── application.yml
│   └── static/            → login.html, student.html, admin.html, css/
└── uploads/                → PDF files land here at runtime
```

## API summary

| Method | Endpoint                          | Access        | Purpose                             |
|--------|------------------------------------|---------------|--------------------------------------|
| GET    | `/api/auth/captcha`                | public        | Get a captcha id + SVG               |
| POST   | `/api/auth/login`                  | public        | Login (returns JWT)                  |
| GET    | `/api/auth/me`                     | authenticated | Current user info                    |
| GET    | `/api/records`                     | student       | Own exam records                     |
| GET    | `/api/records/admin/all`           | admin         | All exam records                     |
| POST   | `/api/records/admin`               | admin         | Create a record                      |
| PUT    | `/api/records/admin/{id}`          | admin         | Update a record                      |
| DELETE | `/api/records/admin/{id}`          | admin         | Delete a record                      |
| GET    | `/api/pdfs`                        | student       | Own PDFs                             |
| GET    | `/api/pdfs/admin/all`              | admin         | All PDFs                             |
| POST   | `/api/pdfs/upload`                 | admin         | Upload a PDF (multipart)             |
| PUT    | `/api/pdfs/{id}/replace`           | admin         | Replace a PDF's file                 |
| GET    | `/api/pdfs/{id}/view`              | owner/admin   | Stream/view a PDF                    |
| DELETE | `/api/pdfs/{id}/delete`            | admin         | Delete a PDF                         |
| GET    | `/api/users`                       | admin         | List login accounts                  |
| POST   | `/api/users`                       | admin         | Create a login account               |
| DELETE | `/api/users/{userId}`              | admin         | Delete a login account               |

## Notes on the frontend behavior

- A student only ever sees exam records and PDFs the admin has added for their user ID. The Hall
  Ticket / Result / Timetable buttons stay disabled ("Not uploaded yet") until the admin uploads that
  document — nothing is auto-generated or shown until the admin adds it.
- The admin's View/Edit/Add Record panel opens as a centered popup, not a right-side slide-in drawer.
- Auth uses a JWT stored in `sessionStorage` and sent as `Authorization: Bearer <token>` on every API call.
- Passwords are stored as BCrypt hashes — never in plain text.
- The captcha is generated server-side (a small SVG with random text) and is single-use.
