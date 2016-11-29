FROM ubuntu:16.04
RUN apt-get -y update && apt-get -y upgrade
RUN echo "deb http://dl.bintray.com/sbt/debian /" > /etc/apt/sources.list.d/sbt.list \
 && apt-key adv --keyserver hkp://keyserver.ubuntu.com:80 --recv 2EE0EA64E40A89B84B2DF73499E82A75642AC823
RUN apt-get -y update \
 && apt-get -y install scala scala-doc vim sbt ed

WORKDIR /root
COPY vis vis/

# Pre-load all the dependencies
WORKDIR /root/vis
RUN sbt fastOptJS "project datasource" compile

COPY scripts ../scripts/
ENV PATH=/root/scripts:$PATH
EXPOSE 12345
ENTRYPOINT ../scripts/start-servers
