FROM debian
RUN sudo apt-get update -y
RUN sudo apt-get install default-jdk -y
RUN sudo apt-get install default-jre -y
COPY /target /var/www
RUN sudo cd /var/www/target && java -jar *.jar
EXPOSE 80
