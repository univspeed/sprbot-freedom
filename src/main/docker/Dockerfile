FROM openjdk:17-alpine
EXPOSE 20101
VOLUME /tmp
ADD *.jar app.jar
ENV JAVA_OPTS=" -Xms512M -Xmx512M "
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS  -Djava.security.egd=file:/dev/./urandom -jar app.jar" ]