FROM amazoncorretto:16.0.2

RUN yum update -y

COPY ts3_query_bot.jar .

CMD java -jar ts3_query_bot.jar
