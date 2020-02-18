FROM openjdk:8-jdk-alpine

WORKDIR /app

COPY jcpenney_com-ecommerce_sample.csv jcpenney_com-ecommerce_sample.csv

COPY target/Project2-0.0.1-SNAPSHOT.jar Project2-0.0.1-SNAPSHOT.jar

CMD java -DIGNITE_QUIET=false -jar Project2-0.0.1-SNAPSHOT.jar