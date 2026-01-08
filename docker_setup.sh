az acr login --name prporegistry
docker build -t prporegistry.azurecr.io/location-service:latest .
docker push prporegistry.azurecr.io/location-service:latest
