# Build stage
FROM maven:3.8-openjdk-17 AS build
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline
COPY src ./src
RUN mvn clean package -DskipTests

# Download PostgreSQL driver in build stage (or in run stage)
RUN mkdir -p /opt/payara/lib && \
    curl -L -o /opt/payara/lib/postgresql.jar \
    https://repo1.maven.org/maven2/org/postgresql/postgresql/42.7.4/postgresql-42.7.4.jar

# Run stage
FROM payara/micro:latest

# Copy WAR from build stage
COPY --from=build /app/target/*.war /opt/payara/deployments/

# Copy JDBC driver from build stage
COPY --from=build /opt/payara/lib/postgresql.jar /opt/payara/lib/postgresql.jar

# Optional: postboot.asadmin
COPY postboot.asadmin /opt/payara/postboot.asadmin
ENV JAVA_OPTIONS="-Djavax.xml.accessExternalSchema=all"
# DB environment variables

#ENV  DB_HOST = postgis
#ENV DB_PORT = 5432
#ENV DB_NAME = locations
#ENV DB_USER = location-user
#ENV DB_PASSWORD = password123
# JVM options

# Start Payara Micro
ENTRYPOINT []
#CMD ["java", "-jar", "/opt/payara/payara-micro.jar", \
#     "--addlibs", "/opt/payara/lib/postgresql.jar", \
#     "--postbootcommandfile", "/opt/payara/postboot.asadmin", \
#     "--deploy", "/opt/payara/deployments/location-service-1.0-SNAPSHOT.war", \
#     "--contextRoot", "/"]
CMD ["java", "-jar", "/opt/payara/payara-micro.jar", \
     "--addlibs", "/opt/payara/lib/postgresql.jar", \
     "--deploy", "/opt/payara/deployments/location-service-1.0-SNAPSHOT.war", \
     "--contextRoot", "/"]
