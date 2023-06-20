FROM sbtscala/scala-sbt:eclipse-temurin-jammy-11.0.17_8_1.8.2_2.13.10

WORKDIR /controller
ADD ./src /controller/src
ADD ./build.sbt /controller/build.sbt

CMD sbt