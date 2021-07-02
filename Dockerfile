FROM hseeberger/scala-sbt:16.0.1_1.5.4_2.13.6
ENV DISPLAY=host.docker.internal:0.0

RUN apt-get update && apt-get install -y libgtk-3-0
WORKDIR /tiles
ADD . /tiles
