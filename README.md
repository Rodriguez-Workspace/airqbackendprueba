# AirQ Backend

## Description
This is the main backend service for the AirQ air quality monitoring system. It is built using Java and Spring Boot. It serves as the central hub for managing clients, users, hardware sensors, and processing telemetry data. 

## Key Features
- **RESTful API**: Provides endpoints for frontend applications (client dashboard, admin panel) and the technician mobile app.
- **MQTT Integration**: Subscribes to the HiveMQ broker to receive real-time telemetry data (CO2, PM2.5, Temperature, Humidity) from Edge Nodes.
- **Machine Learning Integration**: Communicates with the external ML microservice to detect anomalies in air quality measurements.
- **Alert System & Notifications**: Evaluates thresholds and triggers email notifications using the Brevo API.
- **Role-Based Access Control**: Handles authentication and authorization for Clients, Admins, and Technicians using Spring Security.
- **Database**: Uses PostgreSQL for persistent storage of users, campus locations, classrooms, and historical metrics.

## Tech Stack
- Java 17+
- Spring Boot (Web, Data JPA, Security)
- PostgreSQL
- Eclipse Paho (MQTT Client)
- Brevo API (Email Services)
- Maven

## How to Run Locally
1. Ensure PostgreSQL is running and a database named `airq` is created.
2. Verify that `application.yaml` points to your local database credentials.
3. Use the Maven wrapper to start the application:
   ```bash
   ./mvnw spring-boot:run
   ```

## Production Deployment
The project is configured to run on cloud platforms like Render. It uses `application-prod.yml` to inject strict environment variables (e.g., `DATABASE_URL`, `MQTT_BROKER_URL`, `BREVO_API_KEY`) ensuring no hardcoded secrets exist in the repository.
