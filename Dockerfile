FROM debian
RUN apt-get update -y
RUN apt-get install default-jdk -y
RUN apt-get install default-jre -y
COPY /target /home

EXPOSE 80
