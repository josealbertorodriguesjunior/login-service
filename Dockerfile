FROM debian
RUN apt-get update -y
RUN apt-get install default-jdk -y
RUN apt-get install default-jre -y
COPY /target /home
RUN java -jar /home/target/*.jar
EXPOSE 80
