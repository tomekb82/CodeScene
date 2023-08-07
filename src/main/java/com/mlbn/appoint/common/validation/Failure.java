package com.mlbn.appoint.common.validation;

import io.vavr.collection.Seq;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Set;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class Failure {

    private final Set<String> messages;

    public static Failure of(String message) {
        return new Failure(Set.of(message));
    }

    public static Failure of(Set<String> messages) {
        return new Failure(messages);
    }

    public static Failure merge(Seq<Failure> failures) {
        Set<String> errors = failures.toStream()
                .flatMap(Failure::getMessages)
                .toJavaSet();
        return new Failure(errors);
    }
}
