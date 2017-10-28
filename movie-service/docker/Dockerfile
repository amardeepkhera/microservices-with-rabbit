FROM java:8
VOLUME /tmp
ADD movie-service.jar movie-service.jar
RUN bash -c 'touch /movie-service.jar'
ENTRYPOINT ["java","-jar","/movie-service.jar"]