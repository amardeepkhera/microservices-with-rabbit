FROM java:8
VOLUME /tmp
ADD rating-service.jar rating-service.jar
RUN bash -c 'touch /rating-service.jar'
ENTRYPOINT ["java","-jar","/rating-service.jar"]