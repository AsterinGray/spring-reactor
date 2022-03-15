package com.bootcamp.blibli.projectreactorbootcamp.controller;

import com.bootcamp.blibli.projectreactorbootcamp.model.User;
import com.bootcamp.blibli.projectreactorbootcamp.model.request.UserRequest;
import com.bootcamp.blibli.projectreactorbootcamp.model.response.UserResponse;
import com.bootcamp.blibli.projectreactorbootcamp.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@AllArgsConstructor
public class UserController {
  private final UserRepository userRepository;

  //GET /user/{name}
  public Mono<ServerResponse> findByName(ServerRequest serverRequest) {
    String name = serverRequest.pathVariable("name");
    Mono<User> user = this.userRepository.findFirstByName(name);

    return ServerResponse.ok().body(user.map(this::toUserResponse), UserResponse.class);
  }

  //POST /user
  public Mono<ServerResponse> save(ServerRequest serverRequest) {
    Mono<UserRequest> request = serverRequest.bodyToMono(UserRequest.class);
    //Mono<userRequest> -> UserRequest -> User -> Mono<User>
    Mono<User> user = request.map(this::toUser)
        // Mono<User>
        // o -> User
        // Mono<Mono<User>>
        .flatMap(this.userRepository::save);

    return ServerResponse.ok().body(user.map(this::toUserResponse), UserResponse.class);
  }

  private User toUser(UserRequest user) {
    return User.builder()
        .name(user.getName())
        .score(user.getScore())
        .build();
  }

  private UserResponse toUserResponse(User user) {
    return UserResponse.builder()
        .name(user.getName())
        .score(user.getScore())
        .build();
  }
}
