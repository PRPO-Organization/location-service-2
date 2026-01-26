# Location Service
 A microservice for managing and querying location data, deployed on Azure Kubernetes Service (AKS). 
 ## Overview The Location Service provides RESTful APIs for location-based operations including creating, retrieving, updating, and deleting location records. Built as part of a microservices architecture, this service is designed for scalability and reliability. 
 ## Features - **Location Management**: CRUD operations for location entities - **RESTful API**: Standard HTTP endpoints for easy integration - **Containerized Deployment**: Runs on Kubernetes for high availability - **CI/CD Pipeline**: Automated build and deployment via GitHub Actions - **Health Checks**: Built-in liveness and readiness probes 
 ## Technology Stack - **Language**: Jakarta EE
git clone git@github.com:PRPO-Organization/location-service-2.git
cd location-service-2
### Build the Application
bash
mvn clean package
### Run Locally
bash 
docker compose down -v
docker compose up --build

### Run Tests
bash
mvn test
GET /api/locations
#### Get Location by ID
GET /api/locations/{id}
#### Create Location
POST /api/locations
Content-Type: application/json

{
  "name": "Ljubljana",
  "latitude": 46.0569,
  "longitude": 14.5058,
  "country": "Slovenia"
}
#### Update Location
http
PUT /api/locations/{id}
#### Delete Location
http
DELETE /api/locations/{id}
#### Health Check
http
GET /api/location/health


