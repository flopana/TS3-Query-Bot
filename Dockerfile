FROM amazoncorretto:17.0.2

EXPOSE 4567
WORKDIR /usr/local/query_bot

RUN mkdir configs
RUN yum update -y

COPY ts3_query_bot.jar .

CMD java -jar ts3_query_bot.jar
