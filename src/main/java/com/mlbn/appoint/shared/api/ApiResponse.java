package com.mlbn.appoint.shared.api;

import com.mlbn.appoint.shared.validation.Failure;
import io.vavr.control.Either;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;

public class ApiResponse extends ResponseEntity<ApiResponseBody> {

    private ApiResponse(Object success) {
        super(new ApiResponseBody(success, Collections.emptySet()), HttpStatus.OK);
    }

    private ApiResponse(Failure failure) {
        super(new ApiResponseBody(null, failure.getMessages()), HttpStatus.BAD_REQUEST);
    }

    public static ApiResponse from(Either<Failure, ?> result) {
        return result.isRight()
                ? new ApiResponse(result.get())
                : new ApiResponse(result.getLeft());
    }
}
