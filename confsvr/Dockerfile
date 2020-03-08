FROM adoptopenjdk/openjdk11-openj9:alpine-jre
ARG JAR_FILE
ADD target/${JAR_FILE} app.jar
RUN sh -c 'touch /app.jar'
EXPOSE 8888
ENTRYPOINT ["java","-Xmx512M","-XX:+CMSClassUnloadingEnabled","-Djava.security.egd=file:/dev/./urandom", "-Xshareclasses:cacheDir=/tmp","-DlogDirectory=/log","-jar","/app.jar"]
