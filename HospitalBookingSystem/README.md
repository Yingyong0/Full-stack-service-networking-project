# Hospital Appointment and Registration System

Full-stack service project implemented using Java + HTTP/1.1

## Project Structure

```
HospitalBookingSystem/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/hospital/
│   │   │       ├── server/          # HTTP Server
│   │   │       ├── model/           # Data Models
│   │   │       ├── handler/         # HTTP Handlers
│   │   │       └── util/            # Utility Classes
│   │   └── resources/
│   │       └── web/                 # Frontend Static Files
│   └── test/
└── pom.xml
```

## Features

- Patient Management (Registration, Query)
- Doctor Management (Information Query)
- Appointment Management (Create, Query, Cancel Appointments)
- Registration Management (Registration, Query Registration Records)
- RESTful API Interface
- Frontend Web Interface

## Technology Stack

- Java 11+
- HTTP/1.1 (Java Native HttpServer)
- JSON (Gson)
- HTML/CSS/JavaScript (Frontend)

## How to Run

### Compile Project
```bash
mvn clean compile
```

### Run Server
```bash
mvn exec:java
```

The server will start at `http://localhost:8080`

### Access Frontend
Open `http://localhost:8080/index.html` in your browser

## API Endpoints

### Patient Related
- `GET /api/patients/{id}` - Query patient information
- `POST /api/patients` - Register new patient

### Doctor Related
- `GET /api/doctors` - Get all doctors list
- `GET /api/doctors/{id}` - Query doctor information

### Appointment Related
- `GET /api/appointments` - Get all appointments
- `GET /api/appointments/{id}` - Query appointment information
- `POST /api/appointments` - Create appointment
- `DELETE /api/appointments/{id}` - Cancel appointment

### Registration Related
- `GET /api/registrations` - Get all registration records
- `GET /api/registrations/{id}` - Query registration information
- `POST /api/registrations` - Create registration

## Development Notes

This project uses Java's native `com.sun.net.httpserver.HttpServer` to implement HTTP/1.1 server, without relying on any web framework, completely based on HTTP standard protocol.


