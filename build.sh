# see https://hub.docker.com/_/maven
# or use maven:3.6.3-openjdk-15-slim
#docker run -it --rm -v "$PWD":/usr/src/mymaven -v "$HOME/.m2":/root/.m2  -w /usr/src/mymaven maven mvn  clean package
docker run -it --rm -v "$PWD":/usr/src/mymaven -v "$HOME/.m2":/root/.m2  -w /usr/src/mymaven maven:3.6.3-openjdk-15-slim mvn  clean package