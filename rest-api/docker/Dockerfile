FROM java:8
VOLUME /tmp
ADD rest-api.jar rest-api.jar
RUN bash -c 'touch /rest-api.jar'
ENTRYPOINT ["java","-jar","/rest-api.jar"]