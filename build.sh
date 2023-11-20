#!/bin/bash

mvn clean package -DskipTests
sudo docker compose down
sudo docker rmi docker-spring-boot-postgres:latest
sudo docker compose up