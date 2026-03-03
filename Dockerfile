FROM bellsoft/liberica-openjre-alpine:21

WORKDIR /app

COPY app.jar /app/app.jar

EXPOSE 8080

ENTRYPOINT ["java","-XX:+UseSerialGC","-Xms256m","-Xmx384m","-Xss512k","-jar","/app/app.jar"]
