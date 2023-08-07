package com.mlbn.appoint.appointment.domain;

import com.mlbn.appoint.common.validation.Failure;
import com.mlbn.appoint.common.validation.Success;
import com.mlbn.appoint.common.vo.TimeSlot;
import com.mlbn.appoint.facility.domain.ProductId;
import io.vavr.control.Either;

import java.util.Set;

public interface Slots {

    Either<Failure, Success> isAvailable(BookingCommand command);

    Set<TimeSlot> findAvailable(ProductId productId,String date);
}
