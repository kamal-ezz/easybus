# Easybus

This is a full stack application for buying bus tickets built using Spring Boot and Angular 

### Prerequisites
To launch this project these tools need to installed on your machine

- Java JDK 11 or higher
- Node
- Intellij IDE, Eclipse or any other Java IDE for the backend and IDE of your for the frontend
- MySQL

### To start
- Type in terminal: cd src/js && npm i in order to install frontend packages.
- Create a database with the name of bustrans.
- Change application.properties file to match the credentials that you use for MySQL.

### Use MySQL as a container
docker run --name mysql-db -e MYSQL_ROOT_PASSWORD=root -e MYSQL_DATABASE=easybus -e MYSQL_USER=kamal -e MYSQL_PASSWORD=kamal -p 3306:3306 -v mysql:/var/lib/mysql -d mysql:latest

docker run -d -p 9090:9090 -v <path-to-file>/prometheus.yml:/etc/prometheus/prometheus.yml prom/prometheus
docker run -d -p 3000:3000 grafana/grafana