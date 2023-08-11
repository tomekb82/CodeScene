package com.mlbn.appoint.shared.vo;

import com.mlbn.appoint.shared.validation.Failure;
import io.vavr.control.Either;
import io.vavr.control.Validation;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.Value;

import java.math.BigDecimal;

@Value
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class Money {

    public static final Money ZERO = new Money(BigDecimal.ZERO);

    private static final String NO_VALUE_ERROR = "Value is required!";
    private static final String NEGATIVE_VALUE_ERROR = "Negative value is not possible!";

    BigDecimal amount;

    public static Either<Failure, Money> of(BigDecimal value) {
        return validate(value).toEither();
    }

    private static Validation<Failure, Money> validate(BigDecimal value) {
        return Validation.combine(
                        notNull(value),
                        notNegative(value)
                ).ap((d, n) -> new Money(value))
                .mapError(Failure::merge);
    }

    private static Validation<Failure, BigDecimal> notNull(BigDecimal value) {
        return value != null
                ? Validation.valid(value)
                : Validation.invalid(Failure.of(NO_VALUE_ERROR));
    }

    private static Validation<Failure, BigDecimal> notNegative(BigDecimal value) {
        return value.compareTo(BigDecimal.ZERO) >= 0
                ? Validation.valid(value)
                : Validation.invalid(Failure.of(NEGATIVE_VALUE_ERROR));
    }


}
