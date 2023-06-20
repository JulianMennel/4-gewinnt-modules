FROM sbtscala/scala-sbt:eclipse-temurin-jammy-11.0.17_8_1.8.2_2.13.10

WORKDIR /viergewinnt
ADD ./view /viergewinnt/view
ADD ./io /viergewinnt/io
ADD ./controller /viergewinnt/controller
ADD ./core /viergewinnt/core
ADD ./src /viergewinnt/src
ADD ./build.sbt /viergewinnt/build.sbt
ADD ./project/plugins.sbt /viergewinnt/project/plugins.sbt