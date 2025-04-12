FROM maven:3.9.9-amazoncorretto-21-alpine

LABEL maintainer="Paiva <joaovitor.paiva145@hotmail.com>"

WORKDIR /app
COPY . .

RUN mvn clean package -DskipTests

EXPOSE 8080

CMD ["java", "-jar", "target/mplayer-0.0.1-SNAPSHOT.jar"]

