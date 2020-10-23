package com.dvc.reactivespring.dao;

import com.dvc.reactivespring.entities.Fruit;
import com.dvc.reactivespring.entities.FruitType;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

//@Repository
public interface FruitRepository extends ReactiveMongoRepository<Fruit, String> {

    Flux<Fruit> findByFruitType(FruitType fruitType);
    Mono<Fruit> findByName(String name);

}
