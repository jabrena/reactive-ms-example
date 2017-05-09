package org.learning.by.example.reactive.microservices.services;

import reactor.core.publisher.Mono;

import java.util.function.Function;

public interface HelloService {
    Function<Mono<String>, Mono<String>> getGreetings();

    Function<Mono<String>, Mono<String>> decode();

    Function<Mono<String>, Mono<String>> compile();

    Function<Mono<String>, Mono<String>> run();
}
