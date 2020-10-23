package com.dvc.reactivespring.controller;

import com.dvc.reactivespring.dao.FruitRepository;
import com.dvc.reactivespring.entities.Fruit;
import com.dvc.reactivespring.entities.FruitType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
public class FruitControllerTest {
    @Autowired
    private WebTestClient client;

    @Autowired
    private FruitRepository repository;

    private final List<Fruit> fruits = Arrays.asList(
            new Fruit(FruitType.MELONS, "watermelon"),
            new Fruit(FruitType.BERRIES,"blueberry"),
            new Fruit(FruitType.CITRUS, "lemon"),
            new Fruit(FruitType.STONE, "peach"));

    @BeforeEach
    public void setUp() {
        repository.deleteAll()
                .thenMany(Flux.fromIterable(fruits))
                .flatMap(repository::save)
                .doOnNext(System.out::println)
                .then()
                .block();
    }

    @Test
    public void testGetAllFruits() {
        client.get()
                .uri("/fruits")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectHeader()
                .contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(Fruit.class)
                .hasSize(4)
                .consumeWith(System.out::println);
    }

    @Test
    public void testGetFruit() {
        client.get()
                .uri("/fruits/{id}", fruits.get(0)
                        .getId())
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(Fruit.class)
                .consumeWith(System.out::println);
    }

    @Test
    public void testCreateFruit() {
        Fruit fruit = new Fruit(FruitType.STONE,"peach");

        client.post()
                .uri("/fruits")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(fruit), Fruit.class)
                .exchange()
                .expectStatus()
                .isCreated()
                .expectHeader()
                .contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.id")
                .isNotEmpty()
                .jsonPath("$.name")
                .isEqualTo("peach")
                .consumeWith(System.out::println);
    }
}