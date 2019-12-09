FROM openjdk:8
COPY target/token.jar /
ENV PROFILE="dev"
CMD java -jar -Dspring.profiles.active=$PROFILE token.jar
