FROM openjdk:21

WORKDIR /app

COPY . /app

RUN ./mvnw -B clean package -DskipTests

EXPOSE 8082

CMD ["java", "-jar", "target/autenticacaoApi-0.0.1-SNAPSHOT.jar"]