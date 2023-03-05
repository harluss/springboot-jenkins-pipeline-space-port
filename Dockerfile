FROM eclipse-temurin:17-jre-jammy as builder
WORKDIR temp
COPY space-hangar/target/*.jar app.jar
RUN java -Djarmode=layertools -jar app.jar extract

FROM eclipse-temurin:17-jre-jammy
WORKDIR app
COPY --from=builder temp/dependencies/ ./
COPY --from=builder temp/spring-boot-loader/ ./
COPY --from=builder temp/snapshot-dependencies/ ./
COPY --from=builder temp/application/ ./
EXPOSE 8082
ENTRYPOINT ["java", "org.springframework.boot.loader.JarLauncher"]
