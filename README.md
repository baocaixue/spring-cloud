# Introduction    
***   
Welcome.     
According [	John Carnell's Spring Microservices in Action](https://github.com/carnellj)
***
### Reference Documentation
For further reference, please consider the following sections:

* [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)
* [Docker Doc](https://docs.docker.com/)
* [Spring Web](https://docs.spring.io/spring-boot/docs/2.2.5.RELEASE/reference/htmlsingle/#boot-features-developing-web-applications)
* [Spring Data JPA](https://docs.spring.io/spring-boot/docs/2.2.5.RELEASE/reference/htmlsingle/#boot-features-jpa-and-spring-data)
* [Spring Boot Maven Plugin Reference Guide](https://docs.spring.io/spring-boot/docs/2.2.5.RELEASE/maven-plugin/)
* [Spring Cloud Config](https://cloud.spring.io/spring-cloud-static/spring-cloud-config/2.2.2.RELEASE/reference/html/)
* [Spring Cloud Netflix](https://cloud.spring.io/spring-cloud-static/spring-cloud-netflix/2.2.2.RELEASE/reference/html/)   

***   
### Building   
Prerequisite：Apache Maven, Docker, Docker Compose, Java Env    
first step: run `mvn package` by spotify dockerfile-maven-plugin in sibling directory of Dockerfile    
second step: run `docker-compose up -d` in docker directory       

***
### Test Service Uri     
you can test uri after all services are started    

uri | http method | serviceName   
--- | --- | ---     
[eureka](http://localhost:8761) | GET | eurekasvr    
[config-test](http://localhost:8888/licensingservice/dev) | GET | configserver    
[get-license](http://localhost:8082/v1/organizations/e254f8c-c442-4ebe-a82a-e2fc1d1ff78a/licenses/f3831f8c-c338-4ebe-a82a-e2fc1d1ff78a/feign) | GET | licensingservice    
[get-org](http://localhost:8085/v1/organizations/e254f8c-c442-4ebe-a82a-e2fc1d1ff78a) | GET | organizationservice    
[show-routes](http://localhost:5555/actuator/routes) | GET | zuulservice    



