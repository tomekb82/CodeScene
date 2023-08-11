package com.mlbn.appoint.domain.appointment;

import com.mlbn.appoint.shared.validation.Failure;
import com.mlbn.appoint.domain.facility.ProductId;
import io.vavr.collection.Seq;
import io.vavr.control.Validation;

import java.time.Clock;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static com.mlbn.appoint.shared.validation.Validator.*;

public record BookingSlot(ProductId productId, LocalDateTime startDate, Duration duration) {

    private static final String NO_SERVICE_SLOT_ERROR = "Service slot is required!";
    private static final String NO_START_DATE_ERROR = "Start date is required!";
    private static final String NO_DURATION_ERROR = "Duration is required!";
    private static final String DURATION_NEGATIVE_ERROR = "Duration could not be negative!";
    private static final String START_DATE_IN_PAST_ERROR = "Start date is in the past!";
    private static final String NO_PRODUCT_ID_ERROR = "Product ID is required!";

    boolean isEqualTo(BookingSlot input) {
        return startDate.truncatedTo(ChronoUnit.MINUTES).isEqual(input.startDate().truncatedTo(ChronoUnit.MINUTES));
    }

    static Validation<Seq<Failure>, BookingSlot> validate(BookingSlot slot, Clock clock) {
        return Validation.combine(
                isNotNull(slot, NO_SERVICE_SLOT_ERROR),
                isNotNull(slot, NO_SERVICE_SLOT_ERROR).isValid()
                        ? isNotNull(slot.startDate(), NO_START_DATE_ERROR)
                        : Validation.invalid(Failure.of(NO_SERVICE_SLOT_ERROR)),
                isNotNull(slot, NO_SERVICE_SLOT_ERROR).isValid() && isNotNull(slot.startDate(), NO_START_DATE_ERROR).isValid()
                        ? dateNotInPast(slot.startDate(), START_DATE_IN_PAST_ERROR, clock)
                        : Validation.invalid(Failure.of(validateSlot(slot, NO_START_DATE_ERROR))),
                isNotNull(slot, NO_SERVICE_SLOT_ERROR).isValid()
                        ? isNotNull(slot.duration(), NO_DURATION_ERROR)
                        : Validation.invalid(Failure.of(NO_SERVICE_SLOT_ERROR)),
                isNotNull(slot, NO_SERVICE_SLOT_ERROR).isValid() && isNotNull(slot.duration(), NO_DURATION_ERROR).isValid()
                        ? isNegativeDuration(slot.duration(), DURATION_NEGATIVE_ERROR)
                        : Validation.invalid(Failure.of(validateSlot(slot, NO_DURATION_ERROR))),
                isNotNull(slot.productId, NO_PRODUCT_ID_ERROR),
                isNotNull(slot.productId, NO_PRODUCT_ID_ERROR).isValid()
                        ? isNotNull(slot.productId.id(), NO_PRODUCT_ID_ERROR)
                        : Validation.invalid(Failure.of(NO_PRODUCT_ID_ERROR))
        ).ap((s, i0, i1, i3, i4, p1, p2) -> s);
    }

    private static String validateSlot(BookingSlot slot, String noStartDateError) {
        return isNotNull(slot, NO_SERVICE_SLOT_ERROR).isInvalid()
                ? NO_SERVICE_SLOT_ERROR
                : noStartDateError;
    }
}
