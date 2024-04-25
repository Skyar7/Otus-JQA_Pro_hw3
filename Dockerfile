FROM maven:3.9.6-eclipse-temurin-8

RUN mkdir -p /home/ubuntu/api_tests

WORKDIR /home/ubuntu/api_tests

COPY . /home/ubuntu/api_tests

ENTRYPOINT ["/bin/bash", "entrypoint.sh"]