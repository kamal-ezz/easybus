# EasyBus

A full-stack bus ticket booking application with integrated payment processing and authentication.

## Overview

EasyBus is a modern web application that allows users to search for bus trips, book tickets, and make secure payments through PayPal. The application features Google OAuth authentication, JWT-based security, and a responsive Angular frontend powered by Tailwind CSS.

## Tech Stack

### Backend
- **Java 21** with **Spring Boot 4.0.2**
- **Spring Security** with JWT authentication
- **PostgreSQL** database with Flyway migrations
- **PayPal SDK** for payment processing
- **Google OAuth 2.0** for social login
- **SpringDoc OpenAPI** for API documentation
- **Lombok** for boilerplate reduction

### Frontend
- **Angular 21** with TypeScript
- **Tailwind CSS 4** for styling
- **RxJS** for reactive programming
- **Vitest** for testing
- **Bun** package manager

### Infrastructure
- **Docker Compose** for containerized deployment
- **PostgreSQL 16** (Alpine)
- **pgAdmin** for database management (optional)

## Features

- Search and browse bus trips
- Secure booking system with seat management
- PayPal payment integration
- Google OAuth authentication
- JWT-based session management
- Mock mode for development
- RESTful API with OpenAPI documentation
- Responsive UI with modern design

## Prerequisites

- **Java JDK 21**
- **Node.js** (or **Bun** 1.3.5+)
- **Docker** and **Docker Compose** (for containerized setup)
- **PostgreSQL 16** (if running locally without Docker)

## Getting Started

### Using Docker Compose

1. Start the PostgreSQL database:
   ```bash
   docker-compose up postgres
   ```

2. Optionally, start pgAdmin for database management:
   ```bash
   docker-compose --profile tools up pgadmin
   ```
   Access pgAdmin at `http://localhost:5050` (admin@easybus.com / admin)

### Backend Setup

1. Navigate to the backend directory:
   ```bash
   cd backend
   ```

2. Create a `.env` file or set environment variables:
   ```bash
   DB_URL=jdbc:postgresql://localhost:5432/easybus
   DB_USERNAME=postgres
   DB_PASSWORD=postgres
   JWT_SECRET=your-secret-key
   GOOGLE_OAUTH_CLIENT_ID=your-google-client-id
   GOOGLE_OAUTH_CLIENT_SECRET=your-google-secret
   PAYPAL_CLIENT_ID=your-paypal-client-id
   PAYPAL_CLIENT_SECRET=your-paypal-secret
   MOCK_MODE=true  # Set to false for production
   ```

3. Run the application:
   ```bash
   ./gradlew bootRun
   ```

The backend will be available at `http://localhost:8080`

### Frontend Setup

1. Navigate to the frontend directory:
   ```bash
   cd frontend
   ```

2. Install dependencies:
   ```bash
   bun install
   # or
   npm install
   ```

3. Start the development server:
   ```bash
   bun start
   # or
   npm start
   ```

The frontend will be available at `http://localhost:4200`

## API Documentation

Once the backend is running, access the OpenAPI documentation at:
- Swagger UI: `http://localhost:8080/swagger-ui.html`

## Database

The application uses PostgreSQL with Flyway for database migrations. Migrations are located in `backend/src/main/resources/db/migration`.

### Mock Mode

The application supports a mock mode for development and testing. Set `MOCK_MODE=true` in your environment to use mock data without requiring external services.

## Testing

### Backend
```bash
cd backend
./gradlew test
```

### Frontend
```bash
cd frontend
bun test
# or
npm test
```

## Development

### Code Formatting

Backend uses Spotless with Google Java Format:
```bash
./gradlew spotlessApply
```

Frontend uses Prettier (configured in [package.json](frontend/package.json))

## Project Structure

```
easybus/
├── backend/              # Spring Boot application
│   ├── src/main/java/   # Java source files
│   ├── src/main/resources/ # Application config & migrations
│   └── build.gradle     # Gradle build configuration
├── frontend/            # Angular application
│   ├── src/            # TypeScript source files
│   └── package.json    # NPM dependencies
└── docker-compose.yml  # Docker services configuration
```

