FROM docker.io/aomountainu/openjdk19:latest
ADD target/docker-spring-boot.jar docker-spring-boot.jar
ENTRYPOINT [ "java","-jar","docker-spring-boot.jar" ]