FROM gradle:jdk15 as gradleimage
COPY . /home/gradle/source
WORKDIR /home/gradle/source
RUN gradle build -x test

FROM openjdk:15-jdk
COPY --from=gradleimage /home/gradle/source/build/libs/gs-spring-boot-0.1.0.jar app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]