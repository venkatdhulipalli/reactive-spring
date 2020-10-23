package com.dvc.reactivespring.controllers;

import com.dvc.reactivespring.dao.FruitRepository;
import com.dvc.reactivespring.entities.Fruit;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/fruits")
public class FruitController {
    private final FruitRepository repository;

    public FruitController(FruitRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public Flux<Fruit> getAllFruits() {
        return repository.findAll();
    }

    @GetMapping("{id}")
    public Mono<Fruit> getFruit(@PathVariable String id) {
        return repository.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Fruit> saveFruit(@RequestBody Fruit fruit) {
        return repository.save(fruit);
    }

    @PutMapping("{id}")
    public Mono<ResponseEntity<Fruit>> updateFruit(@PathVariable(value = "id") String id,
                                                       @RequestBody Fruit fruit) {
        return repository.findById(id)
                .flatMap(existingFruit -> {
                    existingFruit.setFruitType(fruit.getFruitType());
                    existingFruit.setName(fruit.getName());
                    return repository.save(existingFruit);
                })
                .map(updateFruit -> new ResponseEntity<>(updateFruit, HttpStatus.OK))
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<ResponseEntity<Void>> deleteFruit(@PathVariable(value = "id") String id) {
        return repository.deleteById(id)
                .then(Mono.just(new ResponseEntity<Void>(HttpStatus.NO_CONTENT)))
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping
    public Mono<Void> deleteAllFruits() {
        return repository.deleteAll();
    }
}
