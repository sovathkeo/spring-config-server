# springboot-microservice-demo
microservice with pringboot

## guidlines
# start docker
    docker run --rm --name config-server --network=dotnetms-net -p 8888:8888 -v /home/sovath/workspace/spring-boot/config-files:/config-files  config-server:0.0.1



docker run --rm --name account-service --network=dotnetms-net -p 8081:8080 --spring.profile.active=container -v /home/sovath/workspace/spring-boot/config-files:/config-files  account-service:0.0.1