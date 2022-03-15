package com.bootcamp.blibli.projectreactorbootcamp.controller;

import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import reactor.core.publisher.Mono;

@Component
@RequestMapping("/project-reactor")
public class ProjectReactorController {

    @GetMapping
    public Mono<String> helloWorld() {
        return Mono.just("Hello World!!");
    }
}
