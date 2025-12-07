#start with a base image containing java runtime
FROM eclipse-temurin:17-jdk-jammy

#info around who maintains image
MAINTAINER sharjil

# add application jar to image
COPY target/loans-0.0.1-SNAPSHOT.jar loans-0.0.1-SNAPSHOT.jar

#execute the command
ENTRYPOINT ["java","-jar","loans-0.0.1-SNAPSHOT.jar"]
