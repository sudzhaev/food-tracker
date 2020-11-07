FROM openjdk:11

ARG JAR_FILE

COPY target/$JAR_FILE /usr/bot.jar
CMD java $JAVA_OPTIONS -jar /usr/bot.jar
