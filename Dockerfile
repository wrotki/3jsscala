FROM ubuntu:16.04
RUN apt-get -y update && apt-get -y upgrade
RUN apt-get -y install scala scala-doc vim
RUN echo "deb http://dl.bintray.com/sbt/debian /" > /etc/apt/sources.list.d/sbt.list \
 && apt-key adv --keyserver hkp://keyserver.ubuntu.com:80 --recv 2EE0EA64E40A89B84B2DF73499E82A75642AC823
RUN apt-get -y update \
 && apt-get -y install sbt
WORKDIR /root
COPY . vis/
WORKDIR /root/vis
RUN sbt fastOptJS
#EXPOSE 12345
