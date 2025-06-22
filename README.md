# Simple Web App

A secure Java Spring Boot web application with user authentication, password reset, and real email support.

## Features

- User registration, login, and logout
- Passwords securely hashed with BCrypt
- Custom login, registration, home, and password reset pages (Thymeleaf)
- Forgot password flow with secure token and email link
- Real email sending via Gmail SMTP (or pluggable provider)
- Modular architecture: interfaces for all services and repositories, implementations in `impl/`
- AOP-based `@LogEntryExit` annotation for controller entry/exit logging
- Security: public access to login, register, forgot/reset password; all other routes require authentication
- Responsive UI with Bootstrap and custom CSS
- Secure configuration: secrets in `application-secrets.properties` (not in git)

## Getting Started

### Prerequisites

- Java 17+
- Maven
- Gmail account with [App Password](https://support.google.com/accounts/answer/185833?hl=en) enabled

### Setup

1. **Clone the repo**
2. **Configure email:**
   - Create `src/main/resources/application-secrets.properties` (see below)
   - Add your Gmail app password:
     ```
     GMAIL_APP_PASSWORD=your_gmail_app_password
     ```
   - This file is already in `.gitignore` and will not be committed.
3. **Edit `src/main/resources/application.properties`** if needed (your Gmail address, etc.)
4. **Run the app:**
   ```
   ./mvnw spring-boot:run
   ```
5. **Access the app:**  
   http://localhost:8080

### Password Reset Email

- Uses Gmail SMTP to send password reset links.
- All sensitive config is externalized.

### Project Structure

- `controller/` - Web controllers
- `service/` - Service interfaces
- `service/impl/` - Service implementations
- `repository/` - Spring Data JPA repositories
- `model/` - JPA entities
- `aop/` - Logging annotation/aspect
- `templates/` - Thymeleaf HTML templates
- `static/css/` - CSS

### Security

- Only `/login`, `/register`, `/forgot-password`, `/reset-password`, `/css/**` are public.
- All other routes require authentication.

### Extending

- Add new services by creating an interface and an implementation in `impl/`.
- Swap email providers by implementing `EMailSenderService`.

---

## License

MIT 