package com.dvc.reactivespring;

import com.dvc.reactivespring.dao.FruitRepository;
import com.dvc.reactivespring.entities.Fruit;
import com.dvc.reactivespring.entities.FruitType;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

@Component
public class FruitInit implements ApplicationRunner {
    private final FruitRepository repository;

    public FruitInit(FruitRepository repository) {
        this.repository = repository;
    }

    @Override
    public void run(ApplicationArguments args) {
        repository.deleteAll()
                .thenMany(Flux.just(new Fruit(FruitType.BERRIES, "Strawberry"),
                        new Fruit(FruitType.BERRIES, "Raspberry"),
                        new Fruit(FruitType.CITRUS, "Lemon"),
                        new Fruit(FruitType.MELONS, "watermelon")))
                .flatMap(repository::save)
                .thenMany(repository.findAll())
                .subscribe(System.out::println);
    }
}
