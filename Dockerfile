FROM eclipse-temurin:25-jre

WORKDIR /app

COPY dist/GPSWebServicesClient.jar app.jar
COPY dist/lib/ lib/
COPY lib/ lib/
RUN mkdir -p /app/logs

CMD ["java", "-cp", "app.jar:lib/*", "-DLOG_DIR=/app/logs", "com.qsolutions.gpsclient.MainClient"]