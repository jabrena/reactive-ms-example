package org.learning.by.example.reactive.microservices.services;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import org.learning.by.example.reactive.microservices.exceptions.InvalidParametersException;
import reactor.core.publisher.Mono;

import java.util.function.Function;

public class HelloServiceImpl implements HelloService{
    private static final String EMPTY = "";
    private static final String BAD_PARAMETERS = "bad parameters";

    @Override
    public Function<Mono<String>, Mono<String>> getGreetings() {
        return value -> value.flatMap(name -> {
            if (name.equals(EMPTY)) {
                return Mono.error(new InvalidParametersException(BAD_PARAMETERS));
            }
            return Mono.just(name);
        });
    }

    @Override
    public Function<Mono<String>, Mono<String>> decode() {
        return stringMono -> stringMono.flatMap(s -> Mono.just(new String(Base64.decode(s))));
                //.onErrorMap(Mono.error(new InvalidParametersException("No me gusta tu rollito")));
    }
}
