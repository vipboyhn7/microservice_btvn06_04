# Medical microservices

## Services

- `discovery-service`: Eureka service registry on port `8761`.
- `patient-service`: Patient API on port `8081`, registered with Eureka.
- `doctor-service`: Doctor API on port `8082`, registered with Eureka.
- `appointment-service`: Appointment API on port `8083`, registered with Eureka.

## Run the services

Start the discovery service first:

```bash
cd discovery-service
mvn spring-boot:run
```

Then start the patient service in another terminal:

```bash
cd patient-service
mvn spring-boot:run
```

Start the doctor service in a third terminal:

```bash
cd doctor-service
mvn spring-boot:run
```

The doctor list is available at `http://localhost:8082/api/v1/doctors`.

The appointment endpoint is available at `http://localhost:8083/api/v1/appointments`.
