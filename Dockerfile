FROM debian
RUN apt-get update
RUN apt-get install default-jdk
RUN apt-get install default-jre
RUN apt-get install nano
RUN apt-get install git
COPY /target /var/www
RUN cd /var/www/target && java -jar *.jar
EXPOSE 80
