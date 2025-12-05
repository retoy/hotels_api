# Hotels Management API

REST API for hotels management.

## API Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/property-view/hotels` | Get all hotels (brief info) |
| GET | `/property-view/hotels/{id}` | Get detailed hotel info |
| GET | `/property-view/search` | Search hotels by parameters |
| POST | `/property-view/hotels` | Create new hotel |
| POST | `/property-view/hotels/{id}/amenities` | Add amenities to hotel |
| GET | `/property-view/histogram/{param}` | Get hotels count grouped by parameter |

## Database Support
- **H2** (default, no setup needed)
- **PostgreSQL** (via `--spring.profiles.active=postgresql`)
- **MySQL** (via `--spring.profiles.active=mysql`)

## Quick Start

# Default (H2)
./mvnw spring-boot:run

# With PostgreSQL
./mvnw spring-boot:run -D"spring-boot.run.profiles=postgresql"

# With MySQL
./mvnw spring-boot:run -D"spring-boot.run.profiles=mysql"

# Swagger UI: http://localhost:8092/swagger-ui.html
