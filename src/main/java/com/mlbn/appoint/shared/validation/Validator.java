package com.mlbn.appoint.shared.validation;

import io.vavr.collection.List;
import io.vavr.collection.Stream;
import io.vavr.control.Validation;
import lombok.experimental.UtilityClass;

import java.time.Clock;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashSet;

@UtilityClass
public class Validator {

    public static <T> Validation<Failure, T> validate(T object, Validation... validations) {
        List<String> invalidMessages = List.of(validations)
                .filter(Validation::isInvalid)
                .map(Validation::getError)
                .toStream()
                .flatMap(e -> (e instanceof List ? ((List<Failure>) e).toStream() : Stream.of((Failure)e)))
                .collect(List.collector())
                .flatMap(Failure::getMessages);

        if (!invalidMessages.isEmpty()) {
            return Validation.invalid(Failure.of(new HashSet<>(invalidMessages.asJava())));
        }
        return Validation.valid(object);
    }

    public static <T> Validation<Failure, T> isNotNull(T field, String message) {
        return field != null
                ? Validation.valid(field)
                : Validation.invalid(Failure.of(message));
    }

    public static Validation<Failure, LocalDateTime> dateNotInPast(LocalDateTime date, String message, Clock clock) {
        return date.isAfter(LocalDateTime.now(clock))
                ? Validation.valid(date)
                : Validation.invalid(Failure.of(message));
    }

    public static Validation<Failure, Duration> isNegativeDuration(Duration duration, String message) {
        return !duration.isNegative()
                ? Validation.valid(duration)
                : Validation.invalid(Failure.of(message));
    }

    public static Validation<Failure, LocalDateTime> endDateBeforeStartDate(LocalDateTime startDate, LocalDateTime endDate, String message) {
        return endDate.isAfter(startDate)
                ? Validation.valid(endDate)
                : Validation.invalid(Failure.of(message));
    }
}
