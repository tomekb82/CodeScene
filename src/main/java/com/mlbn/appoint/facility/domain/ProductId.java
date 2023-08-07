package com.mlbn.appoint.facility.domain;

import com.mlbn.appoint.common.validation.Failure;
import io.vavr.collection.Seq;
import io.vavr.control.Validation;

import java.util.UUID;

import static com.mlbn.appoint.common.validation.Validator.isNotNull;

public record ProductId(UUID id) {

    private static final String NO_PRODUCT_ID_ERROR = "Product ID is required!";

    public static Validation<Seq<Failure>, ProductId> validate(ProductId productId) {
        return Validation.combine(
                isNotNull(productId, NO_PRODUCT_ID_ERROR),
                isNotNull(productId, NO_PRODUCT_ID_ERROR).isValid()
                        ? isNotNull(productId.id(), NO_PRODUCT_ID_ERROR)
                        : Validation.invalid(Failure.of(NO_PRODUCT_ID_ERROR))
        ).ap((id, ignore) -> id);
    }
}
