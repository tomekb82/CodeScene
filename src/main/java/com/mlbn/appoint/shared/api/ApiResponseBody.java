package com.mlbn.appoint.shared.api;

import java.util.Set;

public record ApiResponseBody(Object result, Set<String> errors) {
}
