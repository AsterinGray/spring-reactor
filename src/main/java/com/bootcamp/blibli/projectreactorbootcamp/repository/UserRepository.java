package com.bootcamp.blibli.projectreactorbootcamp.repository;

import com.bootcamp.blibli.projectreactorbootcamp.model.User;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface UserRepository extends ReactiveMongoRepository<User, String> {

  Mono<User> findFirstByName(String name);
}
