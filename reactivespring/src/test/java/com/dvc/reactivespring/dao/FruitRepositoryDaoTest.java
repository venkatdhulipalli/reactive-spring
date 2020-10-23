package com.dvc.reactivespring.dao;

import com.dvc.reactivespring.entities.Fruit;
import com.dvc.reactivespring.entities.FruitType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;

@SpringBootTest
public class FruitRepositoryDaoTest {

    @Autowired
    private FruitRepository fruitRepository;

    List<Fruit> fruits = Arrays.asList(
            new Fruit(FruitType.BERRIES, "Strawberry"),
            new Fruit(FruitType.BERRIES, "Raspberry"),
            new Fruit(FruitType.CITRUS, "Lemon"),
            new Fruit(FruitType.MELONS, "watermelon"));

    @BeforeEach
    public void setUp() throws Exception{
        fruitRepository.deleteAll()
                .thenMany(Flux.fromIterable(fruits))
                .flatMap(fruitRepository::save)
                .then()
                .block();
    }

    @Test
    public void save(){
        Fruit watermelon = new Fruit(FruitType.MELONS, "Watermelon");
        StepVerifier.create(fruitRepository.save(watermelon))
                .expectNextMatches(fruit -> !fruit.getId().equals(""))
                .verifyComplete();
    }

    @Test
    public void findAll() {
        StepVerifier.create(fruitRepository.findAll())
                .expectNextCount(4)
                .verifyComplete();
    }

    @Test
    public void findById() {
        fruits.stream()
                .map(Fruit::getId)
                .forEach(id -> StepVerifier.create(fruitRepository.findById(id))
                        .expectNextCount(1)
                        .verifyComplete());
    }

    @Test
    public void findByIdNotExist() {
        StepVerifier.create(fruitRepository.findById("xyz"))
                .verifyComplete();
    }

    @Test
    public void count() {
        StepVerifier.create(fruitRepository.count())
                .expectNext(4L)
                .verifyComplete();
    }

    @Test
    public void findByRank() {
        StepVerifier.create(
                fruitRepository.findByFruitType(FruitType.CITRUS)
                        .map(Fruit::getFruitType)
                        .distinct())
                .expectNextCount(1)
                .verifyComplete();

        StepVerifier.create(
                fruitRepository.findByFruitType(FruitType.STONE)
                        .map(Fruit::getFruitType)
                        .distinct())
                .verifyComplete();

    }

    @Test
    public void findByName() {
        fruits.stream()
                .map(Fruit::getName)
                .forEach(name -> StepVerifier.create(fruitRepository.findByName(name))
                        .expectNextMatches(fruit -> fruit.getName().equals(name))
                        .verifyComplete());
    }
}
