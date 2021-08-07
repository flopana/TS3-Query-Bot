FROM amazoncorretto:16.0.2

WORKDIR /usr/local/query_bot

RUN mkdir configs
RUN yum update -y

COPY ts3_query_bot.jar .

CMD java -jar ts3_query_bot.jar
