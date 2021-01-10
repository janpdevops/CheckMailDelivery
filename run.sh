#docker run -it --rm -v "$PWD":/usr/src/mymaven  -w /usr/src/mymaven openjdk:15-jdk-slim java -jar target/checkMailDelivery-1.0-SNAPSHOT-jar-with-dependencies.jar etc/mail.properties
docker run -it --rm -v "$PWD":/tmp  -w /tmp janpdev/checkmaildelivery-docker  mail.properties
