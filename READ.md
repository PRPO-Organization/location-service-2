# Location Service

A microservice for managing and querying location data, deployed on Azure Kubernetes Service (AKS).

## Overview

The Location Service provides RESTful APIs for location-based operations including creating, retrieving, updating, and deleting location records. Built as part of a microservices architecture, this service is designed for scalability and reliability.

## Features

- **Location Management**: CRUD operations for location entities
- **RESTful API**: Standard HTTP endpoints for easy integration
- **Containerized Deployment**: Runs on Kubernetes for high availability
- **CI/CD Pipeline**: Automated build and deployment via GitHub Actions
- **Health Checks**: Built-in liveness and readiness probes

## Technology Stack

- **Language**: Java / Spring Boot
- **Container Registry**: Azure Container Registry (ACR)
- **Orchestration**: Azure Kubernetes Service (AKS)
- **CI/CD**: GitHub Actions
- **Database**: PostgreSQL

## Prerequisites

- Java 17+
- Docker
- kubectl
- Azure CLI
- Access to Azure subscription

## Local Development

### Clone the Repository
```bash
git clone git@github.com:PRPO-Organization/location-service-2.git
cd location-service-2
```

### Build the Application
```bash
mvn clean package
```

### Run Locally
```bash
java -jar target/location-service.jar
```

### Run Tests
```bash
mvn test
```

## API Endpoints

### Base URL
- **Local**: `http://localhost:8080`
- **Production**: `http://<service-external-ip>`

### Endpoints

#### Get All Locations
```http
GET /api/locations
```

#### Get Location by ID
```http
GET /api/locations/{id}
```

#### Create Location
```http
POST /api/locations
Content-Type: application/json

{
  "name": "Ljubljana",
  "latitude": 46.0569,
  "longitude": 14.5058,
  "country": "Slovenia"
}
```

#### Update Location
```http
PUT /api/locations/{id}
```

#### Delete Location
```http
DELETE /api/locations/{id}
```

#### Health Check
```http
GET /health
```

## Deployment

### CI/CD Pipeline

The service uses GitHub Actions for automated CI/CD:

1. **Trigger**: Push to `main` branch
2. **Build**: Docker image is built
3. **Push**: Image pushed to Azure Container Registry
4. **Deploy**: Kubernetes deployment updated on AKS

### Manual Deployment
```bash
# Login to Azure
az login

# Get AKS credentials
az aks get-credentials --resource-group prpo_cluster123 --name prpo-aks

# Apply Kubernetes manifests
kubectl apply -f app-deployment/deployements/

# Check deployment status
kubectl rollout status deployment/location-app -n default
```

## Monitoring

### View Logs
```bash
kubectl logs -l app=location-app -n default --tail=100
```

### Check Pod Status
```bash
kubectl get pods -n default
```

### View Deployment History
```bash
kubectl rollout history deployment/location-app -n default
```

## Troubleshooting

### Pipeline Fails at Azure Login
- Verify `AZURE_CREDENTIALS` secret is configured in GitHub
- Check service principal has correct permissions

### Pod Crashes
```bash
kubectl logs <pod-name> -n default
kubectl describe pod <pod-name> -n default
```

## Project Structure
```
location-service-2/
├── .github/workflows/          # CI/CD pipeline
├── app-deployment/deployements/ # Kubernetes manifests
├── src/                        # Application code
├── Dockerfile
└── README.md
```

## Contact

PRPO-Organization

## Links

- [GitHub Repository](https://github.com/PRPO-Organization/location-service-2)
- [Azure Portal](https://portal.azure.com)
