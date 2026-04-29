FROM eclipse-temurin:25-jre

WORKDIR /app

COPY dist/GPSWebServicesClient.jar app.jar
COPY dist/lib/ lib/

CMD ["java", "-cp", "app.jar:lib/*", "com.qsolutions.gpsclient.MainClient"]