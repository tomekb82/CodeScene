package com.mlbn.appoint.shared.validation;

import io.vavr.control.Either;
import lombok.experimental.UtilityClass;

@UtilityClass
public class Results {

    public Either<Failure, Success> success() {
        return Either.right(new Success());
    }

    public <T> Either<Failure, T> success(T success) {
        return Either.right(success);
    }

    public <T> Either<Failure, T> failure(String message) {
        return Either.left(Failure.of(message));
    }
}
