package com.example.ordersystem;

import com.example.ordersystem.domain.Order;
import com.example.ordersystem.domain.OrderStatus;
import com.example.ordersystem.jpa.OrderRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.kafka.KafkaContainer;
import org.testcontainers.utility.DockerImageName;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)// run real server
@Testcontainers // to have test in containers
public class OrderIntegrationTest {

    // the integration test do teh following,
    // server recieve post call and return Created http status
    // save order object in postgres
    // send orderEvent to kafka

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres=new PostgreSQLContainer<>("postgres:16-alpine");

    @Container
    @ServiceConnection
    static KafkaContainer kafka=new KafkaContainer(DockerImageName
            .parse("apache/kafka:3.7.0"));//.asCompatibleSubstituteFor("apache/kafka")).withKraft();

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private OrderRepository orderRepository;

    @Test
    public void orderSystemIntegrationTest(){
        // create order object to send it with restTemplate
        Order order=Order.builder()
                .orderStatus(OrderStatus.PENDING)
                .product("Laptop")
                .build();
        // post via restTemplate
        ResponseEntity<Void> response=restTemplate.postForEntity("/api/order/new",order, Void.class);
        // assert response code from response
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        // save order in orderrepository and assert result
        var savedOrder=orderRepository.findAll();
        assertThat(savedOrder).hasSize(1);
        assertThat(savedOrder.get(0).getProduct()).isEqualTo("Laptop");

    }

}
