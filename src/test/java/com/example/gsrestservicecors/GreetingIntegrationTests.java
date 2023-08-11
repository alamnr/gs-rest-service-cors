package com.example.gsrestservicecors;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;

import java.net.URI;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class GreetingIntegrationTests {

    @Autowired
    private TestRestTemplate restTemplate;


    @Test
    public void corsWithAnnotation() throws  Exception {
        ResponseEntity<Greeting> entity = restTemplate.exchange(RequestEntity.get(uri("/greeting"))
                .header(HttpHeaders.ORIGIN,"http://localhost:5352").build(),Greeting.class);
        System.out.println(entity.toString());
        System.out.println(entity.getHeaders());
        Assertions.assertEquals(HttpStatus.OK,entity.getStatusCode());
        Assertions.assertEquals("http://localhost:5352",entity.getHeaders().getAccessControlAllowOrigin());
        Assertions.assertEquals("Hello, World", entity.getBody().getContent());

    }

    @Test
    public void corsWithJavaconfig() {
        ResponseEntity<Greeting> entity = this.restTemplate.exchange(RequestEntity.get(uri("/greeting-javaconfig"))
                .header(HttpHeaders.ORIGIN, "http://localhost:8080").build(), Greeting.class);
        System.out.println(RequestEntity.get(uri("/greeting-javaconfig"))
                .header(HttpHeaders.ORIGIN, "http://localhost:8080").build().toString());
        System.out.println(entity.toString());
        System.out.println(entity.getHeaders());
        Assertions.assertEquals(HttpStatus.OK, entity.getStatusCode());
        //Assertions.assertEquals("http://localhost:8080", entity.getHeaders().getAccessControlAllowOrigin());
        Greeting greeting = entity.getBody();
        Assertions.assertEquals("Hello, World", greeting.getContent());
    }

    private URI uri(String path ){
        return restTemplate.getRestTemplate().getUriTemplateHandler().expand(path);
    }

}
