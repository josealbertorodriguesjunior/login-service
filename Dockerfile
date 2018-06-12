FROM openjdk:8
ADD target/login-1.0.jar login-1.0.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","login-1.0.jar"]
